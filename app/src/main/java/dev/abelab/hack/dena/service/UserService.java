package dev.abelab.hack.dena.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import java.util.List;

import lombok.*;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.UserLike;
import dev.abelab.hack.dena.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.repository.UserLikeRepository;
import dev.abelab.hack.dena.logic.UserLogic;
import dev.abelab.hack.dena.util.AuthUtil;
import dev.abelab.hack.dena.api.response.UserResponse;
import dev.abelab.hack.dena.api.response.UserLikesResponse;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final UserLikeRepository userLikeRepository;

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
     * お気に入り取得
     *
     * @param loginUser ログインユーザ
     *
     * @return お気に入り情報レスポンス
     */
    @Transactional
    public UserLikesResponse getUserLikes(final User loginUser) {
        System.out.println("test");
        List<UserLike> userLikes = this.userLikeRepository.selectById(loginUser.getId());
        // forEachメソッドでループ
        userLikes.forEach(item -> System.out.println(item));
        return new UserLikesResponse();
    }

}
