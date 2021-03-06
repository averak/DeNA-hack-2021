package dev.abelab.hack.dena.db.mapper.base;

import dev.abelab.hack.dena.db.entity.TripPlanItem;
import dev.abelab.hack.dena.db.entity.TripPlanItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TripPlanItemBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    long countByExample(TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int deleteByExample(TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int insert(TripPlanItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int insertSelective(TripPlanItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    List<TripPlanItem> selectByExampleWithBLOBs(TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    List<TripPlanItem> selectByExample(TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    TripPlanItem selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") TripPlanItem record, @Param("example") TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") TripPlanItem record, @Param("example") TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") TripPlanItem record, @Param("example") TripPlanItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TripPlanItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(TripPlanItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TripPlanItem record);
}