package dev.abelab.hack.dena.db.entity;

import java.util.Date;

/**
 * TripPlanItem Sample Builder
 */
public class TripPlanItemSample extends AbstractSample {

	public static TripPlanItemSampleBuilder builder() {
		return new TripPlanItemSampleBuilder();
	}

	public static class TripPlanItemSampleBuilder {

		private Integer id = SAMPLE_INT;
		private Integer tripPlanId = SAMPLE_INT;
		private Integer itemOrder = SAMPLE_INT;
		private String title = SAMPLE_STR;
		private String description = SAMPLE_STR;
		private Integer price = SAMPLE_INT;
		private Date startAt = SAMPLE_DATE;
		private Date finishAt = SAMPLE_DATE;

		public TripPlanItemSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public TripPlanItemSampleBuilder tripPlanId(Integer tripPlanId) {
			this.tripPlanId = tripPlanId;
			return this;
		}

		public TripPlanItemSampleBuilder itemOrder(Integer itemOrder) {
			this.itemOrder = itemOrder;
			return this;
		}

		public TripPlanItemSampleBuilder title(String title) {
			this.title = title;
			return this;
		}

		public TripPlanItemSampleBuilder description(String description) {
			this.description = description;
			return this;
		}

		public TripPlanItemSampleBuilder price(Integer price) {
			this.price = price;
			return this;
		}

		public TripPlanItemSampleBuilder startAt(Date startAt) {
			this.startAt = startAt;
			return this;
		}

		public TripPlanItemSampleBuilder finishAt(Date finishAt) {
			this.finishAt = finishAt;
			return this;
		}

		public TripPlanItem build() {
			return TripPlanItem.builder() //
				.id(this.id) //
				.tripPlanId(this.tripPlanId) //
				.itemOrder(this.itemOrder) //
				.title(this.title) //
				.description(this.description) //
				.price(this.price) //
				.startAt(this.startAt) //
				.finishAt(this.finishAt) //
				.build();
		}

	}

}
