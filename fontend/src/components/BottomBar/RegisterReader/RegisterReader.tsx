import React, { useState } from 'react';
import '../AddBook/AddBook.css';
import SuccNotifications from "../SuccNotifications/SuccNotifications";

interface FormData {
    fullName: string;
    dateOfBirth: string;
    email: string;
    phoneNumber: string;
    password: string;
    photo: string;
}

const RegisterReader: React.FC = () => {
    const [requestStatus, setRequestStatus] = useState("initial");
    const [formData, setFormData] = useState<FormData>({
        fullName: '',
        dateOfBirth: '',
        email: '',
        phoneNumber: '',
        password: '',
        photo: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try{
            const response = await fetch(`http://localhost:8080/auth/registration/reader`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                setRequestStatus("success");
            } else {
                setRequestStatus("failure");
            }
            return true;
        }catch (error) {
            console.error('Error add book:', error);
            setRequestStatus("failure");
            return false;
        }
    };

    const handleOkClick = () =>{
        setRequestStatus("initial");
    }

    return (
        <div className="add-book-container">
            <form className="add-book-form" onSubmit={handleSubmit}>
                <h2>Register Reader</h2>
                <div className="form-group">
                    <label>Full Name</label>
                    <input
                        type="text"
                        name="fullName"
                        value={formData.fullName}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Date of Birth</label>
                    <input
                        type="text"
                        name="dateOfBirth"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Phone Number</label>
                    <input
                        type="tel"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Photo URL</label>
                    <input
                        type="text"
                        name="photo"
                        value={formData.photo}
                        onChange={handleChange}
                    />
                </div>

                <button type="submit" className="submitButton">Register</button>
                {requestStatus === "success" && <SuccNotifications message={"Reader successfully registered"} requestStatus={requestStatus} onOkClick={handleOkClick}/>}
                {requestStatus === "failure" && <SuccNotifications message={"Failed to register reader"} requestStatus={requestStatus} onOkClick={handleOkClick}/>}
            </form>
        </div>
    );
};

export default RegisterReader;
