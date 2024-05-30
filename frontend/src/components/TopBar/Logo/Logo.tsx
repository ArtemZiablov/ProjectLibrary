import React from 'react';
import './Logo.css';
import logoPicture from './logo2.png';

interface LogoProps {
    onLogoClick: () => void; // Добавленный проп
}

const Logo: React.FC<LogoProps> = ({ onLogoClick }) => {
    const handleClick = () => {
        onLogoClick(); // Вызываем обработчик события при нажатии на логотип
    };

    return (
        <div className="logo" onClick={handleClick}> {/* Добавляем обработчик события */}
            <img src={logoPicture} alt="Logo"/>
        </div>
    );
}

export default Logo;
