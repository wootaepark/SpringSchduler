-- level 1 ~ level 3 에서 사용되는 테이블을 생성하는 sql 문
/*CREATE TABLE `Schedule` (
                            `id`	int	NOT NULL	AUTO_INCREMENT  PRIMARY KEY  COMMENT 'Auto Increment',
                            `username`	varchar(100)	NOT NULL	COMMENT '유저의 이름',
                            `email`	varchar(100)	NOT NULL	COMMENT '유저의 이메일',
                            `password`	varchar(100)	NULL,
                            `title`	varchar(100)	NOT NULL,
                            `content`	varchar(1000)	NULL,
                            `scheduledDate` date      NOT NULL    COMMENT '일정 날짜',
                            `createdAt`	datetime	NOT NULL	DEFAULT now()	COMMENT '게시글 최초 작성일',
                            `updatedAt`	datetime	NULL	DEFAULT now() COMMENT '게시글 최근 수정일'


);*/

-- level 4, level 5 에서 사용되는 테이블을 생성하고 테이블 간의 관계를 맺는 sql 문
/*CREATE TABLE `User` (
                        `id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT	COMMENT 'Auto Increment',
                        `username`	varchar(100)	NOT NULL	COMMENT '유저의 이름',
                        `email`	varchar(100)	NOT NULL	COMMENT '유저의 이메일',
                        `createdAt`	datetime	NOT NULL	DEFAULT now()	COMMENT '유저 최초 등록일',
                        `updatedAt`	datetime	NULL	COMMENT '유저 정보 수정일'
);

CREATE TABLE `Schedule` (
                            `id`	int	NOT NULL	PRIMARY KEY  AUTO_INCREMENT COMMENT 'Auto Increment',
                            `password`	varchar(100)	NULL,
                            `title`	varchar(100)	NOT NULL,
                            `content`	varchar(1000)	NULL,
                            `scheduledDate`	date	NOT NULL	COMMENT '일정 예정 날짜',
                            `createdAt`	datetime	NOT NULL	DEFAULT now()	COMMENT '게시글 최초 작성일',
                            `updatedAt`	datetime	NULL	DEFAULT now() COMMENT '게시글 최근 수정일',
                            `user_id`	int	NOT NULL	COMMENT 'Auto Increment'
);


ALTER TABLE `Schedule` ADD CONSTRAINT `FK_User_TO_Schedule_1` FOREIGN KEY (
                                                                           `user_id`
    )
    REFERENCES `User` (
                       `id`
        );

*/





