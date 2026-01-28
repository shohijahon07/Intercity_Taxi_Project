import React, { useContext } from 'react'
import "./../../user/footer.css"
import { LanguageContext } from '../../language/LanguageContext';
import rasm from "../../../pictures/5.webp";
import { useNavigate } from 'react-router-dom';
function DriverFooter() {
  const { language, changeLanguage } = useContext(LanguageContext);
  const navigate=useNavigate();
function path(){
navigate("/yo'nalish")
}
function myself(){
  navigate("/o'zim_haqqimda")
}
  return (
    <div className='footer'>
    <div className="line2"></div>
<div className="footer3">
  <div className="textFooter">
      <ul className='foterUl'>
<li className='list-group-item' onClick={path} > {language==="1"?"Yo'nalish":"Направление"}   </li>
<li  className='list-group-item' onClick={myself} >{language==="1"?"Men haqimda":"Обо мне"}</li>
</ul>
  <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum <br /> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum <br /> tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros <br /> elementum tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in <br /> eros elementum tristique.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius <br /> enim in eros elementum tristique.</p>
  </div>
    <div className="footerYer">
        <img src={rasm} alt="" style={{objectFit:"contain",width:"100%",height:"100%"}} />
    </div>
</div>


    <div className="line2"></div>
</div>
  )
}

export default DriverFooter