package dev.abelab.hack.dena.api.response;

import java.util.List;

import lombok.*;

/**
 * タグ一覧レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagsResponse {

    /**
     * タグリスト
     */
    List<String> tags;

}
