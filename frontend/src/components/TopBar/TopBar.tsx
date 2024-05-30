import React, { useState } from 'react';
import './TopBar.css';
import Logo from './Logo/Logo';
import UserAvatar from './UserAvatar/UserAvatar';
import Search from "./Search/Search";

interface TopBarProps {
    onAvatarClick: () => void; // Функция обработки нажатия на кнопку UserAvatar
    onLogoClick: () => void; // Функция обработки нажатия на кнопку Logo
    onSearch: (value: string) => void;
    avatar: string;
}

const TopBar: React.FC<TopBarProps> = ({ onAvatarClick, onLogoClick, onSearch, avatar }) => {

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
                <Logo onLogoClick={onLogoClick} />
                <Search onSearch={handleSearch} buttons={bookButtons}/>
                <UserAvatar onClick={onAvatarClick} avatar={avatar}/>
        </div>
    );
}

export default TopBar;
