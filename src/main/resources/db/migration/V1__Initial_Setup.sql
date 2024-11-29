
CREATE TABLE address
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    city    VARCHAR(255)          NOT NULL,
    country VARCHAR(255)          NOT NULL,
    number  INT                   NOT NULL,
    street  VARCHAR(255)          NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE announcement
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    date          datetime              NOT NULL,
    `description` VARCHAR(255)          NOT NULL,
    title         VARCHAR(255)          NOT NULL,
    author_id     BIGINT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE availability
(
    id                BIGINT       NOT NULL,
    availability_name VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE caretaker
(
    base_user_id         BIGINT       NOT NULL,
    personal_description VARCHAR(255) NOT NULL,
    salary               DOUBLE       NOT NULL,
    availability_id      BIGINT       NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (base_user_id)
);

CREATE TABLE gender
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    gender_name VARCHAR(255)          NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE manager
(
    base_user_id BIGINT NOT NULL,
    position_id  BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (base_user_id)
);

CREATE TABLE patient
(
    base_user_id         BIGINT       NOT NULL,
    personal_description VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (base_user_id)
);

CREATE TABLE position
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    position_name VARCHAR(255)          NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id        BIGINT       NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE sickness
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE sicknesses_for_caretaker
(
    sickness_id  BIGINT NOT NULL,
    caretaker_id BIGINT NOT NULL
);

CREATE TABLE sicknesses_of_patient
(
    sickness_id BIGINT NOT NULL,
    patient_id  BIGINT NOT NULL
);

CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    email        VARCHAR(255)          NOT NULL,
    first_name   VARCHAR(255)          NOT NULL,
    last_name    VARCHAR(255)          NOT NULL,
    password     VARCHAR(255)          NOT NULL,
    phone_number VARCHAR(255)          NOT NULL,
    address_id   BIGINT                NULL,
    gender_id    BIGINT                NOT NULL,
    role_id      BIGINT                NULL,
    active       BIT(1)                NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE sicknesses_for_caretaker
    ADD PRIMARY KEY (caretaker_id, sickness_id);

ALTER TABLE sicknesses_of_patient
    ADD PRIMARY KEY (patient_id, sickness_id);

ALTER TABLE sicknesses_for_caretaker
    ADD CONSTRAINT FK7dvnqmy8v27xk1iurxc1ycm0t FOREIGN KEY (sickness_id) REFERENCES sickness (id) ON DELETE NO ACTION;

CREATE INDEX FK7dvnqmy8v27xk1iurxc1ycm0t ON sicknesses_for_caretaker (sickness_id);

ALTER TABLE manager
    ADD CONSTRAINT FK82fbju2xn1xjeaai4axohoio FOREIGN KEY (position_id) REFERENCES position (id) ON DELETE NO ACTION;

CREATE INDEX FK82fbju2xn1xjeaai4axohoio ON manager (position_id);

ALTER TABLE user
    ADD CONSTRAINT FKcbf93j56y7t2tyhunb4neewva FOREIGN KEY (gender_id) REFERENCES gender (id) ON DELETE NO ACTION;

CREATE INDEX FKcbf93j56y7t2tyhunb4neewva ON user (gender_id);

ALTER TABLE user
    ADD CONSTRAINT FKddefmvbrws3hvl5t0hnnsv8ox FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE NO ACTION;

CREATE INDEX FKddefmvbrws3hvl5t0hnnsv8ox ON user (address_id);

ALTER TABLE patient
    ADD CONSTRAINT FKexedb8054kul85pmwhej1lvo9 FOREIGN KEY (base_user_id) REFERENCES user (id) ON DELETE NO ACTION;

ALTER TABLE sicknesses_of_patient
    ADD CONSTRAINT FKh37l1m4rc59f1n51a51ydkadn FOREIGN KEY (patient_id) REFERENCES patient (base_user_id) ON DELETE NO ACTION;

ALTER TABLE manager
    ADD CONSTRAINT FKimcjxjn1kpo7bmu0pfvlskdcw FOREIGN KEY (base_user_id) REFERENCES user (id) ON DELETE NO ACTION;

ALTER TABLE caretaker
    ADD CONSTRAINT FKlhnuki0ocf1srs1cj33ae7rgu FOREIGN KEY (base_user_id) REFERENCES user (id) ON DELETE NO ACTION;

ALTER TABLE announcement
    ADD CONSTRAINT FKm2kd0pa1vmfmkobifvdxha0l9 FOREIGN KEY (author_id) REFERENCES manager (base_user_id) ON DELETE NO ACTION;

CREATE INDEX fk_author ON announcement (author_id);

ALTER TABLE user
    ADD CONSTRAINT FKn82ha3ccdebhokx3a8fgdqeyy FOREIGN KEY (role_id) REFERENCES `role` (id) ON DELETE NO ACTION;

CREATE INDEX FKn82ha3ccdebhokx3a8fgdqeyy ON user (role_id);

ALTER TABLE caretaker
    ADD CONSTRAINT FKn8s1tjd4dko2egr7k9t98arnk FOREIGN KEY (availability_id) REFERENCES availability (id) ON DELETE NO ACTION;

CREATE INDEX FKn8s1tjd4dko2egr7k9t98arnk ON caretaker (availability_id);

ALTER TABLE sicknesses_for_caretaker
    ADD CONSTRAINT FKr83edd586ds0wem36wemjydf5 FOREIGN KEY (caretaker_id) REFERENCES caretaker (base_user_id) ON DELETE NO ACTION;

ALTER TABLE sicknesses_of_patient
    ADD CONSTRAINT FKxnh0v1im7611nio6x2jre9ml FOREIGN KEY (sickness_id) REFERENCES sickness (id) ON DELETE NO ACTION;

CREATE INDEX FKxnh0v1im7611nio6x2jre9ml ON sicknesses_of_patient (sickness_id);

