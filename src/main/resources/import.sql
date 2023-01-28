-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
INSERT INTO client (id, username, password) VALUES (100, 'username@email.com', '$2a$10$uSWddQMufSCspxFtzRYCwu97.pkdQiidB3EorsRBE1/Wouj9rKWSu');

INSERT INTO category (title, color) VALUES ('LIVRE', '#2a7ae4');
INSERT INTO category (title, color) VALUES ('Aluraflix', '#0000ff');
INSERT INTO category (title, color) VALUES ('3', '#0000ff');
INSERT INTO category (title, color) VALUES ('4', '#2a7ae4');
INSERT INTO category (title, color) VALUES ('5', '#0000ff');
INSERT INTO category (title, color) VALUES ('6', '#0000ff');

INSERT INTO video (title, description, url, category_id) VALUES ('Aluraflix3', 'Description3', 'Url3', 1);
INSERT INTO video (title, description, url, category_id) VALUES ('4', 'Description4', 'Url4', 2);
INSERT INTO video (title, description, url, category_id) VALUES ('Aluraflix5', 'Description5', 'Url5', 1);
INSERT INTO video (title, description, url, category_id) VALUES ('6', 'Description6', 'Url6', 2);