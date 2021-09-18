package dev.abelab.hack.dena.api.response;

import java.util.List;

import lombok.*;
import dev.abelab.hack.dena.model.TripPlanItemModel;
import dev.abelab.hack.dena.model.TripPlanAttachmentSubmitModel;

/**
 * 旅行プランレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlanResponse {

    /**
     * 旅行プランID
     */
    Integer id;

    /**
     * タイトル
     */
    String title;

    /**
     * 説明文
     */
    String description;

    /**
     * 作成者
     */
    UserResponse author;

    /**
     * 都道府県ID
     */
    Integer regionId;

    /**
     * いいね数
     */
    Integer likes;

    /**
     * タグリスト
     */
    List<String> tags;

    /**
     * 項目リスト
     */
    List<TripPlanItemModel> items;

}
