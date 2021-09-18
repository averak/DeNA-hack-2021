package dev.abelab.hack.dena.api.request;

import javax.validation.constraints.NotNull;

import lombok.*;

/**
 * 旅行プランいいねリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeRequest {

    /**
     * いいねするかどうか
     */
    @NotNull
    Boolean isLike;

}

