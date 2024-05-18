import React, { useState, useEffect } from 'react';
import  { Book } from './ListOfBooks/BookContainer/BookContainer';
import UserProfile from './UserProfile/UserProfile';
import './BottomBar.css';
import ListOfBooks from "./ListOfBooks/ListOfBooks";
import BookProfile, {BookInfo} from "./BookProfile/BookProfile";
import ErrorMessage from "./ErrorMessage/ErrorMessage";

interface BottomBarProps {
    isAvatarClicked: boolean; // Пропс для определения, была ли нажата кнопка UserAvatar
    isTitleClicked: boolean;
    onTitleClick: (isbn: string) => void; // Принимаем параметр isbn
    onBackClick: () => void;
    isSearch: boolean;
    searchValue: string
    isNovelties: boolean;
    onBackUserClick: () => void;
}

const BottomBar: React.FC<BottomBarProps> = ({ isAvatarClicked, isTitleClicked, onTitleClick, onBackClick, isSearch, searchValue, isNovelties, onBackUserClick}) => {
    const sortOptions = ['Title A-Z',  'Author A-Z', 'Genre A-Z', 'Title Z-A', 'Author Z-A', 'Genre Z-A'];
    const [books, setBooks] = useState<Book[]>([]);
    const [book, setBook] = useState<BookInfo | null>(null); // Состояние для хранения данных о выбранной книге
    const [booksLoaded, setBooksLoaded] = useState(false);
    const [bookInfoLoaded, setBookInfoLoaded] = useState(false);
    const [booksFromSearch, setBooksFromSearch] = useState<Book[]>([]);
    const [bookSearchLoaded, setBookSearchLoaded] = useState(false);
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
        if (isSearch) {
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
            fetchBookSearch(searchValue);
        }

    }, [isSearch, searchValue]);

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

    const handleTitleClick = (isbn: string) => {
        setBookInfoLoaded(false);
        fetchBookInfo(isbn);
        onTitleClick(isbn);
    };

    const splitted = searchValue.split("="); // Разбиваем строку по символу "="

    return (
        <div className="bottom-bar">
            {!isAvatarClicked ? (
                <div className="bottom-container">
                    {isTitleClicked && book && bookInfoLoaded && (
                        <BookProfile book={book} onClick={onBackClick}/>
                    )}
                    {!isTitleClicked && isSearch && bookSearchLoaded && (
                        <ListOfBooks
                            sortOptions={sortOptions}
                            books={booksFromSearch}
                            onTitleClick={handleTitleClick}
                            title={`Search by ${splitted[0]}: ${splitted[1]}`}
                        />
                    )}
                    {!isTitleClicked && isNovelties && booksLoaded && (
                        <ListOfBooks
                            sortOptions={sortOptions}
                            books={books}
                            onTitleClick={handleTitleClick}
                            title={"Novelties"}
                        />
                    )}
                    {/*{isTitleClicked && (!book || !bookInfoLoaded) && (*/}
                    {/*    <ErrorMessage/>*/}
                    {/*)}*/}
                    {!isTitleClicked && isSearch && !bookSearchLoaded && (
                        <ErrorMessage errorMessage={errorState}/>
                    )}
                    {!isTitleClicked && isNovelties && !booksLoaded && (
                        <ErrorMessage errorMessage={errorState}/>
                    )}
                </div>
            ) : (
                <UserProfile onBackClick={onBackUserClick}/>
            )}
        </div>
    );

}

export default BottomBar;
