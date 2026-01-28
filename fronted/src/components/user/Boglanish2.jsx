
import React, { useContext } from 'react';
import rasm from "../../pictures/4.webp";
import "./boglanish.css"
import { LanguageContext } from '../language/LanguageContext';
import { useDispatch, useSelector } from 'react-redux';
import { setForm, saveForm } from '../../redux/slices/Connection';
function Boglanish2() {

  const { language } = useContext(LanguageContext);
  const dispatch = useDispatch();
  const { form } = useSelector((state) => state.boglanish);

  function handleInputChange(e) {
    const { id, value } = e.target;
    dispatch(setForm({ [id]: value }));
  }

  function handleSave() {
    if (!form.fullName || !form.phoneNumber || !form.message) {
      alert(language === "1" ? "Iltimos, barcha maydonlarni to'ldiring." : "Пожалуйста, заполните все поля.");
      return;
    }
    dispatch(saveForm({ form, language }));
  }
  return (
      <div className="modal-body" >
        <div className="modal-header-section">
          <h1 className="modal-heading">{language==="1"?"Savollar Bo'lsa Murojaat qiling!":"Пожалуйста, свяжитесь с нами, если у вас есть какие-либо вопросы!"}</h1>
          <p className="modal-subheading">{language==="1"?"Qo’llab-quvvatlash xizmatimiz 09:00-20:00 aloqada":"Наша служба поддержки работает с 09:00 до 20:00."}</p>
        </div>
        <div className="contact-info-section">
          <div className="contact-image-container">
            <img src={rasm} alt="Contact" style={{objectFit:"cover",width:"100%",height:"100%"}}/>
          </div>
          <div className="contact-text">
            <p className="contact-label">{language==="1"?"Telefon raqam":"Номер телефона"}</p>
            <h4 className="contact-number">(+998) 88-300-03-03</h4>
          </div>
        </div>
        <div className="horizontal-divider"></div>
        <div className="yozmaSavollar">
        <h4 className='yozmaSavollarH4'>{language==="1"?"Yozma shaklda murojaatingizni qoldiring":"Оставьте заявку в письменном виде"}</h4>
        <div className="inpgroup">
    <div className="form-floating">
        <input type="text" className="form-control" id="fullName" placeholder="Ismizgiz" value={form.fullName} onChange={handleInputChange}/>
        <label htmlFor="name">{language==="1"?"Ismizgiz":"Ваше имя"}</label>
    </div>
    <div className="form-floating">
        <input type="text" className="form-control" id="phoneNumber" placeholder="Telefon raqamingiz" value={form.phoneNumber} onChange={handleInputChange}/>
        <label htmlFor="phone">{language==="1"?"Telefon raqamingiz":"Ваш номер телефона"}</label>
    </div>
    <div className="form-floating">
        <input type="text" className="form-control" id="message" placeholder="Xabar" value={form.message} onChange={handleInputChange}/>
        <label htmlFor="message">{language==="1"?"Xabar":"Сообщение"}</label>
    </div>
    <button className='yonalish' onClick={handleSave}>{language==="1"?"Yo'nalish":"Направление"}</button>
</div>


        
      </div>
      </div>
    
  );
}

export default Boglanish2;
