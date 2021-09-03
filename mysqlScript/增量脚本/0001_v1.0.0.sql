-- 测试表 by 王刚 2020-03-04
CREATE TABLE `mp_user`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `create_user` varchar(32)  DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `deleted`     tinyint(2) NOT NULL DEFAULT '0',
    `update_time` datetime     DEFAULT NULL,
    `update_user` varchar(32)  DEFAULT NULL,
    `name`        varchar(255) DEFAULT NULL,
    `manager_id`  bigint(20) DEFAULT NULL,
    `age`         int(4) DEFAULT NULL,
    `gender`      int(4) DEFAULT NULL,
    `email`       varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `version`     int(4) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

create table oauth_access_token
(
    create_time       timestamp default now(),
    token_id          VARCHAR(255),
    token             BLOB,
    authentication_id VARCHAR(255),
    user_name         VARCHAR(255),
    client_id         VARCHAR(255),
    authentication    BLOB,
    refresh_token     VARCHAR(255)
        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table oauth_refresh_token
(
    create_time    timestamp default now(),
    token_id       VARCHAR(255),
    token          BLOB,
    authentication BLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

CREATE TABLE `oauth_settings`
(
    `id`          int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一标识，自增id',
    `app_key`     varchar(50) NOT NULL COMMENT 'appKey 验证key',
    `app_secret`  varchar(50) NOT NULL COMMENT 'appKey 验证Secret',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `create_user` varchar(32)  DEFAULT NULL COMMENT '创建用户id 可null',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间 非null',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间 可null',
    `update_user` varchar(32)  DEFAULT NULL COMMENT '更新用户id 可null',
    `deleted`     tinyint(255) DEFAULT 0 COMMENT '是否删除 true ：删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE oauth_access_token
    ADD UNIQUE INDEX authentication_id_unique_index(authentication_id);

