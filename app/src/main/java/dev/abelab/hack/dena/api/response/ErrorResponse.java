package dev.abelab.hack.dena.api.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * error message
     */
    String message;

    /**
     * error code
     */
    int code;

}
