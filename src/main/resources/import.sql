-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
INSERT INTO category (id, title, color) VALUES (2, 'LIVRE', '#2a7ae4');
INSERT INTO category (id, title, color) VALUES (3, 'Aluraflix', '#0000ff');
INSERT INTO video (id, title, description, url, category_id) VALUES (4, 'Title', 'Description', 'Url', 2);
INSERT INTO video (id, title, description, url, category_id) VALUES (5, 'Aluraflix', 'Description', 'Url', 3);