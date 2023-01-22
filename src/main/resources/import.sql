-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
INSERT INTO client (id, username, password) VALUES (2, 'username@email.com', '$2a$10$uSWddQMufSCspxFtzRYCwu97.pkdQiidB3EorsRBE1/Wouj9rKWSu');

INSERT INTO category (id, title, color) VALUES (2, 'LIVRE', '#2a7ae4');
INSERT INTO category (id, title, color) VALUES (3, 'Aluraflix', '#0000ff');
INSERT INTO category (id, title, color) VALUES (4, '4', '#0000ff');
INSERT INTO category (id, title, color) VALUES (5, '5', '#2a7ae4');
INSERT INTO category (id, title, color) VALUES (6, '6', '#0000ff');
INSERT INTO category (id, title, color) VALUES (7, '4', '#0000ff');
INSERT INTO category (id, title, color) VALUES (8, '5', '#2a7ae4');
INSERT INTO category (id, title, color) VALUES (9, '6', '#0000ff');
INSERT INTO category (id, title, color) VALUES (10, '2', '#0000ff');

INSERT INTO video (id, title, description, url, category_id) VALUES (3, 'Aluraflix3', 'Description3', 'Url3', 3);
INSERT INTO video (id, title, description, url, category_id) VALUES (4, '4', 'Description4', 'Url4', 4);
INSERT INTO video (id, title, description, url, category_id) VALUES (5, 'Aluraflix5', 'Description5', 'Url5', 3);
INSERT INTO video (id, title, description, url, category_id) VALUES (6, '6', 'Description6', 'Url6', 6);