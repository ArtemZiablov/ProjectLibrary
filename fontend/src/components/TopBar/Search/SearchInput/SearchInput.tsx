// src/components/TopBar/SearchInput/SearchInput.tsx

import React, {useState} from 'react';
import './SearchInput.css';

interface SearchInputProps {
    filter: string;
    onSearch: (value: string) => void;
}

const SearchInput: React.FC<SearchInputProps> = ({ filter, onSearch }) => {
    const [searchValue, setSearchValue] = useState('');

    const placeholderText = filter === '' ? "Search by Title" : `Search by ${filter}`;

    const handleSearch = () => {
        onSearch(searchValue);
        setSearchValue('');
    };

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(event.target.value);
    };

    const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    return (
        <input
            type="text"
            placeholder={placeholderText}
            className="search-input"
            value={searchValue}
            onChange={handleInputChange}
            onKeyPress={handleKeyPress}
        />
    );
}

export default SearchInput;
