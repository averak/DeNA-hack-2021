package dev.abelab.hack.dena.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.hack.dena.annotation.Authenticated;
import dev.abelab.hack.dena.db.entity.User;
import dev.abelab.hack.dena.api.response.TagsResponse;
import dev.abelab.hack.dena.service.TagService;

@Api(tags = "Tag")
@RestController
@RequestMapping(path = "/api/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class TagRestController {

    private final TagService tagService;

    /**
     * タグ一覧取得API
     *
     * @param loginUser ログインユーザ
     */
    @ApiOperation( //
        value = "タグ一覧の取得", //
        notes = "タグ一覧を取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = TagsResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TagsResponse getTags( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.tagService.getTags(loginUser);
    }

}
