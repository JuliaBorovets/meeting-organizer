DROP TABLE IF EXISTS authority CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS verification_token CASCADE;
DROP TABLE IF EXISTS actions CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS libraries CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS reactions CASCADE;
DROP TABLE IF EXISTS streams CASCADE;
DROP TABLE IF EXISTS subscriptions CASCADE;
DROP TABLE IF EXISTS tags CASCADE;
DROP TABLE IF EXISTS tag_event CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS role_authority CASCADE;
DROP TABLE IF EXISTS event_user_fav CASCADE;
DROP TABLE IF EXISTS library_user_fav CASCADE;
DROP TABLE IF EXISTS library_user_access CASCADE;
DROP TABLE IF EXISTS event_user_access CASCADE;
DROP TABLE IF EXISTS event_user_visitor CASCADE;

CREATE TABLE authority
(
    authority_id BIGSERIAL,
    permission   VARCHAR(255),

    CONSTRAINT authority_pkey PRIMARY KEY (authority_id)
);

CREATE TABLE roles
(
    role_id BIGSERIAL,
    name    VARCHAR(255),

    CONSTRAINT roles_pkey PRIMARY KEY (role_id)
);

CREATE TABLE users
(
    user_id                 BIGSERIAL,
    email                   VARCHAR(255) UNIQUE,
    username                VARCHAR(255) UNIQUE,
    password                VARCHAR(255),
    first_name              VARCHAR(255),
    last_name               VARCHAR(255),
    birthday                DATE,
    bio                     VARCHAR(255),
    image                   BYTEA,
    phone_number            VARCHAR(255),
    creation_date           TIMESTAMP,
    last_modified_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_google2fa          BOOLEAN   DEFAULT false,
    google2fa_secret        VARCHAR(255),
    account_non_expired     BOOLEAN   DEFAULT true,
    account_non_locked      BOOLEAN   DEFAULT true,
    credentials_non_expired BOOLEAN   DEFAULT true,
    enabled                 BOOLEAN   DEFAULT false,
    location_id             BIGINT,

    CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

CREATE TABLE verification_token
(
    verification_token_id BIGSERIAL,
    token                 VARCHAR(255),
    expiry_date           TIMESTAMP,
    user_id               BIGINT,

    CONSTRAINT verification_token_pkey PRIMARY KEY (verification_token_id)
);

CREATE TABLE actions
(
    action_id   BIGSERIAL,
    action_type VARCHAR(255),
    user_id     BIGINT,
    event_id    BIGINT,
    description VARCHAR(255),

    CONSTRAINT actions_pkey PRIMARY KEY (action_id)
);

CREATE TABLE comments
(
    comment_id         BIGSERIAL,
    username           VARCHAR(255),
    text               VARCHAR(255),
    creation_date      TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    event_id           BIGINT,
    user_id            BIGINT,

    CONSTRAINT comments_pkey PRIMARY KEY (comment_id)
);

CREATE TABLE events
(
    event_id                BIGSERIAL,
    start_date              TIMESTAMP,
    end_date                TIMESTAMP,
    max_number_participants INTEGER,
    name                    VARCHAR(255),
    description             VARCHAR(500),
    photo                   BYTEA,
    event_type              VARCHAR(255),
    state                   VARCHAR(255),
    meeting_type            VARCHAR(255),
    external_meeting_id     VARCHAR(255),
    library_id              BIGINT,
    user_id                 BIGINT,
    location_id             BIGINT,
    stream_id               BIGINT,
    join_url                VARCHAR(255),
    access_token            VARCHAR(255),
    creation_date           TIMESTAMP,
    is_private              BOOLEAN,

    CONSTRAINT events_pkey PRIMARY KEY (event_id)
);

CREATE TABLE libraries
(
    library_id   BIGSERIAL,
    name         VARCHAR(255),
    description  VARCHAR(255),
    access_token VARCHAR(255),
    image        BYTEA,
    user_id      BIGINT,
    is_private   BOOLEAN,

    CONSTRAINT libraries_pkey PRIMARY KEY (library_id)
);

CREATE TABLE locations
(
    location_id   BIGSERIAL,
    country       VARCHAR(255),
    city          VARCHAR(255),
    creation_date TIMESTAMP,

    CONSTRAINT locations_pkey PRIMARY KEY (location_id)
);

CREATE TABLE notifications
(
    notification_id BIGSERIAL,
    text            VARCHAR(255),
    is_read         BOOLEAN,
    event_id        BIGINT,
    user_id         BIGINT,

    CONSTRAINT notifications_pkey PRIMARY KEY (notification_id)
);

CREATE TABLE rating
(
    rating_id     BIGSERIAL,
    username      VARCHAR(255),
    score         INTEGER NOT NULL,
    creation_date TIMESTAMP,
    event_id      BIGINT,
    user_id       BIGINT,

    CONSTRAINT rating_pkey PRIMARY KEY (rating_id)
);


CREATE TABLE streams
(
    stream_id  BIGSERIAL,
    name       VARCHAR(255),
    library_id BIGINT,

    CONSTRAINT streams_pkey PRIMARY KEY (stream_id)
);

CREATE TABLE subscriptions
(
    subscription_id BIGSERIAL,
    user_id         BIGINT,

    CONSTRAINT subscriptions_pkey PRIMARY KEY (subscription_id)
);

CREATE TABLE tags
(
    tag_id BIGSERIAL,
    name   VARCHAR(255),

    CONSTRAINT tags_pkey PRIMARY KEY (tag_id)
);


CREATE TABLE tag_event
(
    tag_id   BIGINT,
    event_id BIGINT,

    PRIMARY KEY (tag_id, event_id)
);

CREATE TABLE user_role
(
    user_id BIGINT,
    role_id BIGINT,

    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE role_authority
(
    role_id      BIGINT,
    authority_id BIGINT,

    PRIMARY KEY (role_id, authority_id)
);

CREATE TABLE library_user_fav
(
    library_id BIGINT,
    user_id    BIGINT,

    PRIMARY KEY (library_id, user_id)
);

CREATE TABLE event_user_fav
(
    event_id BIGINT,
    user_id  BIGINT,

    PRIMARY KEY (event_id, user_id)
);

CREATE TABLE event_user_access
(
    event_id BIGINT,
    user_id  BIGINT,

    PRIMARY KEY (event_id, user_id)
);

CREATE TABLE event_user_visitor
(
    event_id BIGINT,
    user_id  BIGINT,

    PRIMARY KEY (event_id, user_id)
);

CREATE TABLE library_user_access
(
    library_id BIGINT,
    user_id    BIGINT,

    PRIMARY KEY (library_id, user_id)
);

ALTER TABLE users
    ADD CONSTRAINT user_location_id_fk FOREIGN KEY (location_id) REFERENCES locations (location_id) ON DELETE SET NULL;

ALTER TABLE verification_token
    ADD CONSTRAINT token_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE actions
    ADD CONSTRAINT action_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    ADD CONSTRAINT action_event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL;

ALTER TABLE events
    ADD CONSTRAINT event_library_id_fk FOREIGN KEY (library_id) REFERENCES libraries (library_id) ON DELETE SET NULL,
    ADD CONSTRAINT event_location_id_fk FOREIGN KEY (location_id) REFERENCES locations (location_id) ON DELETE SET NULL,
    ADD CONSTRAINT event_stream_id_fk FOREIGN KEY (stream_id) REFERENCES streams (stream_id) ON DELETE SET NULL,
    ADD CONSTRAINT event_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;


ALTER TABLE libraries
    ADD CONSTRAINT library_stream_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE notifications
    ADD CONSTRAINT notification_event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL,
    ADD CONSTRAINT notification_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;


ALTER TABLE rating
    ADD CONSTRAINT rating_event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL,
    ADD CONSTRAINT rating_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE streams
    ADD CONSTRAINT stream_library_id_fk FOREIGN KEY (library_id) REFERENCES libraries (library_id) ON DELETE SET NULL;

ALTER TABLE subscriptions
    ADD CONSTRAINT subscription_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE tag_event
    ADD CONSTRAINT tag_event_tag_id_fk FOREIGN KEY (tag_id) REFERENCES tags (tag_id) ON DELETE SET NULL,
    ADD CONSTRAINT tag_event_event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL;


ALTER TABLE role_authority
    ADD CONSTRAINT role_authority_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE SET NULL,
    ADD CONSTRAINT role_authority_authority_id_fk FOREIGN KEY (authority_id) REFERENCES authority (authority_id) ON DELETE SET NULL;


ALTER TABLE user_role
    ADD CONSTRAINT user_role_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE SET NULL,
    ADD CONSTRAINT user_role_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE comments
    ADD CONSTRAINT comment_event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL,
    ADD CONSTRAINT comment_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE library_user_fav
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    ADD CONSTRAINT library_id_fk FOREIGN KEY (library_id) REFERENCES libraries (library_id) ON DELETE SET NULL;

ALTER TABLE event_user_fav
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    ADD CONSTRAINT event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL;

ALTER TABLE library_user_access
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    ADD CONSTRAINT library_id_fk FOREIGN KEY (library_id) REFERENCES libraries (library_id) ON DELETE SET NULL;

ALTER TABLE event_user_access
    ADD CONSTRAINT event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL,
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;

ALTER TABLE event_user_visitor
    ADD CONSTRAINT event_id_fk FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE SET NULL,
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL;
