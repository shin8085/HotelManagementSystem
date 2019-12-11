CREATE DATABASE hms_database;

use hms_database;

CREATE TABLE customer(
    idnum char(18) NOT NULL PRIMARY KEY COMMENT '身份证号',
    cname varchar(10) NOT NULL COMMENT '姓名',
    sex char(1) NOT NULL COMMENT '性别',
    telphone varchar(12) NOT NULL COMMENT '电话号码',
    remark varchar(20) DEFAULT "无"
);

CREATE TABLE _group(
    gid int NOT NULL PRIMARY KEY COMMENT '团队编号',
    cap_idnum char(18) NOT NULL COMMENT '领队身份证号',
    people_num int(3) NOT NULL COMMENT '团队人数',
    CONSTRAINT _group_customer FOREIGN KEY(cap_idnum) REFERENCES customer(idnum) ON DELETE CASCADE
);

CREATE TABLE follow(
    gid int NOT NULL COMMENT '团队编号',
    men_idnum char(18) NOT NULL COMMENT '成员身份证号',
    PRIMARY KEY(gid,men_idnum),
    CONSTRAINT follow_group FOREIGN KEY(gid) REFERENCES _group(gid) ON DELETE CASCADE,
    CONSTRAINT follow_customer FOREIGN KEY(men_idnum) REFERENCES customer(idnum) ON DELETE CASCADE
);

CREATE TABLE room(
    rid varchar(3) NOT NULL PRIMARY KEY COMMENT '房间号',
    rtype varchar(10) NOT NULL COMMENT '房间类型',
    price int NOT NULL COMMENT '价格'
);

CREATE TABLE vip_card(
    vid varchar(20) NOT NULL PRIMARY KEY COMMENT '卡号',
    idnum char(18) NOT NULL COMMENT '身份证号',
    cname varchar(10) NOT NULL COMMENT '姓名',
    sex char(1) NOT NULL COMMENT '性别',
    telphone varchar(12) NOT NULL COMMENT '电话号码',
    mtime DATETIME NOT NULL COMMENT '办理日期'
);

CREATE TABLE check_in(
    idnum char(18) NOT NULL COMMENT '身份证号',
    rid varchar(3) NOT NULL COMMENT '房间号',
    citime DATETIME NOT NULL COMMENT '入住时间',
    PRIMARY KEY(idnum,rid),
    CONSTRAINT cin_customer FOREIGN KEY(idnum) REFERENCES customer(idnum) ON DELETE CASCADE,
    CONSTRAINT cin_room FOREIGN KEY(rid) REFERENCES room(rid) ON DELETE CASCADE
);

CREATE TABLE book(
    idnum char(18) NOT NULL COMMENT '身份证号',
    rid varchar(3) NOT NULL COMMENT '房间号',
    btime DATETIME NOT NULL COMMENT '预定时间',
    PRIMARY KEY(idnum,rid),
    CONSTRAINT book_customer FOREIGN KEY(idnum) REFERENCES customer(idnum) ON DELETE CASCADE,
    CONSTRAINT book_room FOREIGN KEY(rid) REFERENCES room(rid) ON DELETE CASCADE
);

CREATE TABLE room_changes(
    idnum char(18) NOT NULL COMMENT '身份证号',
    orid varchar(5) NOT NULL COMMENT '旧房间号',
    nrid varchar(5) NOT NULL COMMENT '新房间号',
    rtime DATETIME NOT NULL COMMENT '换房时间',
    PRIMARY KEY(idnum,orid,nrid),
    CONSTRAINT rcs_customer FOREIGN KEY(idnum) REFERENCES customer(idnum) ON DELETE CASCADE,
    CONSTRAINT orcs_room FOREIGN KEY(orid) REFERENCES room(rid) ON DELETE CASCADE,
    CONSTRAINT nrcs_room FOREIGN KEY(nrid) REFERENCES room(rid) ON DELETE CASCADE
);

CREATE TABLE deposit(
    idnum char(18) NOT NULL PRIMARY KEY COMMENT '身份证号',
    dprice int NOT NULL COMMENT '押金',
    CONSTRAINT deposit_customer FOREIGN KEY(idnum) REFERENCES customer(idnum) ON DELETE CASCADE
);

CREATE TABLE check_out(
    idnum char(18) NOT NULL COMMENT '身份证号',
    rid varchar(3) NOT NULL COMMENT '房间号',
    cotime DATETIME NOT NULL COMMENT '退房时间',
    PRIMARY KEY(idnum,rid),
    CONSTRAINT cout_room FOREIGN KEY(rid) REFERENCES room(rid) ON DELETE CASCADE
);

CREATE TABLE check_out_normal(
    idnum char(18) NOT NULL COMMENT '身份证号',
    rid varchar(3) NOT NULL COMMENT '房间号',
    conprice int NOT NULL COMMENT '结账金额',
    PRIMARY KEY(idnum,rid),
    CONSTRAINT con_room FOREIGN KEY(rid) REFERENCES room(rid) ON DELETE CASCADE
);

CREATE TABLE chect_out_credit(
    idnum char(18) NOT NULL COMMENT '身份证号',
    rid varchar(3) NOT NULL COMMENT '房间号',
    cocprice int NOT NULL COMMENT '挂账金额',
    PRIMARY KEY(idnum,rid),
    CONSTRAINT coc_room FOREIGN KEY(rid) REFERENCES room(rid) ON DELETE CASCADE
); 

/*插入测试数据*/

INSERT INTO room VALUES("201","单人间",100),("202","单人间",100),("203","单人间",100),("204","单人间",100);
INSERT INTO room VALUES("301","双人间",140),("302","双人间",140),("303","双人间",140),("304","双人间",140);
INSERT INTO room VALUES("401","豪华套间",220),("402","豪华套间",220),("403","豪华套间",220),("404","豪华套间",220);

