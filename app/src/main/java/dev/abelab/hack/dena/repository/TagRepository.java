package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.db.entity.TagExample;
import dev.abelab.hack.dena.db.mapper.TagMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.NotFoundException;
import dev.abelab.hack.dena.exception.ConflictException;

@RequiredArgsConstructor
@Repository
public class TagRepository {

    private final TagMapper tagMapper;

    /**
     * タグ一覧を取得
     *
     * @return タグ一覧
     */
    public List<Tag> selectAll() {
        final var example = new TagExample();
        return this.tagMapper.selectByExample(example);
    }

    /**
     * タグを作成
     *
     * @param tag タグ
     *
     * @return タグID
     */
    public int insert(final Tag tag) {
        if (this.existsByName(tag.getName())) {
            throw new ConflictException(ErrorCode.CONFLICT_TAG_NAME);
        }
        return this.tagMapper.insertSelective(tag);
    }

    /**
     * タグを一括作成
     *
     * @param tags タグリスト
     */
    public void bulkInsert(final List<Tag> tags) {
        final var insertTags = tags.stream().filter(tag -> !this.existsByName(tag.getName())).collect(Collectors.toList());
        if (!insertTags.isEmpty()) {
            this.tagMapper.bulkInsert(insertTags);
        }
    }

    /**
     * IDからタグを検索
     *
     * @param tagId タグID
     *
     * @return タグ
     */
    public Tag selectById(final int tagId) {
        return Optional.ofNullable(this.tagMapper.selectByPrimaryKey(tagId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TAG));
    }

    /**
     * タグ名からタグを検索
     *
     * @param name タグ名
     *
     * @return タグ
     */
    public Tag selectByName(final String name) {
        final var example = new TagExample();
        example.createCriteria().andNameEqualTo(name);
        return this.tagMapper.selectByExample(example).stream().findFirst() //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TAG));
    }

    /**
     * タグ名の存在確認
     *
     * @param name タグ名
     *
     * @return タグ名が存在するか
     */
    public boolean existsByName(final String name) {
        try {
            this.selectByName(name);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    /**
     * 旅行プランIDからタグリストを取得
     *
     * @param tripPlanId 旅行プランID
     *
     * @return タグリスト
     */
    public List<Tag> selectByTripPlanId(final int tripPlanId) {
        return this.tagMapper.selectByTripPlanId(tripPlanId);
    }

}
