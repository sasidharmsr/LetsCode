import jwtDecode from "jwt-decode";
import  {setdata,setError,removedata, setmainloading}  from "../slices/authSlice";
import { settopics } from "../slices/topicsSlice";
import { getHomedetails } from "./userActions";
import { getPosts } from "./postActions";

export function userLogin(user_name,password,navigate){
return async function userLoginThunk(dispatch,getState){
    const body = JSON.stringify({ user_name, password });
    fetch(`${process.env.REACT_APP_API_URL}/api/auth/login`,{
        method:"POST",
        headers:{ 'Content-Type': 'application/json'},
        body:body
    } ).then((res)=>{
        console.log(res)
        if(!res.ok){
            res.text().then((text)=>{
                const error=JSON.parse(text);
                dispatch(setError(error.internalCode));
                setTimeout(()=>{
                    dispatch(setError('-99'));
                },2000)
            })
        }
        return res.json()
    }).then((data)=>{
        const decodedToken = jwtDecode(data.accessToken);
        dispatch(setdata(decodedToken))
        localStorage.setItem("msr_access_token",data.accessToken);
        localStorage.setItem("msr_refresh_token",data.refreshToken);
        navigate('/home');
    })
    .catch((err)=>{
        console.log(err);
    })
  
}
}

export function userRegister(user_name,password,email,name,navigate){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        const body = JSON.stringify({ user_name,email,name, password });
        fetch(`${process.env.REACT_APP_API_URL}/api/auth/register`,{
            method:"POST",
            headers:{ 'Content-Type': 'application/json'},
            body:body
        } ).then((res)=>{
            if(!res.ok){
                dispatch(setmainloading(false))
                res.text().then((text)=>{
                    const error=JSON.parse(text);
                    dispatch(setError(error.internalCode));
                    setTimeout(()=>{
                        dispatch(setError('-99'));
                    },2000)
                })

            }
            return res.json()
        }).then((data)=>{
            const decodedToken = jwtDecode(data.accessToken);
            dispatch(setdata(decodedToken))
            dispatch(setmainloading(false))
            localStorage.setItem("msr_access_token",data.accessToken);
            localStorage.setItem("msr_refresh_token",data.refreshToken);
            navigate('/register/userIds');
        })
        .catch((err)=>{
            console.log(err);
        })
      
    }
    }


    export function passwordChange(user_name,password,navigate){
        return async function userLoginThunk(dispatch,getState){
            const body = JSON.stringify({ user_name, password });
            fetch(`${process.env.REACT_APP_API_URL}/api/auth/passwordchange`,{
                method:"POST",
                headers:{ 'Content-Type': 'application/json'},
                body:body
            } ).then((res)=>{
                if(!res.ok){
                    res.text().then((text)=>{
                        const error=JSON.parse(text);
                        dispatch(setError(error.internalCode));
                        setTimeout(()=>{
                            dispatch(setError('-99'));
                        },2000)
                    })
                }
                return res.json()
            }).then((data)=>{
                const decodedToken = jwtDecode(data.accessToken);
                dispatch(setdata(decodedToken))
                localStorage.setItem("msr_access_token",data.accessToken);
                localStorage.setItem("msr_refresh_token",data.refreshToken);
                dispatch(getHomedetails(navigate))
                dispatch(getPosts(0,0,navigate))  
                navigate('/home');
            })
            .catch((err)=>{
                console.log(err);
            })
          
        }
        }

export function userRegisterUserIds(lcUserId,cfUserId,atUserId,navigate){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        const body = JSON.stringify({ lcUserId,cfUserId,atUserId });
        fetch(`${process.env.REACT_APP_API_URL}/msr/login`,{
            method:"POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
              },
            body:body
        } ).then((res)=>{
            console.log(res)
            if(!res.ok){
                res.text().then((text)=>{
                    const error=JSON.parse(text);
                    dispatch(setmainloading(false))
                    dispatch(setError(error.internalCode));
                    setTimeout(()=>{
                        dispatch(setError('-99'));
                    },2000)
                })
            }
            return res.json()
        }).then((data)=>{
            dispatch(settopics(data))
            dispatch(setmainloading(false))
            navigate('/home');
        })
        .catch((err)=>{
            console.log(err);
        })
      
    }
    }

export const loadUserDetails=(dispatch)=>{
    const decodedToken = jwtDecode(localStorage.getItem("msr_access_token"));
    dispatch(setdata(decodedToken))
}


export function submitTopics(topics,navigate){
    return async function userLoginThunk(dispatch,getState){
        const body = JSON.stringify(topics);
        fetch(`${process.env.REACT_APP_API_URL}/msr/topics`,{
            method:"POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                },
            body:body
        } ).then((res)=>{
            if(!res.ok){
                res.text().then((text)=>{
                    console.log(text)
                })
            }else{
                navigate('/home');
            }
        })
        .catch((err)=>{
            console.log(err);
        })
        
    }
    }

    export function forgotPassword(user_name,phone_number,email,setsuccess){
        return async function userLoginThunk(dispatch,getState){
            const body = JSON.stringify({user_name,phone_number,email});
            dispatch(setmainloading(true))
            fetch(`${process.env.REACT_APP_API_URL}/api/auth/forgot`,{
                method:"POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } ).then(async (res)=>{
                if(!res.ok){
                    res.text().then((text)=>{
                        const error=JSON.parse(text);
                        if(error.internalCode.startsWith("User name not found with username"))error.internalCode='5'
                        dispatch(setError(error.internalCode));
                        setTimeout(()=>{
                            dispatch(setError('-99'));
                        },2000)
                        dispatch(setmainloading(false))
                    })
                }else{
                    const data= await res.json()
                    setsuccess(data.email)
                    dispatch(setmainloading(false))
                }
            })
            .catch((err)=>{
                console.log(err);
            })
            
        }
        }

export const refreshToken=(navigate)=>{
    fetch(`${process.env.REACT_APP_API_URL}/api/auth/refreshToken`,{
        method:"GET",
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('msr_refresh_token')}`
            },
    } ).then((res)=>{
        if(!res.ok){
            res.text().then((text)=>{
                localStorage.removeItem("msr_refresh_token");
                localStorage.removeItem("msr_access_token");
                navigate("/login")
            })
        }
        return res.json()
    }).then((data)=>{
        localStorage.setItem("msr_access_token",data.body.accessToken);
    })
    .catch((err)=>{
        console.log(err);
    })
}


export function validateToken(token,setrecoverypage,setuserName){
    return async function userLoginThunk(dispatch,getState){
        dispatch(setmainloading(true))
        fetch(`${process.env.REACT_APP_API_URL}/api/auth/recovery?token=${token}`,{
            method:"GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                },
        } ).then(async (res)=>{
            if(!res.ok){
                res.text().then((text)=>{
                setrecoverypage(false)
                dispatch(setmainloading(false))
                })
            }else{
                const data= await res.json()
                setuserName(data.userName)
                dispatch(setmainloading(false))
            }
        })
        .catch((err)=>{
            console.log(err);
        })
        
    }
    }

      

export function logout(navigate){
return async function userLoginThunk(dispatch,getState){
    dispatch(removedata())
    localStorage.removeItem("msr_refresh_token");
    localStorage.removeItem("msr_access_token");
}
}
    
    