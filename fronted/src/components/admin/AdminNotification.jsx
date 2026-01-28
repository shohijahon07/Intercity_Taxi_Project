import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { deleteDriver, editDriverIsDriving, fetchDrivers } from '../../redux/slices/DriverSlice';
import { FaCheck } from "react-icons/fa6";
import { ToastContainer, toast } from 'react-toastify';
import { message } from 'antd';

function AdminNotification() {
  const dispatch = useDispatch();
  const { driverIsdriving } = useSelector((state) => state.driver);
  const { drivers = [] } = useSelector((state) => state.driver); // Ensure drivers defaults to an empty array
  const [isDriver, setIsDriver] = useState(false);

  useEffect(() => {
    dispatch(fetchDrivers(isDriver));
  }, [dispatch, isDriver]);

  function changeIsDriving(id) {
    const min = 1000;
    const max = 9999;
    const randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
    dispatch(editDriverIsDriving({ id, driverIsdriving, randomNum }))
      .unwrap()
      .then(() => {
        message.success('Malumot muvaffaqiyatli tahrirlandi!');
        dispatch(fetchDrivers(isDriver));
      })
      .catch((err) => console.log(err));
  }

  function deleteDrive(id) {
    dispatch(deleteDriver({ id }))
      .unwrap()
      .then(() => {
        message.success("Malumot muvaffaqiyatli o'chirildi!");
        dispatch(fetchDrivers(isDriver));
      })
      .catch((err) => console.log(err));
  }

  return (
    <div>
      <table className='table table-bordered'>
        <thead>
          <tr>
            <th>N#</th>
            <th>Mashina Rasmi</th>
            <th>Haydovchilik Guvohnomasi</th>
            <th>Mashina Texpasporti</th>
            <th>Ism Familiya</th>
            <th>Telefon Raqam</th>
            <th>Mashina Rusumi</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {
            drivers.length > 0 ? drivers.map((item, i) => (
              <tr key={item.id}>
                <td>{i + 1}</td>
                <td>
                  <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.carImg}`} alt="image" />
                </td>
                <td>
                  <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.driverImg}`} alt="image" />
                </td>
                <td>
                  <img className="imageTable" style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.cardDocument}`} alt="image" />
                </td>
                <td>{item.fullName}</td>
                <td>{item.phoneNumber}</td>
                <td>{item.carType}</td>
                <td>
                  <button className='saveButton' onClick={() => changeIsDriving(item.id)}> <FaCheck style={{ border: "none" }} size={"25px"} /></button>
                  <button className='deleteBTN' onClick={() => deleteDrive(item.id)}>O'chirish</button>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="8">No drivers available</td>
              </tr>
            )
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

export default AdminNotification;
