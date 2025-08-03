CREATE TABLE quantity_unit (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status INT NOT NULL DEFAULT 1,
    sequence INT NULL,
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP NULL DEFAULT NULL,
    name VARCHAR(255),
    code VARCHAR(255),
    price_per_unit DECIMAL(19,2),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- link to transaction
SET SQL_SAFE_UPDATES = 0;
Update transactions set quantity_unit = NULL where quantity_unit is not null;
SET SQL_SAFE_UPDATES = 1;

ALTER TABLE `transactions` 
DROP FOREIGN KEY `FK_407pg2lxdj8r0ujny4yl3ey66`;
ALTER TABLE `transactions` 
DROP INDEX `FK_407pg2lxdj8r0ujny4yl3ey66` ,
ADD INDEX `FK_407pg2lxdj8r0ujny4yl3ey66_idx` (`quantity_unit` ASC) VISIBLE;

ALTER TABLE `transactions` 
ADD CONSTRAINT `FK_407pg2lxdj8r0ujny4yl3ey66`
  FOREIGN KEY (`quantity_unit`)
  REFERENCES `quantity_unit` (`id`);
