// src/components/user/Landing.jsx
import React, { useContext, useEffect, useState } from 'react';
import "./user.css";
import logo from "../../pictures/mainlogo.png";
import { useNavigate } from 'react-router-dom';
import { LanguageContext } from '../language/LanguageContext';
import telegram from "../../pictures/telegram.webp";
import BizHaqimizdaModal from './bizHaqimizda/BizHaqimizdaModal';
import Boglanish from './Boglanish';
import { useDispatch, useSelector } from 'react-redux';
import { setIsModalOpen, setIsModalOpen1 } from '../../redux/slices/CommentSlice';

function Landing() {
    const { language, changeLanguage } = useContext(LanguageContext);
    const navigate = useNavigate();
    const dispatch=useDispatch()
    const { isModalOpen1,isModalOpen } = useSelector((state) => state.comment);

    function goLanding() {
        navigate("/");
    }
    
    
    const openModal1 = () => {
        if (window.innerWidth <= 720) {
            if (window.innerWidth <= 720 &&window.innerWidth >=600 ) {
                window.scrollTo({ top: 800, behavior: 'smooth' });
            }else if(window.innerWidth <= 600 &&window.innerWidth >=500){
                window.scrollTo({ top:600, behavior: 'smooth' });
            }else if(window.innerWidth <= 500 &&window.innerWidth >=400){
                window.scrollTo({ top: 500, behavior: 'smooth' });
            }else if(window.innerWidth <= 400 ){
                window.scrollTo({ top: 400, behavior: 'smooth' });
            }

        }else{
        dispatch(setIsModalOpen1(true));
    }
    }
    const closeModal1 = () => dispatch(setIsModalOpen1(false));
    const openModal = () => {
        if (window.innerWidth <= 720) {
            if (window.innerWidth <= 720 &&window.innerWidth >=600 ) {
                window.scrollTo({ top: 2800, behavior: 'smooth' });
            }else if(window.innerWidth <= 600 &&window.innerWidth >=500){
                window.scrollTo({ top: 2500, behavior: 'smooth' });
            }else if(window.innerWidth <= 500 &&window.innerWidth >=400){
                window.scrollTo({ top: 2000, behavior: 'smooth' });
            }else if(window.innerWidth <= 400 ){
                window.scrollTo({ top: 1700, behavior: 'smooth' });
            }

        }else{
            dispatch(setIsModalOpen(true));
        }
    };
  const closeModal = () => dispatch(setIsModalOpen(false));

    useEffect(() => { 
       
    }, [language]);
    function handleIcons(value){
        window.open(value, "_blank");
    }
    return (
        <div className="fatherNavbar">
              <div className="navbar">
            <div className="leftUserHeader">
                <img src={logo} className='logoImg'  alt="image"  onClick={goLanding} />
            </div>
            <div className="middleUserHeader">
                <ul className='navbarUl'>
                    <li className='list-group-item hide-first-item'>{language==="1"?"Bosh Sahifa":"Главная страница"}</li>
                    <li className='list-group-item'  onClick={openModal}>{language==="1"?"Biz Haqimizda":"О нас"}</li>
                    <li className='list-group-item'  onClick={openModal1}>{language==="1"?"Bog'lanish":"Связь"}</li>
                </ul>
            </div>
            <div className="rightUserHeader">
                <select className='form-select' value={language} onChange={(e) => changeLanguage(e.target.value)}>
                    <option value="1">O'zbek</option>
                    <option value="2">Rus</option>
                </select>
                <img src={telegram} className='imgTelegram' style={{cursor:"pointer"}}  alt="image" onClick={()=>handleIcons('https://t.me/startuptaxiuz_bot')}/>
            </div>
        </div>
        <BizHaqimizdaModal isOpen={isModalOpen} onClose={closeModal} />
        <Boglanish isOpen1={isModalOpen1} onClose={closeModal1} />
        </div>
      
    );
}

export default Landing;
