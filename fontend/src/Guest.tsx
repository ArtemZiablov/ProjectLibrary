import React, {useState} from 'react';
import './HomePage.css';
import GuestBottomBar from "./components/BottomBar/GuestBottomBar";
import GuestTopBar from "./components/TopBar/GuestTopBar";
import {useNavigate} from "react-router-dom";


const Guest: React.FC = () => {
    const [isTitleClicked, setIsTitleClicked] = useState(false);
    const [searchValue, setSearchValue] = useState<string>('');
    const [isSearch, setIsSearch] = useState(false);
    const [isNovelties, setIsNovelties] = useState(true);

    const navigate = useNavigate();

    if(localStorage.getItem('token')) {
        localStorage.removeItem('token')
    }

    const handleLogInClick = () => {
        navigate('/login');
    };

    const handleLogoClick = () => {
        setIsTitleClicked(false);
        setIsSearch(false);
        setIsNovelties(true);
    };

    const handleTitleClick = () => {
        setIsTitleClicked(true);

    };

    const handleBackClick = () =>{
        setIsTitleClicked(false);
    }

    const handleSearch = (value: string) => {
        setSearchValue(value);
        setIsTitleClicked(false);
        setIsSearch(true);
        setIsNovelties(false);
    };

    return (
        <div className="home-page">
            {/* Передаем функцию обработки нажатия на кнопку UserAvatar и состояние isAvatarClicked в TopBar */}
            <GuestTopBar onLogInClick={handleLogInClick} onLogoClick={handleLogoClick} onSearch={handleSearch}/>
            {/* Передаем состояние isAvatarClicked в BottomBar */}
            <GuestBottomBar isTitleClicked={isTitleClicked} onTitleClick ={handleTitleClick} onBackClick={handleBackClick}  isSearch={isSearch} searchValue={searchValue} isNovelties={isNovelties}/>
        </div>
    );
}

export default Guest;
