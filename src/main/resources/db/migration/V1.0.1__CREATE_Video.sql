CREATE TABLE video
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    title VARCHAR(255),
    description VARCHAR(255),
    url VARCHAR(255),
    PRIMARY KEY (id)
)
