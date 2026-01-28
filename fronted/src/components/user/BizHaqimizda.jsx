import React from 'react'
import "./boglanish.css";
import rasm1 from "../../pictures/1.webp";
import rasm2 from "../../pictures/2.webp";
import rasm3 from "../../pictures/3.webp";
function BizHaqimizda() {
  
  return (
      <div className="boglanishCard">
        <div className="card">
        <div className="card-header">
          <div className="imgboglanish">

          <img src={rasm1} style={{objectFit:"cover",width:"100%",height:"100%"}}  alt="image"/>
          </div>
        </div>
      
        <div className="card-footer">
          <h4>Awesome code</h4>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum tristique.</p>
        </div>
        </div>
        <div className="card">
        <div className="card-header">
          <div className="imgboglanish">

          <img src={rasm2} style={{objectFit:"cover",width:"100%",height:"100%"}}  alt="image"/>
          </div>
        </div>
      
        <div className="card-footer">
          <h4>Awesome code</h4>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum tristique.</p>
        </div>
        </div>
        <div className="card">
        <div className="card-header">
          <div className="imgboglanish">

          <img src={rasm3} style={{objectFit:"cover",width:"100%",height:"100%"}}  alt="image"/>
          </div>
        </div>
      
        <div className="card-footer">
          <h4>Awesome code</h4>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse varius enim in eros elementum tristique.</p>
        </div>
        </div>
      </div>
   
  )
}

export default BizHaqimizda
