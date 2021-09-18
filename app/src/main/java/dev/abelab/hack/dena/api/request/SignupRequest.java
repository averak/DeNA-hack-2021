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
public class SignupRequest {

    /**
     * メールアドレス
     */
    @NotNull
    @Size(max = 255)
    String email;

    /**
     * パスワード
     */
    @NotNull
    @Size(max = 32)
    String password;

    /**
     * ファーストネーム
     */
    @NotNull
    @Size(max = 100)
    String firstName;

    /**
     * ラストネーム
     */
    @NotNull
    @Size(max = 100)
    String lastName;

}
