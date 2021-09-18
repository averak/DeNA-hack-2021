package dev.abelab.hack.dena.db.entity;

import java.util.UUID;

/**
 * TripPlanAttachment Sample Builder
 */
public class TripPlanAttachmentSample extends AbstractSample {

	public static TripPlanAttachmentSampleBuilder builder() {
		return new TripPlanAttachmentSampleBuilder();
	}

	public static class TripPlanAttachmentSampleBuilder {

		private String uuid = UUID.randomUUID().toString();
		private Integer tripPlanId = SAMPLE_INT;
		private String fileName = SAMPLE_STR;
		private byte[] content = SAMPLE_BYTE;

		public TripPlanAttachmentSampleBuilder uuid(String uuid) {
			this.uuid = uuid;
			return this;
		}

		public TripPlanAttachmentSampleBuilder tripPlanId(Integer tripPlanId) {
			this.tripPlanId = tripPlanId;
			return this;
		}

		public TripPlanAttachmentSampleBuilder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public TripPlanAttachmentSampleBuilder content(byte[] content) {
			this.content = content;
			return this;
		}

		public TripPlanAttachment build() {
			return TripPlanAttachment.builder() //
				.uuid(this.uuid) //
				.tripPlanId(this.tripPlanId) //
				.fileName(this.fileName) //
				.content(this.content) //
				.build();
		}

	}

}
