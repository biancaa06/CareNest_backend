CREATE TABLE message
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    sender_id   BIGINT                NOT NULL,
    receiver_id BIGINT                NOT NULL,
    date        TIMESTAMP(6)          NOT NULL,
    text        VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_message PRIMARY KEY (id)
);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_RECEIVER FOREIGN KEY (receiver_id) REFERENCES user (id);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_SENDER FOREIGN KEY (sender_id) REFERENCES user (id);