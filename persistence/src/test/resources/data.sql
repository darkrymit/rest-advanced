INSERT INTO tags (`name`)
VALUES ('tagName1');
INSERT INTO tags (`name`)
VALUES ('tagName3');
INSERT INTO tags (`name`)
VALUES ('tagName5');
INSERT INTO tags (`name`)
VALUES ('tagName4');
INSERT INTO tags (`name`)
VALUES ('tagName2');

INSERT INTO users (id,creation_date)
VALUES ('028fcacb-b19b-4268-9e0c-6d96669b0d5e', '2020-08-29T06:12:15.156');
INSERT INTO users (id,creation_date)
VALUES ('01e5aa8b-52ef-45ac-bc27-d5ae60dd9481', '2019-08-29T06:12:15.156');

INSERT INTO orders (owner_id, creation_date, last_modified_by, last_modified_date)
VALUES ('028fcacb-b19b-4268-9e0c-6d96669b0d5e', '2020-09-29T06:12:15.156', 'test@gmail.com', '2020-09-29T06:12:15.156');
INSERT INTO orders (owner_id, creation_date, last_modified_by, last_modified_date)
VALUES ('028fcacb-b19b-4268-9e0c-6d96669b0d5e', '2020-10-29T06:12:15.156', 'test@gmail.com', '2020-10-29T06:12:15.156');

INSERT INTO order_items (order_id, gift_certificate_id, price)
VALUES (1, 1,200.0);
INSERT INTO order_items (order_id, gift_certificate_id, price)
VALUES (2, 1,180.0);

INSERT INTO certificates (`name`, description, price, duration, create_date, last_update_date)
VALUES ('giftCertificate1', 'description1', 10.1, 1, '2020-08-29T06:12:15.156',
        '2020-08-29T06:12:15.156');
INSERT INTO certificates (`name`, description, price, duration, create_date, last_update_date)
VALUES ('giftCertificate3', 'description3', 30.3, 3, '2019-08-29T06:12:15.156',
        '2019-08-29T06:12:15.156');
INSERT INTO certificates (`name`, description, price, duration, create_date, last_update_date)
VALUES ('giftCertificate2', 'description2', 20.2, 2, '2018-08-29T06:12:15.156',
        '2018-08-29T06:12:15.156');

INSERT INTO certificates_has_tags (certificates_id, tags_id)
VALUES (1, 2);
INSERT INTO certificates_has_tags (certificates_id, tags_id)
VALUES (2, 2);