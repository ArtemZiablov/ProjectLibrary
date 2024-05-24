import React, { useState, useEffect } from 'react';
import './Statistics.css'
import ReaderProfile, {ReaderInfo} from "../ReaderProfile/ReaderProfile";
import ListOfReaders from "../ListOfReaders/ListOfReaders";
import {Book} from "../ListOfBooks/BookContainer/BookContainer";
import {Reader} from "../ListOfReaders/ReaderContainer/ReaderContainer";

interface BooksStatistics {
    books: number;
    copies: number;
    assignedBookCopies: number;
    owedBooks: number;
    reservedBooks: number;
}

interface ReadersStatistics {
    registeredReaders: number;
    readersWhoReservedBooks: number;
    debtors: number;
    ongoingReaders: number;
}

const Statistics: React.FC = () => {
    const [booksStatistics, setBooksStatistics] = useState<BooksStatistics>();
    const [readersStatistics, setReadersStatistics] = useState<ReadersStatistics>();
    const [reader, setReader] = useState<ReaderInfo | null>(null);
    const [showReaderInfo, setShowReaderInfo] = useState(false)
    const [readersFromSearch, setReadersFromSearch] = useState<Reader[]>([]);
    const [titleValue, setTitleValue] = useState('')
    const sortOptionsReader = ['Name A-Z', 'Name Z-A'];

    useEffect(() => {
        const fetchStatistics = async () => {
            try {
                const response = await fetch('http://localhost:8080/book-operation/statistics', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },});
                const data = await response.json();

                const booksStats = data.booksStatistics;
                const readersStats = data.readersStatistics;

                setBooksStatistics(booksStats);
                setReadersStatistics(readersStats);
            } catch (error) {
                console.error('Error fetching statistics:', error);
            }
        }

        fetchStatistics();
    }, []);

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
            setShowReaderInfo(true);
        } catch (error) {
            console.error('Ошибка при загрузке информации о читателе:', error);
        }
    };

    const handleReservedBooksClick =  async() => {
        try {
            // Отправляем запрос для получения информации о читателе
            const response = await fetch(`http://localhost:8080/book-operation/readers-who-reserved-books`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },});
            const data = await response.json();
            const sortedData = data.sort((a: Reader, b: Reader) => a.fullName.localeCompare(b.fullName));
            setReadersFromSearch(sortedData);
            setTitleValue("Readers who reserved books")
        } catch (error) {
            console.error('Ошибка при загрузке информации о читателе:', error);
        }
    };

    const handleDebtorsClick = async() => {
        try {
            // Отправляем запрос для получения информации о читателе
            const response = await fetch(`http://localhost:8080/book-operation/debtors`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },});
            const data = await response.json();
            const sortedData = data.sort((a: Reader, b: Reader) => a.fullName.localeCompare(b.fullName));
            setReadersFromSearch(sortedData);
            setTitleValue("Debtors")
        } catch (error) {
            console.error('Ошибка при загрузке информации о читателе:', error);
        }
    };

    const handleOngoingReadersClick = async() => {
        try {
            // Отправляем запрос для получения информации о читателе
            const response = await fetch(`http://localhost:8080/book-operation/ongoing-readers`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },});
            const data = await response.json();
            const sortedData = data.sort((a: Reader, b: Reader) => a.fullName.localeCompare(b.fullName));
            setReadersFromSearch(sortedData);
            setTitleValue("Ongoing readers")
        } catch (error) {
            console.error('Ошибка при загрузке информации о читателе:', error);
        }
    };

    const handleOnBackReaderClick = () =>{
        setShowReaderInfo(false)
    }

    return (
        <>
            {showReaderInfo && reader ? (
                <ReaderProfile readerInfo={reader} onClick={handleOnBackReaderClick}/>
            ) : (
                <>
                    <div className="statistics-container">
                        {booksStatistics && readersStatistics ? (
                            <>
                                <div className="statistics-box">
                                    <h2>Books Statistics</h2>
                                    <p>Number of books: {booksStatistics.books}</p>
                                    <p>Total copies: {booksStatistics.copies}</p>
                                    <p>Assigned book copies: {booksStatistics.assignedBookCopies}</p>
                                    <p>Owed books: {booksStatistics.owedBooks}</p>
                                    <p>Reserved books: {booksStatistics.reservedBooks}</p>
                                </div>

                                <div className="statistics-box">
                                    <h2>Readers Statistics</h2>
                                    <p>Registered readers: {readersStatistics.registeredReaders}</p>
                                    <button className="button-stat" onClick={handleReservedBooksClick}>
                                        <span className="button-text-stat">Readers who reserved books:</span> {readersStatistics.readersWhoReservedBooks}
                                    </button>
                                    <button className="button-stat" onClick={handleDebtorsClick}>
                                        <span className="button-text-stat">Debtors:</span> {readersStatistics.debtors}
                                    </button>
                                    <button className="button-stat" onClick={handleOngoingReadersClick}>
                                        <span className="button-text-stat">Ongoing readers:</span> {readersStatistics.ongoingReaders}
                                    </button>
                                </div>
                            </>
                        ) : (
                            <p>Loading...</p>
                        )}
                    </div>
                    <div className="profile-bottom-container">
                        {readersFromSearch.length > 0 && (
                            <ListOfReaders
                                sortOptions={sortOptionsReader}
                                readers={readersFromSearch}
                                onReaderClick={fetchReaderInfo}
                                title={titleValue}
                            />
                        )}
                    </div>
                </>
            )}
        </>
    );

};

export default Statistics;
