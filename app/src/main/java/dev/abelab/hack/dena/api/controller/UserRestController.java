package dev.abelab.hack.dena.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.hack.dena.annotation.Authenticated;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.hack.dena.service.UserService;
import dev.abelab.hack.dena.api.response.UserResponse;

@Api(tags = "User")
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class UserRestController {

    private final UserService userService;

    /**
     * ログインユーザのパスワード更新API
     *
     * @param requestBody ログインユーザのパスワード更新リクエスト
     * @param loginUser   ログインユーザ
     */
    @ApiOperation( //
        value = "ログインユーザのパスワード更新", //
        notes = "ログインユーザのパスワードを更新する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "更新成功"), //
                @ApiResponse(code = 400, message = "無効なパスワード"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @PutMapping(value = "/me/password")
    @ResponseStatus(HttpStatus.OK)
    public void updateLoginUserPassword( //
        @ModelAttribute("LoginUser") final User loginUser, //
        @Validated @ApiParam(name = "body", required = true, value = "パスワード更新情報")
        @RequestBody final LoginUserPasswordUpdateRequest requestBody //
    ) {
        this.userService.updateLoginPasswordUser(requestBody, loginUser);
    }

    /**
     * プロフィール取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ユーザ詳細レスポンス
     */
    @ApiOperation( //
        value = "プロフィール取得", //
        notes = "プロフィールを取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = UserResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 404, message = "ユーザが存在しない") //
        })
    @GetMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getLoginUser( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.userService.getUserProfile(loginUser);
    }
}
