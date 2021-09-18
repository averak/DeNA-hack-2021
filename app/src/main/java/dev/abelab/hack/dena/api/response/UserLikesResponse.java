package dev.abelab.hack.dena.api.response;

import lombok.*;

/**
 * お気に入り取得レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLikesResponse {

    /**
     * いいねの総数
     */
    Integer likes;

}

