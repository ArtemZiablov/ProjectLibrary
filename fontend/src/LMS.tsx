import React, {useEffect, useState} from 'react';
import './LMS.css';
import LMSTopBar from "./components/TopBar/LMSTopBar";
import LMSBottomBar from "./components/BottomBar/LMSBottomBar";

const HomePage: React.FC = () => {
    const [selectedButton, setSelectedButton] = useState<number | null>(null); // Добавляем состояние selectedButton

    const [isAvatarClicked, setIsAvatarClicked] = useState(false);
    const [isTitleClicked, setIsTitleClicked] = useState(false);
    const [isReaderClicked, setIsReaderClicked] = useState(false);
    const [searchValueBook, setSearchValueBook] = useState<string>('');
    const [isSearchBook, setIsSearchBook] = useState(false);
    const [searchValueReader, setSearchValueReader] = useState<string>('');
    const [isSearchReader, setIsSearchReader] = useState(false);
    const [isNovelties, setIsNovelties] = useState(true);

    const [avatar, setAvatar] = useState<string>('');

    useEffect(() => {
        const fetchLogo = async () => {
            try {
                // Проверяем, загружено ли уже лого
                if (!avatar) {
                    const response = await fetch('http://localhost:8080/librarian/photo', {
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
        setIsReaderClicked(false);
        setIsSearchBook(false);
        setIsSearchReader(false);*/
        setSelectedButton(null);
    };

    const handleLogoClick = () => {
        setIsAvatarClicked(false);
        setIsReaderClicked(false);
        setIsTitleClicked(false);
        setIsSearchBook(false);
        setIsSearchReader(false);
        setIsNovelties(true);
        setSelectedButton(null);
    };

    const handleTitleClick = () => {
        setIsTitleClicked(true);
        setIsAvatarClicked(false);
        setIsReaderClicked(false);
    };

    const handleReaderClick = () => {
        setIsReaderClicked(true);
        setIsAvatarClicked(false);
    };

    const handleBackBookClick = () =>{
        setIsTitleClicked(false);
        setSelectedButton(null);
    }

    const handleBackReaderClick = () =>{
        setIsReaderClicked(false);
        /*setSelectedButton(null);*/
    }

    const handleSearchBook = (value: string) => {
        setSearchValueBook(value);
        setIsAvatarClicked(false);
        setIsTitleClicked(false);
        setIsSearchBook(true);
        setIsReaderClicked(false);
        setIsNovelties(false);
        //setSelectedButton(null);
        setIsSearchReader(false);
    };

    const handleSearchReader = (value: string) => {
        setSearchValueReader(value);
        setIsAvatarClicked(false);
        setIsTitleClicked(false);
        setIsReaderClicked(false);
        setIsSearchReader(true);
        setIsNovelties(false);
        //setSelectedButton(null);
        setIsSearchBook(false);
    };

    const handleBackUserClick = () =>{
        setIsAvatarClicked(false);
    }

    return (
        <div className="home-page">
            <LMSTopBar
                selectedButton={selectedButton}
                handleButtonSelect={setSelectedButton}
                onAvatarClick={handleAvatarClick}
                onLogoClick={handleLogoClick}
                onSearchBook={handleSearchBook}
                onSearchReader={handleSearchReader}
                avatar={avatar}
            />
            <LMSBottomBar
                isAvatarClicked={isAvatarClicked}
                isTitleClicked={isTitleClicked}
                onTitleClick ={handleTitleClick}
                onBackBookClick={handleBackBookClick}
                onReaderClick={handleReaderClick}
                isReaderClicked={isReaderClicked}
                isSearchBook={isSearchBook}
                searchValueBook={searchValueBook}
                isNovelties={isNovelties}
                isSearchReader={isSearchReader}
                searchValueReader={searchValueReader}
                selectedButton={selectedButton}
                onBackReaderClick={handleBackReaderClick}
                onBackUserClick={handleBackUserClick}
            />
        </div>
    );
}

export default HomePage;
