import React from 'react';
import {BrowserRouter as Router, Routes, Route, Navigate, useLocation} from 'react-router-dom';
import Home from './HomePage';
import Login from './Login';
import LMS from './LMS';
import Guest from "./Guest";

/*import jwt from "jsonwebtoken";*/

function decodeJWT(token: any) {
    const parts = token.split('.'); // Разделение на части
    if (parts.length !== 3) {
        throw new Error('Invalid JWT');
    }

    const header = JSON.parse(window.atob(parts[0].replace(/-/g, '+').replace(/_/g, '/')));
    const payload = JSON.parse(window.atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));

    return { header, payload };
}

// Функция, которая будет проверять аутентификацию пользователя и определять путь перехода
const isAuthenticated = () => {
    const token = localStorage.getItem('token');
    if (token) {
        try {
            const decodedToken: any = decodeJWT(token);
            if (decodedToken && decodedToken.payload.role) {
                console.log(decodedToken.payload.role)
                return decodedToken.payload.role;
            }
        } catch (error) {
            localStorage.removeItem('token');
            return null;
        }
    }
    return null;
};

const PrivateRoute = ({ element: Element, ...rest }: any) => {
    const currentUser = isAuthenticated();
    const { pathname } = useLocation();

    // Если пользователь не авторизован, перенаправляем на страницу входа
    if (!currentUser) {
        return <Navigate to="/login" />;
    }

    // Проверяем доступ пользователя к текущему маршруту
    if (currentUser === 'READER' && !pathname.startsWith('/library')) {
        return <Navigate to="/library" />;
    }
    if (currentUser === 'LIBRARIAN' && !pathname.startsWith('/lms')) {
        return <Navigate to="/lms" />;
    }

    // Отображаем компонент, если пользователь имеет доступ
    return <Element {...rest} />;
};

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/guest" />} />
                <Route path="/guest" element={<Guest />} />
                <Route path="/login" element={<Login />} />
                <Route path="/library/*" element={<PrivateRoute element={Home} />} />
                <Route path="/lms/*" element={<PrivateRoute element={LMS} />} />
            </Routes>
        </Router>
    );
}

export default App;