ALTER TABLE availability ADD CONSTRAINT UNIQUE (availability_name);
ALTER TABLE gender ADD CONSTRAINT UNIQUE (gender_name);
ALTER TABLE position ADD CONSTRAINT UNIQUE (position_name);
ALTER TABLE role ADD CONSTRAINT UNIQUE (role_name);


INSERT INTO availability (id, availability_name) VALUES
                                                     (1, 'FULL_TIME'),
                                                     (2, 'PART_TIME')
ON DUPLICATE KEY UPDATE availability_name = VALUES(availability_name);

INSERT INTO gender (id, gender_name) VALUES
                                         (1, 'MALE'),
                                         (2, 'FEMALE'),
                                         (3, 'OTHER')
ON DUPLICATE KEY UPDATE gender_name = VALUES(gender_name);

INSERT INTO position (id, position_name) VALUES
                                             (1, 'PR'),
                                             (2, 'MEDICAL')
ON DUPLICATE KEY UPDATE position_name = VALUES(position_name);

INSERT INTO `role` (id, role_name) VALUES
                                       (1, 'MANAGER'),
                                       (2, 'PATIENT'),
                                       (3, 'CARETAKER')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);
