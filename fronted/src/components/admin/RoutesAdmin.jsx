import React, { useEffect } from 'react'
import { addToCity, deleteToCity, editToCity, fetchToCity, setEditButtonId, setToCity } from '../../redux/slices/toCity';
import { useDispatch, useSelector } from 'react-redux';
import { ToastContainer, toast } from 'react-toastify';
import "./routesAdmin.css"
import { addFromCity, deleteFromCity, editFromCity, fetchFromCity, setEditButtonId1, setFromCity } from '../../redux/slices/fromCity';
import { message } from 'antd';
function RoutesAdmin() {
const dispatch=useDispatch()
const { toCities,toCityObject,selectedFile,EditButtonId} = useSelector((state) => state.toCity);
const { fromCities,fromCityObject,EditButtonId1} = useSelector((state) => state.fromCity);

  useEffect(() => {
   
    dispatch(fetchToCity())
    dispatch(fetchFromCity())
    }, []);


    const saveToCity = () => {
      if (EditButtonId) {
        dispatch(editToCity({ EditButtonId, toCityObject }))
        .unwrap()
        .then(() => {
          message.success('Malumot muvaffaqiyatli tahrirlandi!');
          dispatch(fetchToCity());
        })
        .catch((err) =>console.log(err));
        
      }else{
          dispatch(addToCity({ toCityObject, }))
        .unwrap()
        .then(() => {
          message.success('Malumot muvaffaqiyatli qoshildi!');
          dispatch(fetchToCity());
        })
        .catch((err) =>console.log(err));
      }
    
  dispatch(setEditButtonId(null));
  dispatch(setToCity( { name:""}));
  

};

    const saveFromCity = () => {
      if (EditButtonId1) {
        dispatch(editFromCity({ EditButtonId1, fromCityObject }))
        .unwrap()
        .then(() => {
          message.success('Malumot muvaffaqiyatli tahrirlandi!');
          dispatch(fetchFromCity());
        })
        .catch((err) =>console.log(err));
      }else{
         dispatch(addFromCity({ fromCityObject }))
        .unwrap()
        .then(() => {
          message.success('Malumot muvaffaqiyatli qoshildi!');
          dispatch(fetchFromCity());
        })
        .catch((err) =>console.log(err));
      }
     
  dispatch(setEditButtonId(null));
  dispatch(setFromCity( { name:""}));
  

};
const EditToCity = (item) => {
  const specificGroupArea = document.getElementById('specificGroupArea');
  specificGroupArea.scrollIntoView({ behavior: 'smooth' });
  dispatch(setEditButtonId(item.id));
  dispatch(setToCity({ name:item.name }));

};
const EditToFromCity = (item) => {
  const specificGroupArea = document.getElementById('specificGroupArea');
  specificGroupArea.scrollIntoView({ behavior: 'smooth' });
  dispatch(setEditButtonId1(item.id));
  dispatch(setFromCity({ name:item.name }));

};

const DeleteFromCity = (id) => {
  if (EditButtonId) {
    message.error('Xatolik yuz berdi!');
  } else {
    dispatch(deleteFromCity({ id }))
      .unwrap()
      .then(() => {
        message.success("Malumot muvaffaqiyatli o'chirildi!");
        dispatch(fetchFromCity());
      })
      .catch(() => message.error('Xatolik yuz berdi!'));
  }
};
const DeleteToCity = (id) => {
  if (EditButtonId) {
    message.error('Xatolik yuz berdi!');
  } else {
    dispatch(deleteToCity({ id }))
      .unwrap()
      .then(() => {
        message.success("Malumot muvaffaqiyatli o'chirildi!");
        dispatch(fetchToCity());
      })
      .catch(() => message.error('Xatolik yuz berdi!'));
  }
};
  return (
    <div style={{paddingBottom:"30px"}}>
        <div className="d-flex justify-content-around">
            <div className="RoutesAdminInput">
        <div className="inputRoute" id="specificGroupArea">
            <p>Qayerdan</p> <input type="text" className='form-control inp11Admin' value={fromCityObject.name} onChange={(e)=>dispatch(setFromCity({...fromCityObject,name:e.target.value}))}/>
        </div>
             <button className='saveButton' onClick={saveFromCity}> Saqlash</button>
      </div>
      <div className="RoutesAdminInput">
        <div className="inputRoute">
            <p>Qayerga</p> <input type="text" className='form-control' value={toCityObject.name} onChange={(e)=>dispatch(setToCity({...toCityObject,name:e.target.value}))} />
        </div>
             <button className='saveButton' onClick={saveToCity}> Saqlash</button>
      </div>
        </div>

       <div className="tableCity">
        <div className="leftCity">
        {
          fromCities.map((item,index)=>{
            return  <li  key={item.id} className='list-group-item  liFromCity'><div className=""><p>{index+1}:{item.name}</p></div> <div className="btn-group"><button className='editBTN' onClick={()=>EditToFromCity(item)}>Tahrirlash</button> <button className='deleteBTN' onClick={()=>DeleteFromCity(item.id)}>O'chirish</button></div> </li>
           
            
          })
        }
        </div>
        <div className="rightCity">
        {
          toCities.map((item,index)=>{
            return  <li key={item.id} className='list-group-item  liFromCity'><div className=""><p>{index+1}:{item.name}</p></div> <div className="btn-group"><button className='editBTN' onClick={()=>EditToCity(item)}>Tahrirlash</button> <button className='deleteBTN' onClick={()=>DeleteToCity(item.id)}>O'chirish</button></div> </li>
           
            
          })
        }
        </div>
       </div>
        <ToastContainer toastStyle={{
          backgroundColor: 'white',
          color: 'black',
        }} autoClose={1000} />
    </div>
  )
}

export default RoutesAdmin
