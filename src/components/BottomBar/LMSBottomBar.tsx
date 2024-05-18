import React, { useState, useEffect } from 'react';
import  { Book } from './ListOfBooks/BookContainer/BookContainer';
import UserProfile from './UserProfile/UserProfile';
import './BottomBar.css';
import ListOfBooks from "./ListOfBooks/ListOfBooks";
import BookProfile, {BookInfo} from "./BookProfile/BookProfile";
import ErrorMessage from "./ErrorMessage/ErrorMessage";
import ListOfReaders from "./ListOfReaders/ListOfReaders";
import {Reader} from "./ListOfReaders/ReaderContainer/ReaderContainer";
import AddBook from "./AddBook/AddBook";
import ReaderProfile, {ReaderInfo} from "./ReaderProfile/ReaderProfile";
import Statistics from "./Statistics/Statistics";
import LibrarianProfile from "./LibrarianProfile/LibrarianProfile";

interface BottomBarProps {
    isAvatarClicked: boolean; // Пропс для определения, была ли нажата кнопка UserAvatar
    isTitleClicked: boolean;
    onTitleClick: (isbn: string) => void; // Принимаем параметр isbn
    onReaderClick: (id: string) => void;
    isReaderClicked: boolean
    onBackBookClick: () => void;
    onBackReaderClick: () => void;
    isSearchBook: boolean;
    searchValueBook: string
    isSearchReader: boolean;
    searchValueReader: string
    isNovelties: boolean;
    selectedButton: number | null;
    onBackUserClick: () => void;
}

const BottomBar: React.FC<BottomBarProps> = ({   isAvatarClicked,
                                                 isTitleClicked,
                                                 onTitleClick,
                                                 onReaderClick,
                                                 isReaderClicked,
                                                 onBackBookClick,
                                                 isSearchBook,
                                                 searchValueBook,
                                                 isNovelties,
                                                 isSearchReader,
                                                 searchValueReader,
                                                 selectedButton,
                                                 onBackReaderClick,
                                                 onBackUserClick
                                             }) => {
    const sortOptions = ['Title A-Z',  'Author A-Z', 'Genre A-Z', 'Title Z-A', 'Author Z-A', 'Genre Z-A'];
    const sortOptionsReader = ['Name A-Z', 'Name Z-A'];
    const [books, setBooks] = useState<Book[]>([]);
    const [book, setBook] = useState<BookInfo | null>(null);
    const [reader, setReader] = useState<ReaderInfo | null>(null);
    const [booksLoaded, setBooksLoaded] = useState(false);
    const [bookInfoLoaded, setBookInfoLoaded] = useState(false);
    const [readerInfoLoaded, setReaderInfoLoaded] = useState(false);
    const [booksFromSearch, setBooksFromSearch] = useState<Book[]>([]);
    const [bookSearchLoaded, setBookSearchLoaded] = useState(false);
    const [readersFromSearch, setReadersFromSearch] = useState<Reader[]>([]);
    const [readerSearchLoaded, setReaderSearchLoaded] = useState(false);
    const [errorState, setErrorState] = useState<string>("");

    useEffect(() => {
        if(!booksLoaded){
            const fetchNovelties = async () => {
                try {
                    const response = await fetch(`http://localhost:8080/book/novelties`);
                    const responseData = await response.json();
                    const booksArray = responseData.searchedBooks;
                    const sortedBooks = booksArray.sort((a: Book, b: Book) => a.title.localeCompare(b.title));
                    setBooks(sortedBooks);
                    setBooksLoaded(true);
                } catch (error) {
                    console.error('Ошибка при загрузке информации о книге:', error);
                    setErrorState("idk")
                }
            };
            fetchNovelties();
        }

        return () => {};
    }, [booksLoaded]);


    useEffect(() => {
        if (isSearchBook) {
            const fetchBookSearch = async (value: string) => {
                try {
                    const response = await fetch(`http://localhost:8080/book/search?${value}`);
                    const responseData = await response.json();
                    const booksArray = responseData.searchedBooks;

                    if (booksArray.length === 0) {
                        setBookSearchLoaded(false);
                        setErrorState("We didn't find anything matching your request");

                    } else {
                        const sortedBooks = booksArray.sort((a: Book, b: Book) => a.title.localeCompare(b.title));
                        setBooksFromSearch(sortedBooks);
                        setBookSearchLoaded(true);
                    }

                } catch (error) {
                    console.error('Ошибка при загрузке информации о книге:', error);
                    setBookSearchLoaded(false);
                    setErrorState("Your request is too small")
                }
            };
            fetchBookSearch(searchValueBook);
        }

    }, [isSearchBook, searchValueBook]);

    useEffect(() => {
        if (isSearchReader) {
            const fetchReaderSearch = async (value: string) => {
                try {
                    const response = await fetch(`http://localhost:8080/reader/search?${value}`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        },});
                    const responseData = await response.json();
                    const readersArray = responseData.searchedReaders;

                    if (readersArray.length === 0) {
                        setReaderSearchLoaded(false);
                        setErrorState("We didn't find anything matching your request");
                    } else {
                        const sortedReaders = readersArray.sort((a: Reader, b: Reader) => a.fullName.localeCompare(b.fullName));
                        setReadersFromSearch(sortedReaders);
                        setReaderSearchLoaded(true);
                    }
                } catch (error) {
                    console.error('Ошибка при загрузке информации о читателе:', error);
                    setReaderSearchLoaded(false);
                    setErrorState("Your request is too small");
                }
            };
            fetchReaderSearch(searchValueReader);
        }
    }, [isSearchReader, searchValueReader]);


    const fetchBookInfo = async (isbn: string) => {
        try {
            // Обратите внимание на правильное формирование URL запроса с параметром query
            const response = await fetch(`http://localhost:8080/book/info?isbn=${isbn}`);
            const data = await response.json();
            setBook(data);
            setBookInfoLoaded(true);
        } catch (error) {
            console.error('Ошибка при загрузке информации о книге:', error);
            setErrorState("idk")
        }
    };

    const fetchReaderInfo = async (id: string) => {
        try {
            // Отправляем запрос для получения информации о читателе
            const response = await fetch(`http://localhost:8080/reader/info?id=${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },});
            const data = await response.json();

            // Убираем обертку readersBooks, оставляя все остальные данные
            const { readersBooks, reservedBooks, ...rest } = data;

            const sortedBooks = readersBooks && readersBooks.length > 0 ?
                readersBooks.sort((a: Book, b: Book) => a.title.localeCompare(b.title)) : [];
            const sortedReservedBooks = reservedBooks && reservedBooks.length > 0 ?
                reservedBooks.sort((a: Book, b: Book) => a.title.localeCompare(b.title)) : [];


            // Устанавливаем данные читателя без обертки в состояние
            setReader({ ...rest, readersBooks: sortedBooks , reservedBooks: sortedReservedBooks});
            setReaderInfoLoaded(true);
        } catch (error) {
            console.error('Ошибка при загрузке информации о читателе:', error);
            setErrorState("idk");
        }
    };

    const handleTitleClick = (isbn: string) => {
        setBookInfoLoaded(false);
        fetchBookInfo(isbn);
        onTitleClick(isbn);
    };

    const handleReaderClick = (id: string) => {
        setReaderInfoLoaded(false);
        fetchReaderInfo(id);
        onReaderClick(id);
    };

    const splittedBook = searchValueBook.split("="); // Разбиваем строку по символу "="
    const splittedReader = searchValueReader.split("="); // Разбиваем строку по символу "="


    return (
        <div className="bottom-bar">
            {!isAvatarClicked ? (
                <div className="bottom-container">
                    {selectedButton === 3 && (
                        <AddBook/>
                    )}
                    {selectedButton === 4 && (
                        <Statistics/>
                    )}
                    {selectedButton !== 3 && selectedButton !== 4 && ( // Проверяем, не выбраны ли кнопки 3 или 4
                        <>
                            {isTitleClicked && book && bookInfoLoaded && (
                                <BookProfile book={book} onClick={onBackBookClick}/>
                            )}
                            {isReaderClicked && reader && readerInfoLoaded && (
                                <ReaderProfile readerInfo={reader} onClick={onBackReaderClick}/>
                            )}
                            {!isTitleClicked && !isReaderClicked && isSearchBook && bookSearchLoaded && (
                                <ListOfBooks
                                    sortOptions={sortOptions}
                                    books={booksFromSearch}
                                    onTitleClick={handleTitleClick}
                                    title={`Search by ${splittedBook[0]}: ${splittedBook[1]}`}
                                />
                            )}
                            {!isTitleClicked && !isReaderClicked && isSearchReader && readerSearchLoaded && (
                                <ListOfReaders
                                    sortOptions={sortOptionsReader}
                                    readers={readersFromSearch}
                                    onReaderClick={handleReaderClick} //Заменить потом
                                    title={`Search by ${splittedReader[0]}: ${splittedReader[1]}`}
                                />
                            )}
                            {!isTitleClicked && !isReaderClicked && isNovelties && booksLoaded && (
                                <ListOfBooks
                                    sortOptions={sortOptions}
                                    books={books}
                                    onTitleClick={handleTitleClick}
                                    title={"Novelties"}
                                />
                            )}
                            {!isTitleClicked && !isReaderClicked && isSearchBook && !bookSearchLoaded && (
                                <ErrorMessage errorMessage={errorState}/>
                            )}
                            {!isTitleClicked && !isReaderClicked && isSearchReader && !readerSearchLoaded && (
                                <ErrorMessage errorMessage={errorState}/>
                            )}
                            {!isTitleClicked && !isReaderClicked && isNovelties && !booksLoaded && (
                                <ErrorMessage errorMessage={errorState}/>
                            )}
                        </>
                    )}
                </div>

            ) : (
                <LibrarianProfile onBackClick={onBackUserClick}/>
            )}
        </div>
    );

}

export default BottomBar;
