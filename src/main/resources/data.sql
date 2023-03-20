--Заполняем БД значениями ассоциации рейтингов фильмов
INSERT INTO mpa (name) SELECT 'G' WHERE NOT EXISTS(SELECT * FROM mpa WHERE name='G');
INSERT INTO mpa (name) SELECT 'PG' WHERE NOT EXISTS(SELECT * FROM mpa WHERE name='PG');
INSERT INTO mpa (name) SELECT 'PG-13' WHERE NOT EXISTS(SELECT * FROM mpa WHERE name='PG-13');
INSERT INTO mpa (name) SELECT 'R' WHERE NOT EXISTS(SELECT * FROM mpa WHERE name='R');
INSERT INTO mpa (name) SELECT 'NC-17' WHERE NOT EXISTS(SELECT * FROM mpa WHERE name='NC-17');

--Заполняем БД минимальным набором жанров
INSERT INTO genres (name) SELECT 'Боевик' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Боевик');
INSERT INTO genres (name) SELECT 'Вестерн' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Вестерн');
INSERT INTO genres (name) SELECT 'Драма' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Драма');
INSERT INTO genres (name) SELECT 'Комедия' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Комедия');
INSERT INTO genres (name) SELECT 'Мелодрама' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Мелодрама');
INSERT INTO genres (name) SELECT 'Трагедия' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Трагедия');
INSERT INTO genres (name) SELECT 'Триллер' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Триллер');
INSERT INTO genres (name) SELECT 'Хоррор' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Хоррор');
INSERT INTO genres (name) SELECT 'Фантастика' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Фантастика');
INSERT INTO genres (name) SELECT 'Фэнтези' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Фэнтези');
INSERT INTO genres (name) SELECT 'Фильм-катастрофа' WHERE NOT EXISTS(SELECT * FROM genres WHERE name='Фильм-катастрофа');