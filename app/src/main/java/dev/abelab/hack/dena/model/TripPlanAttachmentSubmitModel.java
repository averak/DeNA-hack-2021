package dev.abelab.hack.dena.model;

import javax.validation.constraints.NotNull;

import lombok.*;

/**
 * 旅行プランの添付ファイルの提出モデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlanAttachmentSubmitModel {

    /**
     * ファイル名
     */
    @NotNull
    String fileName;

    /**
     * ファイル内容 (Base64)
     */
    @NotNull
    String content;

}
