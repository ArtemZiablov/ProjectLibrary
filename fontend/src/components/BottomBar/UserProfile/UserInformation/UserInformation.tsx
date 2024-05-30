import React, { useState, useEffect } from 'react';
import './UserInformation.css'
import {ReaderInfo} from "../../ReaderProfile/ReaderProfile";

interface UserInformationProps{
    userInfo: ReaderInfo;
}

const UserInformation: React.FC<UserInformationProps> = ({userInfo}) => {

    return (
        <div className="user-information-container">
            <div className="avatar">
                <img src={userInfo.profilePhoto} alt="Avatar"/>
            </div>
            <div className="user-details">
                <h2>
                    {userInfo.fullName} {userInfo.debtor &&
                    <span style={{color: "#DC143C"}}> - Debtor</span>}
                </h2>
                <p>Reader Card ID: {userInfo.readerId}</p>
                <p>Phone Number: {userInfo.phoneNumber}</p>
                <p>Email: {userInfo.email}</p>
            </div>
        </div>
    );
}

export default UserInformation;
