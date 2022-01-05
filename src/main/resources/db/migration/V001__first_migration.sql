-- db init script

CREATE TABLE `user`
(
    `id`               bigint   NOT NULL AUTO_INCREMENT,
    `name`             text     NOT NULL,
    `signup_date_time` datetime NOT NULL,
    UNIQUE KEY `user_id_IDX` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Users Table';


CREATE TABLE `user_log`
(
    `id`        bigint   NOT NULL AUTO_INCREMENT,
    `user_id`   bigint   NOT NULL,
    `date_time` datetime NOT NULL,
    `log`       text DEFAULT NULL,
    UNIQUE KEY `user_log_IDX` (`id`),
    CONSTRAINT `user_log_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Logs related to user actions';



