CREATE TABLE IF NOT EXISTS `trip_plan_attachment` (
  `uuid` CHAR(36) NOT NULL,
  `trip_plan_id` INT UNSIGNED NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `content` BLOB NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `trip_plan_id_UNIQUE` (`trip_plan_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_attachment_trip_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `trip_plan` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);
