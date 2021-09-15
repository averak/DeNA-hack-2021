CREATE TABLE IF NOT EXISTS `trip_plan_tagging` (
  `trip_plan_id` INT UNSIGNED NOT NULL,
  `tag_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`trip_plan_id`, `tag_id`),
  INDEX `fk_trip_plan_tagging_tag_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_tagging_trip_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `trip_plan` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trip_plan_tagging_tag`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tag` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);
