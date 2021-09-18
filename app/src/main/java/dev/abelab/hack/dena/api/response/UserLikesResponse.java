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
     * いいねの総数
     */
    Integer num;
}