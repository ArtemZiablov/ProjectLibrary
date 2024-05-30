import React, {useEffect, useState} from 'react';
import Title from './Title/Title';
import SortButton from './SortButton/SortButton';
import BookContainer, { Book } from './BookContainer/BookContainer';
import './ListOfBooks.css'

interface ListOfBooksProps {
    sortOptions: string[];
    books: Book[];
    onTitleClick: (isbn: string) => void; // Добавляем параметр isbn
    title: string;
    useHorizontalScroll: boolean;
    showViewMore: boolean;
}

const ListOfBooks: React.FC<ListOfBooksProps> = ({ sortOptions, books, onTitleClick, title, useHorizontalScroll, showViewMore }) => {
    const [sortedBooks, setSortedBooks] = useState<Book[]>(books);
    const [sortOption, setSortOption] = useState<string>('Title'); // Изменение начального значения на 'Title'

    useEffect(() => {

        setSortedBooks(books);
    }, [books]);

    const handleSortChange = (option: string) => {
        setSortOption(option);
        if (option === 'Title A-Z') {
            const sortedByTitleAZ = [...books].sort((a, b) => a.title.localeCompare(b.title));
            setSortedBooks(sortedByTitleAZ);
        } else if (option === 'Title Z-A') {
            const sortedByTitleZA = [...books].sort((a, b) => b.title.localeCompare(a.title));
            setSortedBooks(sortedByTitleZA);
        } else if (option === 'Author A-Z') {
            const sortedByAuthorAZ = [...books].sort((a, b) => a.authors.localeCompare(b.authors));
            setSortedBooks(sortedByAuthorAZ);
        } else if (option === 'Author Z-A') {
            const sortedByAuthorZA = [...books].sort((a, b) => b.authors.localeCompare(a.authors));
            setSortedBooks(sortedByAuthorZA);
        } else if (option === 'Genre A-Z') {
            const sortedByGenreAZ = [...books].sort((a, b) => a.genres.localeCompare(b.genres));
            setSortedBooks(sortedByGenreAZ);
        } else if (option === 'Genre Z-A') {
            const sortedByGenreZA = [...books].sort((a, b) => b.genres.localeCompare(a.genres));
            setSortedBooks(sortedByGenreZA);
        }

    };

    const handleTitleClick = (isbn: string) => {
        console.log(isbn)
        onTitleClick(isbn); // Передаем ISBN в функцию onTitleClick
    };

    return (
        <div className="list-of-books">
            {books.length === 0 ? null : (
                <div className="top-container">
                    <Title text={title} />
                    <SortButton options={sortOptions} onSortChange={handleSortChange} />
                </div>
            )}
            <div className="bottom-container">
                {books.length === 0 ? (
                    <div className="empty-message-container">
                        <div className="empty-message">
                            <p>The books have not been taken yet</p>
                        </div>
                    </div>
                ) : (
                    <BookContainer books={sortedBooks} onClick={handleTitleClick} useHorizontalScroll={useHorizontalScroll} showViewMore={showViewMore}/>
                )}
            </div>
        </div>
    );
}

export default ListOfBooks;
