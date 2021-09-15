CREATE TABLE IF NOT EXISTS `user_like` (
  `user_id` INT UNSIGNED NOT NULL,
  `trip_plan_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `trip_plan_id`),
  INDEX `fk_user_like_trip_plan_idx` (`trip_plan_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_like_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_like_trip_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `trip_plan` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);
