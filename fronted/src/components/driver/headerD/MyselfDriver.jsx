import React, { useContext, useEffect, useRef, useState } from 'react';
import DriverHeader from './DriverHeader';
import "../headerD/Myselft.css";
import { addDriverAbout, editDriver, fetchDriverOne, setAbout, setDriver, setEditButtonId, setImg, setImg1, setImg2, setIsEditing, setSelectedFile, setSelectedFile1, setSelectedFile2, setUserName } from '../../../redux/slices/DriverSlice';
import { useSelector } from 'react-redux';
import { useDispatch } from 'react-redux';
import { LanguageContext } from '../../language/LanguageContext';
import { toast } from 'react-toastify';
import DriverFooter from './DriverFooter';
import fileicon from "../../../pictures/fileicon.svg";
import "../../auth/Register.css";
import edit from "../../../pictures/edit.svg"
import apicall from '../../../apicall/apicall';
function MyselfDriver() {
  const dispatch = useDispatch();
  const { EditButtonId, driverObject, driverOne, selectedFile, selectedFile1, selectedFile2, img, img1, img2, isEditing,userName } = useSelector((state) => state.driver);
  const fileInputRef1 = useRef(null);
  const fileInputRef2 = useRef(null);
  const fileInputRef3 = useRef(null);
  const { language } = useContext(LanguageContext);

  useEffect(() => {
    getDriver();
    dispatch(fetchDriverOne(userName));
  }, [userName]);


  function getDriver() {
    apicall(`/auth/name`, "GET",null).then((res)=>{
    dispatch(setUserName(res.id))
    })

  }

  const saveDepartment = () => {
    dispatch(editDriver({ EditButtonId, driverObject, img, img1, img2, selectedFile, selectedFile1, selectedFile2 }))
      .unwrap()
      .then(() => {
        toast.success('Malumot muvaffaqiyatli tahrirlandi!');
        dispatch(fetchDriverOne(userName));
      })
      .catch((err) => console.log(err));

    dispatch(setEditButtonId(null));
    dispatch(setDriver({ fullName: '', phoneNumber: '', carType: "", carImg: "", driverImg: "", cardDocument: "", about: "" }));
    if (fileInputRef1.current) fileInputRef1.current.value = '';
    if (fileInputRef2.current) fileInputRef2.current.value = '';
    if (fileInputRef3.current) fileInputRef3.current.value = '';

    dispatch(setIsEditing(false)); // Reset editing mode
  };

  const EditItem = (item) => {
    dispatch(setEditButtonId(item.id));
    dispatch(setImg(item.carImg));
    dispatch(setImg1(item.driverImg));
    dispatch(setImg2(item.cardDocument));
    dispatch(setDriver({
      fullName: item.fullName, phoneNumber: item.phoneNumber,
      carType: item.carType, carImg: item.carImg,
      driverImg: item.driverImg, cardDocument: item.cardDocument, about: item.about
    }));

    dispatch(setIsEditing(true)); // Enable editing mode
  };

  return (
    <div>
      <DriverHeader />
      <div className="container_myself">
        {driverOne.map((item, i) => (
          <div key={i} className="body_myself_img">
            <h1 className='about_text'> {language==="1"?"O'zim haqimda":"О себе"}  </h1>
            <div className='about_div'>
              {EditButtonId ?
                <input type="text" className='form-control w-100 h-100' value={driverObject.about}
                  onChange={(e) => dispatch(setDriver({ ...driverObject, about: e.target.value }))}
                /> :
                <div className={"editFather"}>
                  <h1>{item.about}</h1>
                    <img src={edit} className='btnedit' onClick={() => EditItem(item)} alt="" />
                </div>
               
              }
            </div>
            <div className="box">
              <div className="body2_center">
              <div className='about_div2'>
                <div className="box_body">
                {EditButtonId ?
                <input type="text" className='form-control w-100 h-100' value={driverObject.about}
                  onChange={(e) => dispatch(setDriver({ ...driverObject, about: e.target.value }))}
                /> :
                <div className={"editFather"}>
                  <h1>{item.about}</h1>
                    <img src={edit} className='btnedit' onClick={() => EditItem(item)} alt="" />
                </div>
               
              }

                </div>
                          </div>
                <div className='box_body'>
                  {EditButtonId ?
                    <input type="text" className='form-control w-100 h-100' value={driverObject.fullName}
                      onChange={(e) => dispatch(setDriver({ ...driverObject, fullName: e.target.value }))}
                    /> : 
                    <div className="editFather">
                    <h1>{item.fullName} </h1>

                    <img src={edit} className='btnedit' onClick={() => EditItem(item)} alt="" />

                    </div>
                  }
                </div>
                <div className='box_body'>
                  {EditButtonId ?
                    <input type="text" className='form-control w-100 h-100' value={driverObject.phoneNumber}
                      onChange={(e) => dispatch(setDriver({ ...driverObject, phoneNumber: e.target.value }))}
                    /> : <div className="editFather">
                    <h1>{item.phoneNumber}</h1>


                    <img src={edit} className='btnedit' onClick={() => EditItem(item)} alt="" />

                    </div>
                    
                  }
                </div>
                <div className='box_body'>
                  {EditButtonId ?
                    <input type="text" className='form-control w-100 h-100' value={driverObject.carType}
                      onChange={(e) => dispatch(setDriver({ ...driverObject, carType: e.target.value }))}
                    /> : <div className="editFather">
                    <h1>{item.carType}</h1>


                    <img src={edit} className='btnedit' onClick={() => EditItem(item)} alt="" />

                    </div>
                    
                  }
                </div>
              </div>
              {/* com ucun rasm yuklash */}
              <div className="body3">
                <div className='box_body'>
                  {EditButtonId ?
                     <div className="custom-file-upload2">
                     <input
                       type="file"
                       id="file1" accept="image/*"  ref={fileInputRef1} onChange={(e) => dispatch(setSelectedFile(e.target.files[0]))} 
                     />
                     <label htmlFor="file1">
                       <img src={fileicon} alt="Upload Icon" className="upload-icon2"  />
                       
           {language==="1"?"Mashinani rasmini yuklash":"Загрузите фотографию автомобиля"}
                     </label>
                   </div>
                    :
                    <div  >
                      
                           <h1>
                            {language==="1"?"Mashinani rasmi":
                            "Загрузите фотографию автомобиля"}
                            

                           </h1>
                           <img style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.carImg}`} alt="#" />

                    </div>
                  }
                </div>
                <div className='box_body'>
                  {EditButtonId ?
                   <div className="custom-file-upload2">
                   <input
                     type="file"
                     id="file2" accept="image/*" ref={fileInputRef2} onChange={(e) => dispatch(setSelectedFile1(e.target.files[0]))} 
                   />
                   <label htmlFor="file2">
                     <img src={fileicon} alt="Upload Icon" className="upload-icon2"  />
                     {language==="1"?"Haydovchilik guvohnomasi yuklash":"Скачать водительское удостоверение"}

                     
         
                   </label>
                 </div>
                 
                 : <div> <h1>

{language==="1"?"Haydovchilik guvohnoma":"Водительское удостоверение"}
                 </h1>
                 <img style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.driverImg}`} alt="#" />
               </div>
               }
                </div>
                <div className='box_body' >
                  {EditButtonId ?
                  
                  <div className="custom-file-upload2">
                  <input
                    type="file"
                    id="file3" 
                    accept="image/*" ref={fileInputRef3} onChange={(e) => dispatch(setSelectedFile2(e.target.files[0]))} 
                  />
                  <label htmlFor="file3">
                    <img src={fileicon} alt="Upload Icon" className="upload-icon2"  />
                    {language==="1"?"Texpasport rasmi":"Фотография паспорта Техаса"}
                    
        
                  </label>
                </div>

                    : <div>
                  <h1>
                  {language==="1"?"Texpasport rasmi":"Фотография паспорта Техаса"}
                    
                    </h1>
                  <img style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.cardDocument}`} alt="#" />

                    </div>
                    
                    
                  }
                </div>
                <div className="box_btn">
                  {isEditing ?
                     <button onClick={saveDepartment}>  {language==="1"?"Saqlash":"Сохранять"} </button> :
                    <button onClick={() => EditItem(item)}> {language==="1"?"Tahrirlash":"Редактирование"} </button>
                  }
                </div>
              </div>
{/* telefon ucun rasmni inputi */}
 <div className="body2_center">
                <div className='inp_one'>
                  {EditButtonId ?
                     <div className="custom-file-upload2">
                     <input
                       type="file"
                       id="file1" accept="image/*"  ref={fileInputRef1} onChange={(e) => dispatch(setSelectedFile(e.target.files[0]))} 
                     />
                     <label htmlFor="file1">
                       <img src={fileicon} alt="Upload Icon" className="upload-icon2"  />
                    
           {language==="1" ? "Mashinani rasmini yuklash":"Загрузите фотографию автомобиля"}
                     </label>
                   </div>
                    :
                    <div className={"inp_child"} >
                           <img style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.carImg}`} alt="#" />
          
                           <h1>
                            {language==="1"?"Mashinani rasmi":"Изображение автомобиля"}
                           </h1>
                           
                           <img src={edit} className='btnedit2' onClick={() => EditItem(item)} alt="" />

                    </div>
                  }
                </div>
                <div className='inp_one'>
                  {EditButtonId ?
                   <div className="custom-file-upload2">
                   <input
                     type="file"
                     id="file2" accept="image/*" ref={fileInputRef2} onChange={(e) => dispatch(setSelectedFile1(e.target.files[0]))} 
                   />
                   <label htmlFor="file2">
                     <img src={fileicon} alt="Upload Icon" className="upload-icon2"  />
                     
         {language==="1"?"Haydovchilik guvohnomasi yuklash":"Скачать водительское удостоверение"}
                   </label>
                 </div>
                 
                 : <div className='inp_child'> 
                 <img style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.driverImg}`} alt="#" />
                 
                 <h1>
                  {language==="1"?"Haydovchilik guvohnomasi":"Водительское удостоверение"}
                 </h1>
                 <img src={edit} className='btnedit2' onClick={() => EditItem(item)} alt="" />

</div>
               }
                </div>
                <div className='inp_one' >
                  {EditButtonId ?
                  
                  <div className="custom-file-upload2">
                  <input
                    type="file"
                    id="file3" 
                    accept="image/*" ref={fileInputRef3} onChange={(e) => dispatch(setSelectedFile2(e.target.files[0]))} 
                  />
                  <label htmlFor="file3">
                    <img src={fileicon} alt="Upload Icon" className="upload-icon2"  />
                    
                    {language==="1"?"Texxpasport rasmi":"Фотография паспорта Техаса"}
        
                  </label>
                </div>

                    : <div className='inp_child'>
                  <img style={{ objectFit: "cover" }} src={`https:/api/fileController/photo?img=${item.cardDocument}`} alt="#" />

                  <h1>
                  {language==="1"?"Texxpasport rasmi":"Фотография паспорта Техаса"}

                  </h1>
                  <img src={edit} className='btnedit2' onClick={() => EditItem(item)} alt="" />

                    </div>
                    
                    
                  }
                </div>
                <div className="box_btn">
                  {isEditing ?
                     <button onClick={saveDepartment}>  {language==="1"?"Saqlash":"Сохранять"} </button> :
                    <button onClick={() => EditItem(item)}>{language==="1"?"Tahrirlash":"Редактирование"} </button>
                  }
                </div>
             </div> 

              {/* pastga ishlaydigan button  */}
              <div className="box_btn2">  
                  {isEditing ?
                     <button onClick={saveDepartment}>{language==="1"?"Saqlash":"Сохранять"}</button> :
                    <button onClick={() => EditItem(item)}>{language==="1"?"Tahrirlash":"Редактирование"}</button>
                  }
                </div>
                {/* edit telefon */}
                <div className="box_btn3">  
                  {/* {isEditing ? */}
                     <button onClick={saveDepartment}>{language==="1"?"Saqlash":"Сохранять"}</button> :
                    {/* <button onClick={() => EditItem(item)}>Tahrirlash</button>
                   } */}
                </div>
            </div>
          </div>
        ))}
      </div>
      <DriverFooter/>
    </div>
  );
}

export default MyselfDriver;
