import React, { useContext, useEffect, useState } from 'react';
import './optionModal.css';
import { LanguageContext } from '../language/LanguageContext';
import { useDispatch, useSelector } from 'react-redux';
import { setActive } from '../../redux/slices/CommentSlice';

const OptionModal = ({ options, onSelect, onClose }) => {
    const dispatch=useDispatch()
    const { active } = useSelector((state) => state.comment);
    const { language } = useContext(LanguageContext);
    useEffect(() => {
        dispatch(setActive(true));
    }, []);

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
        setTimeout(onClose, 1000); 
    };

    return (
        <div className={`option-modal-overlay ${active ? 'active' : ''}`} onClick={handleClose}>
            <div className="option-modal" onClick={(e) => e.stopPropagation()}>
                <div className="btnCloseIcon" style={{paddingRight:"10px"}}>

                <button style={{backgroundColor:"transparent",border:"none",fontSize:"20px"}} onClick={handleClose}>x</button>
                </div>
                <div className="CityH1">

                <p>{language==="1"?"Hududni tanlang!":"Выбирайте регион!"}</p>
                </div>
                <ul style={{ marginTop: "15px",display:"flex",flexWrap:"wrap" }} className="option-list">
                    {options.map((option) => (
                        <li className='fromCityLi' key={option.name} onClick={() => onSelect(option.name)}>
                            {option.name}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default OptionModal;
