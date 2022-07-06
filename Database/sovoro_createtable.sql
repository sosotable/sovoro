use DWORD;

create table userinfo (
    userid varchar(250),
    password varchar(250) not null,
    nickname varchar(250) unique not null,
    etherid varchar(250) not null,
    userimage varchar(250),
    milege int default 0,
    attend int default 0,
    primary key(userid)
);

create table wordinfo(
    wordid int auto_increment,
    engword varchar(250) primary key,
    korword varchar(250) not null,
    kordesc varchar(250),
    wordimage varchar(250)
);

create table socialwordbook (
    wordbookcount int auto_increment,
    wordbookname varchar(250),
    wordbookownerid varchar(250),
    commentscount int,
    likescount int,
    wordbookimage varchar(250),
    primary key (wordbookname, wordbookownerid),
    foreign key(wordbookownerid) references userinfo(userid)
);

create table wordbookcomments (
    wordbookname varchar(250),
    wordbookownerid varchar(250),
    commentorid varchar(250),
    comment varchar(1000),
    primary key (wordbookname, wordbookownerid, commentorid),
    foreign key (wordbookname) references socialwordbook(wordbookname),
    foreign key (wordbookownerid) references userinfo(userid),
    foreign key (commentorid) references userinfo(userid)
);

create table wordbooklikes (
    wordbookname varchar(250),
    wordbookownerid varchar(250),
    wordbookuserid varchar(250),
    primary key (wordbookname,wordbookownerid,wordbookuserid),
    foreign key (wordbookname) references socialwordbook(wordbookname),
    foreign key (wordbookownerid) references userinfo(userid),
    foreign key (wordbookuserid) references userinfo(userid)
);

create table attend (
    attendeeid varchar(250),
    attenddate date,
    primary key(attendeeid,attenddate),
    foreign key (attendeeid) references userinfo(userid)
);

create table todayquoto(
    writerid varchar(250),
    quotonum int auto_increment,
    quoto varchar(1000),
    primary key (writerid, quotonum),
    foreign key(writerid) references userinfo(userid)
);

create table todayquotolikes(
    writerid varchar(250),
    quotonum int,
    likeduserid varchar(250),
    primary key (writerid,quotonum,likeduserid),
    foreign key (writerid, quotonum) references todayquoto(writerid, quotonum),
    foreign key (likeduserid) references userinfo(userid)
);