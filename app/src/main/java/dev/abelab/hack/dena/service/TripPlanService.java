package dev.abelab.hack.dena.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.net.util.Base64;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.entity.TripPlanItem;
import dev.abelab.hack.dena.db.entity.TripPlanAttachment;
import dev.abelab.hack.dena.db.entity.TripPlanTagging;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.db.entity.UserLike;
import dev.abelab.hack.dena.model.FileDownloadModel;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.api.request.TripPlanUpdateRequest;
import dev.abelab.hack.dena.api.request.UserLikeRequest;
import dev.abelab.hack.dena.api.response.TripPlanResponse;
import dev.abelab.hack.dena.api.response.TripPlansResponse;
import dev.abelab.hack.dena.api.response.UserLikesResponse;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.repository.TripPlanItemRepository;
import dev.abelab.hack.dena.repository.TripPlanAttachmentRepository;
import dev.abelab.hack.dena.repository.RegionRepository;
import dev.abelab.hack.dena.repository.TagRepository;
import dev.abelab.hack.dena.repository.TripPlanTaggingRepository;
import dev.abelab.hack.dena.repository.UserLikeRepository;
import dev.abelab.hack.dena.logic.TripPlanLogic;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.ForbiddenException;

@RequiredArgsConstructor
@Service
public class TripPlanService {

    private final ModelMapper modelMapper;

    private final TripPlanRepository tripPlanRepository;

    private final TripPlanItemRepository tripPlanItemRepository;

    private final TripPlanAttachmentRepository tripPlanAttachmentRepository;

    private final RegionRepository regionRepository;

    private final TagRepository tagRepository;

    private final TripPlanTaggingRepository tripPlanTaggingRepository;

    private final UserLikeRepository userLikeRepository;

    private final TripPlanLogic tripPlanLogic;

    /**
     * 旅行プランを取得
     *
     * @param tripPlanId 旅行プランID
     * @param loginUser  ログインユーザ
     *
     * @return 旅行プランレスポンス
     */
    @Transactional
    public TripPlanResponse getTripPlan(final int tripPlanId, final User loginUser) {
        // 旅行プランを取得
        final var tripPlan = this.tripPlanRepository.selectById(tripPlanId);
        return this.tripPlanLogic.buildTripPlanResponse(tripPlan, loginUser);
    }

    /**
     * 旅行プラン一覧を取得
     *
     * @param loginUser ログインユーザ
     * @param maxPrice  上限金額
     * @param tag       タグ名
     * @param userId    作成者ID
     * @param regionId  都道府県ID
     *
     * @return 旅行プラン一覧
     */
    @Transactional
    public TripPlansResponse getTripPlans(final User loginUser, final Integer maxPrice, final String tag, final Integer userId,
        final Integer regionId) {
        // 旅行プラン一覧を取得
        var tripPlans = this.tripPlanRepository.selectAll();
        var tripPlanResponses = tripPlans.stream() //
            .map(tripPlan -> this.tripPlanLogic.buildTripPlanResponse(tripPlan, loginUser)) //
            .collect(Collectors.toList());

        // 上限金額で絞り込み
        if (maxPrice != null) {
            tripPlanResponses = tripPlanResponses.stream().filter(response -> {
                // 合計金額を算出
                var price = 0;
                for (var item : response.getItems()) {
                    price += item.getPrice();
                }

                return price <= maxPrice;
            }).collect(Collectors.toList());
        }

        // タグ名で絞り込み
        if (tag != null) {
            final var tagNames = tag.split(",");
            tripPlanResponses = tripPlanResponses.stream().filter(response -> {
                for (String tagName : tagNames) {
                    if (response.getTags().contains(tagName)) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
        }

        // ユーザIDで絞り込み
        if (userId != null) {
            tripPlanResponses = tripPlanResponses.stream() //
                .filter(response -> response.getAuthor().getId() == userId) //
                .collect(Collectors.toList());
        }

        // 都道府県IDで絞り込み
        if (regionId != null) {
            tripPlanResponses = tripPlanResponses.stream() //
                .filter(response -> response.getRegionId() == regionId) //
                .collect(Collectors.toList());
        }

        return new TripPlansResponse(tripPlanResponses);
    }

    /**
     * 旅行プランを作成
     *
     * @param requestBody 旅行プラン作成リクエスト
     * @param loginUser   ログインユーザ
     */
    @Transactional
    public void createTripPlan(final TripPlanCreateRequest requestBody, final User loginUser) {
        // 都道府県IDが存在するかチェック
        this.regionRepository.selectById(requestBody.getRegionId());

        // 旅行プランを作成
        final var tripPlan = this.modelMapper.map(requestBody, TripPlan.class);
        tripPlan.setUserId(loginUser.getId());
        this.tripPlanRepository.insert(tripPlan);

        // プラン内訳を作成
        final var tripPlanItems = requestBody.getItems().stream().map(item -> {
            final var result = this.modelMapper.map(item, TripPlanItem.class);
            result.setTripPlanId(tripPlan.getId());
            return result;
        }).collect(Collectors.toList());
        this.tripPlanItemRepository.bulkInsert(tripPlanItems);

        // 添付ファイルを保存
        if (requestBody.getAttachment() != null) {
            final var attachment = TripPlanAttachment.builder() //
                .uuid(UUID.randomUUID().toString()) //
                .tripPlanId(tripPlan.getId()) //
                .fileName(requestBody.getAttachment().getFileName()) //
                .content(Base64.decodeBase64(requestBody.getAttachment().getContent())) //
                .build();
            this.tripPlanAttachmentRepository.insert(attachment);
        }

        // タグ一覧を作成
        final var tags = requestBody.getTags().stream() //
            .map(tagName -> Tag.builder().name(tagName).build()).collect(Collectors.toList());
        this.tagRepository.bulkInsert(tags);

        // 旅行プランタギング一覧を作成
        final var tripPlanTaggings = tags.stream() //
            .map(tag -> TripPlanTagging.builder().tripPlanId(tripPlan.getId()).tagId(tag.getId()).build()) //
            .filter(tagging -> tagging.getTagId() != null) //
            .collect(Collectors.toList());
        this.tripPlanTaggingRepository.bulkInsert(tripPlanTaggings);
    }

    /**
     * 旅行プランを更新
     *
     * @param tripPlanId  旅行プランID
     * @param requestBody 旅行プラン更新リクエスト
     * @param loginUser   ログインユーザ
     */
    @Transactional
    public void updateTripPlan(final int tripPlanId, final TripPlanUpdateRequest requestBody, final User loginUser) {
        // 更新する権限があるかチェック
        final var existsTripPlan = this.tripPlanRepository.selectById(tripPlanId);
        if (existsTripPlan.getUserId() != loginUser.getId()) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // 都道府県IDが存在するかチェック
        this.regionRepository.selectById(requestBody.getRegionId());

        // 旅行プランを更新
        final var tripPlan = this.modelMapper.map(requestBody, TripPlan.class);
        tripPlan.setId(tripPlanId);
        tripPlan.setUserId(loginUser.getId());
        this.tripPlanRepository.update(tripPlan);

        // プラン内訳を更新
        this.tripPlanItemRepository.deleteByTripPlanId(tripPlanId);
        final var tripPlanItems = requestBody.getItems().stream().map(item -> {
            final var result = this.modelMapper.map(item, TripPlanItem.class);
            result.setTripPlanId(tripPlan.getId());
            return result;
        }).collect(Collectors.toList());
        this.tripPlanItemRepository.bulkInsert(tripPlanItems);

        // 添付ファイルを更新
        if (requestBody.getAttachment() != null) {
            this.tripPlanAttachmentRepository.deleteByTripPlanId(tripPlanId);
            final var attachment = TripPlanAttachment.builder() //
                .uuid(UUID.randomUUID().toString()) //
                .tripPlanId(tripPlan.getId()) //
                .fileName(requestBody.getAttachment().getFileName()) //
                .content(Base64.decodeBase64(requestBody.getAttachment().getContent())) //
                .build();
            this.tripPlanAttachmentRepository.insert(attachment);
        }

        // タグ一覧を作成
        final var tags = requestBody.getTags().stream() //
            .map(tagName -> Tag.builder().name(tagName).build()).collect(Collectors.toList());
        this.tagRepository.bulkInsert(tags);

        // 旅行プランタギング一覧を更新
        this.tripPlanTaggingRepository.deleteByTripPlanId(tripPlanId);
        final var tripPlanTaggings = tags.stream() //
            .map(tag -> TripPlanTagging.builder().tripPlanId(tripPlan.getId()).tagId(tag.getId()).build()) //
            .filter(tagging -> tagging.getTagId() != null) //
            .collect(Collectors.toList());
        this.tripPlanTaggingRepository.bulkInsert(tripPlanTaggings);

    }

    /**
     * 旅行プランをいいね登録する
     *
     * @param tripPlanId  旅行プランID
     * @param requestBody 旅行プランいいねリクエスト
     * @param loginUser   ログインユーザ
     *
     * @return いいね情報レスポンス
     */
    @Transactional
    public UserLikesResponse likeTripPlan(final int tripPlanId, final UserLikeRequest requestBody, final User loginUser) {
        // いいね対象の旅行プランを取得する
        this.tripPlanRepository.selectById(tripPlanId);

        final var userLike = UserLike.builder() //
            .userId(loginUser.getId()) //
            .tripPlanId(tripPlanId) //
            .build();

        // いいね登録
        if (requestBody.getIsLike()) {
            this.userLikeRepository.insert(userLike);
        } else {
            this.userLikeRepository.deleteByPrimaryKey(loginUser.getId(), tripPlanId);
        }

        final var userLikes = this.userLikeRepository.selectByTripPlanId(tripPlanId);
        return new UserLikesResponse(userLikes.size());
    }

    /**
     * 旅行プランを削除
     *
     * @param tripPlanId 旅行プランID
     * @param loginUser  ログインユーザ
     */
    @Transactional
    public void deleteTripPlan(final int tripPlanId, final User loginUser) {
        // 削除対象の旅行プランを取得
        final var tripPlan = this.tripPlanRepository.selectById(tripPlanId);

        // 削除権限があるかチェック
        if (loginUser.getId() != tripPlan.getUserId()) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        // 旅行プランを削除
        this.tripPlanRepository.deleteById(tripPlanId);
    }

    /**
     * お気に入りの旅行プラン一覧を取得
     *
     * @param loginUser ログインユーザ
     */
    @Transactional
    public TripPlansResponse getLikedTripPlans(final User loginUser) {
        // いいね一覧を取得
        final var myLikes = this.userLikeRepository.selectByUserId(loginUser.getId());

        // 旅行プラン一覧を取得
        final var tripPlanResponses = myLikes.stream() //
            .map(myLike -> this.tripPlanRepository.selectById(myLike.getTripPlanId())) //
            .map(tripPlan -> this.tripPlanLogic.buildTripPlanResponse(tripPlan, loginUser)) //
            .collect(Collectors.toList());

        return new TripPlansResponse(tripPlanResponses);

    }

    /**
     * 添付ファイルを取得
     *
     * @param uuid      UUID
     * @param loginUser ログインユーザ
     *
     * @return 添付ファイル
     */
    public FileDownloadModel getTripPlanAttachment(final String uuid, final User loginUser) {
        // 添付ファイルを取得
        final var attachment = this.tripPlanAttachmentRepository.selectByUuid(uuid);
        return this.modelMapper.map(attachment, FileDownloadModel.class);
    }

}
