package dev.abelab.hack.dena.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.api.request.LoginRequest;
import dev.abelab.hack.dena.api.request.SignupRequest;
import dev.abelab.hack.dena.api.response.AccessTokenResponse;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.logic.UserLogic;
import dev.abelab.hack.dena.util.AuthUtil;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final ModelMapper modelMapper;

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

    /**
     * サインアップ処理
     *
     * @param requestBody サインアップリクエスト
     *
     * @return アクセストークンレスポンス
     */
    @Transactional
    public AccessTokenResponse signup(final SignupRequest requestBody) {
        // 有効なパスワードかチェック
        AuthUtil.validatePassword(requestBody.getPassword());

        // ユーザを作成
        final var user = this.modelMapper.map(requestBody, User.class);
        user.setPassword(this.userLogic.encodePassword(requestBody.getPassword()));
        this.userRepository.insert(user);

        // JWTを発行
        final var jwt = this.userLogic.generateJwt(user);
        return AccessTokenResponse.builder() //
            .accessToken(jwt) //
            .tokenType("Bearer") //
            .build();
    }

}
