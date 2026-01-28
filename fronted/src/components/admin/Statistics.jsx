import React, { useEffect, useState } from 'react'
import "./routesAdmin.css"
import { useDispatch, useSelector } from 'react-redux';
import { countDriversAll, countUsersAll } from '../../redux/slices/DriverSlice';
function Statistics() {
    const dispatch = useDispatch();
    const { countUser,countDriver} = useSelector((state) => state.driver);

    useEffect(() => {
     dispatch(countDriversAll()) 
     dispatch(countUsersAll()) 
    
      }, []);
  return (
    <div className='begin1'>
      <div className="colorback">
          <div className='StaticsText'> <p>Haydovchilar Soni:{countDriver}</p></div>
        <div className='StaticsText1'> <p>Mijozlar Soni:{countUser}</p></div>
      </div>
      
    </div>
  )
}

export default Statistics
