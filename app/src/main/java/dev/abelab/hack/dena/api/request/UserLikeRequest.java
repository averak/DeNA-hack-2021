package dev.abelab.hack.dena.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

/**
 * いいねリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeRequest {

    /**
     * like
     */
    @NotNull
    Boolean isLike;
}