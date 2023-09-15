import { createSlice } from "@reduxjs/toolkit";

const initialState={
    userName:'',
    calendarData:{},
    profileinfo:{},
    userStats:{},
    userPosts:[],
    totalProblems:{},
    cfProblems:{},
    followers:[],
    following:[],
    years: [],
    calendarDescription:{},
    followersCount:0,followingCount:0,
    followerCheck:false,
    postsCount:0,
   isLoading:false,
   chartData:{cf:0,lc:0,at:0}
}

export const profileSlice=createSlice({
    name:"profileSlice",
    initialState,
    reducers:{
        setprofiledata(state,data){
            const payload=data.payload;
            state.userName=payload.profileinfo["user_name"]
            state.calendarData=payload.calendarData;
            state.profileinfo=payload.profileinfo;
            state.userPosts=payload.userPosts;
            state.totalProblems=payload.totalQuestions
            state.cfProblems=payload.solvedCfQuestions;
            state.followers=payload.followers;
            state.following=payload.following;
            state.userStats=payload.userstats;
            state.calendarDescription=payload.yearsdata;
            state.years=payload.years;
            state.followersCount=payload.followers.length
            state.followingCount=payload.following.length
            state.followerCheck=payload.followerCheck
            state.postsCount=payload.postsCount
            state.chartData={cf:payload.profileinfo["cf_count"],lc:payload.profileinfo["lc_count"],at:payload.profileinfo["at_count"]}
        },
        setupdatedprofile(state,data){
            const updatedData={"email_id":data.payload.email,"phone_number":data.payload.phoneNumber,"name":data.payload.name}
            state.profileinfo={...state.profileinfo,...updatedData}
        },
        setupdatedInfo(state,data){
            const payload=data.payload;
            const updatedData={"github_url":payload.githubUrl,"linkedin_url":payload.linkedinUrl,"city":payload.city,
                                "country":payload.country,"company":payload.company,"school":payload.school}
            state.profileinfo={...state.profileinfo,...updatedData}
        },
        setupdatedpic(state,value){
            state.profileinfo={...state.profileinfo,pic:value.payload}
        },
        addfollower(state,data){
            const newArray = []
            state.followers.map(item =>{
                if(item["user_id"]===data.payload[0]){
                    const changedItem=item;changedItem.tag="Follower";
                    newArray.push(changedItem)
                }else newArray.push(item)
            });
            state.followers=newArray
            if(data.payload[1]===true)
                state.followersCount=state.followersCount+1;
        },
        addfollowing(state,data){
            const newArray = []
            state.following.map(item =>{
                if(item["user_id"]===data.payload[0]){
                    const changedItem=item;changedItem.tag="Follower";
                    newArray.push(changedItem)
                }else newArray.push(item)
            });
            state.following=newArray;
            if(data.payload[1]===true)
                state.followingCount=state.followingCount+1;
        },
        removefollower(state,data){
            const newArray = []
            state.followers.map(item =>{
                if(item["user_id"]===data.payload[0]){
                    const changedItem=item;changedItem.tag=null;
                    newArray.push(changedItem)
                }else newArray.push(item)
            });
            state.followers=newArray
            if(data.payload[1]===true)
                state.followersCount=state.followersCount-1;
        },
        removefollowing(state,data){
            const newArray = []
            state.following.map(item =>{
                if(item["user_id"]===data.payload[0]){
                    const changedItem=item;changedItem.tag="NONE";
                    newArray.push(changedItem)
                }else newArray.push(item)
            });
            state.following=newArray;
            if(data.payload[1]===true)
                state.followingCount=state.followingCount-1;
        },
        removefollowercheck(state,data){
            state.followerCheck=false
            state.followersCount=state.followersCount-1
            const newArray = []
            state.followers.map(item =>{
                if(item["user_id"]!==data.payload[0]){
                    newArray.push(item)
                }
            });
            state.followers=newArray
        },
        addfollowercheck(state,data){
            state.followerCheck=true
            const payload=data.payload;
            state.followersCount=state.followersCount+1
            const users=[]
            let check=true
            state.followers.map((user)=>{
                if(user.user_id==payload[0])check=false
            })
            if(check){
                const name=payload[1].name!=='USER'?payload[1].name:payload[1].userRole
                const user={user_id:payload[0],user_name:payload[1].userName,name:name,tag:"Follower",pic:payload[1].pic}
                state.followers.push(user);
            }
        },
        updatepost(state,data){
            const posts=[]
            state.userPosts.map((post)=>{
              if(post["post_id"]!=data.payload[0])posts.push(post)
              else {
                post["post_description"]=data.payload[1].postDescription;
                post["post_type"]=data.payload[1].postType
                post["post_pic"]=data.payload[1].pic
                posts.push(post)
            }
            })
            state.userPosts=posts;
           },togglefollowingInProfile(state,data){
            if(state.userName!==data.payload[2] || state.userName==='')return;
            if(data.payload[1]==='follow')
            {
                state.following=[data.payload[3],...state.following]
                state.followingCount=state.followingCount+1;
                return;
            }
            const newArray = []
            state.following.map(item =>{
                if(item["user_id"]!==data.payload[0])newArray.push(item)
            });
            state.following=newArray;
            state.followingCount=state.followingCount-1;
        },setProfileLoading(state,data){
            if(data.payload)state.isLoading=true;
            else state.isLoading=false;
        },
        setuserPosts(state,data){
            state.userPosts=[...state.userPosts,...data.payload]
        },incrementlikeorcommentuserPosts(state,data){
            const posts=[]
            state.userPosts.map((post)=>{
              if(post["post_id"]!=data.payload[0])posts.push(post)
              else{
                if(data.payload[1]==='like'){post["likes_count"]+=1;post["is_liked"]=true}
                else post["comments_count"]+=1
                posts.push(post)
            }
            })
            state.userPosts=posts;
          },
          decrementlikeorcommentuserPosts(state,data){
            const posts=[]
            state.userPosts.map((post)=>{
              if(post["post_id"]!=data.payload[0])posts.push(post)
              else{
                if(data.payload[1]==='like'){post["likes_count"]-=1;post["is_liked"]=false}
                else post["comments_count"]-=1
                posts.push(post)
            }
            })
            state.userPosts=posts;
          }
    }
})

export const {updatepost,setuserPosts,incrementlikeorcommentuserPosts,decrementlikeorcommentuserPosts,setProfileLoading,togglefollowingInProfile,setprofiledata,setupdatedpic,setupdatedprofile,setupdatedInfo,addfollower,addfollowing,removefollower,removefollowing,addfollowercheck,removefollowercheck}=profileSlice.actions;

export default profileSlice.reducer;

