package dev.abelab.hack.dena.api.response;

import lombok.*;

/**
 * ユーザ情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * ユーザID
     */
    Integer id;

    /**
     * メールアドレス
     */
    String email;

    /**
     * ファーストネーム
     */
    String firstName;

    /**
     * ラストネーム
     */
    String lastName;

}
