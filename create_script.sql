drop table if exists bom;
drop table if exists coordinates;
drop table if exists db_log;
drop table if exists template;
drop table if exists test_types;
drop table if exists dvp cascade;
drop table if exists part cascade;

create table bom
(
    id int auto_increment
        primary key,
    parent varchar(20) null,
    child varchar(20) null
);

create index part_id
    on bom (child);

create table db_log
(
    id int auto_increment
        primary key,
    user_id varchar(20) not null,
    time timestamp default current_timestamp() not null on update current_timestamp(),
    value varchar(100) not null
);

create table dvp
(
    id int auto_increment
        primary key,
    part_id varchar(100) null,
    date varchar(100) not null,
    aa varchar(100) null,
    consumer_id varchar(100) null,
    test_name varchar(100) null,
    test_result varchar(100) null,
    test_soll varchar(100) null,
    test_soll_plus varchar(50) null,
    test_soll_minus varchar(50) null
);

create index part_id
    on dvp (part_id);

create table part
(
    id int auto_increment,
    part_id varchar(20) not null,
    type varchar(20) not null,
    date varchar(30) not null,
    comment varchar(250) null,
    image longblob null,
    developed_from varchar(30) null,
    name varchar(30) null,
    constraint part_id_uindex
        unique (id)
);

alter table part
    add primary key (id);

create table template
(
    id int auto_increment
        primary key,
    name varchar(100) not null
);

create table test_types
(
    id int auto_increment
        primary key,
    name varchar(100) not null
);

create table coordinates
(
    id int auto_increment
        primary key,
    table_id int null,
    row int not null,
    col int not null,
    sheet int not null,
    test_type int null,
    constraint coordinates_ibfk_1
        foreign key (table_id) references template (id),
    constraint coordinates_ibfk_2
        foreign key (test_type) references test_types (id)
);

create index table_id
    on coordinates (table_id);

create index test_type
    on coordinates (test_type);

