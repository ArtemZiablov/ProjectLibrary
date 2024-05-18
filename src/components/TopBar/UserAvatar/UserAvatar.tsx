// UserAvatar.tsx

import React from 'react';
import './UserAvatar.css';

interface UserAvatarProps {
    onClick: () => void; // Функция обработки нажатия на кнопку UserAvatar
    avatar: string;
}

const UserAvatar: React.FC<UserAvatarProps> = ({ onClick, avatar}) => {
    return (
        <div className="user-avatar" onClick={onClick}>
            <img src={avatar} alt="userAvatar" />
        </div>
    );
}

export default UserAvatar;
