package dev.abelab.hack.dena.api.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import dev.abelab.hack.dena.annotation.IntegrationTest;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.db.entity.UserSample;
import dev.abelab.hack.dena.repository.UserRepository;
import dev.abelab.hack.dena.logic.UserLogic;
import dev.abelab.hack.dena.util.ConvertUtil;
import dev.abelab.hack.dena.exception.BaseException;
import dev.abelab.hack.dena.api.response.ErrorResponse;
import dev.abelab.hack.dena.repository.UserLikeRepository;
import dev.abelab.hack.dena.repository.TripPlanRepository;
import dev.abelab.hack.dena.db.entity.UserLikeSample;
import dev.abelab.hack.dena.db.entity.TripPlanSample;
import dev.abelab.hack.dena.db.entity.UserLike;
import dev.abelab.hack.dena.db.entity.TripPlan;

/**
 * Abstract Rest Controller Integration Test
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@IntegrationTest
public abstract class AbstractRestController_IT {

	protected static final int SAMPLE_INT = 1;
	protected static final String SAMPLE_STR = "SAMPLE STRING";
	protected static final Date SAMPLE_DATE = new Date();
	protected static final String LOGIN_USER_EMAIL = "login_user@abelab.dev";
	protected static final String LOGIN_USER_PASSWORD = "f4BabxEr7xA6";
	protected static final Integer LOGIN_USER_ADMISSION_AT = SAMPLE_INT;

	MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserLikeRepository userLikeRepository;

	@Autowired
	private TripPlanRepository tripPlanRepository;

	@Autowired
	private UserLogic userLogic;

	/**
	 * GET request
	 *
	 * @param path path
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder getRequest(final String path) {
		return MockMvcRequestBuilders.get(path);
	}

	/**
	 * POST request
	 *
	 * @param path path
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder postRequest(final String path) {
		return MockMvcRequestBuilders.post(path);
	}

	/**
	 * POST request (Form)
	 *
	 * @param path   path
	 *
	 * @param params query params
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder postRequest(final String path, final MultiValueMap<String, String> params) {
		return MockMvcRequestBuilders.post(path) //
			.contentType(MediaType.APPLICATION_FORM_URLENCODED) //
			.params(params);
	}

	/**
	 * POST request (JSON)
	 *
	 * @param path    path
	 *
	 * @param content request body
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder postRequest(final String path, final Object content) {
		return MockMvcRequestBuilders.post(path) //
			.contentType(MediaType.APPLICATION_JSON) //
			.content(ConvertUtil.convertObjectToJson(content));
	}

	/**
	 * PUT request (JSON)
	 *
	 * @param path    path
	 *
	 * @param content request body
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder putRequest(final String path, final Object content) {
		return MockMvcRequestBuilders.put(path) //
			.contentType(MediaType.APPLICATION_JSON) //
			.content(ConvertUtil.convertObjectToJson(content));
	}

	/**
	 * DELETE request
	 *
	 * @param path path
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder deleteRequest(final String path) {
		return MockMvcRequestBuilders.delete(path);
	}

	/**
	 * Execute request
	 *
	 * @param request HTTP request builder
	 *
	 * @param status  expected HTTP status
	 *
	 * @return MVC result
	 *
	 * @throws Exception exception
	 */
	public MvcResult execute(final MockHttpServletRequestBuilder request, final HttpStatus status) throws Exception {
		final var result = mockMvc.perform(request).andReturn();

		try {
			assertThat(result.getResponse().getStatus()).isEqualTo(status.value());
		} catch (final AssertionError e) {
			Optional.ofNullable(result.getResolvedException()).ifPresent(Throwable::printStackTrace);
			throw e;
		}

		return result;
	}

	/**
	 * Execute request / return response
	 *
	 * @param request HTTP request builder
	 *
	 * @param status  expected HTTP status
	 *
	 * @param clazz   response class
	 *
	 * @return response
	 *
	 * @throws Exception exception
	 */
	public <T> T execute(final MockHttpServletRequestBuilder request, final HttpStatus status, final Class<T> clazz) throws Exception {
		final var result = mockMvc.perform(request).andReturn();

		try {
			assertThat(result.getResponse().getStatus()).isEqualTo(status.value());
		} catch (final AssertionError e) {
			Optional.ofNullable(result.getResolvedException()).ifPresent(Throwable::printStackTrace);
			throw e;
		}

		return ConvertUtil.convertJsonToObject(result.getResponse().getContentAsString(), clazz);
	}

	/**
	 * Execute request / verify exception
	 *
	 * @param request   HTTP request builder
	 *
	 * @param exception expected exception
	 *
	 * @return error response
	 *
	 * @throws Exception exception
	 */
	public ErrorResponse execute(final MockHttpServletRequestBuilder request, final BaseException exception) throws Exception {
		final var result = mockMvc.perform(request).andReturn();
		final var response = ConvertUtil.convertJsonToObject(result.getResponse().getContentAsString(), ErrorResponse.class);

		try {
			assertThat(result.getResponse().getStatus()).isEqualTo(exception.getHttpStatus().value());
			assertThat(response.getCode()).isEqualTo(exception.getErrorCode().getCode());
		} catch (final AssertionError e) {
			Optional.ofNullable(result.getResolvedException()).ifPresent(Throwable::printStackTrace);
			throw e;
		}

		return response;
	}

	/**
	 * ログインユーザを作成
	 *
	 * @return loginUser
	 */
	public User createLoginUser() {
		final var loginUser = UserSample.builder() //
			.email(LOGIN_USER_EMAIL) //
			.password(this.userLogic.encodePassword(LOGIN_USER_PASSWORD)) //
			.build();
		this.userRepository.insert(loginUser);

		return loginUser;
	}

	/**
	 * ログインユーザの資格情報を取得
	 *
	 * @param user ログインユーザ
	 *
	 * @return 資格情報
	 */
	public String getLoginUserCredentials(User user) throws Exception {
		return "Bearer " + this.userLogic.generateJwt(user);
	}

		/**
	 * ログインユーザのお気に入り作成
	 *
	 * @return loginUser
	 */
	public TripPlan createTripPlan(User loginUser) {

		final var userTripPlan = TripPlanSample.builder() //
		.userId(loginUser.getId()) //
		.title(SAMPLE_STR) //
		.description(SAMPLE_STR) //
		.regionId(SAMPLE_INT) //
		.createdAt(SAMPLE_DATE) //
		.updatedAt(SAMPLE_DATE) //
		.build();
		this.tripPlanRepository.insert(userTripPlan);

		return userTripPlan;
	}

	/**
	 * ログインユーザのお気に入り作成
	 *
	 * @return loginUser
	 */
	public UserLike createUserLike(User loginUser, TripPlan tripPlan) {

		final var userLike = UserLikeSample.builder() //
		.userId(loginUser.getId()) //
		.tripPlanId(tripPlan.getId()) //
		.build();
		this.userLikeRepository.insert(userLike);

		return userLike;
	}

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders //
			.webAppContextSetup(this.webApplicationContext) //
			.build();
	}

	/**
	 * Abstract rest controller initialization IT
	 */
	public abstract class AbstractRestControllerInitialization_IT {

		private TransactionDefinition def;
		private TransactionStatus status;

		@BeforeEach
		public void before() {
			this.def = new DefaultTransactionDefinition();
			status = transactionManager.getTransaction(this.def);
		}

		@AfterEach
		public void after() {
			transactionManager.rollback(this.status);
		}

	}

}
