import React, { useContext, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getConnection } from '../../redux/slices/Connection';
import { LanguageContext } from '../language/LanguageContext';

function Connection() {
  const { language } = useContext(LanguageContext);
  const dispatch = useDispatch();
  const { connections, status } = useSelector((state) => state.boglanish);

  useEffect(() => {
    dispatch(getConnection(language));
  }, [dispatch, language]);

  return (
    <div style={{paddingBottom:"30px"}}>
      {status === 'loading' ? (
        <p>Loading...</p>
      ) : status === 'failed' ? (
        <p>Xatolik yuz berdi</p>
      ) : (
        <table className='table table-success'>
          <thead>
            <tr>
              <th>N#</th>
              <th>Ism Familiya</th>
              <th>Telefon Raqam</th>
              <th>Xabar</th>
            </tr>
          </thead>
          <tbody>
            {connections.map((item, i) => (
              <tr key={item.id}>
                <td>{i + 1}</td>
                <td>{item.fullName}</td>
                <td>{item.phoneNumber}</td>
                <td>{item.message}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Connection;
