INSERT INTO `counter_setting_category` (`created_time`, `status`, `code`, `description`, `sequence`) VALUES (NOW(), '1', '1', 'TFI Setting', '5');

INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'USE_TFI', 'Use TFI', '1', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_IP', 'TFI IP Address', '2', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_SCREEN_WIDTH', 'Screen Width', '3', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_SCREEN_HEIGHT', 'Screen Height', '4', '5');

INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_WEIGHT_SCREEN_X', 'Weight Coordinate X', '5', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_WEIGHT_SCREEN_Y', 'Weight Coordinate Y', '6', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_WEIGHT_SCREEN_WIDTH', 'Weight Screen Width', '7', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_WEIGHT_SCREEN_HEIGHT', 'Weight Screen Height', '8', '5');

INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_UNIT_SCREEN_X', 'Unit Coordinate X', '9', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_UNIT_SCREEN_Y', 'Unit Coordinate Y', '10', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_UNIT_SCREEN_WIDTH', 'Unit Screen Width', '11', '5');
INSERT INTO `master_counter_settings` (`created_time`, `status`, `code`, `description`, `sequence`, `category_id`) VALUES (NOW(), '1', 'TFI_UNIT_SCREEN_HEIGHT', 'Unit Screen Height', '12', '5');