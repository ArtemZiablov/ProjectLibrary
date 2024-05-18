import React, { useState, useEffect } from 'react';
import './SuccNotifications.css';

    const SuccNotifications: React.FC<{ message: string, requestStatus: string}> = ({ message, requestStatus }) => {
    const [show, setShow] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => {
            setShow(false);
        }, 1000);

        return () => clearTimeout(timer);
    }, []);

    return (
        <div className={`notification-container${show ? ' show' : ''}`}>
            <div className={`notification${requestStatus === 'success' ? ' success' : ' error'}`}>
                <div className="notification-text">{message}</div>
            </div>
        </div>
    );

    };

export default SuccNotifications;
