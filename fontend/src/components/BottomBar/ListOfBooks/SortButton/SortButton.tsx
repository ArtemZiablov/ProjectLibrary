import React, { useState, useEffect, useRef } from 'react';
import './SortButton.css';
import sortIcon from './Sort.png'; // Путь к изображению

interface SortButtonProps {
    options: string[]; // Варианты сортировки
    onSortChange: (option: string) => void;
}

const SortButton: React.FC<SortButtonProps> = ({ options, onSortChange }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [selectedOption, setSelectedOption] = useState(options[0]);
    const sortButtonRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (sortButtonRef.current && !sortButtonRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleButtonClick = () => {
        setIsOpen(!isOpen);
    };

    const handleOptionClick = (option: string) => {
        setSelectedOption(option);
        onSortChange(option); // Вызываем функцию из родительского компонента для обновления сортировки
        setIsOpen(false);
    };

    return (
        <div className="sort-button" ref={sortButtonRef}>
            <img src={sortIcon} alt="Sort" onClick={handleButtonClick} />
            {isOpen && (
                <div className="options">
                    {options.map((option, index) => (
                        <div key={index} onClick={() => handleOptionClick(option)}>
                            {option}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default SortButton;
