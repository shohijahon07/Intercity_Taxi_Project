import React, { useRef } from 'react'
import "./routesAdmin.css"
import { useDispatch, useSelector } from 'react-redux';
import { addAdvert, setAdvert, setSelectedFileA } from '../../redux/slices/advertisementSlice';
import { message } from 'antd';
function AdversimentBot() {
  const dispatch=useDispatch()
const { adverst,selectedFileA} = useSelector((state) => state.advertisement);
const fileInputRef = useRef(null);
const saveFromCity = () => {
  // Corrected to check the values from adverst instead of addAdvert
  if (!adverst.text || !adverst.link || !adverst.buttonName) {
      message.error("xatolik yuz berdi!");
  } else {
      dispatch(addAdvert({ adverst, selectedFileA }))
          .unwrap()
          .then(() => {
              message.success('Malumot muvaffaqiyatli qoshildi!');
              // Reset the form after successful addition
              dispatch(setAdvert({ text: "", img: "", link: "", buttonName: "" }));
              dispatch(setSelectedFileA(null));
              if (fileInputRef.current) {
                fileInputRef.current.value = ""; // Reset the file input
              }
          })
          .catch((error) => {
              message.error('Malumot qo\'shishda xatolik yuz berdi!');
          });
  }
};

  return (
    
    <div>
      <div className="inputsT">
        <textarea name="text"
          value={adverst.text} style={{width:"90%",height:"150px",marginBottom:"20px",padding:"10px",fontSize:"21px"}} onChange={(e)=>dispatch(setAdvert({...adverst,text:e.target.value}))}  placeholder='text'/>
          
        <input type="file" ref={fileInputRef} value={addAdvert.img}  accept="image/*" className='form-control'style={{width:"250px",height:"50px",fontSize:"18px"}} onChange={(e)=>dispatch(setSelectedFileA(e.target.files[0]))} placeholder='image' />
        <input type="text" value={adverst.link} className='form-control' style={{width:"250px",height:"50px",fontSize:"21px"}} onChange={(e)=>dispatch(setAdvert({...adverst,link:e.target.value}))} placeholder='Link' />
        <input type="text" value={adverst.buttonName} className='form-control' style={{width:"250px",height:"50px",fontSize:"21px"}} placeholder='button name' onChange={(e)=>dispatch(setAdvert({...adverst,buttonName:e.target.value}))} />
        <button className='saveButton1' onClick={saveFromCity}>jo'natish</button>
      </div>
    </div>
  )
}

export default AdversimentBot
