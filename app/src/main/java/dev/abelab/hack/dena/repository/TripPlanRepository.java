package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.mapper.TripPlanMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.ConflictException;
import dev.abelab.hack.dena.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class TripPlanRepository {

    private final TripPlanMapper tripPlanMapper;

    /**
     * 作成
     */
    public int insert(final TripPlan tripPlan) {
        // if (this.existsByEmail(userLike.getEmail())) {
        //     throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        // }
        return this.tripPlanMapper.insertSelective(tripPlan);
    }
}