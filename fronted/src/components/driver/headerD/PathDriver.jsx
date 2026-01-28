import React, { useCallback, useEffect, useMemo, useState } from 'react';
import DriverHeader from './DriverHeader';
import './PathDriver.css';
import '../../user/user.css'
import { useSelector, useDispatch } from 'react-redux';
import { fetchToCity, setCurrentOptionType, setSelectedDate, setShowDateModal } from '../../../redux/slices/toCity';
import { fetchFromCity, setShowFromCityModal, setShowToCityModal, setTranslatedFromCities, setTranslatedToCities } from '../../../redux/slices/fromCity';
import b7 from "../../../pictures/b7.svg";
import b12 from "../../../pictures/b12.webp";
import calendar from "../../../pictures/calendar.webp";
import 'react-datepicker/dist/react-datepicker.css';
import { addRoute, deleteRoutes, deleteRoutesByDay, editRoute, fetchRoutesByDriver, setClearItem1, setEditButtonId, setKoranCourse } from '../../../redux/slices/routeDriver';
import b8 from "../../../pictures/b8.webp";
import { LanguageContext } from '../../language/LanguageContext';
import { useContext } from 'react';
import b9 from "../../../pictures/b9.webp";
import b11 from "../../../pictures/b11.webp";
import OptionModal from '../../user/OptionModal';
import DateModal from '../../user/DateModal';
import TimeSelectorModal from './TimeSelectorModal';
import { ImMan } from "react-icons/im";
import SeatSelectionModal from './SeatSelectionModal';
import { ToastContainer, toast } from 'react-toastify';
import DriverFooter from "./DriverFooter";
import apicall from '../../../apicall/apicall';
import { setUserName } from '../../../redux/slices/DriverSlice';
import { setMaxDate, setMinDate, setOpenModal, setOpenModal1, setSeatCount } from '../../../redux/slices/CommentSlice';
import { message } from 'antd';
function PathDriver() {
  const dispatch = useDispatch();
  const { driverRout, routesByDriver, EditButtonId,item1 } = useSelector((state) => state.routes);
  const { fromCities,translatedFromCities,translatedToCities,showFromCityModal,showToCityModal} = useSelector((state) => state.fromCity);
  const { toCities,showDateModal,selectedDate,currentOptionType } = useSelector((state) => state.toCity);
  const { userName } = useSelector((state) => state.driver);
  const { minDate,maxDate,openModal,seatCount,openModal1 } = useSelector((state) => state.comment);
  const { language } = useContext(LanguageContext);
  const [hours, setHours] = useState('');

  useEffect(() => {
    dispatch(fetchToCity());
    dispatch(fetchFromCity());
    dispatch(fetchRoutesByDriver(userName));
    getDriver();
    
    const today = new Date();
    const dayAfterTomorrow = new Date(today);
    dayAfterTomorrow.setDate(today.getDate() + 2);
    const formatDateForInput = (date) => date.toISOString().split('T')[0];
    dispatch(setMinDate(formatDateForInput(today)));
    dispatch(setMaxDate(formatDateForInput(dayAfterTomorrow)));
  
   

  }, [userName, dispatch]);


 const cityTranslations = useMemo(() => ({
    'Uzbekistan': {
      'Toshkent': 'Ташкент',
      'Samarqand': 'Самарканд',
      'Andijon': 'Андижан',
      'Buxoro': 'Бухара',
      "Farg'ona": 'Фергана',
      'Namangan': 'Наманган',
      "Qaraqalpog'iston": 'Каракалпакстан',
      'Surxondaryo': 'Сурхандарьинская',
      'Xorazm': 'Хорезм',
      'Jizzax': 'Джизак',
      'Qashqadaryo': 'Кашкадарья',
      'Sirdaryo': 'Сырдарья',
      'Navoiy': 'Навои',
      'Nukus': 'Нукус',
      'Urganch': 'Урганч',
      'Termiz': 'Термиз',
      'Denov': 'Денов',
      'Xiva': 'Хива',
      'Qarshi': 'Қарши',
      "Qo'qon": 'Қўқон',
    },
    'Russia': {
      'Ташкент': 'Toshkent',
      'Самарканд': 'Samarqand',
      'Андижан': 'Andijon',
      'Бухара': 'Buxoro',
      'Фергана': "Farg'ona",
      'Каракалпакстан': "Qaraqalpog'iston",
      'Наманган': 'Namangan',
      'Хорезм': 'Xorazm',
      'Джизак': 'Jizzax',
      'Кашкадарья': 'Qashqadaryo',
      'Сырдарья': 'Sirdaryo',
      'Навои': 'Navoiy',
      'Сурхандарьинская': 'Surxondaryo',
      'Нукус': 'Nukus',
      'Урганч': 'Urganch',
      'Термиз': 'Termiz',
      'Денов': 'Denov',
      'Хива': 'Xiva',
      "Қўқон": "Qo'qon",
      'Қарши': 'Qarshi',

    }
  }), []);
  const getDriver = useCallback(() => {
    apicall(`/auth/name`, "GET",null).then((res)=>{
    dispatch(setUserName(res.id))
      dispatch(setSeatCount(res.count))
      
    })
  }, [dispatch]);
  const { fromCity, toCity, day, countSide, hour } = driverRout;
  const isFormValid = fromCity && toCity && day && countSide && hour;

  const monthsUzbek = [
    "yanvar", "fevral", "mart", "aprel", "may", "iyun",
    "iyul", "avgust", "sentabr", "oktabr", "noyabr", "dekabr"
  ];

  const monthsRussian = [
    "января", "февраля", "марта", "апреля", "мая", "июня",
    "июля", "августа", "сентября", "октября", "ноября", "декабря"
  ];
      function delByDay(){
          dispatch(deleteRoutesByDay(item1 ))
            .unwrap()
            .then((res) => {
              toast.success(res.data);
              dispatch(fetchRoutesByDriver(userName));
            })
            // .catch((err) => toast.error("Xatolik yuz berdi!"));
        
        
      }

      
      const latinToCyrillic = (text) => {
        const map = {
            'Ch': 'Ч', 'ch': 'ч',
            'Sh': 'Ш', 'sh': 'ш',
            'A': 'А', 'B': 'Б', 'C': 'С', 'D': 'Д', 'E': 'Е', 'F': 'Ф', 'G': 'Г',
            'H': 'Ҳ', 'I': 'И', 'J': 'Ж', 'K': 'К', 'L': 'Л', 'M': 'М', 'N': 'Н',
            'O': 'О', 'P': 'П', 'Q': 'Қ', 'R': 'Р', 'S': 'С', 'T': 'Т', 'U': 'У',
            'V': 'В', 'X': 'Х', 'Y': 'Й', 'Z': 'З',
            'a': 'а', 'b': 'б', 'c': 'с', 'd': 'д', 'e': 'е', 'f': 'ф', 'g': 'г',
            'h': 'ҳ', 'i': 'и', 'j': 'ж', 'k': 'к', 'l': 'л', 'm': 'м', 'n': 'н',
            'o': 'о', 'p': 'п', 'q': 'қ', 'r': 'р', 's': 'с', 't': 'т', 'u': 'у',
            'v': 'в', 'x': 'х', 'y': 'й', 'z': 'з'
        };
    
        let result = text;
    
        result = result.replace(/Ch/g, map['Ch']).replace(/ch/g, map['ch'])
                       .replace(/Sh/g, map['Sh']).replace(/sh/g, map['sh']);
    
        result = result.split('').map(char => map[char] || char).join('');
    
        return result;
    };
    
    
    const cyrillicToLatin = (text) => {
      const cyrillicToLatinMap = {
          А: 'A',  а: 'a',
          Б: 'B',  б: 'b',
          В: 'V',  в: 'v',
          Г: 'G',  г: 'g',
          Д: 'D',  д: 'd',
          Е: 'E',  е: 'e',
          Ё: 'Yo', ё: 'yo',
          Ж: 'J',  ж: 'j',
          З: 'Z',  з: 'z',
          И: 'I',  и: 'i',
          Й: 'Y',  й: 'y',
          К: 'K',  к: 'k',
          Л: 'L',  л: 'l',
          М: 'M',  м: 'm',
          Н: 'N',  н: 'n',
          О: 'O',  о: 'o',
          П: 'P',  п: 'p',
          Р: 'R',  р: 'r',
          С: 'S',  с: 's',
          Т: 'T',  т: 't',
          У: 'U',  у: 'u',
          Ф: 'F',  ф: 'f',
          Х: 'X',  х: 'x',
          Ц: 'S',  ц: 's',
          Ч: 'Ch', ч: 'ch',
          Ш: 'Sh', ш: 'sh',
          Щ: 'Sh', щ: 'sh',
          Ъ: '',   ъ: '',
          Ы: 'Y',  ы: 'y',
          Ь: '',   ь: '',
          Э: 'E',  э: 'e',
          Ю: 'Yu', ю: 'yu',
          Я: 'Ya', я: 'ya',
          Ҳ: 'H',  ҳ: 'h'
      };
  
      return text.split('').map(char => cyrillicToLatinMap[char] || char).join('');
  };
  
  
  
  const translateFullName = useCallback((name) => {
      if (language === '1') {
          return cyrillicToLatin(name);
      } else if (language === '2') {
          return latinToCyrillic(name);
      }
      return name;
    }, [language]);

      
    const translateCity = useCallback((cityName) => {
    if (language === '2') {
      return cityTranslations['Uzbekistan'][cityName] || cityName;
    }
    return cityName;
  }, [language, cityTranslations]);

  const translateCity1 = useCallback((cityName) => {
    if (language === '2') {
      return cityTranslations['Russia'][cityName] || cityName;
    }
    return cityName;
  }, [language, cityTranslations]);

  const formatDate = useCallback((dateString) => {

    const date = new Date(dateString);
    const day = date.getDate();
    const monthIndex = date.getMonth();
    const month = language === '2' ? monthsRussian[monthIndex] : monthsUzbek[monthIndex];
    return `${day}-${month}`;
  }, [language]);

  const handleDateSelect = useCallback((date) => {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    const formattedDate = date.toLocaleDateString('en-CA', options);
    dispatch(setSelectedDate(formattedDate));
    dispatch(setKoranCourse({ ...driverRout, day: formattedDate }))
  }, [dispatch, driverRout]);
  useEffect(() => {
    dispatch(setTranslatedFromCities(fromCities.map(city => ({
      ...city,
      name: translateCity(city.name)
    }))));
    dispatch(setTranslatedToCities(toCities.map(city => ({
      ...city,
      name: translateCity(city.name)
    }))));
  }, [fromCities, toCities, language]);


  const handleOptionSelect = useCallback((option) => {
    if (currentOptionType === 'from') {
      dispatch(setKoranCourse({ ...driverRout, fromCity: translateCity1(option) }));
      dispatch(setShowFromCityModal(false));
    } else if (currentOptionType === 'to') {
      dispatch(setKoranCourse({ ...driverRout, toCity: translateCity1(option) }));
      dispatch(setShowToCityModal(false));
    }
  }, [currentOptionType, dispatch, driverRout, translateCity1]);
  const openModal3 = () => {
    dispatch(setOpenModal(true));
    // delByDay()
  };
  const openModal4 = () => {
    dispatch(setOpenModal1(true));
  };

  const handleTimeSelect = useCallback((selectedTime) => {
    const formattedHour = selectedTime.hour.toString().padStart(2, '0');
    const formattedMinute = selectedTime.minute.toString().padStart(2, '0');
    const formattedTime = `${formattedHour}:${formattedMinute}`;
    dispatch(setKoranCourse({ ...driverRout, hour: formattedTime }));
    dispatch(setOpenModal(false));
    dispatch(setSeatCount(null))
  }, [dispatch, driverRout]);

  const handleSeatSelect = (seatCount) => {
    dispatch(setKoranCourse({ ...driverRout, countSide: seatCount }));
    dispatch(setOpenModal1(false));
  };
  const saveDepartment = useCallback(() => {
    const today = new Date();
    const dayAfterTomorrow = new Date(today);
    dayAfterTomorrow.setDate(today.getDate() + 2);
  
    const formatDateForInput = (date) => {
      if (!(date instanceof Date)) {
        console.error("Invalid date:", date);
        return '';
      }
      return date.toISOString().split('T')[0];
    };
  
    const getMinuteToday = today.getHours() * 60 + today.getMinutes();
    const getMinuteHours = driverRout.hour.split(':').map(Number)[0] * 60 + driverRout.hour.split(':').map(Number)[1];
  
    if (!driverRout.fromCity || !driverRout.toCity || !driverRout.countSide || !driverRout.day || !driverRout.hour || !driverRout.price) {
      message.error("Iltimos bo'sh maydonlarni to'ldiring!");
    } else {
      const formattedToday = formatDateForInput(today);
      const formattedDay = new Date(driverRout.day); // Convert driverRout.day to Date if it's a string
  
  
      if (formatDateForInput(today) === formatDateForInput(formattedDay) && getMinuteToday >= getMinuteHours) {
        message.error("Agar bugungi kunni tanlagan bo'lsangiz vaqtni hozirgi vaqtdan keyinroq qo'ying!");
      } else {
        if (EditButtonId) {
          dispatch(editRoute({ EditButtonId, driverRout }))
            .unwrap()
            .then(() => {
              message.success('Malumot muvaffaqiyatli tahrirlandi!');
              dispatch(fetchRoutesByDriver(userName));
            })
            .catch((err) => message.error("Xatolik yuz berdi!"));
        } else {
          if (routesByDriver.length >= 1) {
            message.error("Siz ayni damda faqat 1 ta yo'nalishda ishlay olasiz!");
          } else {
            dispatch(addRoute({ driverRout, userName }))
              .unwrap()
              .then(() => {
                message.success("Malumot muvaffaqiyatli qo'shildi!");
                dispatch(fetchRoutesByDriver(userName));
              })
              .catch((err) => message.error("Xatolik yuz berdi!"));
          }
        }
      }
    }
  
    dispatch(setSelectedDate(""));
    dispatch(setEditButtonId(null));
    dispatch(setKoranCourse({ fromCity: '', toCity: '', countSide: '', price: "", day: "", hour: "", userId: "" }));
  }, [dispatch, driverRout]);
  
  const deleteItem = (id) => {
    if (EditButtonId) {
      message.error("Siz tahrirlab turgan vaqt o'chira olmaysiz!");
    } else {
      dispatch(deleteRoutes({ id }))
        .unwrap()
        .then(() => {
          dispatch(fetchRoutesByDriver(userName));
          message.success("Malumot muvaffaqiyatli o'chirildi!");
        })
        .catch((err) =>{
          message.error('Xatolik yuz berdi!')
          dispatch(fetchRoutesByDriver(userName));
        } )
    }
  };
  const [className, setClassName] = useState('inputs');

  useEffect(() => {
    const updateClassName = () => {
      if (window.innerWidth <= 720) {
        setClassName('inputs');
      } else if (window.innerWidth > 720 && window.innerWidth <= 1300) {
        setClassName('inputs11');
      } else if (window.innerWidth > 1300 && window.innerWidth <= 1920) {
        setClassName('inputs');
      } else {
        setClassName('inputs');
      }
    };

    updateClassName();

    window.addEventListener('resize', updateClassName);

    return () => {
      window.removeEventListener('resize', updateClassName);
    };
  }, []);
  const EditItem = (item) => {
    
    const specificGroupArea = document.getElementById('specificGroupArea');
    specificGroupArea.scrollIntoView({ behavior: 'smooth' });
    dispatch(setEditButtonId(item.id));
    dispatch(setKoranCourse({ fromCity: item.fromCity, toCity: item.toCity, countSide: item.countSide, price: item.price, day: item.day, hour: item.hour, userId: userName }));
    dispatch(setSelectedDate(item.day))
   
  };
  return (
    <div>
      <DriverHeader />
      <div className='path_dr_con'>
        <div className="path_dr_con2">

          <div className="h1RouterUser">
            <h1 id="specificGroupArea" className={"header_dr"}>{language==="1"?"Har kuni O’zbekiston bo’ylab qatnovlar":"Ежедневные поездки по Узбекистану"}</h1>
          </div>

          <div className={className} >
            <div className="inputChild">
              <button
                type="button"
                className="btnCity1"
                onClick={() => {
                  dispatch(setCurrentOptionType('from'));
                  dispatch(setShowFromCityModal(true));
                }}
              >
                {driverRout.fromCity ? (
                  <span style={{ color: '#303383', fontWeight: "600", letterSpacing: "1.5px" }}>{translateCity(driverRout.fromCity)}</span>
                ) : (
                  <span style={{ fontWeight: 'normal' }}>
                    {language === "1" ? "Qayerdan  " : "Откуда"}
                  </span>
                )}
                <img src={b7} className='icons'  alt="image" />
              </button>
            </div>

            <div className="inputChild">
              <button
                type="button"
                className="btnCity1"
                onClick={() => {
                  dispatch(setCurrentOptionType('to'));
                  dispatch(setShowToCityModal(true));
                }}
                disabled={!driverRout.fromCity}
                style={{
                  opacity: !driverRout.fromCity ? 0.5 : 1,
                  cursor: !driverRout.fromCity ? 'not-allowed' : 'pointer'
                }}
              >
                {driverRout.toCity ? (
                  <span style={{ color: '#303383', fontWeight: "600", letterSpacing: "1.5px" }}>{translateCity(driverRout.toCity)}</span>
                ) : (
                  <span style={{ fontWeight: 'normal' }}>
                    {language === "1" ? "Qayerga" : "Куда"}
                  </span>
                )}

                <img src={b7} className='icons'  alt="image" />
              </button>
            </div>

            <div className="inputChild">
              <button
                type="button"
                className="btnCity1"
                value={formatDate(driverRout.day)}
                onClick={() => dispatch(setShowDateModal(true))}
                disabled={!driverRout.toCity}
                style={{
                  opacity: !driverRout.toCity ? 0.5 : 1,
                  cursor: !driverRout.toCity ? 'not-allowed' : 'pointer'
                }}
              >

                {selectedDate ? (
                  <span style={{ color: '#303383', fontWeight: "600", letterSpacing: "1.5px" }}>{formatDate(selectedDate)}</span>
                ) : (
                  <span style={{ fontWeight: 'normal' }}>
                    {language === "1" ? "Qachon" : "Когда"}
                  </span>
                )}
                <img src={calendar} className='icons'  alt="image" />
              </button>
            </div>
            <div className="inputChild">
              <button
                type="button"
                className="btnCity1"
                onClick={openModal3}


              >
                {driverRout.hour ? (
                  <span style={{ color: '#303383', fontWeight: "600", letterSpacing: "1.5px" }}>{driverRout.hour}</span>
                ) : (
                  <span style={{ fontWeight: 'normal' }}>
                    {language === "1" ? "Vaqt" : "Время"}
                  </span>
                )}
                <img src={b12} className='icons' alt="image" />
              </button>
            </div>
            <div className="inputChild">
              <button
                type="button"
                className="btnCity1"
                onClick={openModal4}


              >
                {driverRout.countSide ? (
                  <span style={{ color: '#303383', fontWeight: "600", letterSpacing: "1.5px" }}>{driverRout.countSide} ta </span>
                ) : (
                  <span style={{ fontWeight: 'normal' }}>
                    {language === "1" ? "O'rindiq" : "Сиденье"}
                  </span>
                )}
                <ImMan className='icons' />
              </button>
            </div>
            <div className="inputChild">
              <input type='number'
                value={driverRout.price}
                className="btnCity1"
                placeholder={language === "1" ? `Narxi` : "Цена"}
                onChange={(e) => dispatch(setKoranCourse({ ...driverRout, price: e.target.value }))}
              />


            </div>

            <div className="inputChild">
              <button
                className='qidirish1'
                onClick={saveDepartment}

              >
                {language === "1" ? "Saqlash" : "Сохранять"}
              </button>
            </div>
          </div>

          <div className=""  >
            {routesByDriver.map((item) => (
              <div className="mapRoutes" key={item.id} style={{ backgroundColor: "white" }} >
                <li className='list-group-item li1'><div className="l1Child1"><p> {formatDate(item.day)}</p> <p>{item.hour}</p></div> <div className="li1Child2"> <p>{translateCity(item.fromCity)}</p> <img src={b8}  alt="image"/> <p>{translateFullName(item.toCity)}</p></div> <div className="li1Child3"><p>{item.price} {language === "1" ? "So’m" : "Сум"} </p></div></li>
                <li className="list-group-item li2">
                  <div className="l2Child1">
                    {Array.from({ length: Math.min(item.countSide, 6) }).map((_, index) => (
                      <img key={index} src={b9} alt="image" />
                    ))}
                  </div>
                  <div className="li2Child2">
                    <p>{language === "1" ? " Bo'sh O'rindiqlar soni:" : "Количество свободных мест:"}</p>
                    <h6>{item.countSide}</h6>
                  </div>
                  <div className="li2Child3">
                    <h6>{language === "1" ? "Mashina rusumi" : "Модель автомобиля"}</h6>
                    <p>{translateFullName(item.user.carType)}</p>
                  </div>
                </li>


                <li className='list-group-item li3'><div className="l3Child1"><h3>{translateFullName(item.user.fullName)}</h3><div className="liDriverPhone"><img src={b11}  alt="image" /> <p>{item.user.phoneNumber}</p></div> </div><div className="li3Child3"><button onClick={() => EditItem(item)}>  {language === "1" ? "Tahirlash" : "Бронирование"}</button> <button onClick={() => deleteItem(item.id)}>{language === "1" ? "O'chirish" : "Выключать"}</button>  <p className='carType'>  {item.user.carType}</p></div></li>
              </div>
            ))}
          </div>



        </div>
      </div>
      {showFromCityModal && (
        <OptionModal
          options={translatedFromCities}
          onSelect={handleOptionSelect}
          onClose={() => dispatch(setShowFromCityModal(false))}
        />
      )}
      {showToCityModal && (
        <OptionModal
          options={translatedToCities}
          onSelect={handleOptionSelect}
          onClose={() => dispatch(setShowToCityModal(false))}
        />
      )}
      {showDateModal && (
        <DateModal
          minDate={minDate}
          maxDate={maxDate}
          onDateSelect={handleDateSelect}
          onClose={() => dispatch(setShowDateModal(false))}
        />
      )}
      {openModal && (
        <TimeSelectorModal
          options={hours}
          onSelect={handleTimeSelect}
          onClose={() => dispatch(setOpenModal(false))}
        />
      )}
      {openModal1 && (
        <SeatSelectionModal
          onSelect={handleSeatSelect}
          isTrue={seatCount}
          onClose={() => dispatch(setOpenModal1(false))}
        />
      )}

      <ToastContainer toastStyle={{
        backgroundColor: 'white',
        color: 'black',
      }} autoClose={1000} />
      <DriverFooter/>
    </div>
  );
}

export default PathDriver;
