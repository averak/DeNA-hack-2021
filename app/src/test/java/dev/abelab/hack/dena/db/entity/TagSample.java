package dev.abelab.hack.dena.db.entity;

/**
 * Tag Sample Builder
 */
public class TagSample extends AbstractSample {

	public static TagSampleBuilder builder() {
		return new TagSampleBuilder();
	}

	public static class TagSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String name = SAMPLE_STR;

		public TagSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public TagSampleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public Tag build() {
			return Tag.builder() //
				.id(this.id) //
				.name(this.name) //
				.build();
		}

	}

}
