package dev.abelab.hack.dena.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.repository.RegionRepository;
import dev.abelab.hack.dena.repository.TagRepository;

@RequiredArgsConstructor
@Service
public class TripPlanService {

    private final ModelMapper modelMapper;

    private final TripPlanRepository tripPlanRepository;

    private final UserRepository userRepository;

    private final RegionRepository regionRepository;

    private final TagRepository tagRepository;

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

        // TODO: プラン内訳、タグについても検証

        // 旅行プランを作成
        final var tripPlan = this.modelMapper.map(requestBody, TripPlan.class);
        tripPlan.setUserId(loginUser.getId());
        this.tripPlanRepository.insert(tripPlan);
    }

}
