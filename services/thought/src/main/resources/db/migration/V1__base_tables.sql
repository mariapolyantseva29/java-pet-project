CREATE TABLE situations (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    description VARCHAR(1000),
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE thoughts (
    id UUID PRIMARY KEY,
    situation_id UUID NOT NULL,
    critical_content VARCHAR(700),
    real_content VARCHAR(700),
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_thoughts_situation
        FOREIGN KEY (situation_id)
        REFERENCES situations(id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);
