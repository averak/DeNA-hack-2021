package dev.abelab.hack.dena.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.Region;
import dev.abelab.hack.dena.db.entity.RegionExample;
import dev.abelab.hack.dena.db.mapper.RegionMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class RegionRepository {

    private final RegionMapper regionMapper;

    /**
     * 都道府県一覧を取得
     *
     * @return 都道府県一覧
     */
    public List<Region> selectAll() {
        final var example = new RegionExample();
        return this.regionMapper.selectByExample(example);
    }

    /**
     * 都道府県を作成
     *
     * @param region 都道府県
     *
     * @return 都道府県ID
     */
    public int insert(final Region region) {
        return this.regionMapper.insertSelective(region);
    }

    /**
     * IDから都道府県を検索
     *
     * @param regionId 都道府県ID
     *
     * @return 都道府県
     */
    public Region selectById(final int regionId) {
        return Optional.ofNullable(this.regionMapper.selectByPrimaryKey(regionId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_REGION));
    }

}
