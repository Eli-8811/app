CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `lastname` varchar(200) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  `gender` char(1) DEFAULT NULL,
  `born` timestamp NULL DEFAULT NULL,
  `active` tinyint DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_username` (`username`),
  UNIQUE KEY `uk_users_email` (`email`)
);

CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_roles_name` (`name`)
);

CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_roles_role_id` (`role_id`),
  CONSTRAINT `fk_user_roles_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_user_roles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE `role_permissions` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
);

CREATE TABLE `polls` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question` varchar(140) DEFAULT NULL,
  `expiration_date_time` datetime NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `choices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `text` varchar(250) NOT NULL,
  `poll_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_choices_poll_id` (`poll_id`),
  CONSTRAINT `fk_choices_poll_id` FOREIGN KEY (`poll_id`) REFERENCES `polls` (`id`)
);

INSERT INTO `roles` VALUES (1, 'ROLE_CEO');
INSERT INTO `roles` VALUES (2, 'ROLE_SUPERADMIN');
INSERT INTO `roles` VALUES (3, 'ROLE_ADMIN');
INSERT INTO `roles` VALUES (4, 'ROLE_DIRECTOR');
INSERT INTO `roles` VALUES (5, 'ROLE_SUPERVISOR');
INSERT INTO `roles` VALUES (6, 'ROLE_TEACHLEAD');
INSERT INTO `roles` VALUES (7, 'ROLE_ANALYST');
INSERT INTO `roles` VALUES (8, 'ROLE_SCRUM');
INSERT INTO `roles` VALUES (9, 'ROLE_DEVELOPER');
INSERT INTO `roles` VALUES (10, 'ROLE_TESTER');
INSERT INTO `roles` VALUES (11, 'ROLE_USER');

INSERT INTO `permissions` VALUES (1, 'PERMISSION_MENU_00001', 'FRONT END');
INSERT INTO `permissions` VALUES (2, 'PERMISSION_MENU_00002', 'FRONT END');
INSERT INTO `permissions` VALUES (3, 'PERMISSION_MENU_00003', 'FRONT END');
INSERT INTO `permissions` VALUES (4, 'PERMISSION_MENU_00004', 'FRONT END');

INSERT INTO `role_permissions` VALUES (1, 1);
INSERT INTO `role_permissions` VALUES (2, 1);
INSERT INTO `role_permissions` VALUES (3, 1);
INSERT INTO `role_permissions` VALUES (4, 1);
INSERT INTO `role_permissions` VALUES (5, 1);
INSERT INTO `role_permissions` VALUES (6, 1);
INSERT INTO `role_permissions` VALUES (7, 1);
INSERT INTO `role_permissions` VALUES (8, 1);
INSERT INTO `role_permissions` VALUES (9, 1);
INSERT INTO `role_permissions` VALUES (10, 1);
INSERT INTO `role_permissions` VALUES (11, 1);

INSERT INTO `role_permissions` VALUES (1, 2);
INSERT INTO `role_permissions` VALUES (2, 2);
INSERT INTO `role_permissions` VALUES (3, 2);
INSERT INTO `role_permissions` VALUES (4, 2);
INSERT INTO `role_permissions` VALUES (5, 2);
INSERT INTO `role_permissions` VALUES (6, 2);
INSERT INTO `role_permissions` VALUES (7, 2);
INSERT INTO `role_permissions` VALUES (8, 2);
INSERT INTO `role_permissions` VALUES (9, 2);
INSERT INTO `role_permissions` VALUES (10, 2);
INSERT INTO `role_permissions` VALUES (11, 2);

INSERT INTO `role_permissions` VALUES (1, 3);
INSERT INTO `role_permissions` VALUES (2, 3);
INSERT INTO `role_permissions` VALUES (3, 3);
INSERT INTO `role_permissions` VALUES (4, 3);
INSERT INTO `role_permissions` VALUES (5, 3);
INSERT INTO `role_permissions` VALUES (6, 3);
INSERT INTO `role_permissions` VALUES (7, 3);
INSERT INTO `role_permissions` VALUES (8, 3);
INSERT INTO `role_permissions` VALUES (9, 3);
INSERT INTO `role_permissions` VALUES (10, 3);
INSERT INTO `role_permissions` VALUES (11, 3);

INSERT INTO `role_permissions` VALUES (1, 4);
INSERT INTO `role_permissions` VALUES (2, 4);
INSERT INTO `role_permissions` VALUES (3, 4);
INSERT INTO `role_permissions` VALUES (4, 4);
INSERT INTO `role_permissions` VALUES (5, 4);
INSERT INTO `role_permissions` VALUES (6, 4);
INSERT INTO `role_permissions` VALUES (7, 4);
INSERT INTO `role_permissions` VALUES (8, 4);
INSERT INTO `role_permissions` VALUES (9, 4);
INSERT INTO `role_permissions` VALUES (10, 4);
INSERT INTO `role_permissions` VALUES (11, 4);