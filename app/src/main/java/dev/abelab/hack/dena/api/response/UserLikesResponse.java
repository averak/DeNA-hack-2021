package dev.abelab.hack.dena.api.response;

// import java.util.ArrayList;
import java.util.List;

import lombok.*;
import dev.abelab.hack.dena.db.entity.Tag;
import dev.abelab.hack.dena.db.entity.TripPlanItem;
/**
 * お気に入り取得レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLikesResponse {


    /**
     * プランID
     */
    Integer id;

    /**
     * タイトル
     */
    String title;

    /**
     * リージョンID
     */
    Integer regionId;

    /**
     * ユーザーId
     */
    Integer userId;

    /**
     * ユーザーId
     */
    String description;

    /**
     * author
     */
    UserResponse author;

    /**
     * like数
     */
    Integer likes;

    /**
     * tags
     */
    List<Tag> tags;

    /**
     * 画像
     */
    byte[] attachment;

    /**
     * items
    */
    List<TripPlanItem> items;
}
