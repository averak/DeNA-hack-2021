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
import dev.abelab.hack.dena.model.TripPlanItemModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentModel;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.api.response.TripPlanResponse;
import dev.abelab.hack.dena.api.response.TripPlansResponse;
import dev.abelab.hack.dena.api.response.UserResponse;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.repository.TripPlanItemRepository;
import dev.abelab.hack.dena.repository.TripPlanAttachmentRepository;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.repository.RegionRepository;
import dev.abelab.hack.dena.repository.TagRepository;
import dev.abelab.hack.dena.repository.TripPlanTaggingRepository;
import dev.abelab.hack.dena.repository.UserLikeRepository;

@RequiredArgsConstructor
@Service
public class TripPlanService {

    private final ModelMapper modelMapper;

    private final TripPlanRepository tripPlanRepository;

    private final TripPlanItemRepository tripPlanItemRepository;

    private final TripPlanAttachmentRepository tripPlanAttachmentRepository;

    private final UserRepository userRepository;

    private final RegionRepository regionRepository;

    private final TagRepository tagRepository;

    private final TripPlanTaggingRepository tripPlanTaggingRepository;

    private final UserLikeRepository userLikeRepository;

    /**
     * 旅行プラン一覧を取得
     *
     * @param loginUser ログインユーザ
     *
     * @return 旅行プラン一覧
     */
    @Transactional
    public TripPlansResponse getTripPlans(final User loginUser) {
        // 旅行プラン一覧を取得
        final var tripPlans = this.tripPlanRepository.selectAll();
        final var tripPlanResponses = tripPlans.stream().map(tripPlan -> {
            final var response = this.modelMapper.map(tripPlan, TripPlanResponse.class);

            // 作成者を取得
            final var author = this.userRepository.selectById(tripPlan.getUserId());
            response.setAuthor(this.modelMapper.map(author, UserResponse.class));

            // いいね数を取得
            final var userLikes = this.userLikeRepository.selectByTripPlanId(tripPlan.getId());
            response.setLikes(userLikes.size());

            // タグリストを取得
            final var tags = this.tagRepository.selectByTripPlanId(tripPlan.getId());
            response.setTags(tags.stream().map(Tag::getName).collect(Collectors.toList()));

            // 項目リストを取得
            final var items = this.tripPlanItemRepository.selectByTripPlanId(tripPlan.getId());
            response.setItems(items.stream() //
                .map(item -> this.modelMapper.map(item, TripPlanItemModel.class)) //
                .collect(Collectors.toList()));

            // 添付ファイルを取得
            final var attachment = this.tripPlanAttachmentRepository.selectByTripPlanId(tripPlan.getId());
            response.setAttachment(this.modelMapper.map(attachment, TripPlanAttachmentModel.class));

            return response;
        }).collect(Collectors.toList());

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

}
