import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { fetchRoutesByDriver } from '../../redux/slices/routeDriver';
import Landing from './Landing';
import { fetchDriverOne } from '../../redux/slices/DriverSlice';
import "./user.css"
function UserDiverOne() {
    const { userName } = useParams();
    const { driverOne} = useSelector((state) => state.driver);
    const dispatch=useDispatch()

    useEffect(() => {
       
       dispatch(fetchDriverOne(userName)) 
      
         }, []);
  return (
    <div>
        <Landing/>
     <div className="begin1">
        <div className='oneDriver' >
            {
                driverOne.map((item)=>{
                    return <li className='list-group-item'> 
                        <div>
                            <p>Mashina Rasmi:</p>
                            <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.driverImg}`} alt="#" />

                        </div>
                        <div>
                            <p>Ism Familiya:</p>
                            <p>{item.fullName}</p>

                        </div>
                        <div>
                            <p>Telefon Raqam:</p>
                            <p>{item.phoneNumber}</p>

                        </div>
                        <button className='saqlash3'>Band qilish</button>
                    </li>
                })
            }
        </div>
     </div>
    </div>
  )
}

export default UserDiverOne
