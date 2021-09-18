package dev.abelab.hack.dena.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.entity.TripPlanItem;
import dev.abelab.hack.dena.db.entity.TripPlanTagging;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.repository.TripPlanItemRepository;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.repository.RegionRepository;
import dev.abelab.hack.dena.repository.TagRepository;
import dev.abelab.hack.dena.repository.TripPlanTaggingRepository;

@RequiredArgsConstructor
@Service
public class TripPlanService {

    private final ModelMapper modelMapper;

    private final TripPlanRepository tripPlanRepository;

    private final TripPlanItemRepository tripPlanItemRepository;

    private final UserRepository userRepository;

    private final RegionRepository regionRepository;

    private final TagRepository tagRepository;

    private final TripPlanTaggingRepository tripPlanTaggingRepository;

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
