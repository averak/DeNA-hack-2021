package dev.abelab.hack.dena.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.hack.dena.annotation.Authenticated;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.api.request.TripPlanCreateRequest;
import dev.abelab.hack.dena.api.response.TripPlansResponse;
import dev.abelab.hack.dena.service.TripPlanService;

@Api(tags = "TripPlan")
@RestController
@RequestMapping(path = "/api/trip_plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class TripPlanRestController {

    private final TripPlanService tripPlanService;

    /**
     * 旅行プランの一覧取得API
     *
     * @param loginUser ログインユーザ
     */
    @ApiOperation( //
        value = "旅行プランの一覧取得", //
        notes = "旅行プランを一覧取得する" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = TripPlansResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TripPlansResponse getTripPlans( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.tripPlanService.getTripPlans(loginUser);
    }

    /**
     * 旅行プランの作成API
     *
     * @param requestBody 旅行プラン作成リクエスト
     * @param loginUser   ログインユーザ
     */
    @ApiOperation( //
        value = "旅行プランの作成", //
        notes = "旅行プランを作成する" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 201, message = "作成成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 404, message = "都道府県IDが存在しない"), //
        } //
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTripPlan( //
        @ModelAttribute("LoginUser") final User loginUser, //
        @Validated @ApiParam(name = "body", required = true, value = "旅行プラン作成情報") @RequestBody final TripPlanCreateRequest requestBody //
    ) {
        this.tripPlanService.createTripPlan(requestBody, loginUser);
    }

    /**
     * 旅行プランの削除API
     *
     * @param tripPlanId 旅行プランID
     * @param loginUser  ログインユーザ
     */
    @ApiOperation( //
        value = "旅行プランの削除", //
        notes = "旅行プランを削除する" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 201, message = "作成成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 404, message = "旅行プランが存在しない"), //
        } //
    )
    @DeleteMapping(value = "/{trip_plan_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTripPlan( //
        @ModelAttribute("LoginUser") final User loginUser, //
        @ApiParam(name = "trip_plan_id", required = true, value = "旅行プランID") @PathVariable("trip_plan_id") final int tripPlanId //
    ) {
        this.tripPlanService.deleteTripPlan(tripPlanId, loginUser);
    }

    /**
     * いいねした旅行プランの一覧取得API
     *
     * @param loginUser ログインユーザ
     */
    @ApiOperation( //
        value = "いいねした旅行プランの一覧取得", //
        notes = "いいねした旅行プランを一覧取得する" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = TripPlansResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @GetMapping("/likes/me")
    @ResponseStatus(HttpStatus.OK)
    public TripPlansResponse getLikedTripPlans( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.tripPlanService.getLikedTripPlans(loginUser);
    }

}
