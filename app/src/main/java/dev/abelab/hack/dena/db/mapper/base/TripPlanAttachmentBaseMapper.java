package dev.abelab.hack.dena.db.mapper.base;

import dev.abelab.hack.dena.db.entity.TripPlanAttachment;
import dev.abelab.hack.dena.db.entity.TripPlanAttachmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TripPlanAttachmentBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    long countByExample(TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int deleteByExample(TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String uuid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int insert(TripPlanAttachment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int insertSelective(TripPlanAttachment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    List<TripPlanAttachment> selectByExampleWithBLOBs(TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    List<TripPlanAttachment> selectByExample(TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    TripPlanAttachment selectByPrimaryKey(String uuid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") TripPlanAttachment record, @Param("example") TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") TripPlanAttachment record, @Param("example") TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") TripPlanAttachment record, @Param("example") TripPlanAttachmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TripPlanAttachment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(TripPlanAttachment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trip_plan_attachment
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TripPlanAttachment record);
}