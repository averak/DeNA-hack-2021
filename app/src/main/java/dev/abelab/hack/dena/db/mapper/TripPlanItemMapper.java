package dev.abelab.hack.dena.db.mapper;

import java.util.List;

import dev.abelab.hack.dena.db.entity.TripPlanItem;
import dev.abelab.hack.dena.db.mapper.base.TripPlanItemBaseMapper;

public interface TripPlanItemMapper extends TripPlanItemBaseMapper {

    void bulkInsert(List<TripPlanItem> tripPlanItems);

}
