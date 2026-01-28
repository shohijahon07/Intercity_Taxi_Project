import React from 'react';
import "./band_qilish.css";
import PhoneInput from "react-phone-input-2";
import { useDispatch, useSelector } from 'react-redux';
import { addPessenger, setPesenger } from '../../../redux/slices/userSlice';
import { ToastContainer, toast } from 'react-toastify';

const Band_qilish = ({ isOpen, onClose, chatId }) => {
  const dispatch = useDispatch();
  const { pessen } = useSelector((state) => state.pessenger);

  const handleNameChange = (e) => {
    dispatch(setPesenger({ ...pessen, name: e.target.value }));
  };

  const handlePhoneChange = (phone) => {
    dispatch(setPesenger({ ...pessen, phoneNumber: '+' + phone }));
  };

  const savePessenger = () => {
    if (pessen.name === "" || pessen.phoneNumber === "") {
      toast.error("Iltimos bo'sh maydonlarni to'ldiring");
    } else {
      dispatch(addPessenger({ pessen, chatId }))
        .unwrap()
        .then(() => {
          toast.success('Yaqin orada haydovchi sizga aloqaga chiqadi!');
          dispatch(setPesenger({ name: "", phoneNumber: "", driverChatId: "" }));
          onClose()
        })
        .catch((err) => {
          console.error("Error in savePessenger:", err);
          toast.error('Xatolik yuz berdi, iltimos qayta urinib ko\'ring.');
        });
    }
  };

  if (!isOpen) return null;

  return (
    <div className="band_modal">
      <div className="band_content">
        <div className="band_header" onClick={onClose}>
          <button className="exitBtn"  />x
        </div>
        <div className="footer_band">
          <p>Iltimos quyidagilarni kiriting</p>
          <div className="inputs_band">
            <div className="form-floating">
              <input
                type="text"
                className="form-control"
                onChange={handleNameChange}
                value={pessen.name}
                id="name"
                placeholder="Ismingiz"
              />
              <label htmlFor="name">Ismingiz</label>
            </div>
            <div className="form-floating">
              <PhoneInput
                placeholder='Telefon raqam'
                inputClass={"form-control"}
                country={"uz"}
                onChange={handlePhoneChange}
                value={pessen.phoneNumber}
              />
            </div>
          </div>
          <div className="jonatishBtn">
            <button onClick={savePessenger}>Jo'natish</button>
          </div>
        </div>
      </div>
      <ToastContainer toastStyle={{ backgroundColor: 'white', color: 'black' }} autoClose={1000} />
    </div>
  );
};

export default Band_qilish;
