import React, { useContext } from "react";
import "./bizhaqimizda.css";
import b1 from "../../../pictures/b1.webp";
import b2 from "../../../pictures/b2.webp";
import b3 from "../../../pictures/b3.webp";
import b4 from "../../../pictures/b4.webp";
import logo from "../../../pictures/Group (1).webp";
import { LanguageContext } from "../../language/LanguageContext";

const BizHaqimizda2 = () => {
  const { language } = useContext(LanguageContext);
 
  return (
      <div className="bizhaqimizda-modal-content">
        <div className="bizhaqimizda-modal-header">
          <img src={logo} alt="Logo" className="bizhaqimizda-modal-logo" />
          <h2 className="bizhaqimizda-h2">{language==="1"?"Biz Haqimizda":"О нас"}</h2>
  
        </div>
        <div className="bizhaqimizda-modal-body">
         
            <div className="bizhaqimizda-info-item">
              <img src={b1} alt="Info 1" />
              <h5>Надежная сервисная компания</h5>
              <p>Клиентов обслуживает наш собственный сервисный центр Tripinsurance. Мы отслеживаем качество оказаннной медицинской помощи, отбираем лучшие клиники для наших клиентов.</p>
           
            </div>
            <div className="bizhaqimizda-info-item">
              <img src={b2} alt="Info 2" />
              <h5>Надежная сервисная компания</h5>
              <p>Клиентов обслуживает наш собственный сервисный центр Tripinsurance. Мы отслеживаем качество оказаннной медицинской помощи, отбираем лучшие клиники для наших клиентов.</p>
           
            </div>
            <div className="bizhaqimizda-info-item">
              <img src={b3} alt="Info 3" />
              <h5>Надежная сервисная компания</h5>
              <p>Клиентов обслуживает наш собственный сервисный центр Tripinsurance. Мы отслеживаем качество оказаннной медицинской помощи, отбираем лучшие клиники для наших клиентов.</p>
           
            </div>
            <div className="bizhaqimizda-info-item">
              <img src={b4} alt="Info 4" />
              <h5>Надежная сервисная компания</h5>
              <p>Клиентов обслуживает наш собственный сервисный центр Tripinsurance. Мы отслеживаем качество оказаннной медицинской помощи, отбираем лучшие клиники для наших клиентов.</p>
           
           
          </div>
        </div>
      </div>

  );
};

export default BizHaqimizda2;
