import React, {useEffect, useState} from 'react';
import './BookProfile.css';
import SuccNotifications from "../SuccNotifications/SuccNotifications";

interface BookProfileProps {
    book: BookInfo;
    onClick: () => void;
}

export interface BookInfo {
    bookPhoto: string;
    isbn: string;
    title: string;
    authors: string[];
    genres: string[];
    translators: string[];
    yearOfPublishing: number;
    numberOfPages: number;
    annotation: string;
    language: string;
    availableBookCopies: number;
}


const BookProfile: React.FC<BookProfileProps> = ({ book, onClick }) => {
    const [requestStatus, setRequestStatus] = useState("initial");
    const [userRole, setUserRole] = useState("GUEST")
    const [deleteBook, setDeleteBook] = useState(false)
    const [inputValue, setInputValue] = useState('');
    const [notifyValue, setNotifyValue] = useState('');

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
        if(localStorage.getItem('token')) {
            const decodedToken = decodeJWT(localStorage.getItem('token'));
            setUserRole(decodedToken.payload.role)
        }
    },[])

    const handleClick = () => {
        const fetchReserve = async () => {
            try {
                const response = await fetch(`http://localhost:8080/book-reservation/reserve-book?isbn=${book.isbn}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                });

                const data = await response.json()
                console.log(data)
                if (data.response) {
                    setNotifyValue(data.response)
                    setRequestStatus("success");
                } else {
                    setRequestStatus("failure");
                }

                setTimeout(() => {
                    setRequestStatus("initial");
                }, 1500);

            } catch (error) {
                console.error('Ошибка при загрузке информации о книге:', error);
                setRequestStatus("failure");

                setTimeout(() => {
                    setRequestStatus("initial");
                }, 1500);
            }
        };

        fetchReserve();
    }


    const handleDeleteClick = () => {
        setDeleteBook(true);
    }

    const handleSubmitDeleteBook = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const fetchDelete = async () => {
            try {
                const response = await fetch(`http://localhost:8080/book-copy/delete-book-copy?isbn=${book.isbn}&copyId=${inputValue}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                });

                if (response.ok) {
                    setRequestStatus("success");
                    setDeleteBook(false);
                    book.availableBookCopies -= 1;
                    setInputValue('')
                } else {
                    setRequestStatus("failure");
                }

                setTimeout(() => {
                    setRequestStatus("initial");
                }, 1500);

            } catch (error) {
                console.error('Ошибка при загрузке информации о книге:', error);
                setRequestStatus("failure");

                setTimeout(() => {
                    setRequestStatus("initial");
                }, 1500);
            }
        };

        fetchDelete();

    }

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value);
    }

    const handleBackDelete = () => {
        setDeleteBook(false);
    }


    return (
        <div className="book-profile">

            <div className="book-profile-info">
                <button className="close-button-book" onClick={onClick}>
                    <img src="https://i.imgur.com/reHgKyL.png" alt="Back"/>
                </button>
                <img src={book.bookPhoto} alt="Avatar"/>
                <h2>{book.title}</h2>
                <p><strong>ISBN:</strong> {book.isbn}</p>
                <p><strong>Authors:</strong> {book.authors.join(', ')}</p>
                <p><strong>Genres:</strong> {book.genres.join(', ')}</p>
                <p><strong>Translators:</strong> {book.translators.join(', ') || <em>Original</em>}</p>
                <p><strong>Year of Publishing:</strong> {book.yearOfPublishing}</p>
                <p><strong>Number of Pages:</strong> {book.numberOfPages}</p>
                <p><strong>Language:</strong> {book.language}</p>
                <div className="book-description">
                    <p><strong>Description:</strong> {book.annotation}</p>
                </div>
            </div>
            {
                userRole === "GUEST" && null
            }

            {userRole === "READER" && (
                <>
                    <div className="book-profile-numbers">
                        <p><strong>Number of available copies:</strong> {book.availableBookCopies}</p>
                        <button className="reserve-button" onClick={handleClick}>Reserve</button>
                    </div>
                    {requestStatus === "success" && <SuccNotifications message={notifyValue} requestStatus={requestStatus}/>}
                    {requestStatus === "failure" && <SuccNotifications message={"Reservation operation failed"} requestStatus={requestStatus}/>}
                </>
            )}
            {userRole === "LIBRARIAN" && (
                <>
                    <div className="book-profile-numbers">
                        <p><strong>Number of available copies:</strong> {book.availableBookCopies}</p>
                        <button className="reserve-button" onClick={handleDeleteClick}>Delete book</button>
                        {deleteBook && (
                            <div className="delete-overlay">
                                <button className="close-button-book-delete" onClick={handleBackDelete}>
                                    <img src="https://i.imgur.com/AhYhitD.png" alt="Back"/>
                                </button>
                                <div className="delete-content">
                                    <h2>Delete book</h2>
                                    <form onSubmit={handleSubmitDeleteBook}>
                                        <input
                                            type="text"
                                            value={inputValue}
                                            onChange={handleInputChange}
                                            placeholder="Book copy ID..."
                                        />
                                        <button type="submit">Delete</button>
                                    </form>
                                </div>
                            </div>
                        )}
                    </div>
                    {requestStatus === "success" && <SuccNotifications message={"Delete operation was a success"}
                                                                       requestStatus={requestStatus}/>}
                    {requestStatus === "failure" &&
                        <SuccNotifications message={"Delete operation failed"} requestStatus={requestStatus}/>}
                </>
            )}
        </div>
    );
}

export default BookProfile;
