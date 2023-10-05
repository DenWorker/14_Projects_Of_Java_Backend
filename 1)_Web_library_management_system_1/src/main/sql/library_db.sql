create table People
(
    id         int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    fullName   varchar(100) NOT NULL UNIQUE,
    yearOfBorn int CHECK ( yearOfBorn > 1930 AND yearOfBorn < 2025)
);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Шумилова Елена Алексеевна', 2000);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Шереметьевская Мария Валерьевна', 2001);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Иванов Денис Геннадьевич', 1997);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Гальвас Михаил Андреевич', 2000);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Иванова Ольга Владимировна', 1977);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Кабанова Анна Николаевна', 2000);
INSERT INTO People(fullName, yearOfBorn)
VALUES ('Веселов Михаил Алексеевич', 1999);


create table Books
(
    id          int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title       varchar(100) NOT NULL,
    author      varchar(100) NOT NULL,
    releaseDate int CHECK ( releaseDate > 0 AND releaseDate < 2025),
    id          int          REFERENCES People (id) ON DELETE SET NULL
);
INSERT INTO Books(title, author, releaseDate, id)
VALUES ('Думай', 'Стюарт МакРоберт', 1950, 3);
INSERT INTO Books(title, author, releaseDate, id)
VALUES ('Правильные роды', 'К.И.Чайковский', 2008, 5);
INSERT INTO Books(title, author, releaseDate, id)
VALUES ('Спорт', 'Арнольд Шварценеггер', 1950, 2);



INSERT INTO Books(title, author, releaseDate)
VALUES ('День опричника', 'Владимир Сорокин', 2006);

INSERT INTO Books(title, author, releaseDate)
VALUES ('Философия Java', 'Брюс Эккель', 2018);


INSERT INTO Books(title, author, releaseDate)
VALUES ('Игра в бисер', 'Герман Гессе', 1943);

INSERT INTO Books(title, author, releaseDate)
VALUES ('Тайные виды на гору Фудзи', 'Владимир Пелевин', 2018);

INSERT INTO Books(title, author, releaseDate)
VALUES ('Бытие и время', 'Мартин Хайдеггер', 1927);



SELECT books.title, people.fullname, people.yearofborn
FROM books
         LEFT JOIN people on books.id = people.id;





