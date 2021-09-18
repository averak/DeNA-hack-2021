package dev.abelab.hack.dena.api.response;

import java.util.List;

import lombok.*;

/**
 * 旅行プラン一覧レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlansResponse {

    /**
     * 旅行プラン一覧
     */
    List<TripPlanResponse> tripPlans;

}
