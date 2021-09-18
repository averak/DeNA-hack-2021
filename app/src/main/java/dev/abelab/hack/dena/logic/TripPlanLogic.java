package dev.abelab.hack.dena.logic;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.model.TripPlanItemModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentModel;
import dev.abelab.hack.dena.api.response.TripPlanResponse;
import dev.abelab.hack.dena.api.response.UserResponse;
import dev.abelab.hack.dena.repository.TripPlanItemRepository;
import dev.abelab.hack.dena.repository.TripPlanAttachmentRepository;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.repository.TagRepository;
import dev.abelab.hack.dena.repository.UserLikeRepository;

@RequiredArgsConstructor
@Component
public class TripPlanLogic {

    private final ModelMapper modelMapper;

    private final TripPlanItemRepository tripPlanItemRepository;

    private final TripPlanAttachmentRepository tripPlanAttachmentRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    private final UserLikeRepository userLikeRepository;

    /**
     * 旅行プランIDから旅行プランレスポンスを作成
     *
     * @param tripPlan 旅行プラン
     *
     * @return 旅行プランレスポンス
     */
    public TripPlanResponse buildTripPlanResponse(final TripPlan tripPlan) {
        final var tripPlanResponse = this.modelMapper.map(tripPlan, TripPlanResponse.class);

        // 作成者を取得
        final var author = this.userRepository.selectById(tripPlan.getUserId());
        tripPlanResponse.setAuthor(this.modelMapper.map(author, UserResponse.class));

        // いいね数を取得
        final var userLikes = this.userLikeRepository.selectByTripPlanId(tripPlan.getId());
        tripPlanResponse.setLikes(userLikes.size());

        // タグリストを取得
        final var tags = this.tagRepository.selectByTripPlanId(tripPlan.getId());
        tripPlanResponse.setTags(tags.stream().map(Tag::getName).collect(Collectors.toList()));

        // 項目リストを取得
        final var items = this.tripPlanItemRepository.selectByTripPlanId(tripPlan.getId());
        tripPlanResponse.setItems(items.stream() //
            .map(item -> this.modelMapper.map(item, TripPlanItemModel.class)) //
            .collect(Collectors.toList()));

        // 添付ファイルを取得
        final var attachment = this.tripPlanAttachmentRepository.selectByTripPlanId(tripPlan.getId());
        if (attachment != null) {
            tripPlanResponse.setAttachment(this.modelMapper.map(attachment, TripPlanAttachmentModel.class));
        }

        return tripPlanResponse;
    }

}
