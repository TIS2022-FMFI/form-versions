drop table if exists bom;
drop table if exists coordinates;
drop table if exists db_log;
drop table if exists template;
drop table if exists dvp cascade;
drop table if exists test_types cascade;
drop table if exists part cascade;

create table bom
(
    id     int auto_increment
        primary key,
    parent varchar(20) null,
    child  varchar(20) null
);

create index part_id
    on bom (child);

create table db_log
(
    id      int auto_increment
        primary key,
    user_id varchar(20)                           not null,
    time    timestamp default current_timestamp() not null on update current_timestamp(),
    value   varchar(100)                          not null
);

create table part
(
    id             int auto_increment
        primary key,
    part_id        varchar(20)  not null,
    type           varchar(20)  not null,
    date           varchar(30)  not null,
    comment        varchar(250) null,
    image          longblob     null,
    developed_from varchar(30)  null,
    name           varchar(30)  null,
    constraint part_id_uindex
        unique (id)
);

create table template
(
    id    int auto_increment
        primary key,
    name  varchar(100) not null,
    excel longblob     null
);

create table test_types
(
    id   int auto_increment
        primary key,
    name varchar(100) not null
);

create table coordinates
(
    id        int auto_increment
        primary key,
    table_id  int null,
    row       int not null,
    col       int not null,
    sheet     int not null,
    test_type int null,
    constraint coordinates_ibfk_1
        foreign key (table_id) references template (id)
            on delete cascade,
    constraint coordinates_ibfk_2
        foreign key (test_type) references test_types (id)
);

create index table_id
    on coordinates (table_id);

create index test_type
    on coordinates (test_type);

create table dvp
(
    id              int auto_increment
        primary key,
    part_id         varchar(100) null,
    date            varchar(100) not null,
    aa              varchar(100) null,
    consumer_id     varchar(100) null,
    test_result     varchar(100) null,
    test_soll       varchar(100) null,
    test_soll_plus  varchar(50)  null,
    test_soll_minus varchar(50)  null,
    test_type_id    int          null,
    constraint test_type_id
        foreign key (test_type_id) references test_types (id)
);

create index part_id
    on dvp (part_id);





insert into test_types (name) VALUE ('DVP / Teilelebenslauf - xxx.xxx cENGIS- xxxxxx : Customer Design Phase');
insert into test_types (name) VALUE ('DVP / Teilelebenslauf - xxx.xxx cENGIS- xxxxxx : Development Stage : No.');
insert into test_types (name) VALUE ('DVP / Teilelebenslauf - xxx.xxx cENGIS- xxxxxx : Development Stage : Change Description');
insert into test_types (name) VALUE ('DVP / Teilelebenslauf - xxx.xxx cENGIS- xxxxxx : Report');
insert into test_types (name) VALUE ('DVP / Teilelebenslauf - xxx.xxx cENGIS- xxxxxx : Comment');
insert into test_types (name) VALUE ('DVP / Teilelebenslauf - xxx.xxx cENGIS- xxxxxx : Part');
insert into test_types (name) VALUE ('Rubber Compound : Designation : [-]');
insert into test_types (name) VALUE ('Rubber Compound : Batch [ShA]');
insert into test_types (name) VALUE ('Rubber Compound : Real Hardness : [Sh A]');
insert into test_types (name) VALUE ('Rubber Compound : Rebound : [%]');
insert into test_types (name) VALUE ('Rubber Compound');
insert into test_types (name) VALUE ('Static Rating Parameters : Preload : [N]');
insert into test_types (name) VALUE ('Static Rating Parameters : Load : [N] or [°]');
insert into test_types (name) VALUE ('Static Rating Parameters : Evaluation range : [N] or [mm; °]');
insert into test_types (name) VALUE ('Static Rating Parameters : Direction');
insert into test_types (name) VALUE ('C Static : Radial Z : [N/mm]');
insert into test_types (name) VALUE ('C Static : Radial Y : [N/mm]');
insert into test_types (name) VALUE ('C Static : Axial X : [N/mm]');
insert into test_types (name) VALUE ('C Static : Con X : [Nm/°]');
insert into test_types (name) VALUE ('C Static : Con Y : [Nm/°]');
insert into test_types (name) VALUE ('C Static : Tor Z : [Nm/°]');
insert into test_types (name) VALUE ('Static Rating');
insert into test_types (name) VALUE ('Dynamic Rating Parameters : Preload : [N]');
insert into test_types (name) VALUE ('Dynamic Rating Parameters : Amplitude : [mm]');
insert into test_types (name) VALUE ('Dynamic Rating Parameters : Frequency : [Hz]');
insert into test_types (name) VALUE ('Dynamic Rating Parameters : Direction');
insert into test_types (name) VALUE ('Dynamic Values :  Loss Angle @ Frequenzy : [°]');
insert into test_types (name) VALUE ('Dynamic Values :  Loss Angle @ Frequenzy : [Hz]');
insert into test_types (name) VALUE ('Dynamic Values : Cdyn @  Max Loss Angle : [N/mm]');
insert into test_types (name) VALUE ('Dynamic Values : Radial Z Cdyn @ Frequenzy : [N/mm]');
insert into test_types (name) VALUE ('Dynamic Values : Radial Z Cdyn @ Frequenzy : [Hz]');
insert into test_types (name) VALUE ('Dynamic Values : Radial Y Cdyn @ Frequenzy : [N/mm]');
insert into test_types (name) VALUE ('Dynamic Values : Radial Y Cdyn @ Frequenzy : [Hz]');
insert into test_types (name) VALUE ('Dynamic Values : Axial X Cdyn @ Frequenzy : [N/mm]');
insert into test_types (name) VALUE ('Dynamic Values : Axial X Cdyn @ Frequenzy : [Hz]');
insert into test_types (name) VALUE ('Dynamic Rating');
insert into test_types (name) VALUE ('Amplitude 0,1mm : max. Damping : [1°]');
insert into test_types (name) VALUE ('Amplitude 0,1mm : max. Damping : [Hz]');
insert into test_types (name) VALUE ('Amplitude 0,1mm : Cdyn at 50 Hz : [N/mm]');
insert into test_types (name) VALUE ('Amplitude 0,5mm : max. Damping : [1°]');
insert into test_types (name) VALUE ('Amplitude 0,5mm : max. Damping : [Hz]');
insert into test_types (name) VALUE ('Amplitude 0,5mm : Cdyn at 50 Hz : [N/mm]');
insert into test_types (name) VALUE ('Amplitude 1mm : max. Damping : [1°]');
insert into test_types (name) VALUE ('Amplitude 1mm : max. Damping : [Hz]');
insert into test_types (name) VALUE ('Amplitude 1mm : Cdyn at ... Hz : [N/mm]');
insert into test_types (name) VALUE ('X  = ± ... mm : Cdyn  max : [N/mm]');
insert into test_types (name) VALUE ('X  = ± ... mm : Freq. at Cdyn  max : [Hz]');
insert into test_types (name) VALUE ('X  = ± ... mm : Cdyn  at  max Damping : [N/mm]');
insert into test_types (name) VALUE ('X  = ± ... mm : Loss Angle at ... Hz : [1°]');
insert into test_types (name) VALUE ('Dimensions');
insert into test_types (name) VALUE ('Fatigue Strength : Internally');
insert into test_types (name) VALUE ('Fatigue Strength :  OEM');
insert into test_types (name) VALUE ('Fatigue Strength');
insert into test_types (name) VALUE ('Rupture Test : FBruch  : [N]');
insert into test_types (name) VALUE ('Rupture Test : Position R Position RC Position M : [%]');
insert into test_types (name) VALUE ('Rupture Test');
insert into test_types (name) VALUE ('Water Vapor Test : FBruch  : [kN]');
insert into test_types (name) VALUE ('Water Vapor Test : Position R Position RC Position M : [%]');
insert into test_types (name) VALUE ('Water Vapor Test');
insert into test_types (name) VALUE ('Salt Spray Test : Internally');
insert into test_types (name) VALUE ('Salt Spray Test : FBruch  : [kN]');
insert into test_types (name) VALUE ('Salt Spray Test : Position R Position RC Position M : [%]');
insert into test_types (name) VALUE ('Salt Spray Test');
insert into test_types (name) VALUE ('Endurance Strength Test : Inner Part : [mm]');
insert into test_types (name) VALUE ('Endurance Strength Test : Outer Part : [mm]');
insert into test_types (name) VALUE ('Endurance Strength Test');
insert into test_types (name) VALUE ('Satzprüfung : Time 1: 6s at RT : [mm]');
insert into test_types (name) VALUE ('Satzprüfung : Time 2: 24h at RT : [mm]');
insert into test_types (name) VALUE ('Satzprüfung : Time 3: 168h at RT : [mm]');
insert into test_types (name) VALUE ('Satzprüfung : [% / Dekade]');
insert into test_types (name) VALUE ('Satzprüfung : Time 1 Xh at X°C');
insert into test_types (name) VALUE ('Kryotest');
insert into test_types (name) VALUE ('Press-in / Push-out force : Fin PA : Internally');
insert into test_types (name) VALUE ('Press-in / Push-out force : Fout PA : Internally');
insert into test_types (name) VALUE ('Press-in / Push-out force');
insert into test_types (name) VALUE ('Sonstige : Produktspezifische Prüfungen / Sonderprüfung');
insert into test_types (name) VALUE ('Sonstige : Akustik- untersuchung am Prüfstand / Meßmaschine');
insert into test_types (name) VALUE ('Sonstige : Kräfte und Momente werden im Fahrzeug ohne erkennbare Geräusche übertragen');
insert into test_types (name) VALUE ('Other');
insert into test_types (name) VALUE ('Shipment : Date');
insert into test_types (name) VALUE ('Shipment : Purchase order');
insert into test_types (name) VALUE ('Shipment : Pcs.');
insert into test_types (name) VALUE ('Shipment : Kenn-zeichng.');
insert into test_types (name) VALUE ('Shipment');
insert into test_types (name) VALUE ('C-Engis No.');
insert into test_types (name) VALUE ('Requirement to create CMS/CML project? If so, cEngis No.');
insert into test_types (name) VALUE ('Dev.Phase Customer : Ford: M-1; VP; TT; PP');
insert into test_types (name) VALUE ('Comment');

