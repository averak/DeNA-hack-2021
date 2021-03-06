package dev.abelab.hack.dena.db.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripPlanItem {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.trip_plan_id
     *
     * @mbg.generated
     */
    private Integer tripPlanId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.item_order
     *
     * @mbg.generated
     */
    private Integer itemOrder;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.price
     *
     * @mbg.generated
     */
    private Integer price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.start_at
     *
     * @mbg.generated
     */
    private Date startAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.finish_at
     *
     * @mbg.generated
     */
    private Date finishAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trip_plan_item.description
     *
     * @mbg.generated
     */
    private String description;
}