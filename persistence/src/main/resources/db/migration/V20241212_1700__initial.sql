CREATE SEQUENCE users_seq START WITH 10050 INCREMENT BY 50;

CREATE TABLE users
(
    user_id           bigint       NOT NULL PRIMARY KEY,     -- Define user_id as the primary key
    mail              varchar(255) NOT NULL,                 -- User's email
    password          varchar(500) NOT NULL,                 -- Password
    role              varchar(13)    DEFAULT 'MITARBEITER',  -- Userrole
    enabled           BOOL CHECK (enabled IN (TRUE, FALSE)), -- User status
    budget            DECIMAL(10, 2) DEFAULT 0.00,           --User's budget
    question          varchar(255),                          -- Question user might has to answer for authentication
    answer            varchar(255),                          -- Answer to the question
    one_time_password VARCHAR(64),                           --User OTP
    otp_request_time  TIMESTAMP without time zone            --User's request time for the OTP

);

CREATE UNIQUE INDEX uq_users_mail ON users (LOWER(mail));


CREATE TABLE login_events
(
    id               BIGSERIAL PRIMARY KEY, -- Auto-increment ID
    user_id          BIGINT    NOT NULL,    -- User ID
    mail             VARCHAR(255),          -- mail
    login_time       TIMESTAMP NOT NULL,    -- Zeitpunkt des Logins
    ip_address       VARCHAR(255),          -- IP-Adresse des Nutzers
    location         VARCHAR(255),          -- Standort des Nutzers
    browser          VARCHAR(255),          -- Browser des Nutzers
    browser_version  VARCHAR(255),          -- Browser Version des Nutzers
    operating_system VARCHAR(255),          -- Betriebssystem des Nutzers
    status           VARCHAR(255)           -- Status ob Authentifizierung erfolgreich war, oder nicht
);