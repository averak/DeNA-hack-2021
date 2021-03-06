package dev.abelab.hack.dena.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.hack.dena.db.entity.TripPlanAttachment;
import dev.abelab.hack.dena.db.entity.TripPlanAttachmentExample;
import dev.abelab.hack.dena.db.mapper.TripPlanAttachmentMapper;
import dev.abelab.hack.dena.exception.ErrorCode;
import dev.abelab.hack.dena.exception.ConflictException;
import dev.abelab.hack.dena.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class TripPlanAttachmentRepository {

    private final TripPlanAttachmentMapper tripPlanAttachmentMapper;

    /**
     * 添付ファイルを作成
     *
     * @param tripPlanAttachment 添付ファイル
     *
     * @return 添付ファイルID
     */
    public int insert(final TripPlanAttachment tripPlanAttachment) {
        if (this.existsByTripPlanId(tripPlanAttachment.getTripPlanId())) {
            throw new ConflictException(ErrorCode.CONFLICT_TRIP_PLAN_ATTACHMENT);
        }
        return this.tripPlanAttachmentMapper.insertSelective(tripPlanAttachment);
    }

    /**
     * 旅行プランIDから添付ファイルを取得
     *
     * @param tripPlanId 旅行プランID
     *
     * @return 添付ファイル
     */
    public TripPlanAttachment selectByTripPlanId(final int tripPlanId) {
        final var example = new TripPlanAttachmentExample();
        example.createCriteria().andTripPlanIdEqualTo(tripPlanId);
        final var attachments = this.tripPlanAttachmentMapper.selectByExampleWithBLOBs(example);
        if (attachments.isEmpty()) {
            return null;
        } else {
            return attachments.get(0);
        }
    }

    /**
     * UUIDから添付ファイルを取得
     *
     * @param uuid UUID
     *
     * @return 添付ファイル
     */
    public TripPlanAttachment selectByUuid(final String uuid) {
        final var example = new TripPlanAttachmentExample();
        example.createCriteria().andUuidEqualTo(uuid);
        final var attachments = this.tripPlanAttachmentMapper.selectByExampleWithBLOBs(example);
        if (attachments.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ATTACHMENT);
        } else {
            return attachments.get(0);
        }
    }

    /**
     * 旅行プランIDから添付ファイルの存在確認
     *
     * @param tripPlanId 旅行プランID
     *
     * @return 添付ファイルが存在するか
     */
    public boolean existsByTripPlanId(final int tripPlanId) {
        return this.selectByTripPlanId(tripPlanId) != null;
    }

    /**
     * 旅行プランIDから削除
     *
     * @param tripPlanId 旅行プランID
     */
    public void deleteByTripPlanId(final int tripPlanId) {
        final var example = new TripPlanAttachmentExample();
        example.createCriteria().andTripPlanIdEqualTo(tripPlanId);
        this.tripPlanAttachmentMapper.deleteByExample(example);
    }

}
