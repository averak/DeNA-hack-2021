package dev.abelab.hack.dena.model;

import lombok.*;

/**
 * 旅行プラン添付ファイルモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlanAttachmentModel {

    /**
     * UUID
     */
    String uuid;

    /**
     * ファイル名
     */
    String fileName;

}
