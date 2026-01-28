import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from "../../apicall/apicall1";
import apicall from "../../apicall/apicall";
import { toast } from 'react-toastify';
import { message } from 'antd';

export const fetchRoutes = createAsyncThunk('RouteDriverSlice/fetchRoutes', async () => {
  const response = await apicall1(`/driver`, "GET");
  return response.data;
});

export const fetchRoutesByDate = createAsyncThunk('RouteDriverSlice/fetchRoutesByDate', async (driverRout, { getState }) => {
  const response = await apicall1(`/driver/byDate`, "PUT", driverRout);
  
  
  if (!response.data || response.data.length === 0) {
    return { data: response.data, result: true,day:driverRout.day };  
  } else {
    message.success("Yo'nalishlar muvaffaqiyatli topildi!");
    return { data: response.data, result: false,day:driverRout.day };  // Yo'nalishlar mavjud - true
  }
  
});


export const fetchRoutesByDriver = createAsyncThunk('RouteDriverSlice/fetchRoutesByDriver', async (userName) => {
  const response = await apicall1(`/driver/bydriver?id=${userName}`, "GET");

  return response.data;
});
export const fetchRoutesByDay = createAsyncThunk('RouteDriverSlice/fetchRoutesByDay', async (day) => {
  const response = await apicall1(`/driver/byDay?day=${day}`, "GET");
  
  if (!response.data || response.data.length === 0) {
    return { data: response.data, result: true,day };  
  } else {
    message.success("Yo'nalishlar muvaffaqiyatli topildi!");
    return { data: response.data, result: false,day }; 
  }
});



export const addRoute = createAsyncThunk('RouteDriverSlice/addRoute', async ({ driverRout, userName }) => {
  const response = await apicall(`/driver`, "POST", { ...driverRout, userId: userName });
  return response;  
});

export const editRoute = createAsyncThunk('RouteDriverSlice/editRoute', async ({ EditButtonId, driverRout }) => {
  const response = await apicall(`/driver?id=${EditButtonId}`, "PUT", driverRout);
  return { id: EditButtonId, ...driverRout };  
});

export const deleteRoutes = createAsyncThunk('RouteDriverSlice/deleteRoutes', async ({ id }) => {
  await apicall(`/driver?id=${id}`, "DELETE", null);
  return id;
});
export const deleteRoutesByDay = createAsyncThunk('RouteDriverSlice/deleteRoutesByDay', async ( item1 ) => {
 
  await apicall(`/driver/bydel?day=${item1.day}&hour=${item1.hour}`, "DELETE", null);
  return item1.id;
});
export const deleteRoutesByTime = createAsyncThunk('RouteDriverSlice/deleteRoutesByTime', async ( ) => {
  await apicall1(`/driver/byTime`, "DELETE", null);
  return null;
});

const RouteDriverSlice = createSlice({
  name: 'routes',
  initialState: {
    driverRoutes: [],
    allRoutes: [],
    routesByDriver:[],
    driverRoutesByDate: [],
    status: 'idle',
    error: null,
    day:"",
    img: '',
    EditButtonId: null,
    driverRout: { fromCity: '', toCity: '', countSide: "", price: "", day: "", hour: "", userId: "" },
    selectedFile: null,
    item1:null,
    day12:null,
    isAvailable:false
  },
  reducers: {
    setEditButtonId(state, action) {
      state.EditButtonId = action.payload;
    },
    setDay12(state, action) {
      state.day12 = action.payload;
    },
    setDay(state, action) {
      state.day = action.payload;
    },
    setKoranCourse(state, action) {
      state.driverRout = action.payload;
    },
    setSelectedFile(state, action) {
      state.selectedFile = action.payload;
    },
    setClearItem1(state, action) {
      state.item1 = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchRoutesByDriver.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchRoutesByDay.pending, (state) => {
        state.status = 'loading';
      })
   
      .addCase(fetchRoutes.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchRoutesByDriver.fulfilled, (state, action) => {
        
        state.status = 'succeeded';
        state.routesByDriver = action.payload;  

          action.payload.map((item)=>{
             state.item1=item
          })

       
      })
      .addCase(fetchRoutesByDay.fulfilled, (state, action) => {
      
        const today = new Date();
        const nextDay1 = new Date(today);
        const nextDay2 = new Date(today);
        state.status = 'succeeded';
        state.allRoutes = action.payload.data; 
        state.isAvailable = action.payload.result; 
        
        if (action.payload.day == "1") {
            state.day12 = today.toISOString().substring(0, 10);
        } else if (action.payload.day =="2") {
            nextDay1.setDate(today.getDate() + 1);
            state.day12 = nextDay1.toISOString().substring(0, 10);
        } else if (action.payload.day == "3") {
            nextDay2.setDate(today.getDate() + 2);
            state.day12 = nextDay2.toISOString().substring(0, 10);
        }
    })
      .addCase(fetchRoutesByDate.fulfilled, (state, action) => {
  
        if (action.payload.data.length===0) {
            state.day12=action.payload.day
            state.isAvailable = action.payload.result; 
            state.allRoutes=action.payload.data
        }else{
          state.allRoutes = action.payload.data; 
          state.isAvailable = action.payload.result; 
          for (let index = 0; index < action.payload.data.length; index++) {
              state.day12=action.payload.data[index].day
          }
        }
      })
      .addCase(fetchRoutes.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.allRoutes = action.payload; 
      })
      .addCase(fetchRoutesByDate.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(fetchRoutesByDay.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(fetchRoutes.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(addRoute.fulfilled, (state, action) => {
        state.driverRoutes.push(action.payload);  
      })
      .addCase(editRoute.fulfilled, (state, action) => {
        const index = state.driverRoutes.findIndex(driverRoute => driverRoute.id === action.payload.id);
        if (index !== -1) {
          state.driverRoutes[index] = action.payload; 
        }
      })
      .addCase(deleteRoutes.fulfilled, (state, action) => {
        state.routesByDriver = state.routesByDriver.filter(routesByDriver => routesByDriver.id !== action.payload);  
      })
      .addCase(deleteRoutesByDay.fulfilled, (state, action) => {
        state.driverRoutes = state.driverRoutes.filter(driverRoute => driverRoute.id !== action.payload);  
      })
      .addCase(deleteRoutesByTime.fulfilled, (state, action) => {
        state.driverRoutes = state.driverRoutes.filter(driverRoute => driverRoute.id !== action.payload);  
      });
  },
});

export const { setEditButtonId, setKoranCourse, setSelectedFile,setDay, setClearItem1,setDay12} = RouteDriverSlice.actions;

export default RouteDriverSlice.reducer;
