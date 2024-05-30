import React, {useState} from 'react';
import './BookContainer.css';

export interface Book {
    bookPhoto: string;
    isbn: string;
    title: string;
    authors: string;
    genres: string;
    copyId: string;
    status?: string;
    daysLeft?: string;
    returnDeadline?: string;
    assignDate?: string;
    releaseDate?: string;
}

interface BookContainerProps {
    books: Book[];
    onClick: (isbn: string) => void;
    useHorizontalScroll: boolean;
    showViewMore: boolean;
}

const BookContainer: React.FC<BookContainerProps> = ({ books, onClick, useHorizontalScroll, showViewMore}) => {
    const [displayCount, setDisplayCount] = useState(showViewMore ? 3 : books.length);

    const toggleDisplay = () => {
        setDisplayCount(books.length);
    };

    return (
        <div>
            <div className={`book-container ${useHorizontalScroll ? 'horizontal-scroll' : ''}`}>
                {books.slice(0, displayCount).map((book, index) => (
                    <div className="book" key={index}>
                        <img src={book.bookPhoto} alt={book.title}/>
                        <div className="book-info">
                            <button className="title-button" onClick={() => onClick(book.isbn)}>
                                {book.title}{book.status === "Owed" ?
                                <span style={{color: '#DC143C'}}> - {book.status}</span> : ''}
                            </button>
                            <p>Author: {book.authors}</p>
                            <p>Genre: {book.genres}</p>
                            {book.daysLeft && <p>Reserved: {book.daysLeft}</p>}
                            {book.returnDeadline && (
                                <p>
                                    Return Date:{" "}
                                    <span style={{color: book.status === "Owed" ? "#DC143C" : ''}}>
                                    {book.returnDeadline}
                                </span>
                                </p>
                            )}
                            {book.assignDate && <p>Assign date: {book.assignDate}</p>}
                            {book.releaseDate && <p>Release date: {book.releaseDate}</p>}
                        </div>
                    </div>
                ))}
            </div>

            {showViewMore && displayCount < books.length && (
                <button className="view-more-button" onClick={toggleDisplay}>View more</button>
            )}

        </div>
    );
}

export default BookContainer;