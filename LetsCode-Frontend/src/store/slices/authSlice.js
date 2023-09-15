import { createSlice } from "@reduxjs/toolkit";
import { userLogin } from "../actions/authActions";

const initialState={
    userId:'',
    userName:'',
    userRole:'',
    name:'',
    pic:'',
    isAuthenticated:false,
    error:'',
    loading:false,
    errorCode:'-99',
}

export const authSlice=createSlice({
    name:"authSlice",
    initialState,
    reducers:{
        setdata(state,data){
            const payload=data.payload;
            state.userId=payload.User_Id;
            state.userName=payload.sub;
            state.userRole=payload.ROLE;
            state.name=payload.Name;
            state.isAuthenticated=true;
        },
        setError(state,data){
            state.isAuthenticated=false;
            console.log(data.payload)
            state.errorCode=data.payload;
        },
        removedata(state){
            state.userId='';
            state.userName='';
            state.userRole='';
            state.isAuthenticated=false;
        },
        setpic(state,data){
            state.pic=data.payload
        },setmainloading(state,data){
            state.loading=data.payload
        }
    }
})

export const {setdata,setmainloading,setError,removedata,setpic}=authSlice.actions;

export default authSlice.reducer;

