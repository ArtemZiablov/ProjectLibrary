import React from 'react';
import './Title.css';

interface TitleProps {
    text: string; // Добавляем пропс для текста заголовка
}

const Title: React.FC<TitleProps> = ({ text }) => {
    return (
        <div className="title">
            {text}
        </div>
    );
}

export default Title;

