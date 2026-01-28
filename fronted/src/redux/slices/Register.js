import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from '../../apicall/apicall1';

export const saveUserData = createAsyncThunk(
  'register/saveUserData',
  async ({ form, selectFiles, chatId }) => {
    const formData = new FormData();

    // Append the form data (excluding the files)
    formData.append('userDto', JSON.stringify({
      ...form,
      chatId, // Add the chatId to the form data
    }));

    // Append the files if they are selected
    if (selectFiles[0]) {
      formData.append('carImg', selectFiles[0]);
    }
    if (selectFiles[1]) {
      formData.append('driverImg', selectFiles[1]);
    }
    if (selectFiles[2]) {
      formData.append('cardDocument', selectFiles[2]);
    }

    // Send the form data with files and user details in one request
    const response = await apicall1('/user/save', 'POST', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }).then((res)=>{
      console.log(res.data+"sdd");
    })

    return response.data; // Adjust according to your API response structure
  }
);

const registerSlice = createSlice({
  name: 'register',
  initialState: {
    form: {
      count: "",
      carType: "",
      carImg: "",
      driverImg: "",
      cardDocument: "",
      about: ""
    },
    selectFiles: [null, null, null],
    chatId: null,
    language: "",
    isModalVisible: false,
    modalMessage: '',
    modalType: ''
  },
  reducers: {
    setChatId(state, action) {
      state.chatId = action.payload;
    },
    setLanguage(state, action) {
      state.language = action.payload;
    },
    updateForm(state, action) {
      state.form = { ...state.form, ...action.payload };
    },
    updateFile(state, action) {
      const { index, file } = action.payload;
      state.selectFiles[index] = file;
    },
    setModal(state, action) {
      const { isVisible, message, type } = action.payload;
      state.isModalVisible = isVisible;
      state.modalMessage = message;
      state.modalType = type;
    },
    resetForm(state) {
      state.form = {
        count: "",
        carType: "",
        carImg: "",
        driverImg: "",
        cardDocument: "",
        about: ""
      };
      state.selectFiles = [null, null, null];
      state.isModalVisible = false;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(saveUserData.fulfilled, (state) => {
        state.modalMessage = state.language === 'uz' ? 'Ma’lumotlaringiz tekshirilmoqda. Iltimos kuting, biz o’zimiz Telegram bot orqali xabar yuboramiz.' : 'Ваши данные проверяются. Пожалуйста, подождите, мы сами отправим вам сообщение через Telegram-бот.';
        state.modalType = 'success';
        state.isModalVisible = true;
      })
      .addCase(saveUserData.rejected, (state, action) => {
        state.modalMessage = state.language === 'uz' ? "Noma'lum xatolik yuz berdi" : "Произошла неизвестная ошибка";
        state.modalType = 'error';
        state.isModalVisible = true;
      });
  },
});

export const { setChatId, setLanguage, updateForm, updateFile, setModal, resetForm } = registerSlice.actions;
export default registerSlice.reducer;
