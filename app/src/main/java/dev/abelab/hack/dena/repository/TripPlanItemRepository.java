package dev.abelab.hack.dena.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.TripPlanItem;
import dev.abelab.hack.dena.db.entity.TripPlanItemExample;
import dev.abelab.hack.dena.db.mapper.TripPlanItemMapper;

@RequiredArgsConstructor
@Repository
public class TripPlanItemRepository {

    private final TripPlanItemMapper tripPlanItemMapper;

    /**
     * 旅行プラン項目を作成
     *
     * @param tripPlanItem 旅行プラン項目
     *
     * @return 旅行プラン項目ID
     */
    public int insert(final TripPlanItem tripPlanItem) {
        return this.tripPlanItemMapper.insertSelective(tripPlanItem);
    }

    /**
     * 旅行プラン項目を一括作成
     *
     * @param tripPlanItems 旅行プラン項目リスト
     */
    public void bulkInsert(final List<TripPlanItem> tripPlanItems) {
        if (!tripPlanItems.isEmpty()) {
            this.tripPlanItemMapper.bulkInsert(tripPlanItems);
        }
    }

    /**
     * 旅行プランIDから項目リストを取得
     *
     * @param tripPlanId 旅行プランID
     */
    public List<TripPlanItem> selectByTripPlanId(final int tripPlanId) {
        final var example = new TripPlanItemExample();
        example.createCriteria().andTripPlanIdEqualTo(tripPlanId);
        return this.tripPlanItemMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 旅行プランIDから項目を削除
     *
     * @param tripPlanId 旅行プランID
     */
    public void deleteByTripPlanId(final int tripPlanId) {
        final var example = new TripPlanItemExample();
        example.createCriteria().andTripPlanIdEqualTo(tripPlanId);
        this.tripPlanItemMapper.deleteByExample(example);
    }

}
