package dev.abelab.hack.dena.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;

import lombok.*;

/**
 * 旅行プラン項目モデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlanItemModel {

    /**
     * 候補日の順番
     */
    @NotNull
    @Min(1)
    Integer itemOrder;

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
     * 金額
     */
    @NotNull
    @Min(0)
    Integer price;

    /**
     * 開始時間
     */
    @NotNull
    Date startAt;

    /**
     * 終了時間
     */
    @NotNull
    Date finishAt;

}
