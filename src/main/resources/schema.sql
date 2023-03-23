CREATE TABLE IF NOT EXISTS  mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER CHECK (duration > 0),
    mpa_id INTEGER REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS film_genres (
  film_id INTEGER REFERENCES films(id),
  genre_id INTEGER REFERENCES genres(id)
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendship (
    user_id INTEGER REFERENCES users(id),
    friend_id INTEGER REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS likes (
  film_id INTEGER REFERENCES films(id),
  user_id INTEGER REFERENCES users(id)
);