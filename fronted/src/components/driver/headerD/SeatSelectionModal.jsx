import React, { useState, useEffect, useContext } from 'react';
import './seatModal.css'; // CSS fayl nomi o'zgarmadi
import { LanguageContext } from '../../language/LanguageContext';
import { useDispatch, useSelector } from 'react-redux';
import { setSelectedSeat } from '../../../redux/slices/fromCity';

function SeatSelectionModal({ onSelect, onClose, isTrue }) {
    const dispatch=useDispatch()
  const {selectedSeat } = useSelector((state) => state.fromCity);
    const { language } = useContext(LanguageContext);

    const handleSeatSelect = (seatNumber) => {
        dispatch(setSelectedSeat(seatNumber));
        onSelect(seatNumber);
        onClose();
    };
    useEffect(() => {
        // Prevent background scrolling when modal is open
        document.body.style.overflow = 'hidden';
    
        // Cleanup function to restore body scroll when modal is closed
        return () => {
          document.body.style.overflow = 'auto';
        };
      }, []);
    
    return (
        <div className="modal-overlay1" onClick={onClose}>
            <div className="modal-content1" onClick={(e) => e.stopPropagation()}>
                <div className="close-icon-container1">
                    <button className="close-button1" onClick={onClose}>x</button>
                </div>
                <div className="modal-header1">
                    <p>{language === "1" ? "O'rindiqlar sonini tanlang!" : "Выбирайте количество мест!"}</p>
                </div>
                <div className="seat-container1" style={{ marginTop: '15px', display: 'flex', flexWrap: 'wrap' }}>
                    {Array.from({ length: isTrue==true ? 6 : 12 }, (_, index) => (
                        <button
                            key={index + 1}
                            className={`seat-button ${selectedSeat === index + 1 ? 'seat-selected' : ''}`}
                            onClick={() => handleSeatSelect(index + 1)}
                        >
                            {index + 1}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default SeatSelectionModal;
