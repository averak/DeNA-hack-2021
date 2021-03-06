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
	 * ?????????????????????????????????API????????????
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void ???_???????????????????????????????????????() throws Exception {
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
		void ???_????????????????????????() throws Exception {
			// test
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ???????????????????????????API????????????
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void ???_??????????????????????????????() throws Exception {
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
		void ???_????????????????????????() throws Exception {
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
	 * ???????????????????????????API????????????
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class DeleteLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void ???_??????????????????????????????() throws Exception {
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
		void ???_????????????????????????() throws Exception {
			// test
			final var request = deleteRequest(DELETE_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ??????????????????????????????????????????API????????????
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateLoginUserPasswordTest extends AbstractRestControllerInitialization_IT {

		@Test
		void ???_????????????????????????????????????????????????() throws Exception {
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
		void ???_?????????????????????????????????????????????() throws Exception {
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
		void ????????????????????????????????????(final String password, final BaseException exception) throws Exception {
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

		Stream<Arguments> ????????????????????????????????????() {
			return Stream.of( //
				// ??????
				arguments("f4BabxEr", null), //
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdA", null), //
				// ?????????8????????????
				arguments("f4BabxE", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// ?????????33????????????
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdAN", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// ?????????????????????????????????
				arguments("f4babxer", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// ?????????????????????????????????
				arguments("F4BABXER", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// ??????????????????????????????
				arguments("fxbabxEr", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)) //
			);
		}

		@Test
		void ???_????????????????????????() throws Exception {
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
