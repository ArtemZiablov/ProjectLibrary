CREATE TABLE Book
(
    isbn               INT PRIMARY KEY,
    title              VARCHAR(100)   NOT NULL,
    year_of_publishing INT            NOT NULL,
    number_of_pages    INT            NOT NULL,
    annotation         VARCHAR(30000) NOT NULL,
    book_photo         varchar(100)   NOT NULL,
    date_of_add        date,
    language           varchar(50)

);

CREATE TABLE Author
(
    author_id     INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    full_name     VARCHAR(100) NOT NULL,
    date_of_birth DATE         NOT NULL,
    nationality   VARCHAR(50)  NOT NULL
);

-- Join Table
CREATE TABLE Author_of_the_book
(
    author_id INT REFERENCES Author (author_id),
    book_isbn INT REFERENCES Book (isbn),
    PRIMARY KEY (author_id, book_isbn)
);

CREATE TABLE Translator
(
    translator_id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    full_name     VARCHAR(100) NOT NULL
);

-- Join Table
CREATE TABLE Translator_of_the_book
(
    translator_id INT REFERENCES Translator (translator_id),
    book_isbn     INT REFERENCES Book (isbn),
    PRIMARY KEY (translator_id, book_isbn)
);

CREATE TABLE Genre
(
    genre_id   INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    genre_name VARCHAR(50) NOT NULL
);

-- Join Table
CREATE TABLE Genre_of_the_book
(
    genre_id  INT REFERENCES Genre (genre_id),
    book_isbn INT REFERENCES Book (isbn),
    PRIMARY KEY (genre_id, book_isbn)
);

CREATE TABLE Reader
(
    reader_id     INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    full_name     VARCHAR(100) NOT NULL,
    birth_date    DATE         NOT NULL,
    password      VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(50)  NOT NULL,
    email         VARCHAR(50)  NOT NULL,
    profile_photo VARCHAR(300),
    debtor        BOOLEAN
);

CREATE TABLE Book_copy
(
    copy_id   INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    isbn      INT         NOT NULL,
    reader_id INT,
    status    VARCHAR(50) NOT NULL,
    FOREIGN KEY (isbn) REFERENCES Book (isbn),
    FOREIGN KEY (reader_id) REFERENCES Reader (reader_id)
);

CREATE TABLE Librarian
(
    librarian_id  INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    full_name     VARCHAR(100) NOT NULL,
    password      VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(50)  NOT NULL,
    email         VARCHAR(50)  NOT NULL,
    profile_photo VARCHAR(300) NOT NULL
);

CREATE TABLE Operations_with_books
(
    operation_id     INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    date_of_issuance DATE,
    return_deadline  DATE,
    date_of_return   DATE,
    librarian_id     INT NOT NULL,
    reader_id        INT NOT NULL,
    copy_id          INT NOT NULL,
    FOREIGN KEY (librarian_id) REFERENCES Librarian (librarian_id),
    FOREIGN KEY (reader_id) REFERENCES Reader (reader_id),
    FOREIGN KEY (copy_id) REFERENCES Book_copy (copy_id)
);

CREATE TABLE Book_reservation
(
    reservation_id      INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    date_of_reservation TIMESTAMP,
    reader_id           INT,
    book_isbn           INT,
    FOREIGN KEY (reader_id) REFERENCES Reader (reader_id),
    FOREIGN KEY (book_isbn) REFERENCES Book (isbn)
);

create table admin
(
    admin_id      int primary key generated by default as identity,
    full_name     varchar(100) not null,
    phone_number  varchar(50)  not null,
    email         varchar(50)  not null,
    profile_photo varchar(300),
    password      varchar(100) not null
);
