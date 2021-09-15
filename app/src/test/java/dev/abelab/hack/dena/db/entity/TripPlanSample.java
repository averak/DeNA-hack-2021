package dev.abelab.hack.dena.db.entity;

import java.util.Date;

/**
 * TripPlan Sample Builder
 */
public class TripPlanSample extends AbstractSample {

	public static TripPlanSampleBuilder builder() {
		return new TripPlanSampleBuilder();
	}

	public static class TripPlanSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String title = SAMPLE_STR;
		private String description = SAMPLE_STR;
		private Integer regionId = SAMPLE_INT;
		private Integer userId = SAMPLE_INT;
		private Date createdAt = SAMPLE_DATE;
		private Date updatedAt = SAMPLE_DATE;

		public TripPlanSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public TripPlanSampleBuilder title(String title) {
			this.title = title;
			return this;
		}

		public TripPlanSampleBuilder description(String description) {
			this.description = description;
			return this;
		}

		public TripPlanSampleBuilder regionId(Integer regionId) {
			this.regionId = regionId;
			return this;
		}

		public TripPlanSampleBuilder userId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public TripPlanSampleBuilder createdAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public TripPlanSampleBuilder updatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public TripPlan build() {
			return TripPlan.builder() //
				.id(this.id) //
				.title(this.title) //
				.description(this.description) //
				.regionId(this.regionId) //
				.userId(this.userId) //
				.createdAt(this.createdAt) //
				.updatedAt(this.updatedAt) //
				.build();
		}

	}

}
