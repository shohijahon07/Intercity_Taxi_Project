import React, { useContext, useState } from 'react'
import "./footer.css"
import rasm from "../../pictures/5.webp";
import { LanguageContext } from '../language/LanguageContext';
import BizHaqimizdaModal from './bizHaqimizda/BizHaqimizdaModal';
import Boglanish from './Boglanish';
import { useDispatch, useSelector } from 'react-redux';
import { setIsModalOpen, setIsModalOpen1 } from '../../redux/slices/CommentSlice';
function Footer() {
  const { language } = useContext(LanguageContext);
  const dispatch=useDispatch()
  const { isModalOpen1,isModalOpen } = useSelector((state) => state.comment);

  
    function top(){
      const scrollHeight = document.documentElement.scrollHeight;
        const clientHeight = document.documentElement.clientHeight;
        
            window.scrollTo({ top: scrollHeight - clientHeight - 4000, behavior: 'smooth' });
        
    }
  const openModal1 = () => {
    if (window.innerWidth <= 720) {
        const scrollHeight = document.documentElement.scrollHeight;
        const clientHeight = document.documentElement.clientHeight;
        
        if (window.innerWidth <= 720 && window.innerWidth >= 600) {
            window.scrollTo({ top: scrollHeight - clientHeight - 3200, behavior: 'smooth' });
        } else if (window.innerWidth <= 600 && window.innerWidth >= 500) {
            window.scrollTo({ top: scrollHeight - clientHeight - 3000, behavior: 'smooth' });
        } else if (window.innerWidth <= 500 && window.innerWidth >= 400) {
            window.scrollTo({ top: scrollHeight - clientHeight - 2700, behavior: 'smooth' });
        } else if (window.innerWidth <= 400) {
            window.scrollTo({ top: scrollHeight - clientHeight - 2500, behavior: 'smooth' });
        }
    } else {
        dispatch(setIsModalOpen1(true));
    }
};

    const closeModal1 = () => dispatch(setIsModalOpen1(false));
    
    const openModal = () => {
      if (window.innerWidth <= 720) {
          const scrollHeight = document.documentElement.scrollHeight;
          const clientHeight = document.documentElement.clientHeight;
  
          if (window.innerWidth <= 720 && window.innerWidth >= 600) {
              window.scrollTo({ top: scrollHeight - clientHeight - 1300, behavior: 'smooth' });
          } else if (window.innerWidth <= 600 && window.innerWidth >= 500) {
              window.scrollTo({ top: scrollHeight - clientHeight - 1000, behavior: 'smooth' });
          } else if (window.innerWidth <= 500 && window.innerWidth >= 400) {
              window.scrollTo({ top: scrollHeight - clientHeight - 1000, behavior: 'smooth' });
          } else if (window.innerWidth <= 400) {
              window.scrollTo({ top: scrollHeight - clientHeight - 1000, behavior: 'smooth' });
          }
      } else {
          dispatch(setIsModalOpen(true));
      }
  };
  
  const closeModal = () => dispatch(setIsModalOpen(false));
  return (
    <div className='footer'>
            <div className="line2"></div>
        <div className="footer3">
          <div className="textFooter">
              <ul className='foterUl'>
        <li className='list-group-item' onClick={top}>{language==="1"?"Bosh Sahifa":"Главная страница"}</li>
        <li  className='list-group-item'  onClick={openModal}>{language==="1"?"Biz Haqimizda":"О нас"}</li>
        <li  className='list-group-item' onClick={openModal1}>{language==="1"?"Bog'lanish":"Связь"}</li>
      </ul>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum <br /> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum <br /> tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros <br /> elementum tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in <br /> eros elementum tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius <br /> enim in eros elementum tristique.</p>
          </div>
            <div className="footerYer">
                <img src={rasm}  alt="image" style={{objectFit:"contain",width:"100%",height:"100%"}} />
            </div>
        </div>
    

            <div className="line2"></div>
            <BizHaqimizdaModal isOpen={isModalOpen} onClose={closeModal} />
        <Boglanish isOpen1={isModalOpen1} onClose={closeModal1} />
    </div>
  )
}

export default Footer
