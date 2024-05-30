import React, {useEffect, useState} from 'react';
import TopBar from './components/TopBar/TopBar';
import './HomePage.css';
import BottomBar from "./components/BottomBar/BottomBar";

const HomePage: React.FC = () => {
    const [isAvatarClicked, setIsAvatarClicked] = useState(false);
    const [isTitleClicked, setIsTitleClicked] = useState(false);
    const [searchValue, setSearchValue] = useState<string>('');
    const [isSearch, setIsSearch] = useState(false);
    const [isNovelties, setIsNovelties] = useState(true);
    const [avatar, setAvatar] = useState<string>('');

    useEffect(() => {
        const fetchLogo = async () => {
            try {
                // Проверяем, загружено ли уже лого
                if (!avatar) {
                    const response = await fetch('http://localhost:8080/reader/photo', {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        },
                    });

                    if (response.ok) {
                        const responseData = await response.json();
                        // Если запрос прошел успешно, получаем фото
                        const photoUrl = responseData.photo;
                        // Устанавливаем полученный логотип в состояние
                        setAvatar(photoUrl);
                    } else {
                        // Обрабатываем ошибку, если логотип не удалось загрузить
                        console.error('Failed to load logo:', response.status);
                    }
                }
            } catch (error) {
                // Обрабатываем ошибку, если возникла ошибка при загрузке логотипа
                console.error('Error loading logo:', error);
            }
        };

        // Вызываем функцию для загрузки логотипа при монтировании компонента
        fetchLogo();
    }, [avatar]);


    const handleAvatarClick = () => {
        setIsAvatarClicked(true);
        /*setIsTitleClicked(false);
        setIsSearch(false);*/
    };

    const handleLogoClick = () => {
        setIsAvatarClicked(false);
        setIsTitleClicked(false);
        setIsSearch(false);
        setIsNovelties(true);
    };

    const handleTitleClick = () => {
        setIsTitleClicked(true);
        setIsAvatarClicked(false);

    };

    const handleBackClick = () =>{
        setIsTitleClicked(false);
    }

    const handleSearch = (value: string) => {
        setSearchValue(value);
        setIsAvatarClicked(false);
        setIsTitleClicked(false);
        setIsSearch(true);
        setIsNovelties(false);
    };

    const handleBackUserClick = () =>{
        setIsAvatarClicked(false);
    }

    return (
        <div className="home-page">
            {/* Передаем функцию обработки нажатия на кнопку UserAvatar и состояние isAvatarClicked в TopBar */}
            <TopBar onAvatarClick={handleAvatarClick} onLogoClick={handleLogoClick} onSearch={handleSearch} avatar={avatar}/>
            {/* Передаем состояние isAvatarClicked в BottomBar */}
            <BottomBar isAvatarClicked={isAvatarClicked} isTitleClicked={isTitleClicked} onTitleClick ={handleTitleClick} onBackClick={handleBackClick}  isSearch={isSearch} searchValue={searchValue} isNovelties={isNovelties} onBackUserClick={handleBackUserClick}/>
        </div>
    );
}

export default HomePage;
