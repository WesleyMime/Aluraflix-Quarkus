ALTER TABLE video ADD categoryId bigint;
ALTER TABLE video ADD FOREIGN KEY (categoryId) REFERENCES category(id);

ALTER TABLE category ADD videoId bigint;
ALTER TABLE category ADD FOREIGN KEY (videoId) REFERENCES video(id);