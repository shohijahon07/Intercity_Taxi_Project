import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { saveUserData, setChatId, setLanguage, updateForm, updateFile, resetForm, setModal } from '../../redux/slices/Register';
import { Modal, Button } from 'antd';
import fileicon from "../../pictures/fileicon.svg";
import "./Register.css";

function Register() {
    const dispatch = useDispatch();
    const { form, selectFiles, chatId, language, isModalVisible, modalMessage, modalType } = useSelector(state => state.register);
    const [previewImages, setPreviewImages] = useState([null, null, null]);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const chatIdParam = urlParams.get('chatId');
        const languageParam = urlParams.get('language');
        if (chatIdParam) {
            dispatch(setChatId(chatIdParam));
        }
        if (languageParam) {
            dispatch(setLanguage(languageParam));
        }
    }, [dispatch]);

    const validateForm = () => {
        return form.carType && form.about && form.count && selectFiles.every(fayl => fayl);
    };

    const showModal = (message, type) => {
        dispatch(setModal({ isVisible: true, message, type }));
    };

    const handleOk = () => {
        dispatch(setModal({ isVisible: false, message: '', type: '' }));
    };

    const save = async () => {
        if (!validateForm()) {
            showModal(
                language === 'uz' 
                    ? "Barcha ma'lumotlarni to'ldiring. Har bir maydonni to'ldirishingiz kerak." 
                    : "Pojaluysta, zapolnite Vy doljny zapolnit kajdoe pole.",
                'error'
            );
            return;
        }

        showModal(
            language === 'uz' 
                ? "Ma'lumotlaringiz tekshirilmoqda. Iltimos kuting, biz o'zimiz Telegram bot orqali xabar yuboramiz." 
                : "Vasha ma'lumot proveryaetsya. Pojaluysta, pododite, my otpravim soobshchenie cherez Telegram-bot.", 
            'success'
        );

        try {
            await dispatch(saveUserData({ form, selectFiles, chatId }));
            dispatch(resetForm());
            setPreviewImages([null, null, null]); // Oldindan ko'rishlarni o'chirish
        } catch (error) {
            console.error('Saqlash paytidagi xato:', error);
            showModal(
                language === 'uz' 
                    ? `Xatolik yuz berdi: ${error.message}` 
                    : `Proizoshla oshibka: ${error.message}`,
                'error'
            );
        }
    };

    const handleFileChange = (e, index) => {
        const file = e.target.files[0];
        dispatch(updateFile({ index, file }));

        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                const newPreviews = [...previewImages];
                newPreviews[index] = reader.result;
                setPreviewImages(newPreviews);
            };
            reader.readAsDataURL(file);
        }
    };

    const fileLabels = [
        { uz: "Mashina rasmini yuklash", ru: "Zagruzit izobrajenie mashiny" },
        { uz: "Haydovchini guvohnomasi", ru: "Zagruzit voditelskoe udosoverenie" },
        { uz: "Texpasportni yuklash", ru: "Zagruzit texpasport" }
    ];

    return (
        <div className='register_con'>
            <h3 className='text_rg'>
                {language === "uz" ? "Ro'yxatdan o'tish uchun ma'lumotlarni kiriting" : "Vvedite sleduyushchuyu informatsiyu dlya registratsii"}
            </h3>
            <div className="body-con">
                <input 
                    type="text" 
                    placeholder={language === "uz" ? "Mashina rusumi" : "Marka mashiny"} 
                    className='form-control my-2' 
                    value={form.carType} 
                    onChange={(e) => dispatch(updateForm({ carType: e.target.value }))} 
                />
                <input 
                    type="text" 
                    placeholder={language === "uz" ? "O'zingiz haqingizda yozing" : "Napishite o sebe"} 
                    className='form-control my-2' 
                    value={form.about} 
                    onChange={(e) => dispatch(updateForm({ about: e.target.value }))} 
                />
                <select 
                    className='form-control' 
                    value={form.count} 
                    style={{ marginBottom: "10px" }} 
                    onChange={(e) => dispatch(updateForm({ count: e.target.value }))}>
                    <option className='form-control' value={""}>
                        {language === "uz" ? "Tanlang" : "Vyberite"}
                    </option>
                    <option className='form-control' value={true}>
                        {language === "uz" ? "Yengil mashina" : "Legkovoy avtomobil"}
                    </option>
                    <option className='form-control' value={false}>
                        {language === "uz" ? "Mikro avtobus" : "Mikroavtobus"}
                    </option>
                </select>
                
                {fileLabels.map((label, index) => (
                    <div className="custom-file-upload" key={index}>
                        <input 
                            type="file" 
                            id={`file${index + 1}`} 
                            accept="image/*" 
                            onChange={(e) => handleFileChange(e, index)} 
                        />
                        <label htmlFor={`file${index + 1}`}>
                            <img src={fileicon} alt="Yuklash belgisi" className="upload-icon" />
                            <span>{language === "uz" ? label.uz : label.ru}</span>
                            {previewImages[index] && (
                                <img src={previewImages[index]} alt="Preview" className="file_rg" />
                            )}
                        </label>
                    </div>
                ))}
                
                <button type="primary" className='btn_rg' onClick={save}>
                    {language === "uz" ? "Yuborish" : "Otpravka"}
                </button>
            </div>
            
            {isModalVisible && (
                <Modal 
                    title={language === "uz" ? "Ma'lumot" : "Information"} 
                    visible={isModalVisible} 
                    onCancel={handleOk} 
                    footer={[
                        <Button key="ok" onClick={handleOk}>
                            {language === "uz" ? "OK" : "OK"}
                        </Button>,
                    ]}
                    closable={true} // Enable close (X) button
                    maskClosable={false} // Prevent closing by clicking outside
                    keyboard={false} // Prevent closing with the Escape key
                >
                    <h5 style={{ color: modalType === 'success' ? 'green' : 'red' }}>
                        {modalMessage}
                    </h5>
                </Modal>
            )}
        </div>
    );
}

export default Register;
