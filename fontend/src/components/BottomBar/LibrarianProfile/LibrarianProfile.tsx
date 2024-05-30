import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";


interface LibrarianProfileProps{
    onBackClick: () => void;
}

interface LibrarianInfo{
    profilePhoto: string;
    fullName: string;
    librarianId: string;
    phoneNumber: string;
    email: string;
}

const LibrarianProfile: React.FC<LibrarianProfileProps> = ({ onBackClick}) => {
    const [librarianInfo, setLibrarianInfo] = useState<LibrarianInfo | null>(null);
    const [librarianInfoLoaded, setLibrarianInfoLoaded] = useState(false);
    const navigate = useNavigate();

    function decodeJWT(token: any) {
        const parts = token.split('.'); // Разделение на части
        if (parts.length !== 3) {
            throw new Error('Invalid JWT');
        }

        const header = JSON.parse(window.atob(parts[0].replace(/-/g, '+').replace(/_/g, '/')));
        const payload = JSON.parse(window.atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));

        return { header, payload };
    }

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                // Отправляем запрос для получения информации о читателе
                const response = await fetch(`http://localhost:8080/librarian/info`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },});
                if (!response.ok) {
                    throw new Error('Ошибка при загрузке информации о читателе: ' + response.statusText);
                }

                const data = await response.json();

                setLibrarianInfo(data);
                setLibrarianInfoLoaded(true);

            } catch (error) {
                console.error(error);
            }
        };

        fetchUserInfo();

    }, []);

    const handleOnLogOutClick =() => {
        localStorage.removeItem('token');
        console.log(localStorage.getItem('token'))
        navigate('/');
    }

    return (
        <div className="profile-top-container">
            <button className="close-button-reader" onClick={onBackClick}>
                <img src="https://i.imgur.com/reHgKyL.png" alt="Back"/>
            </button>

            <button className="logout" onClick={handleOnLogOutClick}>
                Log out
            </button>
            {librarianInfoLoaded && librarianInfo ? (
                <div className="user-information-container">
                    <div className="avatar">
                        <img src={librarianInfo.profilePhoto} alt="Avatar"/>
                    </div>
                    <div className="user-details">
                        <h2>{librarianInfo.fullName}</h2>
                        <p>Librarian Card ID: {librarianInfo.librarianId}</p>
                        <p>Phone Number: {librarianInfo.phoneNumber}</p>
                        <p>Email: {librarianInfo.email}</p>
                    </div>
                </div>
            ) : null}
        </div>
    );
}

export default LibrarianProfile;