import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from "../../apicall/apicall1";
import apicall from "../../apicall/apicall";

export const fetchFromCity = createAsyncThunk('FromCitySlice/fetchFromCity', async () => {
  const response = await apicall1(`/fromCity`, "GET");
  return response.data;
});
export const addFromCity = createAsyncThunk('FromCitySlice/addFromCity', async ({ fromCityObject }) => {

  const response = await apicall(`/fromCity`, "POST", fromCityObject );
  return response;  
});

export const editFromCity= createAsyncThunk('FromCitySlice/editFromCity', async ({ EditButtonId1, fromCityObject }) => {
  const response = await apicall(`/fromCity?id=${EditButtonId1}`, "PUT", fromCityObject);
  return { id: EditButtonId1, ...fromCityObject };  
});

export const deleteFromCity = createAsyncThunk('FromCitySlice/deleteFromCity', async ({ id }) => {
  await apicall(`/fromCity?id=${id}`, "DELETE", null);
  return id;
});

const FromCitySlice = createSlice({
  name: 'fromCity',
  initialState: {
    fromCities: [],
    translatedFromCities:[],
    translatedToCities:[],
    showFromCityModal:false,
    showToCityModal:false,
    selectedSeat:null,
    status: 'idle',
    error: null,
    img: '',
    EditButtonId1: null,
    fromCityObject: { name:"" },
   
  },
  reducers: {
    setEditButtonId1(state, action) {
      state.EditButtonId1 = action.payload;
    },
    setTranslatedFromCities(state, action) {
      state.translatedFromCities = action.payload;
    },
    setTranslatedToCities(state, action) {
      state.translatedToCities = action.payload;
    },
    setShowFromCityModal(state, action) {
      state.showFromCityModal = action.payload;
    },
    setShowToCityModal(state, action) {
      state.showToCityModal = action.payload;
    },
    setFromCity(state, action) {
      state.fromCityObject = action.payload;
    },
    setSelectedFile(state, action) {
      state.selectedFile = action.payload;
    },
    setSelectedSeat(state, action) {
      state.selectedSeat = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchFromCity.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchFromCity.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.fromCities= action.payload;  // Null yoki undefined qiymatlarga e'tibor bering
      })
      .addCase(fetchFromCity.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(addFromCity.fulfilled, (state, action) => {
        state.fromCities.push(action.payload);  // Yangi yo'nalishni ro'yxatga qo'shamiz
      })
      .addCase(editFromCity.fulfilled, (state, action) => {
        const index = state.fromCities.findIndex(fromcity => fromcity.id === action.payload.id);
        if (index !== -1) {
          state.fromCities[index] = action.payload;  // Tahrirlangan yo'nalishni yangilaymiz
        }
      })
      .addCase(deleteFromCity.fulfilled, (state, action) => {
        state.fromCities = state.fromCities.filter(fromCity => fromCity.id !== action.payload);  // Yo'nalishni ro'yxatdan o'chiramiz
      });
  },
});

export const { setEditButtonId1, setFromCity, setSelectedFile,setTranslatedFromCities,setTranslatedToCities,setShowFromCityModal,setShowToCityModal,setSelectedSeat } = FromCitySlice.actions;

export default FromCitySlice.reducer;
