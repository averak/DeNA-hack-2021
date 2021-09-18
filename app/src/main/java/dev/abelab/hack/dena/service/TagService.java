package dev.abelab.hack.dena.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.api.response.TagsResponse;
import dev.abelab.hack.dena.repository.TagRepository;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    /**
     * タグ一覧を取得
     *
     * @param loginUser ログインユーザ
     *
     * @return タグ一覧
     */
    @Transactional
    public TagsResponse getTags(final User loginUser) {
        final var tags = this.tagRepository.selectAll().stream().map(Tag::getName).collect(Collectors.toList());
        return TagsResponse.builder().tags(tags).build();
    }

}
