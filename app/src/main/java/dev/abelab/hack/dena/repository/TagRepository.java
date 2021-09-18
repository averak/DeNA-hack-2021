package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.db.entity.TagExample;
import dev.abelab.hack.dena.db.mapper.TagMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.NotFoundException;

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
        return this.tagMapper.insertSelective(tag);
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

}
