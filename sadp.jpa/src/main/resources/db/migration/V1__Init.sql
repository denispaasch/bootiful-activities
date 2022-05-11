CREATE TABLE activity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alternate_key VARCHAR(255) NOT NULL UNIQUE,
    external_key VARCHAR(255) NULL,
    action VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    no_of_participants INT NOT NULL,
    details VARCHAR(255) NULL
);

CREATE TABLE participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alternate_key VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

CREATE TABLE activity_participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id INT NOT NULL,
    participant_id INT NOT NULL
);

ALTER TABLE activity_participant
    ADD FOREIGN KEY (activity_id)
    REFERENCES activity(id);

ALTER TABLE activity_participant
    ADD FOREIGN KEY (participant_id)
    REFERENCES participant(id)
