CREATE TABLE IF NOT EXISTS `trip_plan` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `region_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_trip_plan_user_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_trip_plan_region_idx` (`region_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_region`
    FOREIGN KEY (`region_id`)
    REFERENCES `region` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trip_plan_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);
