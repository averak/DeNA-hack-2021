package dev.abelab.hack.dena.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.logic.UserLogic;
import dev.abelab.hack.dena.util.AuthUtil;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserLogic userLogic;

    /**
     * ログインユーザのパスワードを更新
     *
     * @param requestBody ログインユーザパスワード更新リクエスト
     * @param loginUser   ログインユーザ
     */
    @Transactional
    public void updateLoginPasswordUser(final LoginUserPasswordUpdateRequest requestBody, final User loginUser) {
        // 現在のパスワードチェック
        this.userLogic.verifyPassword(loginUser, requestBody.getCurrentPassword());

        // 有効なパスワードかチェック
        AuthUtil.validatePassword(requestBody.getNewPassword());

        // ログインユーザのパスワードを更新
        loginUser.setPassword(this.userLogic.encodePassword(requestBody.getNewPassword()));
        this.userRepository.update(loginUser);
    }

}
