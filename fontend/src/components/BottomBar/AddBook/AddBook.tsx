import React, { useState } from 'react';
import './AddBook.css';
import SuccNotifications from "../SuccNotifications/SuccNotifications";

const AddBook: React.FC = () => {
    const [formData, setFormData] = useState({
        isbn: '',
        copiesAmount: '',
        title: '',
        yearOfPublishing: '',
        numberOfPages: '',
        annotation: '',
        language: '',
        bookPhoto: '',
        authors: [{ fullName: '', dateOfBirth: '', nationality: '' }],
        translators: [{ translator: '' }],
        genres: [{ genreName: '' }],
    });
    const [showFullForm, setShowFullForm] = useState(false); // Флаг для отображения полной формы
    const [requestStatus, setRequestStatus] = useState("initial");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleAuthorChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
        const { name, value } = e.target;
        const updatedAuthors = [...formData.authors];
        updatedAuthors[index] = { ...updatedAuthors[index], [name]: value };
        setFormData({
            ...formData,
            authors: updatedAuthors,
        });
    };

    const handleTranslatorChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = e.target;
        setFormData({
            ...formData,
            translators: [{ translator: value }],
        });
    };

    const addAuthorField = () => {
        setFormData({
            ...formData,
            authors: [...formData.authors, { fullName: '', dateOfBirth: '', nationality: '' }],
        });
    };

    const addTranslatorField = () => {
        setFormData({
            ...formData,
            translators: [...formData.translators, { translator: '' }],
        });
    };

    const handleGenreChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
        const { name, value } = e.target;
        const updatedGenres = [...formData.genres];
        updatedGenres[index] = { ...updatedGenres[index], [name]: value };
        setFormData({
            ...formData,
            genres: updatedGenres,
        });
    };

    const addGenreField = () => {
        setFormData({
            ...formData,
            genres: [...formData.genres, { genreName: '' }],
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        // Отправка запроса на сервер для проверки наличия книги по isbn
        const exists = await checkBookAvailability(formData.isbn, formData.copiesAmount);
        if (!exists) {
            setShowFullForm(true); // Показываем полную форму, если книга существует
        } else {
            setRequestStatus("success");
            /*setTimeout(() => {
                setRequestStatus("initial");
            }, 1500);*/
        }
    };

    const handleSubmitFullForm = async (e: React.FormEvent) => {
        e.preventDefault();
        //http://localhost:8080/book/add-book
        try {

            const translators = formData.translators[0].translator.trim() === '' ? [] : formData.translators;
            const dataToSend = {
                ...formData,
                translators: translators
            };

            const response = await fetch(`http://localhost:8080/book/add-book`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(dataToSend)
            });

            if (response.ok) {
                setRequestStatus("success");
            } else {
                setRequestStatus("failure");
            }
            /*setTimeout(() => {
                setRequestStatus("initial");
            }, 1500);*/

            return response.ok;
        } catch (error) {
            console.error('Error add book:', error);
            setRequestStatus("failure");

            /*setTimeout(() => {
                setRequestStatus("initial");
            }, 1500);*/
            return false;
        }
    }

    const renderAuthors = () => {
        return formData.authors.map((author, index) => (
            <div key={index}>
                <div className="form-group">
                    <label>Author {index + 1}:</label>
                    <input
                        type="text"
                        name="fullName"
                        placeholder={"Author's full name..."}
                        value={author.fullName}
                        onChange={(e) => handleAuthorChange(e, index)}
                    />
                </div>
                <div className="author-details">
                    <div className="form-group">
                        <label>Author`s {index + 1} date of birth:</label>
                        <input
                            type="text"
                            name="dateOfBirth"
                            placeholder={"Author's date of birth..."}
                            value={author.dateOfBirth}
                            onChange={(e) => handleAuthorChange(e, index)}
                        />
                    </div>
                    <div className="form-group">
                        <label>Author`s {index + 1} nationality:</label>
                        <input
                            type="text"
                            name="nationality"
                            placeholder={"Author's nationality..."}
                            value={author.nationality}
                            onChange={(e) => handleAuthorChange(e, index)}
                        />
                    </div>
                </div>
            </div>
        ));
    };

    const renderTranslators = () => {
        return formData.translators.map((translator, index) => (
            <div key={index} className="form-group">
                <label>Translator {index + 1}:</label>
                <input
                    type="text"
                    name="translatorName"
                    placeholder={"Translator's full name..."}
                    value={translator.translator}
                    onChange={handleTranslatorChange}
                />
            </div>
        ));
    };

    const renderGenres = () => {
        return formData.genres.map((genre, index) => (
            <div key={index} className="form-group">
            <label>Genre {index + 1}:</label>
                <input
                    type="text"
                    name="genreName"
                    placeholder={"Genre..."}
                    value={genre.genreName}
                    onChange={(e) => handleGenreChange(e, index)}
                />
            </div>
        ));
    };

    const removeAuthorField = () => {
        if (formData.authors.length > 1) {
            const updatedAuthors = [...formData.authors];
            updatedAuthors.pop(); // Удаляем последний элемент из массива авторов
            setFormData({
                ...formData,
                authors: updatedAuthors,
            });
        }
    };

    const removeTranslatorField = () => {
        if (formData.translators.length > 1) {
            const updatedTranslators = [...formData.translators];
            updatedTranslators.pop(); // Удаляем последний элемент из массива переводчиков
            setFormData({
                ...formData,
                translators: updatedTranslators,
            });
        }
    };

    const removeGenreField = () => {
        if (formData.genres.length > 1) {
            const updatedGenres = [...formData.genres];
            updatedGenres.pop(); // Удаляем последний элемент из массива жанров
            setFormData({
                ...formData,
                genres: updatedGenres,
            });
        }
    };

    const handleOkClick = () =>{
        setRequestStatus("initial");
    }


    const renderForm = () => {
        if (!showFullForm) {
            return (
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>ISBN:</label>
                        <input type="text" name="isbn" value={formData.isbn} placeholder={"ISBN..."} onChange={handleChange} />
                    </div>
                    <div className="form-group">
                        <label>Copies amount:</label>
                        <input type="text" placeholder={"Copies amount..."} name="copiesAmount" value={formData.copiesAmount} onChange={handleChange} />
                    </div>
                    <button className={"submitButton"} type="submit">Check Book</button>
                    {requestStatus === "success" && <SuccNotifications message={"Book copies successfully added"} requestStatus={requestStatus} onOkClick={handleOkClick}/>}
                </form>
            );
        } else {
            // Отображаем полную форму после успешной проверки
            return (
                <div>
                    <form onSubmit={handleSubmitFullForm} className="add-book-form">
                        <h2>Add Book</h2>
                        <div className="form-group">
                            <label>ISBN:</label>
                            <input type="text" placeholder={"ISBN..."} name="isbn" value={formData.isbn}
                                   onChange={handleChange}/>
                        </div>

                        <div className="form-group">
                            <label>Title:</label>
                            <input type="text" placeholder={"Book title..."} name="title" value={formData.title}
                                   onChange={handleChange}/>
                        </div>

                        <div className="form-group">
                            <label>Year of publishing:</label>
                            <input
                                type="text"
                                name="yearOfPublishing"
                                placeholder={"Year of publishing..."}
                                value={formData.yearOfPublishing}
                                onChange={handleChange}
                            />
                        </div>

                        <div className="form-group">
                            <label>Number of pages:</label>
                            <input type="text" placeholder={"Number of pages..."} name="numberOfPages"
                                   value={formData.numberOfPages}
                                   onChange={handleChange}/>
                        </div>

                        <div className="form-group">
                            <label>Annotation:</label>
                            <textarea name="annotation" placeholder={"Annotation..."} value={formData.annotation}
                                      onChange={handleChange}/>
                        </div>

                        <div className="form-group">
                            <label>Language:</label>
                            <input type="text" placeholder={"Language..."} name="language" value={formData.language}
                                   onChange={handleChange}/>
                        </div>

                        <div className="form-group">
                            <label>Book Photo:</label>
                            <input type="text" placeholder={"Book Photo..."} name="bookPhoto" value={formData.bookPhoto}
                                   onChange={handleChange}/>
                        </div>

                        <div className="form-group">
                            <label>Copies Amount:</label>
                            <input type="text" placeholder={"Copies amount..."} name="copiesAmount"
                                   value={formData.copiesAmount}
                                   onChange={handleChange}/>
                        </div>

                        <div>
                            {renderAuthors()}
                            <button className={"addButton"} type="button" onClick={addAuthorField}><img
                                src={'https://i.imgur.com/Q6ath1w.png'} alt="Add"/></button>
                            <button className={"addButton"} type="button" onClick={removeAuthorField}><img
                                src={'https://i.imgur.com/MMYu2PA.png'} alt="Remove"/></button>
                        </div>

                        <div>
                            {renderTranslators()}
                            <button className={"addButton"} type="button" onClick={addTranslatorField}><img
                                src={'https://i.imgur.com/Q6ath1w.png'} alt="Add"/></button>
                            <button className={"addButton"} type="button" onClick={removeTranslatorField}><img
                                src={'https://i.imgur.com/MMYu2PA.png'} alt="Remove"/></button>
                        </div>

                        <div>
                            {renderGenres()}
                            <button className={"addButton"} type="button" onClick={addGenreField}><img
                                src={'https://i.imgur.com/Q6ath1w.png'} alt="Add"/></button>
                            <button className={"addButton"} type="button" onClick={removeGenreField}><img
                                src={'https://i.imgur.com/MMYu2PA.png'} alt="Remove"/></button>
                        </div>

                        <button className={"submitButton"} type="submit">Submit</button>
                        {requestStatus === "success" && <SuccNotifications message={"Book successfully added"} requestStatus={requestStatus} onOkClick={handleOkClick}/>}
                        {requestStatus === "failure" && <SuccNotifications message={"Failed to add book"} requestStatus={requestStatus} onOkClick={handleOkClick}/>}
                    </form>
                </div>
            );
        }
    };

    return (
        <div className="add-book-container">
            {renderForm()}
        </div>
    );
};

// Функция для отправки запроса на сервер для проверки наличия книги по isbn
const checkBookAvailability = async (isbn: string, copiesAmount: string): Promise<boolean> => {
    try {
        const data = {
            isbn: isbn,
            copiesAmount: copiesAmount
        };
        const response = await fetch(`http://localhost:8080/book-copy/add-book-copies`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(data)
        });

        console.log(data);
        return response.ok;
    } catch (error) {
        console.error('Error checking book availability:', error);
        return false; // В случае ошибки считаем, что книги нет
    }
};

export default AddBook;