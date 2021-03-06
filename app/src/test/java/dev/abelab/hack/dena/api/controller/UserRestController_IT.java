package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.UserSample;
import dev.abelab.hack.dena.api.request.LoginUserUpdateRequest;
import dev.abelab.hack.dena.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.hack.dena.api.response.UserResponse;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.BaseException;
import dev.abelab.hack.dena.exception.BadRequestException;
import dev.abelab.hack.dena.exception.NotFoundException;
import dev.abelab.hack.dena.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String UPDATE_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String DELETE_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String UPDATE_LOGIN_USER_PASSWORD_PATH = BASE_PATH + "/me/password";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	/**
	 * ログインユーザ詳細取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザの詳細を取得() throws Exception {
			// setup
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			// test
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, UserResponse.class);

			// verify
			assertThat(response) //
				.extracting(UserResponse::getId, UserResponse::getEmail, UserResponse::getFirstName, UserResponse::getLastName) //
				.containsExactly( //
					loginUser.getId(), loginUser.getEmail(), loginUser.getFirstName(), loginUser.getLastName());
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ログインユーザ更新APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザを更新() throws Exception {
			// setup
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			userRepository.insert(user);

			user.setEmail(user.getEmail() + "xxx");
			user.setFirstName(user.getFirstName() + "xxx");
			user.setLastName(user.getLastName() + "xxx");
			final var requestBody = modelMapper.map(user, LoginUserUpdateRequest.class);

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			// verify
			final var updatedUser = userRepository.selectByEmail(requestBody.getEmail());
			assertThat(updatedUser) //
				.extracting(User::getEmail, User::getFirstName, User::getLastName) //
				.containsExactly(user.getEmail(), user.getFirstName(), user.getLastName());
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// setup
			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			final var requestBody = modelMapper.map(user, LoginUserUpdateRequest.class);

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ログインユーザ削除APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class DeleteLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザを削除() throws Exception {
			// setup
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			// test
			final var request = deleteRequest(DELETE_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			// verify
			final var occurredException = assertThrows(NotFoundException.class, () -> userRepository.selectById(loginUser.getId()));
			assertThat(occurredException.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = deleteRequest(DELETE_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

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
