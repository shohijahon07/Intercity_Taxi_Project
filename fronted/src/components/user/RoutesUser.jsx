import React, { useCallback, useContext, useEffect, useMemo, useState } from 'react';
import Landing from './Landing';
import { useDispatch, useSelector } from 'react-redux';
import { fetchToCity, setCurrentOptionType, setIsModalOpen, setIsModalOpen1, setSelectedDate, setShowDateModal } from '../../redux/slices/toCity';
import { fetchFromCity, setShowFromCityModal, setShowToCityModal, setTranslatedFromCities, setTranslatedToCities } from '../../redux/slices/fromCity';
import { deleteRoutesByTime, fetchRoutes, fetchRoutesByDate, fetchRoutesByDay, setDay12, setKoranCourse } from '../../redux/slices/routeDriver';
import "./user.css";
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { LanguageContext } from '../language/LanguageContext';
import DateModal from './DateModal';
import OptionModal from './OptionModal'; // Import the OptionModal
import calendar from "../../pictures/calendar.webp";
import Boglanish from './Boglanish';
import BizHaqimizda from './BizHaqimizda';
import Footer from './Footer';
import b7 from "../../pictures/b7.svg";
import b8 from "../../pictures/b8.webp";
import b9 from "../../pictures/b9.webp";
import b11 from "../../pictures/b11.webp";
import b14 from "../../pictures/b18.webp";
import Boglanish2 from './Boglanish2';
import BizHaqimizda2 from './bizHaqimizda/BizHaqimizda2';
import Izohlar from './izohlar/Izohlar';
import Band_qilish from './bandQilish/Band_qilish';
import { setChatId, setId, setMaxDate, setMinDate } from '../../redux/slices/CommentSlice';
import { message } from 'antd';

function RoutesUser() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const { language } = useContext(LanguageContext);
    const { toCities,showDateModal,selectedDate,currentOptionType,isModalOpen,isModalOpen1 } = useSelector((state) => state.toCity);
    const { fromCities,translatedFromCities,translatedToCities,showFromCityModal,showToCityModal } = useSelector((state) => state.fromCity);
    const { allRoutes, driverRout,day12,isAvailable } = useSelector((state) => state.routes);
    const { id,chatId,minDate,maxDate } = useSelector((state) => state.comment);
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


    const monthsUzbek = useMemo(() => [
        "yanvar", "fevral", "mart", "aprel", "may", "iyun",
        "iyul", "avgust", "sentabr", "oktabr", "noyabr", "dekabr"
    ], []);

    const monthsRussian = useMemo(() => [
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    ], []);

    const translateCity = useMemo(() => (cityName) => {
        if (language === '2') {
            return cityTranslations['Uzbekistan'][cityName] || cityName;
        }
        return cityName;
    }, [cityTranslations, language]);

    const translateCity1 = useMemo(() => (cityName) => {
        if (language === '2') {
            return cityTranslations['Russia'][cityName] || cityName;
        }
        return cityName;
    }, [cityTranslations, language]);
    const formatDate = useMemo(() => (dateString) => {
        const date = new Date(dateString);
        const day = date.getDate();
        const monthIndex = date.getMonth();
        const month = language === '2' ? monthsRussian[monthIndex] : monthsUzbek[monthIndex];
        return `${day}-${month}`;
    }, [monthsUzbek, monthsRussian, language]);

    const handleDateSelect = (date) => {
        // Format: YYYY-MM-DD
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        const formattedDate = date.toLocaleDateString('en-CA', options); // 'en-CA' locale for YYYY-MM-DD format
        dispatch(setSelectedDate(formattedDate));
        dispatch(setShowDateModal(false)); // Close the date modal
        dispatch(setKoranCourse({ ...driverRout, day: formattedDate }));
    };

    

    const handleOptionSelect = (option) => {
        if (currentOptionType === 'from') {
            dispatch(setKoranCourse({ ...driverRout, fromCity: translateCity1(option) }));
            dispatch(setShowFromCityModal(false));
        } else if (currentOptionType === 'to') {
            dispatch(setKoranCourse({ ...driverRout, toCity: translateCity1(option) }));
            dispatch(setShowToCityModal(false));
        }
    };

    useEffect(() => {
        dispatch(fetchToCity());
        dispatch(fetchFromCity());
        const today = new Date();
        const dayAfterTomorrow = new Date(today);
        dayAfterTomorrow.setDate(today.getDate() + 2);
        const formatDateForInput = (date) => date.toISOString().split('T')[0];
        dispatch(setMinDate(formatDateForInput(today)));
        dispatch(setMaxDate(formatDateForInput(dayAfterTomorrow)));


    }, [dispatch, language]);

    useEffect(() => {
        dispatch(setKoranCourse({ fromCity: '', toCity: '', countSide: '', price: "", day: "", hour: "", userId: "" }));
      
        dispatch(setTranslatedFromCities(fromCities.map(city => ({
            ...city,
            name: translateCity(city.name)
        }))));
        dispatch(setTranslatedToCities(toCities.map(city => ({
            ...city,
            name: translateCity(city.name)
        }))));
    }, [fromCities, toCities, language]);

    const getRouteOne = useCallback(() => {
        if (!driverRout.fromCity || !driverRout.toCity || !driverRout.day) {
            message.error('Iltimos, barcha maydonlarni to\'ldiring!');
            return; // Funksiyani to'xtatish
        }
    
    
        dispatch(fetchRoutesByDate(driverRout))
            .unwrap()
            .then(() => {
               
            })
            .catch((err) => {
                message.error('Ma\'lumotlarni olishda xatolik yuz berdi!');
            })
            .finally(() => {
            });
            setDay12(null)
        dispatch(setSelectedDate(""))
        dispatch(setKoranCourse({ fromCity: '', toCity: '', countSide: '', price: "", day: "", hour: "", userId: "" }));
    }, [dispatch, driverRout]);
    
    const getRoutesByDay = useCallback((day1) =>  {
        let day = parseInt(day1);
        dispatch(fetchRoutesByDay(day))
            .unwrap()
            .then(() => {
               
                day = null; 
            })
            .catch((err) => {
                message.error('Ma\'lumotlarni olishda xatolik yuz berdi!');
            });
            setDay12(null)
        }, [dispatch]);
    
    const today = new Date();
    const nextDay1 = new Date(today);
    const nextDay2 = new Date(today);
    nextDay1.setDate(today.getDate() + 1);
    nextDay2.setDate(today.getDate() + 2);
  
    
    function goDriverOne(userName) {
        navigate(`/id_haydovchi/${userName}`);
    }
    const { fromCity, toCity, day } = driverRout;
    const isFormValid = fromCity && toCity && day;

    function openIzohlar(id){
            dispatch(setIsModalOpen(true))
            dispatch(setId(id))
    }
    const closeModal = () => dispatch(setIsModalOpen(false));

    const openModal1 = (chatId) =>{
           
            dispatch(setChatId(chatId))
        dispatch(setIsModalOpen1(true));
    } 
    const closeModal1 = () => dispatch(setIsModalOpen1(false));


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
        for (const [key, value] of Object.entries(map)) {
            if (key.length > 1) {
                // Katta yoki kichik harf kombinatsiyalarni almashtiramiz
                const regex = new RegExp(key, 'g');
                result = result.replace(regex, value);
            }
        }
    
        for (const [key, value] of Object.entries(map)) {
            if (key.length === 1) {
                const regex = new RegExp(key, 'g');
                result = result.replace(regex, value);
            }
        }
    
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
            Ж: 'j', ж: 'j',
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
            Х: 'x', х: 'x',
            Ц: 's', ц: 's',
            Ч: 'Ch', ч: 'ch',
            Ш: 'Sh', ш: 'sh',
            Щ: 'Sh', щ: 'sh',
            Ъ: '',   ъ: '',
            Ы: 'Y',  ы: 'y',
            Ь: '',   ь: '',
            Э: 'E',  э: 'e',
            Ю: 'Yu', ю: 'yu',
            Я: 'Ya', я: 'ya',
            ҳ: 'h',h: 'ҳ'
        };
    
        return text.split('').map(char => cyrillicToLatinMap[char] || char).join('');
    };
    
    const translateFullName = (fullName) => {
        // if (language === '1') {
        //     return cyrillicToLatin(fullName);
        // }
          if (language === '2') {
            return latinToCyrillic(fullName);
        }
        return fullName;
    };
  
    return (
        <div>
            <Landing />
            <div className="routUser">
                <div className="imageBacground">
                    <div className="backgroundColor">
                        <div className="h1RouterUser">
                <h1 className=''>{language==="1"?"Har kuni O’zbekiston bo’ylab qatnovlar":"Ежедневные поездки по Узбекистану"}</h1>

                </div>
                 <div className="inputs" >
                 <div className="inputChild">
                    <button 
                        type="button"
                        className="btnCity"
                        onClick={() => {
                            dispatch(setCurrentOptionType('from'));
                            dispatch(setShowFromCityModal(true));
                        }}
                    >
                        {driverRout.fromCity ? (
                            <span style={{ color: '#303383',fontWeight:"600",letterSpacing:"1.5px" }}>{translateCity(driverRout.fromCity)}</span>
                        ) : (
                            <span style={{ fontWeight: 'normal' }}>
                                {language === "1" ? "Qayerdan  " : "Откуда"}
                            </span>
                        )} 
                        <img src={b7} className='locationImg' alt="image" />
                    </button>
                            </div>

                            <div className="inputChild">
                            <button 
                                type="button"
                                className="btnCity"
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
                            <span style={{ color: '#303383',fontWeight:"600",letterSpacing:"1.5px" }}>{translateCity(driverRout.toCity)}</span>
                        ) : (
                            <span style={{ fontWeight: 'normal' }}>
                                { language === "1" ? "Qayerga" : "Куда"}
                            </span>
                        )} 

                        <img src={b7} className='locationImg'  alt="image" />
                            </button>
                        </div>

                <div className="inputChild">
                    <button
                        type="button"
                        className="btnCity"
                        value={formatDate(driverRout.day)}
                        onClick={() => dispatch(setShowDateModal(true))} // Ensure this triggers the modal
                    >

                    {selectedDate? (
                            <span style={{ color: '#303383',fontWeight:"600",letterSpacing:"1.5px" }}>{formatDate(selectedDate)}</span>
                        ) : (
                            <span style={{ fontWeight: 'normal' }}>
                                { language === "1" ? "Qachon" : "Когда"}
                            </span>
                        )} 
                        <img src={calendar} className='imgCalendar'  alt="image" />
                    </button>
                </div>
                <div className="inputChild">
                <button
                            className='qidirish'
                            onClick={getRouteOne}
                            disabled={!isFormValid} // Agar barcha maydonlar to'ldirilmagan bo'lsa, button disabled bo'ladi
                            style={{
                                opacity: !isFormValid ? 0.5 : 1,
                                cursor: !isFormValid ? 'not-allowed' : 'pointer'
                            }}
                        >
                            {language === "1" ? "Qidirish" : "Поиск"}
                        </button>
                </div>
            </div>
           

          {allRoutes.length<1?"":  <div className="allRoutes" >
            <div className="getRoutes">
                         <div className="topRoutesUser">
                            <h2>{language==="1"?`Yo’nalish bo’yicha qatnovlar `:`Маршруты`} </h2>
                        </div>
                        <div className="days">
                        <button className='btnDays' onClick={()=>getRoutesByDay('1')} style={{backgroundColor: (formatDate(day12) === formatDate(today) ? "#303383" : ""),color:(formatDate(day12) === formatDate(today) ? "white" : "")}}>
                        {formatDate(today)}
                        </button>

                            <button className='btnDays'  onClick={()=>getRoutesByDay('2')} style={{backgroundColor: (formatDate(day12) === formatDate(nextDay1) ? "#303383" : ""),color:(formatDate(day12) === formatDate(nextDay1) ? "white" : "")}}>{formatDate(nextDay1)}</button>
                            <button className='btnDays'  onClick={()=>getRoutesByDay('3')} style={{backgroundColor: (formatDate(day12) === formatDate(nextDay2) ? "#303383" : ""),color:(formatDate(day12) === formatDate(nextDay2) ? "white" : "")}}>{formatDate(nextDay2)}</button>
                        </div>
                       </div>
               
                
           
            {allRoutes.map((item, index) => (
                   <div className="mapRoutes" key={item.id}>
                    <li className='list-group-item li1'><div className="l1Child1"><p> {formatDate(item.day)}</p> <p>{item.hour}</p></div> <div className="li1Child2"> <p>{translateCity(item.fromCity)}</p> <img src={b8}  alt="image" /> <p>{translateCity(item.toCity)}</p></div> <div className="li1Child3"><p>{item.price} {language==="1"?"So’m":"Сум"} </p></div></li>
                    <li className="list-group-item li2">
                                <div className="l2Child1">
                                    {Array.from({ length: Math.min(item.countSide, 6) }).map((_, index) => (
                                    <img key={index} src={b9}  alt="image" />
                                    ))}
                                </div>
                                <div className="li2Child2">
                                    <p>{language==="1"?" Bo'sh O'rindiqlar soni:":"Место:"}</p>
                                    <h6>{item.countSide}</h6>
                                </div>
                                <div className="li2Child3">
                                    <h6>{language==="1"?"Mashina rusumi":"Модель автомобиля"}</h6>
                                    <p>{translateFullName(item.user.carType)}</p>
                                </div>
                                </li>


                    <li className='list-group-item li3'><div className="l3Child1"><h3>{translateFullName(item.user.fullName)}</h3><div className="liDriverPhone"><img src={b11}  alt="image" /> <p>{item.user.phoneNumber}</p></div> </div><div className="li3Child3"><button onClick={()=>openModal1(item.user.chatId)}>  {language==="1"?"Band Qilish":"Бронирование"}</button> <button onClick={()=>openIzohlar(item.user.id)}>{language==="1"?"Izohlar":"Примечания"}</button>  <p className='carType'>  {item.user.carType}</p></div></li>
                   </div> 
                ))}
          
               
            </div>}

            {
                isAvailable?
                <div className="allRoutes" >
                     <div className="getRoutes">
                         <div className="topRoutesUser">
                            <h2>{language==="1"?`Yo’nalish bo’yicha qatnovlar `:`Маршруты`} <h3> {isAvailable ? (language === "1" ? "mavjud emas" : "нет в наличии") : ""}</h3></h2>
                        </div>
                        <div className="days">
                        <button className='btnDays' onClick={()=>getRoutesByDay('1')} style={{backgroundColor: (formatDate(day12) === formatDate(today) ? "#303383" : ""),color:(formatDate(day12) === formatDate(today) ? "white" : "")}}>
                        {formatDate(today)}
                        </button>

                            <button className='btnDays'  onClick={()=>getRoutesByDay('2')} style={{backgroundColor: (formatDate(day12) === formatDate(nextDay1) ? "#303383" : ""),color:(formatDate(day12) === formatDate(nextDay1) ? "white" : "")}}>{formatDate(nextDay1)}</button>
                            <button className='btnDays'  onClick={()=>getRoutesByDay('3')} style={{backgroundColor: (formatDate(day12) === formatDate(nextDay2) ? "#303383" : ""),color:(formatDate(day12) === formatDate(nextDay2) ? "white" : "")}}>{formatDate(nextDay2)}</button>
                        </div>
                       </div> 
                       <div className='catchRoute'>
                <h3>{language==="1"?"Sanani o'zgartiring":"Изменить дату"}</h3>
               <div className="imgCatchRoute"> <img style={{width:"100%",height:"100%",objectFit:"cover"}} src={b14}  alt="image" /></div>
          </div>
                </div>:""
            }
            
            <div className="boglanish2">
            <Boglanish2 />
            </div>
                <BizHaqimizda/>         
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
                    onClose={() => dispatch(setShowDateModal(false))} // Ensure this function is defined
                />
            )}
            <ToastContainer toastStyle={{ backgroundColor: 'white', color: 'black' }} autoClose={1000} />
            <Boglanish/>
            </div>
          
            <div className="boglanish2">
            
            <BizHaqimizda2/>
            </div>
            <Footer/>
             <Band_qilish isOpen={isModalOpen1} onClose={closeModal1} chatId={chatId} />

            <Izohlar  isOpen={isModalOpen} onClose={closeModal} userName={id}/>
        </div>
    );
}

                 
        
export default RoutesUser;
