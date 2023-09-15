import { sethomedata, setcontests } from "../slices/HomePageSlice"
import { setError, setmainloading, setpic } from "../slices/authSlice";
import { addfollower, addfollowercheck, addfollowing, removefollower, removefollowercheck, removefollowing, setprofiledata, setupdatedInfo, setupdatedpic, setupdatedprofile, togglefollowingInProfile } from "../slices/profileSlice";
import { setSubmissions, settopics, togglefollower } from "../slices/topicsSlice";
import { refreshToken } from "./authActions";

export function getHomedetails(navigate){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/home`,{
                method:"GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if(error.internalCode==='43'){
                    dispatch(setError('43'))
                    dispatch(setmainloading(false))
                    setTimeout(() => {
                        dispatch(setError('-99'))
                    }, 3000);
                }
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getHomedetails(navigate));
                }
            }else{
                const allData = await res.json();
                const data=allData.calendar;
                const monthlydata={};
                for (const key in data) {
                    const date = new Date(key);
                    const month = date.getMonth() + 1;
                    if(month in monthlydata)
                        monthlydata[month]+=data[key];
                    else
                    monthlydata[month]=data[key];
                }
                for(const key in monthlydata){
                    data[key]=monthlydata[key]
                }
                dispatch(settopics(allData.topics))
                dispatch(sethomedata(allData))
                dispatch(setpic(allData.pic))
                dispatch(setmainloading(false))
            }
        }catch(err){
            console.log(err)
        }        
    }
}


export function followUser(follower_id,following_id,check,userData,type,navigate){

    return async function userLoginThunk(dispatch,getState){
        const body= JSON.stringify({follower_id,following_id})
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/follow`,{
                method:"POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(followUser(follower_id,following_id,check,userData,type,navigate));
                }
            }else{
                const data = await res.json();
                if(check==="following"){
                    dispatch(addfollowing([following_id,type]))
                }else if(check==="followers"){
                    dispatch(addfollower([follower_id,type]))
                }else if(check==="followerCheck"){
                    dispatch(addfollowercheck([follower_id,userData]))
                }
                else if((check==="likes")){
                    dispatch(togglefollower([following_id,"follow"]))
                    let follower={...type};
                    follower.tag="Follow"
                    dispatch(togglefollowingInProfile([following_id,"follow",userData.userName,follower]))
                }
            }
        }catch(err){
            console.log(err)
        }        
    }
}

export function unfollowUser(follower_id,following_id,check,userData,type,navigate){
    return async function userLoginThunk(dispatch,getState){
        console.log("api call",follower_id,following_id,check,type)
        const body= JSON.stringify({follower_id,following_id})
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/unfollow`,{
                method:"POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(unfollowUser(follower_id,following_id,check,userData,type,navigate));
                }
            }else{
                if(check==="following"){
                    dispatch(removefollowing([following_id,type]))
                }else if(check==="followers"){
                    dispatch(removefollower([follower_id,type]))
                }else if(check==="followerCheck"){
                    dispatch(removefollowercheck([follower_id,userData]))
                }else if((check==="likes")){
                    dispatch(togglefollower([following_id,"unfollow"]))
                    dispatch(togglefollowingInProfile([following_id,"unfollow",userData.userName,type]))
                }
            }
        }catch(err){
            console.log(err)
        }        
    }
}

export function getUserProfile(userId,navigate){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/profile/${userId}`,{
                method:"GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
            } );
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                dispatch(setmainloading(false))
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getUserProfile(userId,navigate));
                }
            }else{
                const data = await res.json();
                data.years.sort((a, b) => b - a);
                dispatch(setprofiledata(data))
                dispatch(setmainloading(false))
            }
        }
        catch(err){
            console.log(err)
        }
        
    }
}


export function getUserSubmissions(platform,date,offset,navigate){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        if(date===null)date="$$$$-$$-$$";
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/submissions/${platform}?date=${date}&offset=${offset}`,{
                method:"GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
            } );
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getUserSubmissions(platform,date,offset,navigate));
                }
                dispatch(setmainloading(false))
            }else{
                const data = await res.json();
                dispatch(setSubmissions(data))
                dispatch(setmainloading(false))
            }
        }
        catch(err){
            console.log(err)
        }
        
    }
}


export function getContests(selectedPlatform,contest_id,type,navigate){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        let platform='cf';
        if(!selectedPlatform)platform='at'
        console.log(platform,contest_id,type);
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/contests/${platform}?contest_id=${contest_id}&type=${type}`,{
                method:"GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
            } );
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getContests(contest_id,type,navigate));
                }
                dispatch(setmainloading(false))
            }else{
                const data = await res.json();
                const result={}
                let maxContestId=123456;
                if(!selectedPlatform)maxContestId=0
                result["total"]=data[0].total;
                for(let i=1;i<data.length;i++){
                    if(selectedPlatform)maxContestId=Math.min(maxContestId,data[i].contest_id)
                    else {
                    maxContestId=Math.max(parseInt(data[i].contest_id.replace(/\D/g, '')),maxContestId);
                    }
                    if((selectedPlatform && -1*data[i].contest_id in result) || (!selectedPlatform && data[i].contest_id in result )){
                        const subdata=[data[i].index,data[i].title,data[i].contest_title,data[i].submission_id]
                        if(!selectedPlatform && data[i].index=='Ex')continue;
                        if(selectedPlatform)result[-1*data[i].contest_id].push(subdata);
                        else result[data[i].contest_id].push(subdata);
                    }
                    else{
                        const subdata=[data[i].index,data[i].title,data[i].contest_title,data[i].submission_id]
                        if(!selectedPlatform && data[i].index=='Ex')continue;
                        if(selectedPlatform)result[-1*data[i].contest_id]=[subdata];
                        else result[data[i].contest_id]=[subdata];
                    }
                }
                Object.keys(result).map((contestKey)=>{
                    if(contestKey!=="total")result[contestKey].sort((a, b) => a[0].localeCompare(b[0]))
                })
                result["contest_id"]=maxContestId
                dispatch(setcontests(result))
                dispatch(setmainloading(false))
            }
        }
        catch(err){
            console.log(err)
        }
        
    }
}


export function updateuser(name,email,phoneNumber,message,navigate){
    return async function userLoginThunk(dispatch,getState){
        const body= JSON.stringify({name,email,phoneNumber})
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/users`,{
                method:"PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(updateuser(name,email,phoneNumber,navigate));
                }
            }else{
                const data = await res.json();
                message.success("Details is Successfully Updated")
                dispatch(setupdatedprofile(data))
            }
        }catch(err){
            console.log(err)
        }        
    }
}

export const postProfilePic = (image,dispatch,message,navigate)=>{
    const data = new FormData();
    data.append("file",image)
    data.append("folder","profile_pics")
    data.append("upload_preset","msr_CodeArena")
    data.append("cloud_name","dp145twws")
    fetch('https://api.cloudinary.com/v1_1/dp145twws/image/upload',{
      method:"POST",
      body:data
    })
    .then(res=>res.json())
    .then(data=>{
        console.log(data.url)
        dispatch(updateProfilepic(data.url,message,navigate))
    })
    .catch(err=>{
      console.log(err)
    })
  }

  export function updateProfilepic(pic,message,navigate){
    return async function userLoginThunk(dispatch,getState){
        console.log("resoikj")
        const body= JSON.stringify({pic})
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/profilepic`,{
                method:"POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(updateProfilepic(pic,message,navigate));
                }
            }else{
                message.success("Profile Pic is Successfully Updated")
                dispatch(setupdatedpic(pic));
                dispatch(setpic(pic))
            }
        }catch(err){
            console.log(err)
        }        
    }
}


export function updateuserinfo(linkedinUrl,githubUrl,school,city,country,company,message,navigate){
    return async function userLoginThunk(dispatch,getState){
        const body= JSON.stringify({linkedinUrl,githubUrl,school,city,country,company})
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/userinfo`,{
                method:"PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(updateuserinfo(linkedinUrl,githubUrl,school,city,country,company,navigate));
                }
            }else{
                const data = await res.json();
                message.success("Details is Successfully Updated")
                dispatch(setupdatedInfo(data))
            }
        }catch(err){
            console.log(err)
        }        
    }
}


export function saveuserStats(enabled,userId,cfCount,lcCount,acCount,rating,difficulty,atType,message,navigate){
    return async function userLoginThunk(dispatch,getState){
        const body= JSON.stringify({enabled,userId,cfCount,lcCount,acCount,rating,difficulty,atType})
        try{
            const res=await fetch(`${process.env.REACT_APP_API_URL}/msr/usersstats`,{
                method:"POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(updateuserinfo(enabled,userId,cfCount,lcCount,acCount,rating,difficulty,atType,navigate));
                }
            }else{
                const data = await res.json();
                message.success("Details is Successfully Updated")
                console.log(data)
            }
        }catch(err){
            console.log(err)
        }        
    }
}