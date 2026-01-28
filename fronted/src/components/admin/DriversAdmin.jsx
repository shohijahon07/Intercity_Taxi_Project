import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { deleteDriver, fetchDrivers1, fetchDriversByFullName } from '../../redux/slices/DriverSlice';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { FaSearch } from "react-icons/fa";
import "./routesAdmin.css";
import { message } from 'antd';

function DriversAdmin() {
  const { drivers1  } = useSelector((state) => state.driver); 
  const dispatch = useDispatch();
  const [isDriver, setIsDriver] = useState(true);
  const [selectedImage, setSelectedImage] = useState(null);
  useEffect(() => {
    dispatch(fetchDrivers1(isDriver));
  }, [dispatch, isDriver]);

  const navigate = useNavigate();

  function goAboutDrivers(userName) {
    navigate(`/bir_haydovchi/${userName}`);
  }

  function getByName(name) {
    dispatch(fetchDriversByFullName(name))
      .unwrap()
      .then(() => {
      })
      .catch(() => {
        message.error('Ma\'lumotlarni olishda xatolik yuz berdi!');
      });
  }

  function deleteDrive(id) {
    dispatch(deleteDriver({ id }))
      .unwrap()
      .then(() => {
        message.success("Malumot muvaffaqiyatli o'chirildi!");
        dispatch(fetchDrivers1(isDriver));
      })
      .catch((err) => console.log(err));
  }
  function openModal(imgSrc) {
    setSelectedImage(imgSrc); // Rasmingizni modalga joylashtirish
  }

  function closeModal() {
    setSelectedImage(null); // Modalni yopish
  }

  return (
    <div style={{paddingBottom:"30px"}}>
      <div className="qidirishHeader">
        <div className="search-input-wrapper">
          <input
            type="text"
            className="form-control"
            placeholder="Qidirish"
            onChange={(e) => getByName(e.target.value)}
          />
          <FaSearch className="search-icon" />
        </div>
      </div>

      <table className="table table-success">
        <thead>
          <tr>
            <th>N#</th>
            <th>Ism Familiya</th>
            <th>Telefon Raqam</th>
            <th>Mashina Rusumi</th>
            <th>Mashina Rasmi</th>
            <th>Haydovchilik Guvohnomasi</th>
            <th>Mashina Texpasporti</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {
            drivers1.length > 0 ? (
              drivers1.map((item, i) => (
                <tr key={item.id}>
                  <td>{i + 1}</td>
                  <td>{item.fullName}</td>
                  <td>{item.phoneNumber}</td>
                  <td>{item.carType}</td>
                  <td>
                    <img
                      className="imageTable"
                      style={{ objectFit: "cover" }}
                      src={`https:/api/fileController/photo?img=${item.carImg}`}
                      alt="image"
                      onClick={() => openModal(`https:/api/fileController/photo?img=${item.carImg}`)}
                    />
                  </td>
                  <td>
                    <img
                      className="imageTable"
                      style={{ objectFit: "cover" }}
                      src={`https:/api/fileController/photo?img=${item.driverImg}`}
                      alt="image"
                      onClick={() => openModal(`https:/api/fileController/photo?img=${item.driverImg}`)}
                    />
                  </td>
                  <td>
                    <img
                      className="imageTable"
                      style={{ objectFit: "cover" }}
                      src={`https:/api/fileController/photo?img=${item.cardDocument}`}
                      alt="image"
                      onClick={() => openModal(`https:/api/fileController/photo?img=${item.cardDocument}`)}
                    />
                  </td>
                  <td>
                    <button
                      className="saveButton"
                      onClick={() => goAboutDrivers(item.id)}
                    >
                      Haqida
                    </button>
                    <button
                      className="deleteBTN"
                      onClick={() => deleteDrive(item.id)}
                    >
                      O'chirish
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8">No drivers found</td>
              </tr>
            )
          }
        </tbody>
      </table>
      {selectedImage && (
        <div className="modal12">
          <span className="close12" onClick={closeModal}>&times;</span>
          <img className="modal-content12" src={selectedImage} alt="Selected" />
        </div>
      )}
      <ToastContainer
        toastStyle={{
          backgroundColor: 'white',
          color: 'black',
        }}
        autoClose={1000}
      />
    </div>
  );
}

export default DriversAdmin;
