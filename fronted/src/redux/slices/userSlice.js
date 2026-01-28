import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from "../../apicall/apicall1";

export const addPessenger = createAsyncThunk('DriverSlice/addPessenger', async ({ pessen, chatId }) => {
  const response = await apicall1(`/user/pessenger`, "POST", { ...pessen, driverChatId: chatId });
  return response.data;
});

const PessengerSlice = createSlice({
  name: 'pessenger',
  initialState: {
    pessen: { name: "", phoneNumber: "", driverChatId: "" },
    user:{ phoneNumber: '', password: '' },
    loading:false,
    showPassword:false,
    status: 'idle',
    error: null,
  },
  reducers: {
    setPesenger(state, action) {
      state.pessen = { ...state.pessen, ...action.payload };
    },
    setUser(state, action) {
      state.user = { ...state.pessen, ...action.payload };
    },
    setLoading(state, action) {
      state.loading = action.payload;
    },
    setShowPassword(state, action) {
      state.showPassword = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(addPessenger.fulfilled, (state, action) => {
        state.pessen = { ...state.pessen, ...action.payload };
        state.status = 'succeeded';
      })
      .addCase(addPessenger.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(addPessenger.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { setPesenger,setUser,setLoading,setShowPassword } = PessengerSlice.actions;

export default PessengerSlice.reducer;
