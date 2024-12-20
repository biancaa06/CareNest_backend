CREATE TABLE reset_password_code
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    reset_code      INT                   NOT NULL,
    expiration_time datetime              NOT NULL,
    user_id         BIGINT                NOT NULL,
    CONSTRAINT pk_reset_password_code PRIMARY KEY (id)
);

ALTER TABLE reset_password_code
    ADD CONSTRAINT uc_reset_password_code_user UNIQUE (user_id);

ALTER TABLE reset_password_code
    ADD CONSTRAINT FK_RESET_PASSWORD_CODE_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);