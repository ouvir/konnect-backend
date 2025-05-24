DROP DATABASE IF EXISTS konnect;
CREATE DATABASE konnect;
use konnect;
show tables;
DROP TABLE IF EXISTS `attractions`;

CREATE TABLE `attractions` (
                               `no`	int	NOT NULL auto_increment COMMENT '명소코드',
                               `content_id`	int	NULL	DEFAULT NULL	COMMENT '콘텐츠번호',
                               `title`	varchar(500)	NULL	DEFAULT NULL	COMMENT '명소이름',
                               `title_eng`	varchar(500)	NULL	DEFAULT NULL	COMMENT '명소이름(영문)',
                               `content_type_id`	int	NULL	DEFAULT NULL COMMENT '콘텐츠타입',
                               `area_code`	int	NULL	DEFAULT NULL	COMMENT '시도코드',
                               `si_gun_gu_code`	int	NULL	DEFAULT NULL	COMMENT '구군코드',
                               `first_image1`	varchar(100)	NULL	DEFAULT NULL	COMMENT '이미지경로1',
                               `first_image2`	varchar(100)	NULL	DEFAULT NULL	COMMENT '이미지경로2',
                               `map_level`	int	NULL	DEFAULT NULL	COMMENT '줌레벨',
                               `latitude`	decimal(20,17)	NULL	DEFAULT NULL	COMMENT '위도',
                               `longitude`	decimal(20,17)	NULL	DEFAULT NULL	COMMENT '경도',
                               `tel`	varchar(20)	NULL	DEFAULT NULL	COMMENT '전화번호',
                               `addr1`	varchar(100)	NULL	DEFAULT NULL	COMMENT '주소1',
                               `addr2`	varchar(100)	NULL	DEFAULT NULL	COMMENT '주소2',
                               `homepage` TEXT NULL DEFAULT NULL COMMENT '홈페이지',
                               `overview` TEXT NULL DEFAULT NULL COMMENT '설명',
                               `overview_eng` TEXT NULL DEFAULT NULL COMMENT '설명(영문)',
                               PRIMARY KEY (`no`)
);

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
                            `comment_id`	bigint	NOT NULL auto_increment,
                            `user_id`	bigint	NOT NULL,
                            `content`	text	NOT NULL,
                            `diary_id`	bigint	NOT NULL,
                            PRIMARY KEY (`comment_id`)
);

DROP TABLE IF EXISTS `guguns`;

CREATE TABLE `guguns` (
                          `no`    int    NOT NULL auto_increment    COMMENT '구군번호',
                          `sido_code`    int    NOT NULL     COMMENT '시도코드',
                          `gugun_code`    int    NOT NULL    COMMENT '구군코드',
                          `gugun_name`    varchar(20)    NULL    DEFAULT NULL    COMMENT '구군이름',
                          `gugun_name_eng`    varchar(20)    NULL    DEFAULT NULL    COMMENT '구군이름(영문)',
                          PRIMARY KEY (`no`),
                          INDEX `idx_gugun_code` (`gugun_code`)
);

DROP TABLE IF EXISTS `diaries`;

CREATE TABLE `diaries` (
                           `diary_id`    bigint auto_increment    NOT NULL,
                           `user_id`    bigint    NOT NULL,
                           `area_id`    int    NULL,
                           `title`    VARCHAR(255)    NULL,
                           `content`    VARCHAR(255)    NULL,
                           `start_date`    VARCHAR(30)    NULL,
                           `end_date`    VARCHAR(30)    NULL,
                           `status` VARCHAR(20) NOT NULL,
                           `created_at` VARCHAR(255) NOT NULL,
                           PRIMARY KEY (`diary_id`)
);

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `user_id`	bigint	NOT NULL auto_increment,
                         `name`	VARCHAR(255)	NOT NULL	COMMENT '이름',
                         `email`	VARCHAR(255)	NOT NULL	COMMENT '이메일',
                         `password`	VARCHAR(255)	NULL	COMMENT '비밀번호',
                         `role`	VARCHAR(20)	NOT NULL,
                         `oauth_code`	VARCHAR(255)	NULL,
                         PRIMARY KEY (`user_id`)
);

DROP TABLE IF EXISTS `routes`;

CREATE TABLE `routes` (
                          `id`	int	NOT NULL auto_increment,
                          `diary_id`	bigint	NOT NULL,
                          `visitedDate` VARCHAR(20),
                          `visitedTime` VARCHAR(20),
                          `distance` double,
                          `title` varchar(500),
                          `latitude`	double	NOT NULL	COMMENT '위도',
                          `longitude`	double	NOT NULL	COMMENT '경도',
                          PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `diarytags`;

CREATE TABLE `diarytags` (
                             `id`	bigint	NOT NULL auto_increment,
                             `diary_id`	bigint	NOT NULL,
                             `tag_id`	bigint	NOT NULL,
                             PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sidos`;

CREATE TABLE `sidos` (
                         `no`	int	NOT NULL	auto_increment COMMENT '시도번호',
                         `sido_code`	int	NOT NULL UNIQUE	COMMENT '시도코드',
                         `sido_name`	varchar(20)	NULL	DEFAULT NULL	COMMENT '시도이름',
                         `sido_name_eng`	varchar(20)	NULL	DEFAULT NULL	COMMENT '시도이름(영문)',
                         PRIMARY KEY (`no`)
);

DROP TABLE IF EXISTS `contenttypes`;

CREATE TABLE `contenttypes` (
                                `content_type_id`	int	NOT NULL auto_increment	COMMENT '콘텐츠타입번호',
                                `content_type_name`	varchar(45)	NULL	DEFAULT NULL	COMMENT '콘텐츠타입이름',
                                `content_type_name_eng`	varchar(45)	NULL	DEFAULT NULL	COMMENT '콘텐츠타입이름(영문)',
                                PRIMARY KEY (`content_type_id`)
);

DROP TABLE IF EXISTS `tags`;

CREATE TABLE `tags` (
                        `tag_id`	bigint 	NOT NULL auto_increment,
                        `name`	VARCHAR(255)	NOT NULL,
                        PRIMARY KEY (`tag_id`)
);

DROP TABLE IF EXISTS `areas`;

CREATE TABLE `areas` (
                         `area_id`	int	NOT NULL auto_increment,
                         `name`	VARCHAR(255)	NOT NULL,
                         `name_eng` VARCHAR(255)	NOT NULL,
                         PRIMARY KEY (`area_id`)
);

DROP TABLE IF EXISTS `likes`;

CREATE TABLE `likes` (
                         `like_id`    BIGINT    NOT NULL auto_increment,
                         `diary_id`    bigint    NOT NULL,
                         `user_id`    bigint    NOT NULL,
                         `is_deleted` BOOL NOT NULL DEFAULT FALSE,
                         PRIMARY KEY (`like_id`)
);

ALTER TABLE `attractions` ADD CONSTRAINT `FK_contenttypes_TO_attractions_1` FOREIGN KEY (
                                                                                         `content_type_id`
    )
    REFERENCES `contenttypes` (
                               `content_type_id`
        );

ALTER TABLE `attractions` ADD CONSTRAINT `FK_sidos_TO_attractions_1` FOREIGN KEY (
                                                                                  `area_code`
    )
    REFERENCES `sidos` (
                        `sido_code`
        );

ALTER TABLE `attractions` ADD CONSTRAINT `FK_guguns_TO_attractions_1` FOREIGN KEY (
                                                                                   `si_gun_gu_code`
    )
    REFERENCES `guguns` (
                         `gugun_code`
        );

ALTER TABLE `comments` ADD CONSTRAINT `FK_users_TO_comments_1` FOREIGN KEY (
                                                                            `user_id`
    )
    REFERENCES `users` (
                        `user_id`
        );

ALTER TABLE `comments` ADD CONSTRAINT `FK_diaries_TO_comments_1` FOREIGN KEY (
                                                                              `diary_id`
    )
    REFERENCES `diaries` (
                          `diary_id`
        );

ALTER TABLE `guguns` ADD CONSTRAINT `FK_sidos_TO_guguns_1` FOREIGN KEY (
                                                                        `sido_code`
    )
    REFERENCES `sidos` (
                        `sido_code`
        );

ALTER TABLE `diaries` ADD CONSTRAINT `FK_users_TO_diaries_1` FOREIGN KEY (
                                                                          `user_id`
    )
    REFERENCES `users` (
                        `user_id`
        );

ALTER TABLE `diaries` ADD CONSTRAINT `FK_areas_TO_diaries_1` FOREIGN KEY (
                                                                          `area_id`
    )
    REFERENCES `areas` (
                        `area_id`
        );

ALTER TABLE `routes` ADD CONSTRAINT `FK_attractions_TO_routes_1` FOREIGN KEY (
                                                                              `no`
    )
    REFERENCES `attractions` (
                              `no`
        );

ALTER TABLE `routes` ADD CONSTRAINT `FK_diaries_TO_routes_1` FOREIGN KEY (
                                                                          `diary_id`
    )
    REFERENCES `diaries` (
                          `diary_id`
        );

ALTER TABLE `diarytags` ADD CONSTRAINT `FK_diaries_TO_diarytags_1` FOREIGN KEY (
                                                                                `diary_id`
    )
    REFERENCES `diaries` (
                          `diary_id`
        );

ALTER TABLE `diarytags` ADD CONSTRAINT `FK_tags_TO_diarytags_1` FOREIGN KEY (
                                                                             `tag_id`
    )
    REFERENCES `tags` (
                       `tag_id`
        );

ALTER TABLE `likes` ADD CONSTRAINT `FK_diaries_TO_likes_1` FOREIGN KEY (
                                                                        `diary_id`
    )
    REFERENCES `diaries` (
                          `diary_id`
        );

ALTER TABLE `likes` ADD CONSTRAINT `FK_users_TO_likes_1` FOREIGN KEY (
                                                                      `user_id`
    )
    REFERENCES `users` (
                        `user_id`
        );

