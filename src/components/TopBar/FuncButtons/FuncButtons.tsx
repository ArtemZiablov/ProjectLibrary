import React, { useState } from 'react';
import './FuncButtons.css'

interface FuncButtonsProps {
    onButtonSelect: (buttonIndex: number) => void;
}

const FuncButtons: React.FC<FuncButtonsProps> = ({ onButtonSelect }) => {
    return (
        <div className={"func-button"}>
            <button onClick={() => onButtonSelect(1)}>Find reader</button>
            <button onClick={() => onButtonSelect(2)}>Find book</button>
            <button onClick={() => onButtonSelect(3)}>Add book</button>
            <button onClick={() => onButtonSelect(4)}>Statistics</button>
        </div>
    );
}

export default FuncButtons;
