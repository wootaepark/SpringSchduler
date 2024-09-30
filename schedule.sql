CREATE TABLE `Schedule` (
                            `id`	int	NOT NULL	AUTO_INCREMENT  PRIMARY KEY  COMMENT 'Auto Increment',
                            `username`	varchar(100)	NOT NULL	COMMENT '유저의 이름',
                            `email`	varchar(100)	NOT NULL	COMMENT '유저의 이메일',
                            `password`	varchar(100)	NULL,
                            `title`	varchar(100)	NOT NULL,
                            `content`	varchar(1000)	NULL,
                            `created_at`	datetime	NOT NULL	DEFAULT now()	COMMENT '게시글 최초 작성일',
                            `updated_at`	datetime	NULL	DEFAULT now() COMMENT '게시글 최근 수정일'


);






