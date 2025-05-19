DROP TABLE IF EXISTS `attractions`;

CREATE TABLE `attractions` (
	`no`	int	NOT NULL	COMMENT '명소코드',
	`content_id`	int	NULL	DEFAULT NULL	COMMENT '콘텐츠번호',
	`title`	varchar(500)	NULL	DEFAULT NULL	COMMENT '명소이름',
	`content_type_id`	int	NULL	DEFAULT NULL	COMMENT '콘텐츠타입',
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
	`homepage`	varchar(1000)	NULL	DEFAULT NULL	COMMENT '홈페이지',
	`overview`	varchar(10000)	NULL	DEFAULT NULL	COMMENT '설명'
);

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
	`comment_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`content`	text	NOT NULL,
	`diary_id2`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `guguns`;

CREATE TABLE `guguns` (
	`no`	int	NOT NULL	COMMENT '구군번호',
	`sido_code`	int	NOT NULL	COMMENT '시도코드',
	`gugun_code`	int	NOT NULL	COMMENT '구군코드',
	`gugun_name`	varchar(20)	NULL	DEFAULT NULL	COMMENT '구군이름'
);

DROP TABLE IF EXISTS `diaries`;

CREATE TABLE `diaries` (
	`diary_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`area_id`	int	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	VARCHAR(255)	NOT NULL,
	`image_total_count`	int	NOT NULL	DEFAULT 0,
	`start_date`	DATE	NOT NULL,
	`end_date`	DATE	NOT NULL
);

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
	`user_id`	bigint	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL	COMMENT '이름',
	`email`	VARCHAR(255)	NOT NULL	COMMENT '이메일',
	`password`	VARCHAR(255)	NULL	COMMENT '비밀번호',
	`role`	VARCHAR(20)	NOT NULL	COMMENT '권한 (0: 사용자, 1: 관리자)' ) CREATE TABLE IF NOT EXISTS `notices` ( `notice_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '공지사항 번호',
	`oauth_code`	VARCHAR(255)	NULL
);

DROP TABLE IF EXISTS `routes`;

CREATE TABLE `routes` (
	`id`	int	NOT NULL,
	`no`	int	NOT NULL	COMMENT '명소코드',
	`diary_id`	bigint	NOT NULL,
	`idx`	int	NOT NULL
);

DROP TABLE IF EXISTS `tags`;

CREATE TABLE `tags` (
	`id`	bigint	NOT NULL,
	`diary_id`	bigint	NOT NULL,
	`tag_id2`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `sidos`;

CREATE TABLE `sidos` (
	`no`	int	NOT NULL	COMMENT '시도번호',
	`sido_code`	int	NOT NULL	COMMENT '시도코드',
	`sido_name`	varchar(20)	NULL	DEFAULT NULL	COMMENT '시도이름'
);

DROP TABLE IF EXISTS `contenttypes`;

CREATE TABLE `contenttypes` (
	`content_type_id`	int	NOT NULL	COMMENT '콘텐츠타입번호',
	`content_type_name`	varchar(45)	NULL	DEFAULT NULL	COMMENT '콘텐츠타입이름'
);

DROP TABLE IF EXISTS `Untitled`;

CREATE TABLE `Untitled` (
	`tag_id`	bigint	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL
);

DROP TABLE IF EXISTS `areas`;

CREATE TABLE `areas` (
	`area_id`	int	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL
);

DROP TABLE IF EXISTS `likes`;

CREATE TABLE `likes` (
	`like_id`	BIGINT	NOT NULL,
	`diary_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL
);

ALTER TABLE `attractions` ADD CONSTRAINT `PK_ATTRACTIONS` PRIMARY KEY (
	`no`
);

ALTER TABLE `comments` ADD CONSTRAINT `PK_COMMENTS` PRIMARY KEY (
	`comment_id`
);

ALTER TABLE `guguns` ADD CONSTRAINT `PK_GUGUNS` PRIMARY KEY (
	`no`
);

ALTER TABLE `diaries` ADD CONSTRAINT `PK_DIARIES` PRIMARY KEY (
	`diary_id`
);

ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `routes` ADD CONSTRAINT `PK_ROUTES` PRIMARY KEY (
	`id`
);

ALTER TABLE `tags` ADD CONSTRAINT `PK_TAGS` PRIMARY KEY (
	`id`
);

ALTER TABLE `sidos` ADD CONSTRAINT `PK_SIDOS` PRIMARY KEY (
	`no`
);

ALTER TABLE `contenttypes` ADD CONSTRAINT `PK_CONTENTTYPES` PRIMARY KEY (
	`content_type_id`
);

ALTER TABLE `Untitled` ADD CONSTRAINT `PK_UNTITLED` PRIMARY KEY (
	`tag_id`
);

ALTER TABLE `areas` ADD CONSTRAINT `PK_AREAS` PRIMARY KEY (
	`area_id`
);

ALTER TABLE `likes` ADD CONSTRAINT `PK_LIKES` PRIMARY KEY (
	`like_id`
);

