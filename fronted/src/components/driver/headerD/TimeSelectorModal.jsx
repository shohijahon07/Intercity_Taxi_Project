import React, { useState, useEffect, useContext } from 'react';
import './TimeSelectorModal.css';
import { LanguageContext } from '../../language/LanguageContext';

const TimeSelectorModal = ({ onClose, onSelect }) => {
  const [selectedHour, setSelectedHour] = useState(new Date().getHours());
  const [selectedMinute, setSelectedMinute] = useState(new Date().getMinutes());
  const [touchStartY, setTouchStartY] = useState(null);
  const { language } = useContext(LanguageContext);

  useEffect(() => {
    // Prevent background scrolling when modal is open
    document.body.style.overflow = 'hidden';

    // Cleanup function to restore body scroll when modal is closed
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, []);

  const hours = Array.from({ length: 24 }, (_, i) => i);
  const minutes = Array.from({ length: 60 }, (_, i) => i);

  const handleConfirm = () => {
    onSelect({ hour: selectedHour, minute: selectedMinute });
    onClose();
  };

  const getWrappedIndex = (array, index) => {
    const length = array.length;
    return (index + length) % length;
  };

  const handleWheel = (e, setSelected, selectedValue, valuesArray) => {
    e.preventDefault();
    const direction = e.deltaY > 0 ? 1 : -1;
    const newValueIndex = getWrappedIndex(valuesArray, selectedValue + direction);
    setSelected(newValueIndex);
  };

  const handleTouchStart = (e) => {
    setTouchStartY(e.touches[0].clientY);
  };

  const handleTouchMove = (e, setSelected, selectedValue, valuesArray) => {
    if (touchStartY === null) return;

    const touchCurrentY = e.touches[0].clientY;
    const direction = touchStartY - touchCurrentY > 0 ? 1 : -1;
    const newValueIndex = getWrappedIndex(valuesArray, selectedValue + direction);
    setSelected(newValueIndex);
    setTouchStartY(touchCurrentY); // Update the touch start position
  };

  return (
    <div className="time-selector-overlay" onClick={onClose}>
      <div className="time-selector-modal" onClick={(e) => e.stopPropagation()}>
        <div className="time-selector-header">
          <h2>{language === "1" ? "Vaqtni tanlang!" : "Выберите время!"}</h2>
          <button className="exi" onClick={onClose}>x</button>
        </div>
        <div className="time-selector-body">
          <div className="time-selector-picker">
            <div className="time-selector-column">
              <div
                className="spinner"
                onWheel={(e) => handleWheel(e, setSelectedHour, selectedHour, hours)}
                onTouchStart={handleTouchStart}
                onTouchMove={(e) => handleTouchMove(e, setSelectedHour, selectedHour, hours)}
              >
                {[-2, -1, 0, 1, 2].map((offset) => {
                  const hourIndex = getWrappedIndex(hours, selectedHour + offset);
                  const hour = hours[hourIndex];
                  return (
                    <div
                      key={hourIndex}
                      className={`spinner-item ${selectedHour === hour ? 'selected' : ''}`}
                      style={{
                        fontSize: selectedHour === hour ? '24px' : '20px',
                        color: selectedHour === hour ? 'blue' : 'black',
                      }}
                    >
                      {hour < 10 ? `0${hour}` : hour}
                    </div>
                  );
                })}
              </div>
            </div>
            <div className="time-selector-divider">-</div>
            <div className="time-selector-column">
              <div
                className="spinner"
                onWheel={(e) => handleWheel(e, setSelectedMinute, selectedMinute, minutes)}
                onTouchStart={handleTouchStart}
                onTouchMove={(e) => handleTouchMove(e, setSelectedMinute, selectedMinute, minutes)}
              >
                {[-2, -1, 0, 1, 2].map((offset) => {
                  const minuteIndex = getWrappedIndex(minutes, selectedMinute + offset);
                  const minute = minutes[minuteIndex];
                  return (
                    <div
                      key={minuteIndex}
                      className={`spinner-item ${selectedMinute === minute ? 'selected' : ''}`}
                      style={{
                        fontSize: selectedMinute === minute ? '24px' : '20px',
                        color: selectedMinute === minute ? 'blue' : 'black',
                      }}
                    >
                      {minute < 10 ? `0${minute}` : minute}
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
        <div className="time-selector-actions">
          <button className="btn-confirm" onClick={handleConfirm}>
            Saqlash
          </button>
        </div>
      </div>
    </div>
  );
};

export default TimeSelectorModal;
