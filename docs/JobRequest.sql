CREATE TABLE `Job_Request` (
  `id` integer PRIMARY KEY,
  `comment` varchar(255),
  `user_id` integer,
  `file_id` integer,
  `Job_description` varchar(255),
  `created_at` datetime
);

CREATE TABLE `File` (
  `id` integer PRIMARY KEY,
  `filename` varchar(255),
  `uri` varchar(255)
);

CREATE TABLE `App_User` (
  `id` integer PRIMARY KEY,
  `name` varchar(255),
  `surname` varchar(255),
  `email` varchar(255),
  `state` varchat
);

ALTER TABLE `File` ADD FOREIGN KEY (`id`) REFERENCES `Job_Request` (`file_id`);

ALTER TABLE `App_User` ADD FOREIGN KEY (`id`) REFERENCES `Job_Request` (`user_id`);
