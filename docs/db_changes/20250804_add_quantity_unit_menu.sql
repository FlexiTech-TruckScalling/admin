INSERT INTO `menus` (`created_time`, `status`, `menu_code`, `description`, `name`, `parent_menu_code`, `parent_status`, `url`, `order`) VALUES (NOW(), '1', 'QUANTITY_UNIT', 'Quantity Units', 'Quantity Units', 'MASTER_SETTING', '0', 'quantity-units.fxt', '4');

INSERT INTO `menus_role_access`
(`created_time`,
`status`,
`menu_id`,
`role_id`)
VALUES
(
now(),
1,
(select id from menus where menu_code = 'QUANTITY_UNIT'),
1);
