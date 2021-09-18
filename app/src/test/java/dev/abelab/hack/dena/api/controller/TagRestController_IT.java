package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import java.util.Arrays;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import dev.abelab.hack.dena.db.entity.TagSample;
import dev.abelab.hack.dena.api.response.TagsResponse;
import dev.abelab.hack.dena.repository.TagRepository;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.UnauthorizedException;

/**
 * TagRestController Integration Test
 */
public class TagRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/tags";
	static final String GET_TAGS_PATH = BASE_PATH;

	@Autowired
	TagRepository tagRepository;

	/**
	 * タグ一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetTagsTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_タグ一覧を取得() throws Exception {
			// setup
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tags = Arrays.asList( //
				TagSample.builder().name("タグ1").build(), //
				TagSample.builder().name("タグ2").build(), //
				TagSample.builder().name("タグ3").build() //
			);
			tagRepository.bulkInsert(tags);

			// test
			final var request = getRequest(GET_TAGS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, TagsResponse.class);

			// verify
			assertThat(response.getTags().size()).isEqualTo(tags.size());
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = getRequest(GET_TAGS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
