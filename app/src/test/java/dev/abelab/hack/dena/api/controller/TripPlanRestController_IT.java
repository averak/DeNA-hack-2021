package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.net.util.Base64;
import org.modelmapper.ModelMapper;

import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.entity.TripPlanSample;
import dev.abelab.hack.dena.db.entity.TripPlanItemSample;
import dev.abelab.hack.dena.db.entity.TripPlanAttachmentSample;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.model.TripPlanItemModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentSubmitModel;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.BaseException;
import dev.abelab.hack.dena.exception.NotFoundException;
import dev.abelab.hack.dena.exception.UnauthorizedException;

/**
 * TripPlanRestController Integration Test
 */
public class TripPlanRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/trip_plans";
	static final String GET_TRIP_PLANS_PATH = BASE_PATH;
	static final String CREATE_TRIP_PLAN_PATH = BASE_PATH;
	static final String DELETE_TRIP_PLAN_PATH = BASE_PATH + "/%d";
	static final String LIKE_TRIP_PLAN_PATH = BASE_PATH + "/%d/likes";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TripPlanRepository tripPlanRepository;

	/**
	 * 旅行プラン作成APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class TripPlanCreateTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_旅行プランを作成() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlan = TripPlanSample.builder().build();
			final var items = Arrays.asList( //
				TripPlanItemSample.builder().itemOrder(1).tripPlanId(tripPlan.getId()).build(), //
				TripPlanItemSample.builder().itemOrder(2).tripPlanId(tripPlan.getId()).build() //
			);
			final var attachment = TripPlanAttachmentSample.builder().build();
			final var attachmentSubmitmodel = modelMapper.map(attachment, TripPlanAttachmentSubmitModel.class);
			attachmentSubmitmodel.setContent(Base64.encodeBase64String(attachment.getContent()));

			final var requestBody = modelMapper.map(tripPlan, TripPlanCreateRequest.class);
			requestBody.setTags(Arrays.asList(SAMPLE_STR));
			requestBody.setItems(items.stream().map(item -> modelMapper.map(item, TripPlanItemModel.class)).collect(Collectors.toList()));
			requestBody.setAttachment(attachmentSubmitmodel);

			// test
			final var request = postRequest(CREATE_TRIP_PLAN_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.CREATED);

			// verify
			final var createdTripPlans = tripPlanRepository.selectAll();
			assertThat(createdTripPlans)
				.extracting(TripPlan::getTitle, TripPlan::getDescription, TripPlan::getRegionId, TripPlan::getUserId) //
				.containsExactlyInAnyOrder(
					tuple(tripPlan.getTitle(), tripPlan.getDescription(), tripPlan.getRegionId(), loginUser.getId()));
			// TODO: plan itemについてもテスト
		}

		@Test
		void 異_無効な位置情報() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlan = TripPlanSample.builder().regionId(48).build();
			final var requestBody = modelMapper.map(tripPlan, TripPlanCreateRequest.class);
			requestBody.setTags(Arrays.asList());
			requestBody.setItems(Arrays.asList());

			// test
			final var request = postRequest(CREATE_TRIP_PLAN_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_REGION));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// login user
			final var tripPlan = TripPlanSample.builder().build();
			final var requestBody = modelMapper.map(tripPlan, TripPlanCreateRequest.class);

			// test
			final var request = postRequest(CREATE_TRIP_PLAN_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
