import React from 'react';
import './BookContainer.css';

export interface Book {
    bookPhoto: string;
    isbn: string;
    title: string;
    authors: string;
    genres: string;
    copyId: string;
    status?: string;
    reserved?: string;
    returnDeadline?: string;
}

interface BookContainerProps {
    books: Book[];
    onClick: (isbn: string) => void; // Принимаем параметр isbn
}

const BookContainer: React.FC<BookContainerProps> = ({ books, onClick }) => {

    const handleOnClick = (isbn: string) => {
        onClick(isbn); // Передаем параметр isbn в функцию onClick
    }

    return (
        <div className="book-container">

            {books.map((book, index) => (
                <div className="book" key={index}>
                    <img src={book.bookPhoto} alt={book.title}/>
                    <div className="book-info">
                        <button className="title-button" onClick={() => handleOnClick(book.isbn)}>{book.title}{book.status === "Owed" ? <span style={{ color: '#DC143C' }}> - {book.status}</span> : ''}</button>
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
                </div>
            ))}
        </div>
    );
}


export default BookContainer;
