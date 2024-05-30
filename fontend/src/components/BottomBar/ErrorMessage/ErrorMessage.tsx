import React from 'react';
import './ErrorMessage.css';

interface ErrorMessageProps{
    errorMessage: string;
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ errorMessage }) => {
    let displayMessage;

    if (errorMessage  === "Your request is too small") {
        displayMessage = "Oops... your request is too small";
    }
    else if (errorMessage  == "We didn't find anything matching your request") {
        displayMessage = "Oops... we didn't find anything matching your request";
    }
    else {
        displayMessage = "Oops... something went wrong";
    }

    return (
        <div className="error-message">
            {displayMessage} 😧
        </div>
    );
};


export default ErrorMessage;

