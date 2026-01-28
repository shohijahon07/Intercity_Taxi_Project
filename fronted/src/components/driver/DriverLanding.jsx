import React, { useEffect, useState } from 'react'
import "./driverLanding.css"
import axios from 'axios';
import { addRoute, deleteRoutes, editRoute, fetchRoutesByDriver, setEditButtonId, setKoranCourse } from '../../redux/slices/routeDriver';
import { useDispatch, useSelector } from 'react-redux';
import { ToastContainer, toast } from 'react-toastify';
import { fetchToCity } from '../../redux/slices/toCity';
import { fetchFromCity } from '../../redux/slices/fromCity';
import { setMaxDate, setMinDate } from '../../redux/slices/CommentSlice';
import { setUserName } from '../../redux/slices/DriverSlice';

function DriverLanding() {
  const dispatch = useDispatch();
  const { userName } = useSelector((state) => state.driver);
  const { minDate,maxDate } = useSelector((state) => state.comment);
  const { EditButtonId, driverRout,driverRoutes} = useSelector((state) => state.routes);
  const { toCities} = useSelector((state) => state.toCity);
  const { fromCities} = useSelector((state) => state.fromCity);
  const { language } = useContext(LanguageContext);
  // const [minDate, setMinDate] = useState('');
  // const [maxDate, setMaxDate] = useState('');
  // const [userName, setUserName] = useState("");

  useEffect(() => {
    dispatch(fetchToCity())
    dispatch(fetchFromCity())
    const today = new Date();
    const tomorrow = new Date(today);
    const dayAfterTomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);
    dayAfterTomorrow.setDate(today.getDate() + 2);
    const formatDate = (date) => date.toISOString().split('T')[0];
    dispatch(setMinDate(formatDate(today)));
    dispatch(setMaxDate(formatDate(dayAfterTomorrow)));
    getDriver()
    dispatch(fetchRoutesByDriver(userName));
  }, [userName,dispatch]);

  const saveDepartment = () => {
    if (driverRoutes.length >= 1) {
      toast.error("Siz ayni damda faqat 1 ta yo'nalishda ishlay olasiz!")
       
    }else{
      if (EditButtonId) {
        dispatch(editRoute({ EditButtonId, driverRout }))
          .unwrap()
          .then(() => {
            toast.success('Malumot muvaffaqiyatli tahrirlandi!');
            dispatch(fetchRoutesByDriver(userName));
          })
          .catch((err) =>console.log(err));
      } else {
        dispatch(addRoute({driverRout,userName }))
          .unwrap()
          .then(() => {
            toast.success("Malumot muvaffaqiyatli qo'shildi!");
            dispatch(fetchRoutesByDriver(userName));
          })
          .catch((err) => toast.error(toast.error("xatolik yuz berdi!")));
      }
    }
    
    
    

    dispatch(setEditButtonId(null));
    dispatch(setKoranCourse( { fromCity: '', toCity: '', countSide: '',price:"" ,day:"",hour:"",userId:""}));
    

  };
  function getDriver(){
    axios({
      url:"https:/api/auth/name",
      method:"get",
      headers:{Authorization:localStorage.getItem("refresh_token")}
  }).then((res)=>{
    dispatch(setUserName(res.data.id))
  })
  }
  const EditItem = (item) => {
    dispatch(setEditButtonId(item.id));
    dispatch(setKoranCourse({ fromCity: item.fromCity, toCity: item.toCity, countSide: item.countSide,price:item.price,day:item.day,hour:item.hour,userId:userName }));
 
  };
  const deleteItem = (id) => {
    if (EditButtonId) {
      toast.error('Xatolik yuz berdi!');
    } else {
      dispatch(deleteRoutes({ id }))
        .unwrap()
        .then(() => {
          toast.success("Malumot muvaffaqiyatli o'chirildi!");
          dispatch(fetchRoutesByDriver(userName));
        })
        .catch(() => toast.error('Xatolik yuz berdi!'));
    }
  };
  return (
    <div style={{padding:"0 20px"}}>
      <h1 className='header_dr' >Har kuni O’zbekiston bo’ylab qatnovlar</h1>

     






      <div className="begin" >
        <div className="inputs">
        <div className="inputChild">
        
          <select onChange={e=>dispatch(setKoranCourse({...driverRout,fromCity:e.target.value}))} value={driverRout.fromCity}>
            <option value="" disabled>Joyni tanlang...</option>
           {
            fromCities.map((item)=>{
              return <option value={item.name}>{item.name}</option>
            })
            
           }

          </select>
        </div>
        <div className="inputChild">
          <p>Qayerga</p>
          <select onChange={e=>dispatch(setKoranCourse({...driverRout,toCity:e.target.value}))} value={driverRout.toCity}>
          <option value="" disabled>Joyni tanlang...</option>
          {
            toCities.map((item)=>{
              return <option value={item.name}>{item.name}</option>
            })
           }
          </select>
        </div>
        <div className="inputChild">
        <p>Qachon</p>
      <input
        type="date"
        placeholder="Qachon"
        className="form-control"
        min={minDate}
        max={maxDate} onChange={e=>dispatch(setKoranCourse({...driverRout,day:e.target.value})) } value={driverRout.day}
      />
        </div>
        <div className="inputChild">
        <p>Soat va minut</p>
      <input
        type="time" value={driverRout.hour}
        className="form-control" onChange={e=>dispatch(setKoranCourse({...driverRout,hour:e.target.value}))}
      />
        </div>
        <div className="inputChild">
          <p>Joylar Soni</p>
          <input type="number" placeholder='Joylar Soni' className='form-control' value={driverRout.countSide}
          onChange={e=>dispatch(setKoranCourse({...driverRout,countSide:e.target.value}))}
          />
        </div>
        <div className="inputChild">
          <p>Narxi</p>
          <input type="number" placeholder='Narxi' className='form-control'  value={driverRout.price}
          onChange={e=>dispatch(setKoranCourse({...driverRout,price:e.target.value}))}
          />
        </div>
      </div>
      <div className="buttonSave">
        <button className='saqlash' onClick={saveDepartment}>Saqlash</button>
      </div>
      </div>

      <table className='table table-hover' >
        <thead>
          <tr>
            <th>N#</th>
            <th>Qayerdan</th>
            <th>Qayerga</th>
            <th>Qachon</th>
            <th>Soat va Minut</th>
            <th>Joylar Soni </th>
            <th>Narxi</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {driverRoutes==null?[]:
            driverRoutes.map((item,index)=>{
              return <tr key={item.id}>
                <td>{index+1}</td>
                <td>{item.fromCity}</td>
                <td>{item.toCity}</td>
                <td>{item.day}</td>
                <td>{item.hour}</td>
                <td>{item.countSide}</td>
                <td>{item.price}</td>
                <td>
                  <button className='editButton' onClick={()=>EditItem(item)}>Tahrirlash</button>
                  <button className='deleteButton' onClick={()=>deleteItem(item.id)}>o'chirish</button>
                </td>
              </tr>
            })
          }
        </tbody>
      </table>
      <ToastContainer toastStyle={{
          backgroundColor: 'white',
          color: 'black',
        }} autoClose={1000} />
    </div>
  )
}

export default DriverLanding
