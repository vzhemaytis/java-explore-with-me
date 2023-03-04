CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_name UNIQUE (name)
);
CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR(20)                             NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    CONSTRAINT pk_events
        PRIMARY KEY (id),
    CONSTRAINT fk_events_category
        FOREIGN KEY (category_id) REFERENCES categories,
    CONSTRAINT fk_events_users
        FOREIGN KEY (initiator_id) REFERENCES users
);
CREATE TABLE IF NOT EXISTS participation_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_participation_requests
        PRIMARY KEY (id),
    CONSTRAINT fk_participation_requests_events
        FOREIGN KEY (event_id) REFERENCES events,
    CONSTRAINT fk_participation_requests_users
        FOREIGN KEY (requester_id) REFERENCES users
);
CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(120)                            NOT NULL,
    CONSTRAINT pk_compilations
        PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS compilation_event
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT pk_compilation_event
        PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_ce_compilations
        FOREIGN KEY (compilation_id) REFERENCES compilations,
    CONSTRAINT fk_ce_events
        FOREIGN KEY (event_id) REFERENCES events
);