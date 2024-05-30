import React, { useEffect, useState } from 'react';
import Title from '../ListOfBooks/Title/Title';
import SortButton from '../ListOfBooks/SortButton/SortButton';
import ReaderContainer, { Reader } from './ReaderContainer/ReaderContainer';

interface ListOfReadersProps {
    sortOptions: string[];
    readers: Reader[];
    onReaderClick: (readerId: string) => void; // Добавляем параметр readerId
    title: string;
}

const ListOfReaders: React.FC<ListOfReadersProps> = ({ sortOptions, readers, onReaderClick, title }) => {
    const [sortedReaders, setSortedReaders] = useState<Reader[]>(readers);
    const [sortOption, setSortOption] = useState<string>('Name'); // Изменение начального значения на 'Name'

    useEffect(() => {
        setSortedReaders(readers);
    }, [readers]);

    const handleSortChange = (option: string) => {
        setSortOption(option);
        if (option === 'Name A-Z') {
            const sortedByNameAZ = [...readers].sort((a, b) => a.fullName.localeCompare(b.fullName));
            setSortedReaders(sortedByNameAZ);
        } else if (option === 'Name Z-A') {
            const sortedByNameZA = [...readers].sort((a, b) => b.fullName.localeCompare(a.fullName));
            setSortedReaders(sortedByNameZA);
        }
    };

    const handleReaderClick = (readerId: string) => {
        onReaderClick(readerId); // Передаем readerId в функцию onReaderClick
    };

    return (
        <div className="list-of-readers">
            <div className="top-container">
                <Title text={title} />
                <SortButton options={sortOptions} onSortChange={handleSortChange} />
            </div>
            <div className="bottom-container">
                <ReaderContainer readers={sortedReaders} onClick={handleReaderClick} />
            </div>
        </div>
    );
}

export default ListOfReaders;
