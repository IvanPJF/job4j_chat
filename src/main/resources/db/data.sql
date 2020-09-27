insert into room (name)
values ('java'),
       ('world');

insert into role (name)
values ('R_ADMIN'),
       ('R_USER');


insert into person (name, role_id)
values ('John', (select id from role where name = 'R_ADMIN')),
       ('Man', (select id from role where name = 'R_USER'));

insert into message (body, room_id, person_id)
values ('Hi, java', (select id from room where name = 'java'), (select id from person where name = 'John')),
       ('Hi, world. Its John', (select id from room where name = 'world'), (select id from person where name = 'John')),
       ('Hi, world. Its Man', (select id from room where name = 'world'), (select id from person where name = 'Man'));