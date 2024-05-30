import React from 'react';
import './LMSTopBar.css';
import Logo from './Logo/Logo';
import UserAvatar from './UserAvatar/UserAvatar';
import Search from "./Search/Search";
import FuncButtons from "./FuncButtons/FuncButtons";
import closeImg from "./CloseButton.png"

interface LMSTopBarProps {
    selectedButton: number | null; // Определяем selectedButton в пропсах
    onAvatarClick: () => void;
    onLogoClick: () => void;
    onSearchBook: (value: string) => void;
    onSearchReader: (value: string) => void;
    handleButtonSelect: (buttonIndex: number | null) => void; // Подправляем тип данных
    avatar:string;
}

const LMSTopBar: React.FC<LMSTopBarProps> = ({
                                                 selectedButton,
                                                 onAvatarClick,
                                                 onLogoClick,
                                                 onSearchBook,
                                                 onSearchReader,
                                                 handleButtonSelect,
                                                 avatar
                                             }) => {
    const resetSelection = () => {
        handleButtonSelect(null); // Передаем null в handleButtonSelect
    };

    const handleSearchBook = (value: string) => {
        onSearchBook(value);
    };

    const handleSearchReader = (value: string) => {
        onSearchReader(value);
    };

    const bookButtons = [
        { label: 'Title', value: 'title' },
        { label: 'Author', value: 'author' },
        { label: 'Genre', value: 'genre' }
    ];

    const readerButtons = [
        { label: 'Name', value: 'name' },
        { label: 'ID', value: 'id' },
        { label: 'Phone', value: 'phone' }
    ];

    let componentToDisplay = null;
    switch (selectedButton) {
        case 1:
            componentToDisplay = (
                <div>
                    <div><Search onSearch={handleSearchReader} buttons={readerButtons}/></div>
                    <button className={"closeFunc"} onClick={resetSelection}>
                        <img src={"https://i.imgur.com/dFEPYDI.png"} alt="Закрыть"/>
                    </button>
                </div>
            );
            break;
        case 2:
            componentToDisplay = (
                <div>
                    <div><Search onSearch={handleSearchBook} buttons={bookButtons}/></div>
                    <button className={"closeFunc"} onClick={resetSelection}>
                        <img src={"https://i.imgur.com/dFEPYDI.png"} alt="Закрыть"/>
                    </button>
                </div>
            );
            break;
        case 3:
        case 4:
        case 5:
        default:
            componentToDisplay = <FuncButtons onButtonSelect={handleButtonSelect} />;
    }

    return (
        <div className="top-bar">
            <Logo onLogoClick={onLogoClick} />
            {componentToDisplay}
            <UserAvatar onClick={onAvatarClick} avatar={avatar}/>
        </div>
    );
}

export default LMSTopBar;
