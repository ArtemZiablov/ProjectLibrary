import React, { useState } from 'react';
import './FilterButtons.css';

interface FilterButtonsProps {
    onFilterChange: (filter: string) => void;
    buttons: { label: string; value: string }[];
}

const FilterButtons: React.FC<FilterButtonsProps> = ({ onFilterChange, buttons }) => {
    const [activeButton, setActiveButton] = useState(buttons[0].value);

    const handleButtonClick = (filter: string) => {
        setActiveButton(filter);
        onFilterChange(filter);
    };

    return (
        <div className="filter-buttons-container">
            {buttons.map(button => (
                <button
                    key={button.value}
                    className={activeButton === button.value ? "filter-button active" : "filter-button"}
                    onClick={() => handleButtonClick(button.value)}
                >
                    {button.label}
                </button>
            ))}
        </div>
    );
}

export default FilterButtons;
