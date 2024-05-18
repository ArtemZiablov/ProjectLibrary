import React, {useState} from "react";
import {Book} from "../ListOfBooks/BookContainer/BookContainer";
import ListOfBooks from "../ListOfBooks/ListOfBooks";
import BookProfile, {BookInfo} from "../BookProfile/BookProfile";
import './ReaderProfile.css'
import SuccNotifications from "../SuccNotifications/SuccNotifications";

interface ReaderProfileProps {
    readerInfo: ReaderInfo;
    onClick: () => void;
}

export interface ReaderInfo {
    profilePhoto: string;
    fullName: string;
    dateOfBirth: Date;
    debtor: boolean;
    readerId: string;
    phoneNumber: string;
    email: string;
    readersBooks: Book[];
    reservedBooks: Book[]
}

const ReaderProfile: React.FC<ReaderProfileProps> = ({ readerInfo , onClick}) => {
    const sortOptions = ['Title A-Z', 'Title Z-A', 'Author A-Z', 'Author Z-A', 'Genre A-Z', 'Genre Z-A'];
    const [isLoading, setIsLoading] = useState(true);
    const [bookInfoLoaded, setBookInfoLoaded] = useState(false);
    const [book, setBook] = useState<BookInfo | null>(null);
    const [isTitleClicked, setIsTitleClicked] = useState(false);
    const [errorState, setErrorState] = useState<string>("");
    const [selectedCase, setSelectedCase] = useState<number>(0);
    const [books, setBooks] = useState<string[]>([""]); // массив для хранения ISBN книг
    const [error, setError] = useState<string>("");
    const [requestStatus, setRequestStatus] = useState("initial");

    const maxFields = 5 - readerInfo.readersBooks.length; // максимальное количество допустимых полей

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

    const handleCaseButtonClick = (index: number) => {
        setError('')
        setSelectedCase(index);
    };

    const handleAddField = () => {
        if (books.length < maxFields) {
            setBooks([...books, ""]); // добавляем новое пустое поле ввода, если не достигнуто максимальное количество
        }
    };

    const handleRemoveField = (index: number) => {
        if (books.length === 1) {
            return; // не удаляем поле, если это единственное поле
        }

        const updatedBooks = [...books];
        updatedBooks.splice(index, 1); // удаляем поле ввода по индексу
        setBooks(updatedBooks);
    };

    const handleChange = (index: number, value: string) => {
        const updatedBooks = [...books];
        updatedBooks[index] = value; // обновляем значение ISBN книги по индексу
        setBooks(updatedBooks);
    };

    const reloadReaderBooks = async () => {
        try {
            // Обратите внимание на правильное формирование URL запроса с параметром query
            const response = await fetch(`http://localhost:8080/reader/taken-books?readerId=${readerInfo.readerId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },});
            readerInfo.readersBooks = await response.json()
        } catch (error) {
            console.error('Ошибка при загрузке информации о книге:', error);
            setErrorState("idk")
        }
    }

    const handleAccept = async () => {
        // Место для логики приема книг
        const isEmpty = books.some(book => book.trim() === "");
        if (isEmpty) {
            setError("Please fill in all book ISBNs.");
            setTimeout(() => {
                setError('');
            }, 3000);
        } else {
            try {
                const readerId = readerInfo.readerId;
                const bookCopiesIds = books.map(isbn => parseInt(isbn));

                const data = {
                    readerId: readerId,
                    bookCopiesId: bookCopiesIds
                };

                const response = await fetch(`http://localhost:8080/book-copy/assign-book-copies`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                    body: JSON.stringify(data)
                });

                console.log(data);

                if (response.ok) {
                    setBooks([""]);
                    setError("");
                    reloadReaderBooks();
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
        }
    };



    function releaseBook(isbn: string) {
        const fetchRelease = async () => {
            try {
                const data = {
                    readerId: readerInfo.readerId,
                    bookCopiesId: [isbn]
                };

                const response = await fetch(`http://localhost:8080/book-copy/release-book-copy`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                    body: JSON.stringify(data)
                });

                console.log(data)

                if (response.ok) {
                    readerInfo.readersBooks = readerInfo.readersBooks.filter(book => book.copyId !== data.bookCopiesId[0]);

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


        fetchRelease();
    }

    return (
        <div>
            {isTitleClicked ? ((book && bookInfoLoaded) && <BookProfile book={book} onClick={handleBackClick}/>) : (
            <>
                <div className="user-information-container">
                    <button className="close-button-reader" onClick={onClick}>
                        <img src="https://i.imgur.com/reHgKyL.png" alt="Back"/>
                    </button>
                    <div className="avatar">
                        <img src={readerInfo.profilePhoto} alt="Avatar"/>
                    </div>
                    <div className="user-details">
                        <h2>
                            {readerInfo.fullName} {readerInfo.debtor &&
                            <span style={{color: "#DC143C"}}> - Debtor</span>}
                        </h2>
                        <p>Reader Card ID: {readerInfo.readerId}</p>
                        <p>Phone Number: {readerInfo.phoneNumber}</p>
                        <p>Email: {readerInfo.email}</p>
                    </div>
                </div>
                <div className="case-buttons">
                    <button className={selectedCase === 0 ? "selected" : ""}
                            onClick={() => handleCaseButtonClick(0)}>Reader's books
                </button>
                <button className={selectedCase === 1 ? "selected" : ""} onClick={() => handleCaseButtonClick(1)}>Issue
                    a book
                </button>
                <button className={selectedCase === 2 ? "selected" : ""} onClick={() => handleCaseButtonClick(2)}>Accept
                    the book
                </button>
            </div>
            {selectedCase === 0 && (
                <>
                    {readerInfo.reservedBooks.length > 0 ?(
                    <ListOfBooks
                        sortOptions={sortOptions}
                        books={readerInfo.reservedBooks}
                        onTitleClick={handleTitleClick}
                        title={"Reader`s reserved books"}
                    />): null}
                    <ListOfBooks
                        sortOptions={sortOptions}
                        books={readerInfo.readersBooks}
                        onTitleClick={handleTitleClick}
                        title={"Reader`s books"}
                    />
                </>
            )}
            {selectedCase === 1 && (
                <div className="add-book-container">
                    {readerInfo.readersBooks.length === 5 ? (
                        <p className="max-books-message">The user already has the maximum number of books.</p>
                    ) : (
                        <form className="add-book-form">
                            {books.map((book, index) => (
                                <div key={index} className="form-group">
                                    <label htmlFor={`bookInput${index}`}>Book ISBN:</label>
                                        <input
                                            type="string"
                                            id={`bookInput${index}`}
                                            placeholder={"ISBN..."}
                                            name={`bookInput${index}`}
                                            value={book}
                                            onChange={(e) => handleChange(index, e.target.value)}
                                        />
                                        <div className="button-group">

                                            <button type="button" className="addButton" onClick={() => handleRemoveField(index)}>
                                                <img src={'https://i.imgur.com/MMYu2PA.png'} alt="Remove" />
                                            </button>

                                        {index === books.length - 1 && books.length + readerInfo.readersBooks.length < 5 && (
                                            <button type="button" className="addButton" onClick={handleAddField}>
                                                <img src={'https://i.imgur.com/Q6ath1w.png'} alt="Add" />
                                            </button>
                                        )}
                                        </div>
                                    </div>
                            ))}
                            {error && <p className="error">{error}</p>}
                            <button type="button" className="submitButton" onClick={handleAccept}>Accept</button>
                        </form>
                    )}
                    {requestStatus === "success" && <SuccNotifications message={"Book successfully issued"} requestStatus={requestStatus}/>}
                    {requestStatus === "failure" && <SuccNotifications message={"Failed to issue book"} requestStatus={requestStatus}/>}
                </div>
            )}

                {selectedCase === 2 && (
                    <div>
                        {readerInfo.readersBooks.length > 0 ? (
                            <div className="book-container">
                                {readerInfo.readersBooks.map((book, index) => (
                                    <div className="book" key={index}>
                                        <img src={book.bookPhoto} alt={book.title}/>
                                        <div className="book-info">
                                            <h2>{book.title}{book.status === "Owed" ?
                                                <span style={{color: '#DC143C'}}> - {book.status}</span> : ''}</h2>
                                            <p>Author: {book.authors}</p>
                                            <p>Genre: {book.genres}</p>
                                            {book.reserved && <p>Reserved: {book.reserved}</p>}
                                            {book.returnDeadline && (
                                                <p>
                                                    Return Date:{" "}
                                                    <span style={{color: book.status === "Owed" ? "#DC143C" : ''}}>
                                        {book.returnDeadline}
                                    </span>
                                                </p>
                                            )}
                                        </div>
                                        <button className="accept-button" onClick={() => releaseBook(book.copyId)}>Accept the
                                            book
                                        </button>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="empty-message-container">
                                <div className="empty-message">
                                    <p>The reader has not received any books yet</p>
                                </div>
                            </div>
                        )}
                        {requestStatus === "success" &&
                            <SuccNotifications message={"Book successfully released"} requestStatus={requestStatus}/>}
                        {requestStatus === "failure" &&
                            <SuccNotifications message={"Failed to release book"} requestStatus={requestStatus}/>}
                    </div>
                )}
            </>
            )}
        </div>
    );
}

export default ReaderProfile;
