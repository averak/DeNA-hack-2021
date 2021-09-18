package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.mapper.TripPlanMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.ConflictException;
import dev.abelab.hack.dena.db.entity.TripPlanExample;
import dev.abelab.hack.dena.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class TripPlanRepository {

    private final TripPlanMapper tripPlanMapper;

    /**
     * 旅行プラン一覧を取得
     *
     * @return 旅行プラン一覧
     */
    public List<TripPlan> selectAll() {
        final var example = new TripPlanExample();
        example.setOrderByClause("updated_at desc");
        return this.tripPlanMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 旅行プランを作成
     *
     * @param tripPlan 旅行プラン
     *
     * @return 旅行プランID
     */
    public int insert(final TripPlan tripPlan) {
        return this.tripPlanMapper.insertSelective(tripPlan);
    }

    /**
     * 旅行プランを削除
     *
     * @param tripPlanId 旅行プランID
     */
    public void deleteById(final int tripPlanId) {
        if (this.existsById(tripPlanId)) {
            this.tripPlanMapper.deleteByPrimaryKey(tripPlanId);
        } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_TRIP_PLAN);
        }
    }

    /**
     * IDから旅行プランを検索
     *
     * @param tripPlanId 旅行プランID
     *
     * @return 旅行プラン
     */
    public TripPlan selectById(final int tripPlanId) {
        return Optional.ofNullable(this.tripPlanMapper.selectByPrimaryKey(tripPlanId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TRIP_PLAN));
    }

    /**
     * 旅行プランIDの存在確認
     *
     * @param tripPlanId 旅行プランID
     *
     * @return 旅行プランIDが存在するか
     */
    public boolean existsById(final int tripPlanId) {
        try {
            this.selectById(tripPlanId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

}
