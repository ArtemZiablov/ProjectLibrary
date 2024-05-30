import React from 'react';
import './ReaderContainer.css';

export interface Reader {
    readerId: string;
    fullName: string;
    phoneNumber: string;
    profilePhoto: string;
}

interface ReaderContainerProps {
    readers: Reader[];
    onClick: (readerId: string) => void; // Accepts reader's ID as parameter
}

const ReaderContainer: React.FC<ReaderContainerProps> = ({ readers, onClick }) => {

    const handleOnClick = (readerId: string) => {
        onClick(readerId); // Pass the reader's ID to the onClick function
    }

    return (
        <div className="reader-container">
            {readers.map((reader, index) => (
                <div className="reader" key={index}>
                    <img src={reader.profilePhoto} alt={reader.fullName}/>
                    <div className="reader-info">
                        <button className="reader-name"
                                onClick={() => handleOnClick(reader.readerId)}>{reader.fullName}</button>
                        <p>Reader ID: {reader.readerId}</p>
                        <p>Phone number: {reader.phoneNumber}</p>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default ReaderContainer;
