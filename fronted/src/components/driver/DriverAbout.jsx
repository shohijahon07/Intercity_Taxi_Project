import React, { useEffect, useRef, useState } from 'react'
import "./driverLanding.css"
import axios from 'axios'
import { useDispatch, useSelector } from 'react-redux';
import { addDriverAbout, editDriver, fetchDriverOne, setAbout, setDriver, setEditButtonId, setImg, setImg1, setImg2, setSelectedFile, setSelectedFile1, setSelectedFile2 } from '../../redux/slices/DriverSlice';
import { ToastContainer, toast } from 'react-toastify';


function DriverAbout() {
const dispatch=useDispatch()
const [userName, setUserName] = useState("");
const { EditButtonId, driverObject,driverOne,selectedFile,selectedFile1,selectedFile2,img,img1,img2,about} = useSelector((state) => state.driver);
const fileInputRef1 = useRef(null);
const fileInputRef2 = useRef(null);
const fileInputRef3 = useRef(null);

useEffect(() => {
  getDriver()
  dispatch(fetchDriverOne(userName))
  }, [userName]);

function getDriver(){
    axios({
      url:"https:/api/auth/name",
      method:"get",
      headers:{Authorization:localStorage.getItem("refresh_token")}
  }).then((res)=>{
    setUserName(res.data.id)
  })
  }

  const saveDepartment = () => {
        dispatch(editDriver({ EditButtonId, driverObject,img,img1,img2,selectedFile,selectedFile1,selectedFile2 }))
          .unwrap()
          .then(() => {
            toast.success('Malumot muvaffaqiyatli tahrirlandi!');
            dispatch(fetchDriverOne(userName));
          })
          .catch((err) =>console.log(err));
    dispatch(setEditButtonId(null));
    dispatch(setDriver( { fullName: '', phoneNumber: '', carType: "", carImg: "", driverImg: "", cardDocument: "", about: ""}));
    if (fileInputRef1.current) fileInputRef1.current.value = '';
    if (fileInputRef2.current) fileInputRef2.current.value = '';
    if (fileInputRef3.current) fileInputRef3.current.value = '';

  };
  const EditItem = (item) => {
    dispatch(setEditButtonId(item.id));
    dispatch(setImg(item.carImg));
    dispatch(setImg1(item.driverImg));
    dispatch(setImg2(item.cardDocument));

    dispatch(setDriver({ fullName: item.fullName, phoneNumber: item.phoneNumber, carType: item.carType, carImg: item.carImg, driverImg: item.driverImg, cardDocument: item.cardDocument, about: item.about  }));
 
  };
  const saqlashAbout = () => {
    dispatch(addDriverAbout({ about, userName }))
      .unwrap()
      .then(() => {
        toast.success('Malumot muvaffaqiyatli tahrirlandi!');
        dispatch(fetchDriverOne(userName));
      })
      .catch((err) => {
        console.error("Error in saqlashAbout:", err);
        toast.error('Xatolik yuz berdi, iltimos qayta urinib ko\'ring.');
      });
    dispatch(setAbout(""));
  };
  return (
    <div className='container_dr_about'>
    {
      driverOne.map((item) => {
        return  <ul className='list-group' key={item.id}>
          <li className='list-group-item'><h5>Ism Familiya:</h5> <h6>{item.fullName}</h6>
            <input type="text" className='from-control' value={driverObject.fullName}
              onChange={(e) => dispatch(setDriver({ ...driverObject, fullName: e.target.value }))}
            />
            </li>
          <li className='list-group-item'><h5>Telefon raqam:</h5><h6>{item.phoneNumber}</h6>
            <input type="text" className='from-control' value={driverObject.phoneNumber}
              onChange={(e) => dispatch(setDriver({ ...driverObject, phoneNumber: e.target.value }))}
            /></li>
          <li className='list-group-item'><h5>Mashina Rusumi:</h5><h6>{item.carType}</h6>
            <input type="text" className='from-control' value={driverObject.carType}
              onChange={(e) => dispatch(setDriver({ ...driverObject, carType: e.target.value }))}
            /></li>
          <li className='list-group-item'><h5>Mashina Rasmi:</h5>
            <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.carImg}`} alt="#" />
           <div className="imputFile">
           <p>Rasm tanlang</p>
           </div>
            
          </li>
          <li className='list-group-item'><h5>Haydovchilik Guvohnomasi:</h5>
            <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.driverImg}`} alt="#" />
           <div className="imputFile">
            <p>Rasm tanlang</p>
            <input type="file" className='from-control' accept="image/*"  ref={fileInputRef2} onChange={(e) => dispatch(setSelectedFile1(e.target.files[0]))} />
           </div>
          </li>
          <li className='list-group-item'><h5>Mashina Texpasporti:</h5>
            <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.cardDocument}`} alt="#" />
            <div className="imputFile">
            <p>Rasm tanlang</p>

            </div>
           
          </li>
          <li className='list-group-item'><h5>Haydovchi haqida:</h5><h6>{item.about == null ? "malumot mavjud emas" : item.about}</h6>
            <input type="text" className='from-control' value={driverObject.about}
              onChange={(e) => dispatch(setDriver({ ...driverObject, about: e.target.value }))}
            /></li>
          <li className='list-group-item'><h5>Actions:</h5> <div className="btn-group">
            <button className='editButton' onClick={() => EditItem(item)}>Tahrirlash</button>
            <button className='saqlash1' onClick={saveDepartment}>Saqlash</button>
          </div></li>
        </ul>
      })
    }
    <div className="driverFooterAbout">
      <p>O'zingiz haqida oddiy matn kiriting?</p>
      <div className="Text-driver">
        <input type="text" className='form-control' value={about} onChange={(e)=>dispatch(setAbout(e.target.value))}/> <button className='saqlash1' onClick={saqlashAbout}>saqlash</button>
      </div>
    </div>
     <ToastContainer toastStyle={{
          backgroundColor: 'white',
          color: 'black',
        }} autoClose={1000} />
  </div>
  )
}

export default DriverAbout
