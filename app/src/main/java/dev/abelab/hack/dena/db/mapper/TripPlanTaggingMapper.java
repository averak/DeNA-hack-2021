package dev.abelab.hack.dena.db.mapper;

import java.util.List;

import dev.abelab.hack.dena.db.entity.TripPlanTagging;
import dev.abelab.hack.dena.db.mapper.base.TripPlanTaggingBaseMapper;

public interface TripPlanTaggingMapper extends TripPlanTaggingBaseMapper {

    void bulkInsert(List<TripPlanTagging> tripPlanTaggings);

}
