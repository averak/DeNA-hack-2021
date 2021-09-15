package dev.abelab.hack.dena.db.entity;

/**
 * UserLike Sample Builder
 */
public class UserLikeSample extends AbstractSample {

	public static UserLikeSampleBuilder builder() {
		return new UserLikeSampleBuilder();
	}

	public static class UserLikeSampleBuilder {

		private Integer userId = SAMPLE_INT;
		private Integer tripPlanId = SAMPLE_INT;

		public UserLikeSampleBuilder userId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public UserLikeSampleBuilder tripPlanId(Integer tripPlanId) {
			this.tripPlanId = tripPlanId;
			return this;
		}

		public UserLike build() {
			return UserLike.builder() //
				.userId(this.userId) //
				.tripPlanId(this.tripPlanId) //
				.build();
		}

	}

}
