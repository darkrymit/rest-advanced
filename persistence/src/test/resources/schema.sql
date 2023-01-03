DROP TABLE IF EXISTS certificates_has_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS certificates;

CREATE TABLE certificates
(
    id               bigint         NOT NULL AUTO_INCREMENT,
    `name`          varchar(45)    NOT NULL,
    `description`    varchar(45)    NOT NULL,
    price            decimal(19, 4) NOT NULL,
    duration         int            NOT NULL,
    create_date      datetime(6)    NOT NULL,
    last_update_date datetime(6)    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (`name`),
    UNIQUE (id)
);

CREATE TABLE tags
(
id     bigint      NOT NULL AUTO_INCREMENT,
`name` varchar(45) NOT NULL,
PRIMARY KEY (id),
UNIQUE (id),
UNIQUE (`name`)
);

CREATE TABLE certificates_has_tags
(
    certificates_id bigint NOT NULL,
    tags_id         bigint NOT NULL,
    PRIMARY KEY (certificates_id, tags_id),
    CONSTRAINT fk_certificates_has_tags_certificates FOREIGN KEY (certificates_id) REFERENCES certificates (id),
    CONSTRAINT fk_certificates_has_tags_tags1 FOREIGN KEY (tags_id) REFERENCES tags (id)
);

