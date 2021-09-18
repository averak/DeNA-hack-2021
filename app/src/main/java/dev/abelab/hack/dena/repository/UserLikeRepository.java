package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.UserLike;
import dev.abelab.hack.dena.db.entity.UserLikeExample;
import dev.abelab.hack.dena.db.mapper.UserLikeMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.ConflictException;
import dev.abelab.hack.dena.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class UserLikeRepository {

    private final UserLikeMapper userLikeMapper;

    /**
     * IDから検索
     *
     * @param userId ユーザID
     *
     * @return ユーザ
     */
    public UserLike selectById(final int userId) {
        final var example = new UserLikeExample();
        example.createCriteria().andUserIdEqualTo(userId);
        System.out.println(example);
        return this.userLikeMapper.selectByExample(example).stream().findFirst() //
        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * 作成
     *
     * @param userLike ユーザ
     *
     * @return ユーザID
     */
    public int insert(final UserLike userLike) {
        // if (this.existsByEmail(userLike.getEmail())) {
        //     throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        // }
        return this.userLikeMapper.insertSelective(userLike);
    }
}