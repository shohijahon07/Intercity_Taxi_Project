// src/context/LanguageContext.js
import React, { createContext, useState } from 'react';

export const LanguageContext = createContext();

export const LanguageProvider = ({ children }) => {
    const [language, setLanguage] = useState(localStorage.getItem('lan') || '1');

    const changeLanguage = (lang) => {
        const selectedLang = lang === '0' ? '1' : lang;
        localStorage.setItem('lan', selectedLang);
        setLanguage(selectedLang);
    };

    return ( 
        <LanguageContext.Provider value={{ language, changeLanguage }}>
            {children}
        </LanguageContext.Provider>
    );
};
