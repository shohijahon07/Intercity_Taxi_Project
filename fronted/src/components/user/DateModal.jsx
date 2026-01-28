import React, { useContext, useEffect, useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import "./date.css";
import { LanguageContext } from '../language/LanguageContext';
import { useDispatch, useSelector } from 'react-redux';
import { setActive, setSelectedDate } from '../../redux/slices/toCity';

function DateModal({ minDate, maxDate, onDateSelect, onClose }) {
    const dispatch=useDispatch()
    const { selectedDate,active } = useSelector((state) => state.toCity);
    const { language } = useContext(LanguageContext);

    useEffect(() => {
        dispatch(setActive(true));
    }, []);

    const handleChange = (date) => {
        if (date) {
            const dateOnly = new Date(date.getFullYear(), date.getMonth(), date.getDate());
            dispatch(setSelectedDate(dateOnly));
            onDateSelect(dateOnly);
            handleClose();
        }
    };
    useEffect(() => {
        if (active) {
            // Modal ochilganda body scrollni to'xtatamiz
            document.body.style.overflow = 'hidden';
        } else {
            // Modal yopilganda body scrollni tiklaymiz
            document.body.style.overflow = '';
        }

        return () => {
            // Komponent unmount bo'lganda scrollni tiklaymiz
            document.body.style.overflow = '';
        };
    }, [active]);
    const handleClose = () => {
        dispatch(setActive(false));
        setTimeout(onClose, 1000); // 1 second delay to allow the closing animation
    };

    return (
        <div className={`date-modal-overlay ${active ? 'active' : ''}`} onClick={handleClose}>
            <div className="date-modal" onClick={(e) => e.stopPropagation()}>
            <div className="btnCloseIcon" style={{paddingLeft:"8px"}}>

            <button style={{border:"none",backgroundColor:"transparent",fontSize:"20px"}} onClick={handleClose}>x</button>
            </div>
                <h3>{language==="1"?"Kunni tanlang!":"Выберите день!"}</h3>
                <DatePicker
                    selected={selectedDate}
                    onChange={handleChange}
                    minDate={new Date(minDate)}
                    maxDate={new Date(maxDate)}
                    dateFormat="yyyy/MM/dd"
                    inline
                />
            </div>
        </div>
    );
}

export default DateModal;
