import React, { useState } from 'react';
import './TopBar.css';
import Logo from './Logo/Logo';
import Search from "./Search/Search";

interface GuestTopBarProps {
    onLogInClick: () => void; // Функция обработки нажатия на кнопку UserAvatar
    onLogoClick: () => void; // Функция обработки нажатия на кнопку Logo
    onSearch: (value: string) => void;
}

const GuestTopBar: React.FC<GuestTopBarProps> = ({ onLogInClick, onLogoClick, onSearch}) => {

    const bookButtons = [
        { label: 'Title', value: 'title' },
        { label: 'Author', value: 'author' },
        { label: 'Genre', value: 'genre' }
    ];
    const handleSearch = (value: string) => {
        onSearch(value);
    };

    return (
        <div className="top-bar">
            <Logo onLogoClick={onLogoClick}/>
            <Search onSearch={handleSearch} buttons={bookButtons}/>
            <div className="sign-in" onClick={onLogInClick}> Sign In </div>
        </div>
    );
}

export default GuestTopBar;
