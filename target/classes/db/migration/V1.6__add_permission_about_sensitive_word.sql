INSERT INTO `permission` (`id`, `name`, `value`, `pid`)
VALUES
	(50, 'Sensitive Word', 'sensitive_word', 0),
	(51, 'Sensitive Word List', 'sensitive_word:list', 50),
	(52, 'Sensitive Word Add', 'sensitive_word:add', 50),
	(53, 'Sensitive Word Edit', 'sensitive_word:edit', 50),
	(54, 'Sensitive Word Delete', 'sensitive_word:delete', 50),
	(55, 'Sensitive Word Import', 'sensitive_word:import', 50);

INSERT INTO `role_permission` (`role_id`, `permission_id`)
VALUES
	(1, 51),
	(1, 52),
	(1, 53),
	(1, 54),
	(1, 55);

