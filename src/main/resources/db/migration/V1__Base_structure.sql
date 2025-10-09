CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR,
    email VARCHAR UNIQUE,
    created_at TIMESTAMP,
    password VARCHAR,
    profile_picture VARCHAR
);

CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE games (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    game_name VARCHAR,
    game_description VARCHAR,
    image_url VARCHAR
);

CREATE TABLE platforms (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    platform_name VARCHAR,
    platform_description VARCHAR,
    image_url VARCHAR
);

CREATE TABLE games_platforms (
    game_id UUID NOT NULL,
    platform_id UUID NOT NULL,
    PRIMARY KEY(game_id, platform_id),
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (platform_id) REFERENCES platforms(id)
);

CREATE TABLE achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    achievement_name VARCHAR,
    achievement_description VARCHAR,
    achievement_author UUID,
    verified BOOLEAN,
    game UUID,
    FOREIGN KEY (achievement_author) REFERENCES users(id),
    FOREIGN KEY (game) REFERENCES games(id)
);

CREATE TABLE challenges (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    challenge_name VARCHAR,
    challenge_description VARCHAR,
    challenge_author UUID,
    verified BOOLEAN,
    FOREIGN KEY (challenge_author) REFERENCES users(id)
);

CREATE TABLE challenges_achievements (
    achievement_id UUID NOT NULL,
    challenge_id UUID NOT NULL,
    PRIMARY KEY(achievement_id, challenge_id),
    FOREIGN KEY (achievement_id) REFERENCES achievements(id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id)
);

CREATE TABLE achievements_finished (
    achievement_id UUID NOT NULL,
    user_id UUID NOT NULL,
    completed_at TIMESTAMP NOT NULL,
    PRIMARY KEY(achievement_id, user_id),
    FOREIGN KEY (achievement_id) REFERENCES achievements(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE challenges_finished (
    challenge_id UUID NOT NULL,
    user_id UUID NOT NULL,
    completed_at TIMESTAMP NOT NULL,
    PRIMARY KEY(challenge_id, user_id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE votes_achievements (
    user_id UUID NOT NULL,
    achievement_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, achievement_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (achievement_id) REFERENCES achievements(id)
);

CREATE TABLE votes_challenges (
    user_id UUID NOT NULL,
    challenge_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, challenge_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id)
);

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_OWNER');

INSERT INTO games(game_name, game_description, image_url) VALUES ('Counter Strike 2', 'Descricao do cs2', 'Imagem do cs2');
INSERT INTO platforms(platform_name, platform_description, image_url) VALUES ('Steam', 'Plataforma do titio gaben', 'Imagem da Steam');

INSERT INTO games_platforms(game_id, platform_id)
SELECT
    (SELECT id FROM games WHERE game_name = 'Counter Strike 2'),
    (SELECT id FROM platforms WHERE platform_name = 'Steam');