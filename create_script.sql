create table bom_id
(
	id int auto_increment
		primary key,
	part_id int null
);

create index part_id
	on bom_id (part_id);

create table db_log
(
	id int auto_increment
		primary key,
	user_id varchar(20) not null,
	time timestamp default current_timestamp() not null on update current_timestamp(),
	value varchar(100) not null
);

create table part
(
	id int auto_increment
		primary key,
	type varchar(20) not null,
	date date not null,
	comment varchar(250) null,
	bom_id int not null,
	constraint bom_id
		foreign key (bom_id) references bom_id (id)
);

create table bom
(
	id int auto_increment
		primary key,
	bom_id int null,
	part_id int null,
	constraint bom_ibfk_1
		foreign key (bom_id) references bom_id (id),
	constraint bom_ibfk_2
		foreign key (part_id) references part (id)
);

create index bom_id
	on bom (bom_id);

create index part_id
	on bom (part_id);

alter table bom_id
	add constraint bom_id_ibfk_1
		foreign key (part_id) references part (id);

create table dvp
(
	id int auto_increment
		primary key,
	part_id int null,
	date date not null,
	aa varchar(100) null,
	constraint dvp_ibfk_1
		foreign key (part_id) references part (id)
);

create index part_id
	on dvp (part_id);

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

create table dvp_result
(
	id int auto_increment
		primary key,
	dvp_id int null,
	test_name int null,
	result varchar(100) null,
	constraint dvp_result_ibfk_1
		foreign key (dvp_id) references dvp (id),
	constraint dvp_result_ibfk_2
		foreign key (test_name) references test_types (id)
);

create index dvp_id
	on dvp_result (dvp_id);

create index test_name
	on dvp_result (test_name);

