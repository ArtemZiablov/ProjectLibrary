import React, { useState, useEffect } from 'react';
import './UserProfile.css';
import UserInformation from "./UserInformation/UserInformation";
import {Book} from "../ListOfBooks/BookContainer/BookContainer";
import ListOfBooks from "../ListOfBooks/ListOfBooks";
import BookProfile, {BookInfo} from "../BookProfile/BookProfile";
import ErrorMessage from "../ErrorMessage/ErrorMessage";
import {ReaderInfo} from "../ReaderProfile/ReaderProfile";
import {useNavigate} from "react-router-dom";

interface UserProfileProps {
    onBackClick: () => void;
}

const UserProfile: React.FC<UserProfileProps> = ({onBackClick}) => {
    const sortOptions = ['Title A-Z',  'Author A-Z', 'Genre A-Z', 'Title Z-A', 'Author Z-A', 'Genre Z-A'];
    const [isTitleClicked, setIsTitleClicked] = useState(false);
    const [book, setBook] = useState<BookInfo | null>(null);
    const [bookInfoLoaded, setBookInfoLoaded] = useState(false);// Состояние для отслеживания загрузки данных о книгах
    const [errorState, setErrorState] = useState<string>("");
    const [reader, setReader] = useState<ReaderInfo | null>(null);
    const [readerInfoLoaded, setReaderInfoLoaded] = useState(false);
    const navigate = useNavigate();

    function decodeJWT(token: any) {
        const parts = token.split('.'); // Разделение на части
        if (parts.length !== 3) {
            throw new Error('Invalid JWT');
        }

        const header = JSON.parse(window.atob(parts[0].replace(/-/g, '+').replace(/_/g, '/')));
        const payload = JSON.parse(window.atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));

        return { header, payload };
    }

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const token = localStorage.getItem('token');
                const decodedToken: any = decodeJWT(token);

                // Отправляем запрос для получения информации о читателе
                const response = await fetch(`http://localhost:8080/reader/info?id=${decodedToken.payload.userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },});
                if (!response.ok) {
                    throw new Error('Ошибка при загрузке информации о читателе: ' + response.statusText);
                }

                const data = await response.json();

                // Устанавливаем данные читателя без обертки в состояние
                const { readersBooks, reservedBooks, ...rest } = data;

                const sortedBooks = readersBooks && readersBooks.length > 0 ?
                    readersBooks.sort((a: Book, b: Book) => a.title.localeCompare(b.title)) : [];
                const sortedReservedBooks = reservedBooks && reservedBooks.length > 0 ?
                    reservedBooks.sort((a: Book, b: Book) => a.title.localeCompare(b.title)) : [];

                // Устанавливаем данные читателя без обертки в состояние
                setReader({ ...rest, readersBooks: sortedBooks , reservedBooks: sortedReservedBooks});
                setReaderInfoLoaded(true);
            } catch (error) {
                console.error(error);
                setErrorState("idk");
            }
        };

        fetchUserInfo();

    }, []);

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
        setIsTitleClicked(true);
    };

    const handleBackClick = () =>{
        setIsTitleClicked(false);
    }

    const handleOnLogOutClick =() => {
        localStorage.removeItem('token');
        console.log(localStorage.getItem('token'))
        navigate('/');
    }

    return (
        <div className="user-profile">
            {isTitleClicked ? ((book && bookInfoLoaded) && <BookProfile book={book} onClick={handleBackClick}/>) : (
                <>
                    <div className="profile-top-container">
                        <button className="close-button-reader" onClick={onBackClick}>
                            <img src="https://i.imgur.com/reHgKyL.png" alt="Back"/>
                        </button>

                        <button className="logout" onClick={handleOnLogOutClick}>
                            Log out
                        </button>

                        {readerInfoLoaded && reader ? <UserInformation userInfo={reader}/> : null}
                    </div>
                    <div className="profile-bottom-container">
                        {reader && reader.reservedBooks && reader.reservedBooks.length > 0 ? (
                            <ListOfBooks sortOptions={sortOptions} books={reader.reservedBooks} onTitleClick={handleTitleClick} title={"Reserved books"} />
                        ) : null}
                        {reader ?(
                            <ListOfBooks sortOptions={sortOptions} books={reader.readersBooks} onTitleClick={handleTitleClick} title={"Taken books"} />
                        ): null}
                    </div>
                </>
            )}
        </div>
    );

}

export default UserProfile;
