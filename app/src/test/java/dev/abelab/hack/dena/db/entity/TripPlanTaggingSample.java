package dev.abelab.hack.dena.db.entity;

/**
 * TripPlanTagging Sample Builder
 */
public class TripPlanTaggingSample extends AbstractSample {

	public static TripPlanTaggingSampleBuilder builder() {
		return new TripPlanTaggingSampleBuilder();
	}

	public static class TripPlanTaggingSampleBuilder {

		private Integer tripPlanId = SAMPLE_INT;
		private Integer tagId = SAMPLE_INT;

		public TripPlanTaggingSampleBuilder tripPlanId(Integer tripPlanId) {
			this.tripPlanId = tripPlanId;
			return this;
		}

		public TripPlanTaggingSampleBuilder tagId(Integer tagId) {
			this.tagId = tagId;
			return this;
		}

		public TripPlanTagging build() {
			return TripPlanTagging.builder() //
				.tripPlanId(this.tripPlanId) //
				.tagId(this.tagId) //
				.build();
		}

	}

}
