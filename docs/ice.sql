CREATE TABLE `Topping` (
  `id` integer PRIMARY KEY,
  `name` varchar(255),
  `topping_type` varchar(255),
  `price_id` integer,
  `description` varchar(255),
  `is_vegan` bool
);

CREATE TABLE `Cup` (
  `id` integer PRIMARY KEY,
  `description` varchar(255),
  `cup_type` varchar(255),
  `price` decimal,
  `is_vegan` bool
);

CREATE TABLE `Flavour` (
  `id` integer PRIMARY KEY,
  `name` varchar(255),
  `price_id` integer,
  `description` varchar(255),
  `is_vegan` bool
);

CREATE TABLE `Pricing` (
  `id` integer PRIMARY KEY,
  `description` varchar(255),
  `price` decimal
);

CREATE TABLE `Cup_Flavour` (
  `cup_id` integer,
  `flavour_id` integer
);

CREATE TABLE `Cup_Topping` (
  `cup_id` integer,
  `topping_id` integer
);

ALTER TABLE `Topping` ADD FOREIGN KEY (`id`) REFERENCES `Pricing` (`id`);

ALTER TABLE `Flavour` ADD FOREIGN KEY (`id`) REFERENCES `Pricing` (`id`);

ALTER TABLE `Cup_Flavour` ADD FOREIGN KEY (`flavour_id`) REFERENCES `Flavour` (`id`);

ALTER TABLE `Cup_Topping` ADD FOREIGN KEY (`topping_id`) REFERENCES `Topping` (`id`);

ALTER TABLE `Cup_Flavour` ADD FOREIGN KEY (`cup_id`) REFERENCES `Cup` (`id`);

ALTER TABLE `Cup_Topping` ADD FOREIGN KEY (`cup_id`) REFERENCES `Cup` (`id`);
