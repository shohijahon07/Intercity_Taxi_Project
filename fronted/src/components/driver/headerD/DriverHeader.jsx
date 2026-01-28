import React, { useContext, useEffect, useState } from 'react'
import "./header12.css"
import { LanguageContext } from '../../language/LanguageContext';
import logo from "../../../pictures/Group (1).webp";

import telegram from "../../../pictures/telegram.webp";
import { useNavigate } from 'react-router-dom';
function DriverHeader() {
  const { language, changeLanguage } = useContext(LanguageContext);
  let navigate = useNavigate();
  useEffect(() => {
}, [language,changeLanguage]);
function functionDeleteToken() {
  navigate("/")
  localStorage.removeItem("access_token")
  localStorage.removeItem("refresh_token")
}
function handleIcons(value){
  window.open(value, "_blank");
}
function functionDeleteToken() {
  navigate("/")
  localStorage.removeItem("access_token")
  localStorage.removeItem("refresh_token")
}
    function navigatePath(){
navigate("/yo'nalish")
    }
    
    function goLanding() {
      navigate("/");
  }
    function navigateMyselft(){
      navigate("/o'zim_haqqimda")
          }

  return (

        <div className="fatherNavbar">
              <div className="navbar1">
            <div className="leftUserHeader1">
                <img src={logo} className='logoImg1'  onClick={goLanding} />
            </div>
            <div className="middleUserHeader1">
                <ul className='navbarUl1'>
                    <li className='list-group-item' onClick={navigatePath}>{language==="1"?"Yo'nalish":"Направление"}</li>
                    <li className='list-group-item'  onClick={navigateMyselft}>{language==="1"?"Men haqimda":"Обо мне"}</li>
                </ul>
            </div>
            <div className="rightUserHeader1">
                <select className='form-select' value={language} onChange={(e) => changeLanguage(e.target.value)}>
                    <option value="1">O'zbek</option>
                    <option value="2">Rus</option>
                </select>
                <img src={telegram} className='imgTelegram1' style={{cursor:"pointer"}}  alt="image" onClick={()=>handleIcons('https://t.me/kenjacar_bot')}/>
                <button className='logout1' onClick={functionDeleteToken}>chiqish</button>
            </div>
        </div>
        </div>

  )
}

export default DriverHeader
