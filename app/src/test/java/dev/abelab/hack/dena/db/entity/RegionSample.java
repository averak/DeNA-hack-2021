package dev.abelab.hack.dena.db.entity;

/**
 * Region Sample Builder
 */
public class RegionSample extends AbstractSample {

	public static RegionSampleBuilder builder() {
		return new RegionSampleBuilder();
	}

	public static class RegionSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String name = SAMPLE_STR;

		public RegionSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public RegionSampleBuilder email(String name) {
			this.name = name;
			return this;
		}

		public Region build() {
			return Region.builder() //
				.id(this.id) //
				.name(this.name) //
				.build();
		}

	}

}
