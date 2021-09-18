package dev.abelab.hack.dena.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.UserLike;
import dev.abelab.hack.dena.db.entity.UserLikeExample;
import dev.abelab.hack.dena.db.mapper.UserLikeMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.NotFoundException;
import dev.abelab.hack.dena.exception.ConflictException;

@RequiredArgsConstructor
@Repository
public class UserLikeRepository {

    private final UserLikeMapper userLikeMapper;

    /**
     * いいねを作成
     *
     * @param userLike いいね
     *
     * @return いいねID
     */
    public void insert(final UserLike userLike) {
        if (this.exists(userLike)) {
            throw new ConflictException(ErrorCode.CONFLICT_USER_LIKE);
        }
        this.userLikeMapper.insertSelective(userLike);
    }

    /**
     * いいねを削除
     *
     * @param userLike いいね
     */
    public void update(final UserLike userLike) {
        if (this.exists(userLike)) {
            this.userLikeMapper.deleteByPrimaryKey(userLike.getUserId(), userLike.getTripPlanId());
        } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER_LIKE);
        }
    }

    /**
     * ユーザIDからいいねリストを取得
     *
     * @param userId ユーザID
     *
     * @return いいねリスト
     */
    public List<UserLike> selectByUserId(final int userId) {
        final var example = new UserLikeExample();
        example.createCriteria().andUserIdEqualTo(userId);

        return this.userLikeMapper.selectByExample(example);
    }

    /**
     * 旅行プランIDからいいねリストを取得
     *
     * @param tripPlanId 旅行プランID
     *
     * @return いいねリスト
     */
    public List<UserLike> selectByTripPlanId(final int tripPlanId) {
        final var example = new UserLikeExample();
        example.createCriteria().andTripPlanIdEqualTo(tripPlanId);

        return this.userLikeMapper.selectByExample(example);
    }

    /**
     * いいねが存在するか
     *
     * @param userLike いいね
     *
     * @return いいねが存在するか
     */
    public boolean exists(final UserLike userLike) {
        final var example = new UserLikeExample();
        example.createCriteria().andUserIdEqualTo(userLike.getUserId());
        example.createCriteria().andTripPlanIdEqualTo(userLike.getTripPlanId());

        return !this.userLikeMapper.selectByExample(example).isEmpty();
    }

}
