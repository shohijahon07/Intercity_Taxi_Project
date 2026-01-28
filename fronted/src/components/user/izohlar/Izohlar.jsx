import React, { useContext, useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { fetchDriverOne, setActive } from '../../../redux/slices/DriverSlice';
import "./izohlar.css"
import { fetchComments } from '../../../redux/slices/CommentSlice';
import { LanguageContext } from '../../language/LanguageContext';

const Izohlar = ({ isOpen, onClose, userName }) => {
    const dispatch = useDispatch();
    const { driverOne,active } = useSelector((state) => state.driver);
    const { comments } = useSelector((state) => state.comment);
    const { language } = useContext(LanguageContext);

    useEffect(() => {
        if (isOpen) {
            dispatch(fetchDriverOne(userName));
            dispatch(fetchComments({language,userName}));
            dispatch(setActive(true));
        } else {
            dispatch(setActive(false));
        }
    }, [dispatch, isOpen, userName]);

    const handleClose = () => {
        dispatch(setActive(false));
        setTimeout(onClose, 1000); 
    };

    if (!isOpen) return null;

    return (
        <div className={`izohlar-modal ${active ? 'active' : ''}`}>
            <div className={`izohlar-modal-child ${active ? 'active' : ''}`}>
                <button className='exitBtn' onClick={handleClose}>x</button>
                <div className="text">
                    {driverOne?.length > 0 && driverOne.map((item) => (
                        <li className='list-group-item' key={item.id}>
                            {language==="1"?"Haydovchi o’zi haqida:":"О водителе:"} {item.about}
                        </li>
                    ))}
                    {comments?.length > 0 && comments.map((item) => (
                        <li key={item.id} className='list-group-item userComment'>
                            <p>{item.name}</p>: <p>{item.text}</p>
                        </li>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Izohlar;
