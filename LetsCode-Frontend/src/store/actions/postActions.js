import { addpost, decrementlike, decrementlikeorcomment, deletpost, incrementlike, incrementlikeorcomment, offLoading, onLoading, setposts, updatehomepost } from "../slices/HomePageSlice"
import { setmainloading } from "../slices/authSlice";
import { decrementlikeorcommentuserPosts, incrementlikeorcommentuserPosts, setProfileLoading, setuserPosts, updatepost } from "../slices/profileSlice";
import { addnewComment, addnewComments, deletecomment, setcomments, setlikedusers, setproblems } from "../slices/topicsSlice";
import { refreshToken } from "./authActions"



export function getPosts(offset,type,navigate) {
    return async function getPostsThunk(dispatch, getState) {
        console.log(offset,"Fetcing ra!")
        if(type)dispatch(onLoading())
        try {
            const res = await fetch(`${process.env.REACT_APP_API_URL}/msr/posts?offset=${offset}`, {
                method: "GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                },
            });

            if (!res.ok) {
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getPosts(offset,type,navigate));
                }
                dispatch(offLoading())
            } else {
                const data = await res.json();
                if(type){
                    setTimeout(()=>{
                        dispatch(setposts(data));
                        dispatch(offLoading())
                    },2000);
                }
                else{
                    dispatch(setposts(data));
                }
            }
        } catch (err) {
            console.log(err);
        }
    }
}


export function getuserPosts(offset,userId,navigate) {
    return async function getPostsThunk(dispatch, getState) {
        dispatch(setProfileLoading(true))
        try {
            const res = await fetch(`${process.env.REACT_APP_API_URL}/msr/userposts?offset=${offset}&user_id=${userId}`, {
                method: "GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                },
            });

            if (!res.ok) {
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getuserPosts(offset,userId,navigate));
                }
                dispatch(setProfileLoading(false))
            } else {
                const data = await res.json();
                console.log(data)
                    setTimeout(()=>{
                        dispatch(setuserPosts(data));
                        dispatch(setProfileLoading(false))
                    },2000);
            }
        } catch (err) {
            console.log(err);
        }
    }
}

export function postPost(postDescription,pic,postType,user,navigate){
    let body= JSON.stringify({postDescription,pic,postType})
    if(pic==="")body=JSON.stringify({postDescription,postType})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/posts`,{
                method:"POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
                body:body
            } )
            console.log(res)
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(postPost(postDescription,pic,postType,user,navigate));
                }
            }
            else{
                const data = await res.json();
                const post={post_id:data.post_id,is_liked:'unlike',pic:user.pic,user_name:user.userName,post_description:postDescription,post_pic:data.pic,likes_count:data.likesCount}
                dispatch(addpost(post))
            }
    }
    catch(err){
        console.log(err);
    }
}
}


export const postDetails = (image,postDescription,postType,user,dispatch,navigate)=>{
    if(image===null){
        dispatch(postPost(postDescription,"",postType,user,navigate))
        return;
    }
    console.log(user)
    const data = new FormData()
    data.append("file",image)
    data.append("upload_preset","msr_CodeArena")
    data.append("cloud_name","dp145twws")
    fetch('https://api.cloudinary.com/v1_1/dp145twws/image/upload',{
      method:"POST",
      body:data
    })
    .then(res=>res.json())
    .then(data=>{
      dispatch(postPost(postDescription,data.url,postType,user,navigate))
    })
    .catch(err=>{
      console.log(err)
    })
  }


  
  export function getProblems(type,status,topics,or,ratingL,ratingR,difficulty,offset,title,navigate) {
    return async function getProblemsThunk(dispatch, getState) {
        console.log("api call")
        dispatch(setmainloading(true))
        try {
            const res = await fetch(`${process.env.REACT_APP_API_URL}/msr/problems/${type}?status=${status}&topics=${topics}&or=${!or}&ratingL=${ratingL}&title=${title}&ratingR=${ratingR}&difficulty=${difficulty}&offset=${offset}`, {
                method: "GET",
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                },
            });

            if (!res.ok) {
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getProblems(type,status,topics,or,offset,navigate));
                }
                dispatch(setmainloading(false))
            } else {
                const data = await res.json();
                dispatch(setproblems(data));
                dispatch(setmainloading(false))
            }
        } catch (err) {
            console.log(err);
        }
    }
}

export function postComment(postId,commentText,userData,type,navigate){
    let body= JSON.stringify({postId,commentText})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/comments`,{
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
                    await dispatch(postComment(postId,commentText,userData,type,navigate));
                }
            }
            else{
                const data = await res.json();
                const commentObj={timeStamp:data.timeStamp,userId:userData.user_id,commentText:commentText,pic:userData.pic,userName:userData.userName}
                dispatch(addnewComment(commentObj))
                if(type){
                    dispatch(incrementlikeorcommentuserPosts([postId,'comment']))
                }else{
                    dispatch(incrementlikeorcomment([postId,'comment']))
                }
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export function postLike(postId,type,navigate){
    let body= JSON.stringify({postId})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/likes`,{
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
                    await dispatch( postLike(postId,type,navigate));
                }
            }
            else{
               if(type){
                dispatch(incrementlikeorcommentuserPosts([postId,'like']))
            }else{
                dispatch(incrementlikeorcomment([postId,'like']))
            }
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export function updateComment(commentText,navigate){
    let body= JSON.stringify({commentText})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/comments`,{
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
                    await dispatch(updateComment(commentText,navigate));
                }
            }
            else{
                const data = await res.json();
                console.log(data)
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export function updatePost(post_id,postDescription,pic,postType,navigate){
    let body= JSON.stringify({post_id,postDescription,pic,postType})
    return async function updatePostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/posts`,{
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
                    await dispatch(updatePost(post_id,postDescription,pic,postType,navigate));
                }
            }
            else{
                const data = await res.json();
                dispatch(updatepost([post_id,data]))
                dispatch(updatehomepost([post_id,data]))
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export const updatPostDetails = (post_id,postDescription,pic,postType,dispatch,navigate)=>{
    if(pic===null){
        dispatch(updatePost(post_id,postDescription,"",postType,navigate))
        return;
    }

    const data = new FormData()
    data.append("file",pic)
    data.append("upload_preset","msr_CodeArena")
    data.append("cloud_name","dp145twws")
    fetch('https://api.cloudinary.com/v1_1/dp145twws/image/upload',{
      method:"POST",
      body:data
    })
    .then(res=>res.json())
    .then(data=>{
      dispatch(updatePost(post_id,postDescription,data.url,postType,navigate))
    })
    .catch(err=>{
      console.log(err)
    })
  }

export function deleteLike(postId,type,navigate){
    let body= JSON.stringify({postId})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/likes`,{
                method:"DELETE",
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
                    await dispatch(deleteLike(postId,type,navigate));
                }
            }
            else{
                if(type){
                    dispatch(decrementlikeorcommentuserPosts([postId,'like']))
                }else{
                    dispatch(decrementlikeorcomment([postId,'like']))
                }
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export function deletePost(post_id,navigate){
    let body= JSON.stringify({post_id})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/post`,{
                method:"DELETE",
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
                    await dispatch(deletePost(post_id,navigate));
                }
            }
            else{
                dispatch(deletpost(post_id))
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export function deleteComment(commentId,postId,type,navigate){
    let body= JSON.stringify({commentId,postId})
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/comment`,{
                method:"DELETE",
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
                    await dispatch(deleteComment(commentId,postId,type,navigate));
                }
            }
            else{
                dispatch(deletecomment(commentId))
                if(type){
                    dispatch(decrementlikeorcommentuserPosts([postId,'comment']))
                }else{
                    dispatch(decrementlikeorcomment([postId,'comment']))
                }
            }
    }
    catch(err){
        console.log(err);
    }
}
}


export function getLikesforPost(post_id,navigate,handleModalOpen){
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/likes?post_id=${post_id}`,{
                method:"GET",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getLikesforPost(post_id,navigate));
                }
            }
            else{
                const data = await res.json();
                dispatch(setlikedusers(data))
                handleModalOpen()
            }
    }
    catch(err){
        console.log(err);
    }
}
}

export function getCommentsforPost(post_id,offset,setoffset,type,navigate){
    return async function postPostThunk(dispatch,getState){
        try{
            const res= await fetch(`${process.env.REACT_APP_API_URL}/msr/comments?post_id=${post_id}&offset=${offset}`,{
                method:"GET",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('msr_access_token')}`
                    },
            } )
            if(!res.ok){
                const text = await res.text();
                const error = JSON.parse(text);
                if (error.message === "Token Expired") {
                    refreshToken(navigate);
                    await dispatch(getCommentsforPost(post_id,navigate));
                }
            }
            else{
                const data = await res.json();
                if(type===2){
                    dispatch(addnewComments(data))
                    setoffset(offset)
                }else{ 
                    dispatch(setcomments(data))
                    setoffset(0)
                }
            }
    }
    catch(err){
        console.log(err);
    }
}
}