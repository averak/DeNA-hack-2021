package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import dev.abelab.hack.dena.db.entity.UserSample;
import dev.abelab.hack.dena.api.request.LoginRequest;
import dev.abelab.hack.dena.api.response.AccessTokenResponse;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.NotFoundException;
import dev.abelab.hack.dena.exception.UnauthorizedException;

/**
 * AuthRestController Integration Test
 */
public class AuthRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api";
	static final String LOGIN_PATH = BASE_PATH + "/login";

	/**
	 * ログインAPIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class LoginTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ユーザがログイン() throws Exception {
			// setup
			createLoginUser();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(LOGIN_USER_EMAIL) //
				.password(LOGIN_USER_PASSWORD) //
				.build();

			// test
			final var request = postRequest(LOGIN_PATH, requestBody);
			final var response = execute(request, HttpStatus.OK, AccessTokenResponse.class);

			// verify
			assertThat(response.getAccessToken()).isNotNull();
			assertThat(response.getTokenType()).isEqualTo("Bearer");
		}

		@Test
		void 異_存在しないユーザがログイン() throws Exception {
			// setup
			final var loginUser = UserSample.builder().build();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(loginUser.getEmail()) //
				.password(loginUser.getPassword()) //
				.build();

			// test
			final var request = postRequest(LOGIN_PATH, requestBody);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

		@Test
		void 異_パスワードが間違えている() throws Exception {
			// setup
			createLoginUser();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(LOGIN_USER_EMAIL) //
				.password(LOGIN_USER_PASSWORD + "dummy") //
				.build();

			// test
			final var request = postRequest(LOGIN_PATH, requestBody);
			execute(request, new UnauthorizedException(ErrorCode.WRONG_PASSWORD));
		}

	}

}
