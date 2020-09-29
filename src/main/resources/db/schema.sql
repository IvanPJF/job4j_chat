drop table if exists message;
drop table if exists person;
drop table if exists role;
drop table if exists room;

create table room
(
    id   serial,
    name varchar(100) unique not null,
    primary key (id)
);

create table role
(
    id   serial,
    name varchar(20),
    primary key (id)
);

create table person
(
    id       serial,
    name     varchar(200),
    username varchar(200) not null unique,
    password varchar(200) not null,
    role_id  integer      not null,
    primary key (id),
    foreign key (role_id) references role (id)
);

create table message
(
    id        serial,
    body      varchar(2000),
    room_id   integer not null,
    person_id integer not null,
    primary key (id),
    foreign key (room_id) references room (id),
    foreign key (person_id) references person (id)
);