package dev.abelab.hack.dena.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

/**
 * サインアップeリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeRequest {

    /**
     * メールアドレス
     */
    @NotNull
    boolean isLike;
}