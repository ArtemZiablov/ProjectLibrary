import React, { useState } from 'react';
import './SuccNotifications.css';

interface SuccNotificationsProps {
    message: string;
    requestStatus: string;
    onOkClick: () => void;
}

const SuccNotifications: React.FC<SuccNotificationsProps> = ({ message, requestStatus, onOkClick }) => {
    const [show, setShow] = useState(true);

    const handleClick = () => {
        setShow(false);
        onOkClick();
    };

    return (
        <div className={`notification-container${show ? ' show' : ''}`}>
            <div className={`notification${requestStatus === 'success' ? ' success' : ' error'}`}>
                <div className="notification-text">{message}</div>
                <button className="notification-button" onClick={handleClick}>OK</button>
            </div>
        </div>
    );
};

export default SuccNotifications;
