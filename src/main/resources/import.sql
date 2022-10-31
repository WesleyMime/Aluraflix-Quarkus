-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
insert into video (id, title, description, url) values(nextval('hibernate_sequence'), 'title-1', 'description-1', 'url-1');
insert into video (id, title, description, url) values(nextval('hibernate_sequence'), 'title-2', 'description-2', 'url-2');
insert into video (id, title, description, url) values(nextval('hibernate_sequence'), 'title-3', 'description-3', 'url-3');