import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from '../../apicall/apicall1';
import { toast } from 'react-toastify';
import { message } from 'antd';
import apicall from '../../apicall/apicall';

export const saveForm = createAsyncThunk(
  'boglanish/saveForm',
  async ({ form, language }, { rejectWithValue }) => {
    try {
      const response = await apicall1(`/connection?language=${language}`, 'POST', form);
      message.success('Sizning savolingiz muvaffaqiyatli yuborildi');
      return response;
    } catch (error) {
      toast.error('Xatolik yuz berdi');
      return rejectWithValue(error.response.data);
    }
  }
);

export const getConnection = createAsyncThunk(
  'connection/getConnection',
  async (language, { rejectWithValue }) => {
    try {
      const response = await apicall(`/connection?language=${language}`, 'GET');
      return response.body;
    } catch (error) {
      toast.error('Xatolik yuz berdi');
      return rejectWithValue(error.response.data);
    }
  }
);

const boglanishSlice = createSlice({
  name: 'boglanish',
  initialState: {
    form: {
      fullName: '',
      phoneNumber: '',
      message: '',
    },
    connections: [],

    status: null,
  },
  reducers: {
    setForm: (state, action) => {
      state.form = { ...state.form, ...action.payload };
    },
    clearForm: (state) => {
      state.form = { fullName: '', phoneNumber: '', message: '' };
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(saveForm.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(saveForm.fulfilled, (state) => {
        state.status = 'success';
        state.form = { fullName: '', phoneNumber: '', message: '' };
      })
      .addCase(saveForm.rejected, (state) => {
        state.status = 'failed';
      })
      .addCase(getConnection.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(getConnection.fulfilled, (state, action) => {
        state.status = 'success';
        state.connections = action.payload; // Update connections here
      })
      .addCase(getConnection.rejected, (state) => {
        state.status = 'failed';
      });
  },
});

export const { setForm, clearForm } = boglanishSlice.actions;
export default boglanishSlice.reducer;
