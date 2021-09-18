package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.TripPlanTagging;
import dev.abelab.hack.dena.db.entity.TripPlanTaggingExample;
import dev.abelab.hack.dena.db.mapper.TripPlanTaggingMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.ConflictException;

@RequiredArgsConstructor
@Repository
public class TripPlanTaggingRepository {

    private final TripPlanTaggingMapper tripPlanTaggingMapper;

    /**
     * 旅行プランタギングを作成
     *
     * @param tripPlanTagging 旅行プランタギング
     *
     * @return タグID
     */
    public int insert(final TripPlanTagging tripPlanTagging) {
        if (this.exists(tripPlanTagging)) {
            throw new ConflictException(ErrorCode.CONFLICT_TRIP_PLAN_TAGGING);
        }
        return this.tripPlanTaggingMapper.insertSelective(tripPlanTagging);
    }

    /**
     * 旅行プランタギングを一括作成
     *
     * @param tripPlanTaggings 旅行プランタギングリスト
     */
    public void bulkInsert(final List<TripPlanTagging> tripPlanTaggings) {
        final var insertTripPlanTaggings = tripPlanTaggings.stream().filter(tagging -> !this.exists(tagging)).collect(Collectors.toList());
        if (!insertTripPlanTaggings.isEmpty()) {
            this.tripPlanTaggingMapper.bulkInsert(insertTripPlanTaggings);
        }
    }

    /**
     * 旅行プランタギングが存在するか
     *
     * @param tripPlanTagging 旅行プランタギング
     *
     * @return 旅行プランタギングが存在するか
     */
    public boolean exists(final TripPlanTagging tripPlanTagging) {
        final var example = new TripPlanTaggingExample();
        example.createCriteria() //
            .andTripPlanIdEqualTo(tripPlanTagging.getTripPlanId()) //
            .andTagIdEqualTo(tripPlanTagging.getTagId());

        return !this.tripPlanTaggingMapper.selectByExample(example).isEmpty();
    }

}
