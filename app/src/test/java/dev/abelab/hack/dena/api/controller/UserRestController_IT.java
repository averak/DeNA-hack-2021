package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.BaseException;
import dev.abelab.hack.dena.exception.BadRequestException;
import dev.abelab.hack.dena.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;
	static final String CREATE_USER_PATH = BASE_PATH;
	static final String UPDATE_USER_PATH = BASE_PATH + "/%d";
	static final String DELETE_USER_PATH = BASE_PATH + "/%d";
	static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String UPDATE_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String UPDATE_LOGIN_USER_PASSWORD_PATH = BASE_PATH + "/me/password";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	/**
	 * ログインユーザパスワード更新APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateLoginUserPasswordTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザのパスワードを更新() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			// request body
			final var requestBody = LoginUserPasswordUpdateRequest.builder() //
				.currentPassword(LOGIN_USER_PASSWORD) //
				.newPassword(LOGIN_USER_PASSWORD + "xxx") //
				.build();

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PASSWORD_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			// verify
			final var updatedUser = userRepository.selectById(loginUser.getId());
			assertThat(passwordEncoder.matches(requestBody.getNewPassword(), updatedUser.getPassword())).isTrue();
		}

		@Test
		void 異_現在のパスワードが間違えている() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			// request body
			final var requestBody = LoginUserPasswordUpdateRequest.builder() //
				.currentPassword(LOGIN_USER_PASSWORD + "xxx") //
				.newPassword(LOGIN_USER_PASSWORD + "xxx") //
				.build();

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PASSWORD_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new UnauthorizedException(ErrorCode.WRONG_PASSWORD));
		}

		@ParameterizedTest
		@MethodSource
		void パスワードが有効かどうか(final String password, final BaseException exception) throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			// request body
			final var requestBody = LoginUserPasswordUpdateRequest.builder() //
				.currentPassword(LOGIN_USER_PASSWORD) //
				.newPassword(password) //
				.build();

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PASSWORD_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			if (exception == null) {
				execute(request, HttpStatus.OK);
			} else {
				execute(request, exception);
			}
		}

		Stream<Arguments> パスワードが有効かどうか() {
			return Stream.of( //
				// 有効
				arguments("f4BabxEr", null), //
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdA", null), //
				// 無効：8文字以下
				arguments("f4BabxE", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// 無効：33文字以上
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdAN", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// 無効：大文字を含まない
				arguments("f4babxer", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// 無効：小文字を含まない
				arguments("F4BABXER", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// 無効：数字を含まない
				arguments("fxbabxEr", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)) //
			);
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// request body
			final var requestBody = LoginUserPasswordUpdateRequest.builder() //
				.currentPassword(LOGIN_USER_PASSWORD) //
				.newPassword(LOGIN_USER_PASSWORD) //
				.build();

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PASSWORD_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}