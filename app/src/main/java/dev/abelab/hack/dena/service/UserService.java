package dev.abelab.hack.dena.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.api.request.LoginUserUpdateRequest;
import dev.abelab.hack.dena.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.logic.UserLogic;
import dev.abelab.hack.dena.util.AuthUtil;
import dev.abelab.hack.dena.api.response.UserResponse;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;

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

    /**
     * プロフィール情報を取得
     *
     * @param loginUser ログインユーザ
     *
     * @return プロフィール情報レスポンス
     */
    @Transactional
    public UserResponse getUserProfile(final User loginUser) {
        return this.modelMapper.map(loginUser, UserResponse.class);
    }

    /**
     * ログインユーザを更新
     *
     * @param requestBody ログインユーザ更新リクエスト
     * @param loginUser   ログインユーザ
     */
    @Transactional
    public void updateLoginUser(final LoginUserUpdateRequest requestBody, final User loginUser) {
        // ログインユーザを更新
        loginUser.setFirstName(requestBody.getFirstName());
        loginUser.setLastName(requestBody.getLastName());
        loginUser.setEmail(requestBody.getEmail());
        this.userRepository.update(loginUser);
    }

    /**
     * ログインユーザを削除
     *
     * @param loginUser ログインユーザ
     */
    @Transactional
    public void deleteLoginUser(final User loginUser) {
        // ログインユーザを削除
        this.userRepository.deleteById(loginUser.getId());
    }

}
