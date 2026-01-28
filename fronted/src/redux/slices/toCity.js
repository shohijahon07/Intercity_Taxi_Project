import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from "../../apicall/apicall1";
import apicall from "../../apicall/apicall";

export const fetchToCity = createAsyncThunk('ToCitySlice/fetchToCity', async () => {
  const response = await apicall1(`/toCity`, "GET");
  return response.data;
});
export const addToCity = createAsyncThunk('ToCitySlice/addToCity', async ({ toCityObject }) => {
  const response = await apicall(`/toCity`, "POST", toCityObject );
  return response;  
});

export const editToCity = createAsyncThunk('ToCitySlice/editToCity', async ({ EditButtonId, toCityObject }) => {
  const response = await apicall(`/toCity?id=${EditButtonId}`, "PUT", toCityObject);
  return { id: EditButtonId, ...toCityObject };  
});

export const deleteToCity = createAsyncThunk('ToCitySlice/deleteToCity', async ({ id }) => {
  await apicall(`/toCity?id=${id}`, "DELETE", null);
  return id;
});

const ToCitySlice = createSlice({
  name: 'toCity',
  initialState: {
    toCities: [],
    showDateModal:false,
    selectedDate:'',
    currentOptionType:'',
    isModalOpen:false,
    isModalOpen1:false,
    active:false,
    status: 'idle',
    error: null,
    img: '',
    EditButtonId: null,
    toCityObject: { name:"" },
    selectedFile: null,
  },
  reducers: {
    setEditButtonId(state, action) {
      state.EditButtonId = action.payload;
    },
    setShowDateModal(state, action) {
      state.showDateModal = action.payload;
    },
    setSelectedDate(state, action) {
      state.selectedDate = action.payload;
    },
    setCurrentOptionType(state, action) {
      state.currentOptionType = action.payload;
    },
    setIsModalOpen(state, action) {
      state.isModalOpen = action.payload;
    },
    setIsModalOpen1(state, action) {
      state.isModalOpen1 = action.payload;
    },
    setToCity(state, action) {
      state.toCityObject = action.payload;
    },
    setActive(state, action) {
      state.active = action.payload;
    },
    setSelectedFile(state, action) {
      state.selectedFile = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchToCity.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchToCity.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.toCities = action.payload;  // Null yoki undefined qiymatlarga e'tibor bering
      })
      .addCase(fetchToCity.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(addToCity.fulfilled, (state, action) => {
        state.toCities.push(action.payload);  // Yangi yo'nalishni ro'yxatga qo'shamiz
      })
      .addCase(editToCity.fulfilled, (state, action) => {
        const index = state.toCities.findIndex(toCity => toCity.id === action.payload.id);
        if (index !== -1) {
          state.toCities[index] = action.payload;  // Tahrirlangan yo'nalishni yangilaymiz
        }
      })
      .addCase(deleteToCity.fulfilled, (state, action) => {
        state.toCities = state.toCities.filter(toCity => toCity.id !== action.payload);  // Yo'nalishni ro'yxatdan o'chiramiz
      });
  },
});

export const { setEditButtonId, setToCity, setSelectedFile,setShowDateModal,setSelectedDate,setCurrentOptionType,setIsModalOpen,setIsModalOpen1,setActive} = ToCitySlice.actions;

export default ToCitySlice.reducer;
