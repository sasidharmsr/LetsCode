import { createSlice } from "@reduxjs/toolkit";

const initialState={
    topics:[],
    total:0,
    userSubmissions:[],
    problems:[],
    count:0,
    likesUsers:[],
    comments:[]
}

export const topicsSlice=createSlice({
    name:"topicSlice",
    initialState,
    reducers:{
        settopics(state,data){
           state.topics=data.payload;
        },
        setSubmissions(state,data){
            state.userSubmissions=data.payload.slice(1);
            state.total=data.payload[0].total;
        },
        setproblems(state,data){
            state.problems=data.payload.questions;
            state.count=data.payload.total
        },
        setlikedusers(state,data){
            state.likesUsers=data.payload
        },togglefollower(state,data){
            const users=[]
            state.likesUsers.map((user)=>{
                if(user["user_id"]!==data.payload[0])users.push(user)
                else{
                    if(data.payload[1]==="follow")user.tag="FOLLOWER"
                    else user.tag="NONE"
                }
            })
        },
        setcomments(state,data){
            state.comments=data.payload
        },addnewComment(state,data){
            state.comments=[data.payload,...state.comments]
        },addnewComments(state,data){
            state.comments=[...state.comments,...data.payload]
        },
        deletecomment(state,data){
            state.comments=state.comments.filter(comment=>comment.commentId!==data.payload)
        },

    }
})

export const {settopics,deletecomment,addnewComments,addnewComment,setcomments,togglefollower,setlikedusers,setSubmissions,setproblems}=topicsSlice.actions;

export default topicsSlice.reducer;

