import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from "../../apicall/apicall1";
import apicall from '../../apicall/apicall';

export const fetchComments = createAsyncThunk('CommentSlice/fetchComments', async ({language,userName}) => {
  const response = await apicall1(`/comment?language=${language}&id=${userName}`, "GET");
  return response.data;
});
export const deleteComment = createAsyncThunk('CommentSlice/deleteComment', async ({ language,id }) => {
  
  await apicall(`/comment?language=${language}&id=${id}`, "DELETE", null);
  return id;
});
const CommentSlice = createSlice({
  name: 'comment',
  initialState: {
    comments: [],
    id:"",
    minDate:'',
    maxDate:'',
    chatId:"",
    active:false,
    isModalOpen1:false,
    isModalOpen:false,
    openModal:false,
    seatCount:null,
    openModal1:false,
    status: 'idle',
    error: null,
  },
  reducers: {
    setId(state, action) {
      state.id = action.payload;
    },
    setChatId(state, action) {
      state.chatId = action.payload;
    },
    setMinDate(state, action) {
      state.minDate = action.payload;
    },
    setMaxDate(state, action) {
      state.maxDate = action.payload;
    },
    setActive(state, action) {
      state.active = action.payload;
    },
    setIsModalOpen1(state, action) {
      state.isModalOpen1 = action.payload;
    },
    setIsModalOpen(state, action) {
      state.isModalOpen = action.payload;
    },
    setOpenModal(state, action) {
      state.openModal = action.payload;
    },
    setSeatCount(state, action) {
      state.seatCount = action.payload;
    },
    setOpenModal1(state, action) {
      state.openModal1 = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchComments.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchComments.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.comments = action.payload;
      })
      .addCase(fetchComments.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(deleteComment.fulfilled, (state, action) => {
        state.comments = state.comments.filter(comment => comment.id !== action.payload);  // Yo'nalishni ro'yxatdan o'chiramiz
      });
  },
});
export const { setId,setChatId,setMinDate,setMaxDate,setActive,setIsModalOpen1,setIsModalOpen,setOpenModal,setSeatCount,setOpenModal1} = CommentSlice.actions;

export default CommentSlice.reducer;
