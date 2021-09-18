package dev.abelab.hack.dena.db.mapper;

import java.util.List;

import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.db.mapper.base.TagBaseMapper;

public interface TagMapper extends TagBaseMapper {

    void bulkInsert(List<Tag> tags);

    List<Tag> selectByTripPlanId(int tripPlanId);

}
