import React, { useState } from 'react';
import SearchInput from './SearchInput/SearchInput';
import FilterButtons from './FilterButtons/FilterButtons';

interface SearchProps {
    onSearch: (value: string) => void;
    buttons: { label: string; value: string }[];
}

const Search: React.FC<SearchProps> = ({ onSearch, buttons }) => {
    const [activeFilter, setActiveFilter] = useState(buttons[0].value);

    const handleFilterChange = (filter: string) => {
        setActiveFilter(filter);
    };

    const handleSearch = (value: string) => {
        onSearch(`${activeFilter}=${value}`);
    };

    return (
        <div className="search">
            <div className="search-text-input">
                <SearchInput filter={activeFilter} onSearch={handleSearch} />
            </div>
            <div className="search-buttons">
                <FilterButtons onFilterChange={handleFilterChange} buttons={buttons} />
            </div>
        </div>
    );
}

export default Search;
