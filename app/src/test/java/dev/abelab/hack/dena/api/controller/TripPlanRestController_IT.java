package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.net.util.Base64;
import org.modelmapper.ModelMapper;

import dev.abelab.hack.dena.db.entity.UserSample;
import dev.abelab.hack.dena.db.entity.TripPlan;
import dev.abelab.hack.dena.db.entity.TripPlanSample;
import dev.abelab.hack.dena.db.entity.TripPlanItemSample;
import dev.abelab.hack.dena.db.entity.TripPlanItem;
import dev.abelab.hack.dena.db.entity.TripPlanAttachment;
import dev.abelab.hack.dena.db.entity.TripPlanAttachmentSample;
import dev.abelab.hack.dena.db.entity.TagSample;
import dev.abelab.hack.dena.db.entity.TripPlanTaggingSample;
import dev.abelab.hack.dena.db.entity.UserLikeSample;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.api.response.TripPlanResponse;
import dev.abelab.hack.dena.api.response.TripPlansResponse;
import dev.abelab.hack.dena.model.TripPlanItemModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentSubmitModel;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.repository.TripPlanItemRepository;
import dev.abelab.hack.dena.repository.TripPlanAttachmentRepository;
import dev.abelab.hack.dena.repository.TagRepository;
import dev.abelab.hack.dena.repository.TripPlanTaggingRepository;
import dev.abelab.hack.dena.repository.UserLikeRepository;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.NotFoundException;
import dev.abelab.hack.dena.exception.ForbiddenException;
import dev.abelab.hack.dena.exception.UnauthorizedException;
import dev.abelab.hack.dena.api.response.UserLikesResponse;
import dev.abelab.hack.dena.api.request.UserLikeRequest;

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
	static final String GET_LIKE_TRIP_PLANS_PATH = BASE_PATH + "/likes/me";
	static final String DOWNLOAD_ATTACHMENT_PATH = BASE_PATH + "/attachments/%s";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TripPlanRepository tripPlanRepository;

	@Autowired
	TripPlanItemRepository tripPlanItemRepository;

	@Autowired
	TripPlanAttachmentRepository tripPlanAttachmentRepository;

	@Autowired
	TagRepository tagRepository;

	@Autowired
	TripPlanTaggingRepository tripPlanTaggingRepository;

	@Autowired
	UserLikeRepository userLikeRepository;

	/**
	 * 旅行プラン一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetTripPlansTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_旅行プラン一覧を取得() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlan = TripPlanSample.builder().userId(loginUser.getId()).build();
			tripPlanRepository.insert(tripPlan);

			final var items = Arrays.asList( //
				TripPlanItemSample.builder().itemOrder(1).tripPlanId(tripPlan.getId()).build(), //
				TripPlanItemSample.builder().itemOrder(2).tripPlanId(tripPlan.getId()).build() //
			);
			tripPlanItemRepository.bulkInsert(items);

			final var attachment = TripPlanAttachmentSample.builder().tripPlanId(tripPlan.getId()).build();
			tripPlanAttachmentRepository.insert(attachment);

			final var tags = Arrays.asList( //
				TagSample.builder().name("タグ1").build(), //
				TagSample.builder().name("タグ2").build() //
			);
			tagRepository.bulkInsert(tags);

			final var tripPlanTaggings = tags.stream() //
				.map(tag -> TripPlanTaggingSample.builder().tripPlanId(tripPlan.getId()).tagId(tag.getId()).build()) //
				.collect(Collectors.toList());
			tripPlanTaggingRepository.bulkInsert(tripPlanTaggings);

			final var users = Arrays.asList( //
				UserSample.builder().email("email1").build(), //
				UserSample.builder().email("email2").build(), //
				UserSample.builder().email("email3").build() //
			);
			users.forEach(userRepository::insert);
			final var userLikes = Arrays.asList( //
				UserLikeSample.builder().userId(users.get(0).getId()).tripPlanId(tripPlan.getId()).build(), //
				UserLikeSample.builder().userId(users.get(1).getId()).tripPlanId(tripPlan.getId()).build() //
			);
			userLikes.forEach(userLikeRepository::insert);

			// test
			final var request = getRequest(GET_TRIP_PLANS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, TripPlansResponse.class);

			// verify
			// 旅行プラン
			assertThat(response.getTripPlans())
				.extracting(TripPlanResponse::getId, TripPlanResponse::getTitle, TripPlanResponse::getDescription,
					TripPlanResponse::getRegionId) //
				.containsExactlyInAnyOrder(tuple(tripPlan.getId(), tripPlan.getTitle(), tripPlan.getDescription(), tripPlan.getRegionId()));

			// 旅行プラン項目
			assertThat(response.getTripPlans().get(0).getItems())
				.extracting(TripPlanItemModel::getItemOrder, TripPlanItemModel::getTitle, TripPlanItemModel::getDescription,
					TripPlanItemModel::getPrice) //
				.containsExactlyInAnyOrderElementsOf(
					items.stream().map(item -> tuple(item.getItemOrder(), item.getTitle(), item.getDescription(), item.getPrice()))
						.collect(Collectors.toList()));

			// タグ
			assertThat(response.getTripPlans().get(0).getTags().size()).isEqualTo(tags.size());

			// いいね数
			assertThat(response.getTripPlans().get(0).getLikes()).isEqualTo(userLikes.size());

			// 添付ファイル
			assertThat(response.getTripPlans().get(0).getAttachment())
				.extracting(TripPlanAttachmentModel::getFileName, TripPlanAttachmentModel::getUuid) //
				.containsExactlyInAnyOrder(attachment.getFileName(), attachment.getUuid());
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = getRequest(GET_TRIP_PLANS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * 旅行プランのいいね登録APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class LikeTripPlanTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_旅行プランをいいねする() throws Exception {
			// setup
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlans = Arrays.asList( //
				TripPlanSample.builder().userId(loginUser.getId()).build(), //
				TripPlanSample.builder().userId(loginUser.getId()).build() //
			);
			tripPlans.forEach(tripPlanRepository::insert);

			final var requestBody = UserLikeRequest.builder() //
				.isLike(true) //
				.build();

			// test
			final var request = putRequest(String.format(LIKE_TRIP_PLAN_PATH, tripPlans.get(0).getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, UserLikesResponse.class);

			// verify
			assertThat(response.getLikes()).isEqualTo(1);
			final var existsUserLikes = userLikeRepository.selectByUserId(loginUser.getId());
			assertThat(existsUserLikes.size()).isEqualTo(1);
		}

		@Test
		void 正_お気に入り削除() throws Exception {
			// setup
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlans = Arrays.asList( //
				TripPlanSample.builder().userId(loginUser.getId()).build(), //
				TripPlanSample.builder().userId(loginUser.getId()).build() //
			);
			tripPlans.forEach(tripPlanRepository::insert);

			// いいね済みにする
			final var userLikes = Arrays.asList( //
				UserLikeSample.builder().userId(loginUser.getId()).tripPlanId(tripPlans.get(0).getId()).build(), //
				UserLikeSample.builder().userId(loginUser.getId()).tripPlanId(tripPlans.get(1).getId()).build() //
			);
			userLikes.forEach(userLikeRepository::insert);

			final var requestBody = UserLikeRequest.builder() //
				.isLike(false) //
				.build();

			// test
			final var request = putRequest(String.format(LIKE_TRIP_PLAN_PATH, tripPlans.get(0).getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, UserLikesResponse.class);

			// verify
			assertThat(response.getLikes()).isEqualTo(0);
			final var existsUserLikes = userLikeRepository.selectByUserId(loginUser.getId());
			assertThat(existsUserLikes.size()).isEqualTo(1);
		}
	}

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
				TripPlanItemSample.builder().itemOrder(1).build(), //
				TripPlanItemSample.builder().itemOrder(2).build() //
			);
			final var attachment = TripPlanAttachmentSample.builder().build();
			final var attachmentSubmitModel = modelMapper.map(attachment, TripPlanAttachmentSubmitModel.class);
			attachmentSubmitModel.setContent(Base64.encodeBase64String(attachment.getContent()));
			final var tags = Arrays.asList("タグ1", "タグ2");

			final var requestBody = modelMapper.map(tripPlan, TripPlanCreateRequest.class);
			requestBody.setTags(tags);
			requestBody.setItems(items.stream().map(item -> modelMapper.map(item, TripPlanItemModel.class)).collect(Collectors.toList()));
			requestBody.setAttachment(attachmentSubmitModel);

			// test
			final var request = postRequest(CREATE_TRIP_PLAN_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.CREATED);

			// verify
			// 旅行プラン
			final var createdTripPlans = tripPlanRepository.selectAll();
			assertThat(createdTripPlans)
				.extracting(TripPlan::getTitle, TripPlan::getDescription, TripPlan::getRegionId, TripPlan::getUserId) //
				.containsExactlyInAnyOrder(
					tuple(tripPlan.getTitle(), tripPlan.getDescription(), tripPlan.getRegionId(), loginUser.getId()));

			// 旅行プラン項目
			final var createdTripPlanItems = tripPlanItemRepository.selectByTripPlanId(createdTripPlans.get(0).getId());
			assertThat(createdTripPlanItems)
				.extracting(TripPlanItem::getTripPlanId, TripPlanItem::getItemOrder, TripPlanItem::getTitle, TripPlanItem::getDescription,
					TripPlanItem::getPrice) //
				.containsExactlyInAnyOrderElementsOf(items.stream().map(item -> tuple(createdTripPlans.get(0).getId(), item.getItemOrder(),
					item.getTitle(), item.getDescription(), item.getPrice())).collect(Collectors.toList()));

			// タグ
			final var createdTags = tagRepository.selectByTripPlanId(createdTripPlans.get(0).getId());
			assertThat(createdTags.size()).isEqualTo(tags.size());

			// 添付ファイル
			final var createdTripPlanAttachments = tripPlanAttachmentRepository.selectByTripPlanId(createdTripPlans.get(0).getId());
			assertThat(createdTripPlanAttachments)
				.extracting(TripPlanAttachment::getTripPlanId, TripPlanAttachment::getFileName, TripPlanAttachment::getContent) //
				.containsExactlyInAnyOrder( //
					createdTripPlans.get(0).getId(), attachment.getFileName(), attachment.getContent() //
				);
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

	/**
	 * 旅行プラン削除APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class TripPlanDeleteTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_作成者が旅行プランを削除() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlan = TripPlanSample.builder().userId(loginUser.getId()).build();
			tripPlanRepository.insert(tripPlan);

			// test
			final var request = deleteRequest(String.format(DELETE_TRIP_PLAN_PATH, tripPlan.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);
		}

		@Test
		void 異_作成者以外は旅行プランを削除不可() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var author = UserSample.builder().build();
			userRepository.insert(author);

			final var tripPlan = TripPlanSample.builder().userId(author.getId()).build();
			tripPlanRepository.insert(tripPlan);

			// test
			final var request = deleteRequest(String.format(DELETE_TRIP_PLAN_PATH, tripPlan.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = deleteRequest(String.format(DELETE_TRIP_PLAN_PATH, SAMPLE_INT));
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * お気に入り旅行プラン一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetLikeTripPlansTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_お気に入りにの旅行プラン一覧を取得() throws Exception {

			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlans = Arrays.asList( //
				TripPlanSample.builder().userId(loginUser.getId()).build(), //
				TripPlanSample.builder().userId(loginUser.getId()).build(), //
				TripPlanSample.builder().userId(loginUser.getId()).build() //
			);
			tripPlans.forEach(tripPlanRepository::insert);

			final var otherUser = UserSample.builder().build();
			userRepository.insert(otherUser);

			// 旅行プラン0と2をいいねする
			final var userLikes = Arrays.asList( //
				// login user
				UserLikeSample.builder().userId(loginUser.getId()).tripPlanId(tripPlans.get(0).getId()).build(), //
				UserLikeSample.builder().userId(loginUser.getId()).tripPlanId(tripPlans.get(2).getId()).build(), //
				// other user
				UserLikeSample.builder().userId(otherUser.getId()).tripPlanId(tripPlans.get(0).getId()).build(), //
				UserLikeSample.builder().userId(otherUser.getId()).tripPlanId(tripPlans.get(1).getId()).build() //
			);
			userLikes.forEach(userLikeRepository::insert);

			// test
			final var request = getRequest(GET_LIKE_TRIP_PLANS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, TripPlansResponse.class);

			// verify
			assertThat(response.getTripPlans()).extracting(TripPlanResponse::getId) //
				.containsExactlyInAnyOrder(tripPlans.get(0).getId(), tripPlans.get(2).getId());
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = getRequest(GET_LIKE_TRIP_PLANS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * 添付ファイルダウンロードAPIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class DownloadAttachmentTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_添付ファイルをダウンロード() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var tripPlan = TripPlanSample.builder().userId(loginUser.getId()).build();
			tripPlanRepository.insert(tripPlan);

			final var items = Arrays.asList( //
				TripPlanItemSample.builder().itemOrder(1).tripPlanId(tripPlan.getId()).build(), //
				TripPlanItemSample.builder().itemOrder(2).tripPlanId(tripPlan.getId()).build() //
			);
			tripPlanItemRepository.bulkInsert(items);

			final var attachment = TripPlanAttachmentSample.builder().tripPlanId(tripPlan.getId()).build();
			tripPlanAttachmentRepository.insert(attachment);

			// test
			final var request = getRequest(String.format(DOWNLOAD_ATTACHMENT_PATH, attachment.getUuid()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK);

			// verify
			assertThat(response.getResponse().getContentLength()).isEqualTo(attachment.getContent().length);
		}

		@Test
		void 異_添付ファイルが存在しない() throws Exception {
			// login user
			final var loginUser = createLoginUser();
			final var credentials = getLoginUserCredentials(loginUser);

			final var attachment = TripPlanAttachmentSample.builder().build();

			// test
			final var request = getRequest(String.format(DOWNLOAD_ATTACHMENT_PATH, attachment.getUuid()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_ATTACHMENT));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			// test
			final var request = getRequest(String.format(DOWNLOAD_ATTACHMENT_PATH, SAMPLE_STR));
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
