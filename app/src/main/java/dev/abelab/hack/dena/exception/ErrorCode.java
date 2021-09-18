package dev.abelab.hack.dena.exception;

import lombok.*;

/**
 * The enum error code
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Internal Server Error: 1000~1099
     */
    UNEXPECTED_ERROR(1000, "exception.internal_server_error.unexpected_error"),

    /**
     * Not Found: 1100~1199
     */
    NOT_FOUND_API(1100, "exception.not_found.api"),

    NOT_FOUND_USER(1101, "exception.not_found.user"),

    NOT_FOUND_REGION(1102, "exception.not_found.region"),

    NOT_FOUND_TRIP_PLAN(1103, "exception.not_found.trip_plan"),

    NOT_FOUND_TAG(1104, "exception.not_found.tag"),

    NOT_FOUND_USER_LIKE(1105, "exception.not_found.user_like"),

    /**
     * Conflict: 1200~1299
     */
    CONFLICT_EMAIL(1200, "exception.conflict.email"),

    CONFLICT_TAG_NAME(1201, "exception.conflict.tag_name"),

    CONFLICT_TRIP_PLAN_TAGGING(1202, "exception.conflict.trip_plan_tagging"),

    CONFLICT_TRIP_PLAN_ATTACHMENT(1203, "exception.conflict.trip_plan_attachment"),

    CONFLICT_USER_LIKE(1204, "exception.conflict.user_like"),


    /**
     * Forbidden: 1300~1399
     */
    USER_HAS_NO_PERMISSION(1300, "exception.forbidden.user_has_no_permission"),

    /**
     * Bad Request: 1400~1499
     */
    VALIDATION_ERROR(1400, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(1401, "exception.bad_request.invalid_request_parameter"),

    INVALID_PASSWORD_SIZE(1402, "exception.bad_request.invalid_password_size"),

    TOO_SIMPLE_PASSWORD(1403, "exception.bad_request.too_simple_password"),

    /**
     * Unauthorized: 1500~1599
     */
    USER_NOT_LOGGED_IN(1500, "exception.unauthorized.user_not_logged_in"),

    WRONG_PASSWORD(1501, "exception.unauthorized.wrong_password"),

    INVALID_ACCESS_TOKEN(1502, "exception.unauthorized.invalid_access_token"),

    EXPIRED_ACCESS_TOKEN(1503, "exception.unauthorized.expired_access_token");

    private final int code;

    private final String messageKey;

}
