CREATE SEQUENCE users_seq START WITH 10050 INCREMENT BY 50;

CREATE TABLE users
(
    user_id  bigint       NOT NULL PRIMARY KEY, -- Define user_id as the primary key
    mail     varchar(255) NOT NULL,             -- User's email
    password varchar(500) NOT NULL,             -- Password
    role     varchar(13) DEFAULT 'MITARBEITER'  -- Userrole
);

CREATE UNIQUE INDEX uq_users_mail ON users (LOWER(mail));