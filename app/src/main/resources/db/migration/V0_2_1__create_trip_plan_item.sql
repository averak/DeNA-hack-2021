CREATE TABLE IF NOT EXISTS `trip_plan_item` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `trip_plan_id` INT UNSIGNED NOT NULL,
  `item_order` INT UNSIGNED NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `price` INT UNSIGNED NOT NULL,
  `start_at` DATETIME NOT NULL,
  `finish_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `trip_plan_and_item_order_UNIQUE` (`trip_plan_id` ASC, `item_order` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_item_trip_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `trip_plan` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);
