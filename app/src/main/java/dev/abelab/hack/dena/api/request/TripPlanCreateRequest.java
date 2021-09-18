package dev.abelab.hack.dena.api.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;

import lombok.*;
import dev.abelab.hack.dena.model.TripPlanItemModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentSubmitModel;

/**
 * 旅行プラン作成リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlanCreateRequest {

    /**
     * タイトル
     */
    @NotNull
    @Size(max = 255)
    String title;

    /**
     * 説明文
     */
    @NotNull
    @Size(max = 255)
    String description;

    /**
     * 都道府県ID
     */
    @NotNull
    Integer regionId;

    /**
     * 添付ファイル
     * */
    @NotNull
    TripPlanAttachmentSubmitModel attachment;

    /**
     * タグリスト
     */
    @NotNull
    List<String> tags;

    /**
     * 項目リスト
     */
    @NotNull
    List<TripPlanItemModel> items;

}
