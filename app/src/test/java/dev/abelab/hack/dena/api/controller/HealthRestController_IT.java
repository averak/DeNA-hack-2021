package dev.abelab.hack.dena.api.controller;

import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

/**
 * HealthRestController Integration Test
 */
public class HealthRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/health";
	static final String HEALTH_CHECK_PATH = BASE_PATH;

	/**
	 * ヘルスチェックAPIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class HealthCheckTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_APIが正常に動いている() throws Exception {
			// test
			final var request = getRequest(HEALTH_CHECK_PATH);
			execute(request, HttpStatus.OK);
		}

	}

}
