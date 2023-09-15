import { createSlice } from "@reduxjs/toolkit";

const initialState={
   calenderData:{},
   posts:[],
   contests:{},
   counts:{},
   users:[],
   totalPosts:0,
   isLoading:false
}

export const homeSlice=createSlice({
    name:"HomeSlice",
    initialState,
    reducers:{
      sethomedata(state,data){
        const payload=data.payload;
        state.calenderData=payload.calendar;
        state.users=payload.users;
        state.counts=payload.count;
        state.totalPosts=payload.totalPosts;
      },
      setposts(state,data){
        state.posts=[...state.posts,...data.payload]
     },
     onLoading(state){
      state.isLoading=true
     },
     offLoading(state){
      state.isLoading=false
     },
     addpost(state,data){
         state.posts=[data.payload,...state.posts]
     },
     updatehomepost(state,data){
      const posts=[]
      state.posts.map((post)=>{
        if(post["post_id"]!=data.payload[0])posts.push(post)
        else {
          post["post_description"]=data.payload[1].postDescription;
          post["post_type"]=data.payload[1].postType
          post["post_pic"]=data.payload[1].pic
          posts.push(post)
      }
      })
      state.posts=posts;
     },
     deletpost(state,data){
      const posts=[]
      state.posts.map((post)=>{
        if(post["post_id"]!=data.payload)posts.push(post)
      })
      state.posts=posts;
    },
    incrementlikeorcomment(state,data){
      const posts=[]
      state.posts.map((post)=>{
        if(post["post_id"]!=data.payload[0])posts.push(post)
        else{
          if(data.payload[1]==='like'){post["likes_count"]+=1;post["is_liked"]=true}
          else post["comments_count"]+=1
          posts.push(post)
      }
      })
      state.posts=posts;
    },
    decrementlikeorcomment(state,data){
      const posts=[]
      state.posts.map((post)=>{
        if(post["post_id"]!=data.payload[0])posts.push(post)
        else{
          if(data.payload[1]==='like'){post["likes_count"]-=1;post["is_liked"]=false}
          else post["comments_count"]-=1
          posts.push(post)
      }
      })
      state.posts=posts;
    },
     setcontests(state,data){
      state.contests=data.payload
     },
    }
})

export const {incrementlikeorcomment,updatehomepost,offLoading,onLoading,decrementlikeorcomment,sethomedata,deletpost,setposts,addpost,setcontests}=homeSlice.actions;

export default homeSlice.reducer;

