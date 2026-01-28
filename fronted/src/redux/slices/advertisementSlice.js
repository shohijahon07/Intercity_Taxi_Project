import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apicall1 from "../../apicall/apicall1";
import apicall from "../../apicall/apicall";

export const addAdvert = createAsyncThunk('AdvertisementSlice/addToCity', async ({ adverst, selectedFileA }) => {
    let newImg2 = null;

    if (selectedFileA) {
        const formData = new FormData();
        formData.append('file', selectedFileA); 

        const res = await apicall1('/fileController/photo', "POST", formData);
        newImg2 = res.data; 
    }

    const updatedCourse = { ...adverst, img: newImg2 }; 


    const response = await apicall(`/advertising`, "POST", updatedCourse);
    return response; 
});
const AdvertisementSlice = createSlice({
    name: 'advertisement',
    initialState: {
        adverst: { text: "", img: "", link: "", buttonName: "" },
        selectedFileA: null,
        status: 'idle',
        error: null,
    },
    reducers: {
        setAdvert(state, action) {
            state.adverst = { ...state.adverst, ...action.payload };
        },
        setSelectedFileA(state, action) {
            state.selectedFileA = action.payload;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(addAdvert.pending, (state) => {
                state.status = 'loading'; // Yuklash holati
            })
            .addCase(addAdvert.fulfilled, (state, action) => {
                state.status = 'succeeded'; // Yana holat
               
            })
            .addCase(addAdvert.rejected, (state, action) => {
                state.status = 'failed'; // Xato holati
                state.error = action.error.message; // Xato xabarini saqlash
            });
    },
});

export const { setAdvert, setSelectedFileA } = AdvertisementSlice.actions;
export default AdvertisementSlice.reducer;
