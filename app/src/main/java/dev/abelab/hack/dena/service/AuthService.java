package dev.abelab.hack.dena.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.api.request.LoginRequest;
import dev.abelab.hack.dena.api.response.AccessTokenResponse;
import dev.abelab.hack.dena.logic.UserLogic;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserLogic userLogic;

    private final UserRepository userRepository;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     *
     * @return アクセストークンレスポンス
     */
    @Transactional
    public AccessTokenResponse login(final LoginRequest requestBody) {
        // ユーザ情報を取得
        final var user = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        this.userLogic.verifyPassword(user, requestBody.getPassword());

        // JWTを発行
        final var jwt = this.userLogic.generateJwt(user);
        return AccessTokenResponse.builder() //
            .accessToken(jwt) //
            .tokenType("Bearer") //
            .build();
    }

}
