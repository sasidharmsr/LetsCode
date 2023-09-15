import React, { useEffect, useState } from 'react'
import UserSidebar from './Sidebar'
import UserGrid from './header'
import PieChart from './headerpie'
import styles from '../../assets/Profile.module.css'
import LcProblems from './LcProblems'
import CfProblems from './cfProblems'
import CalendarGrids from './CalenderGrid'
import Post from '../Home/Post'
import { useDispatch, useSelector } from 'react-redux'
import { followUser, getUserProfile, unfollowUser } from '../../store/actions/userActions'
import { useLocation, useNavigate } from 'react-router-dom'
import { faArrowDown, faChevronDown, faChevronUp, faL, faTimes } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import CalendarGrid from '../Home/CalenderGrid'
import EditProfilePage from './EditProfile'
import ImageGrid from './ImagePosts'
import Loader from '../Common/Loading'
import { getuserPosts } from '../../store/actions/postActions'

const Profile = () => {
  const profileData=useSelector(state=>Object(state.profile))
  const userData=useSelector(state=>Object(state.auth))
  const location=useLocation();
  const [images,setimages]=useState(false)
  const [singlePost,setsinglePost]=useState(null)
  
  const homeData=useSelector(state=>Object(state.home))
  const [offset,setoffset]=useState(10)

  const [postsHide,setpostsHide] =useState(true)
  const [editpage,seteditpage] =useState(true)
  const [showmore,setshowmore]=useState(true)
  const dispatch=useDispatch()
  const navigate=useNavigate()

  const userName=location.pathname.split('/')[2]
  useEffect(()=>{
    if(userName!=profileData.userName){
      dispatch(getUserProfile(userName,navigate))
    }
    setpostsHide(true)
  },[userName])

  const togglePosts=(val)=>{
    if(profileData.userPosts.length)
      setpostsHide(!postsHide)
    if(val===0)setpostsHide(true)
    seteditpage(true)
  }

  const singlePostRender=(postId,type)=>{
    setsinglePost({post:postId,type:type})
  }

  const handleScroll = () => {
    const scrollPosition = window.scrollY;
    const pageHeight = document.body.scrollHeight;
    const windowHeight = window.innerHeight;
    const scrollPercentage = (scrollPosition / (pageHeight - windowHeight)) * 100;
    console.log("Somethin call", profileData.postsCount>(offset))
    if (scrollPercentage >= 75 && !profileData.isLoading && profileData.postsCount>(offset)) {
      console.log("fetchin call")
      dispatch(getuserPosts(offset,profileData.profileinfo.user_id,navigate));
      setoffset(offset+10)
    }
  };

  const urlParams = new URLSearchParams(window.location.search);

  //console.log(editpage,urlParams.get('edit_page'))
  useEffect(() => {
    console.log(editpage,urlParams.get('edit_page'),userData.userName,profileData.userName)
    if(urlParams.get('edit_page')==='1' && userData.userName!=='' &&userData.userName===profileData.userName){
      seteditpage(false)
    }
    if(postsHide===false){
      console.log("useEfeect")
      window.addEventListener('scroll', handleScroll);
      return () => {
        window.removeEventListener('scroll', handleScroll);
      };
  }
  }, [profileData.isLoading,profileData.postsCount,postsHide]);


  const toggleeditpage=()=>{
    seteditpage(!editpage)
    setpostsHide(true)
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
    setshowmore(!showmore)
  }


  const myfollower = profileData.following.find((obj) => {
    return obj.userName === userName;
  });
  
  const toggleFollower=(followerId,type,check)=>{
    if(!type)
    {
      if(check==="followers")
        dispatch(followUser(followerId,userData.userId,check,userData,userData.userName===profileData.userName,navigate))
      else if(check==="following")
        dispatch(followUser(userData.userId,followerId,check,userData,userData.userName===profileData.userName,navigate))
      else
        dispatch(followUser(userData.userId,followerId,check,userData,userData.userName===profileData.userName,navigate))
    }else{
      if(check==="followers")
        dispatch(unfollowUser(followerId,userData.userId,check,userData,userData.userName===profileData.userName,navigate))
      else if(check==="following")
        dispatch(unfollowUser(userData.userId,followerId,check,userData,userData.userName===profileData.userName,navigate))
      else
        dispatch(unfollowUser(userData.userId,followerId,check,userData,userData.userName===profileData.userName,navigate))
    }
  }

  const buttonExist=(userData.userName==='' ? true : userName===userData.userName)
  
  return (
    <div className={`${styles.profileContainer} container-fluid`}>
       <div className="row" >
       <div className="col-lg-3 col-md-5 col-sm-12">
       <div className='d-md-none'>
       <UserSidebar toggleeditpage={toggleeditpage} profileData={profileData} showmore={!showmore} curUser={userData.userName} toggleFollower={toggleFollower} togglePosts={togglePosts}/>
       </div>
       <div className='d-md-block d-none'>
       <UserSidebar toggleeditpage={toggleeditpage} profileData={profileData} showmore={true} curUser={userData.userName} toggleFollower={toggleFollower} togglePosts={togglePosts}/>
       </div>
       <div className='d-md-none'>
       {showmore?
         <div style={{color:"white"}} onClick={()=>setshowmore(!showmore)} className='d-flex justify-content-center align-items-center m-1'>
         Show More
         <FontAwesomeIcon icon={faChevronDown} style={{marginLeft:10}} />
       </div>:
       <div style={{color:"white"}} onClick={()=>setshowmore(!showmore)} className='d-flex justify-content-center align-items-center m-1'>
       Show Less 
       <FontAwesomeIcon icon={faChevronUp} style={{marginLeft:10}}/>
     </div>
       }
       </div>
         </div>
         <div className= {`${postsHide && editpage?'':'d-none'} container-fluid col-lg-9  col-md-7 col-sm-12`}>
         <div className="row" >
         <div className="col-lg-6  col-sm-12 mt-3">
         {profileData.profileinfo!==undefined && !userData.loading && <UserGrid profileinfo={profileData.profileinfo}/>}
         </div>
         {profileData.profileinfo!==undefined &&
         <div className="col-lg-6 col-sm-12 mt-3">
           <PieChart 
           chartData={profileData.chartData}
           />
         </div>}
       {  !userData.loading && <>
        <div className="col-lg-6 col-sm-12 mt-3">
             <LcProblems profileinfo={profileData.profileinfo} totalProblems={profileData.totalProblems} />
           </div>
           <div className="col-lg-6 col-sm-12 mt-3">
             <CfProblems profileinfo={profileData.profileinfo} cfProblems={profileData.cfProblems} totalProblems={profileData.totalProblems} />
           </div>
           <div  className="col-sm-12 d-md-none mt-3">
           <CalendarGrid  calenderData={homeData.calenderData} />
           </div>
           <div  className="col-sm-12 mt-3">
           { profileData.calendarDescription.activedays!==undefined && <CalendarGrids calendarDescription={profileData.calendarDescription} calendarData={profileData.calendarData} years={profileData.years}/>}
           </div></>}
         </div>
         </div>

         <div className={`${postsHide || singlePost!==null ? 'd-none' : ''} container-fluid col-lg-9 col-md-7 col-sm-12`}>
         <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
         <button style={{ marginRight: '8px' }} onClick={()=>setimages(true)}>Images</button>
         <button style={{ marginRight: '8px' }} onClick={()=>setimages(false)}>Articles</button>
         <div style={{ marginLeft: 'auto' }}>
           <span color='white' onClick={()=>setpostsHide(true)} style={{cursor:"pointer"}}> <FontAwesomeIcon icon={faTimes} style={{color:"white"}}/></span>
         </div>
       </div>
       <div className='d-flex flex-column' style={{position:"relative"}}> 
       { images?( <ImageGrid singlePostRender={singlePostRender} posts={profileData.userPosts}/>):
         ( <div className={styles.postContainer}>
   {profileData.userPosts.map((post, idx) => {
     if((post["post_pic"]!==undefined && post["post_pic"]!==null && post["post_pic"]!=="null" && post["post_pic"]!=="" ))return;
     return(
     <div className={styles.postdiv} key={idx}>
         <Post
         singlePostRender={singlePostRender}
         index={idx}
         userData={userData}
         showfulldescription={false}
         subcheck={true}
         showComment={false}
           postId={post["post_id"]}
           isLiked={post["is_liked"]}
           description={post["post_description"]}
           imageSrc={post["post_pic"]}
           likesCount={post["likes_count"]}
           check={userData.userName === profileData.userName}
           profilePic={profileData.profileinfo.pic}
           userName={profileData.userName}
           postType={post["post_type"]}
           commentsCount={post["comments_count"]}
           showallComments={true}
         CommentAddCheck={false}
         />
        </div>
   )})}

 </div>)
}
{profileData.isLoading && 
      <div style={{height:70}}>
       <div className={styles.loader}>
          <div className={styles.spinner}></div>
        </div>
        </div>}

       </div>

</div>


 <div className={`${singlePost===null ? 'd-none' : ''} container-fluid col-lg-7 col-md-7 col-sm-12`}>
 <div className='d-flex justify-content-end'>
<span color='white' onClick={()=>setsinglePost(null)} style={{cursor:"pointer"}}> <FontAwesomeIcon icon={faTimes} style={{color:"white",fontSize:20}}/></span>
</div>
 {
  singlePost!==null &&  profileData.userPosts[singlePost.post]!==undefined &&
   <Post
         subcheck={false}
         userData={userData}
         CommentAddCheck={true}
         showfulldescription={singlePost.type===0?true:false}
         showComment={singlePost.type>=1?true:false}
           postId={profileData.userPosts[singlePost.post]["post_id"]}
           isLiked={profileData.userPosts[singlePost.post]["is_liked"]}
           description={profileData.userPosts[singlePost.post]["post_description"]}
           imageSrc={profileData.userPosts[singlePost.post]["post_pic"]}
           likesCount={profileData.userPosts[singlePost.post]["likes_count"]}
           check={userData.userName === profileData.userName}
           profilePic={profileData.profileinfo.pic}
           userName={profileData.userName}
           postType={profileData.userPosts[singlePost.post]["post_type"]}
           commentsCount={profileData.userPosts[singlePost.post]["comments_count"]}
           showallComments={singlePost.type===2?false:true}
         />
 }
 </div>
 {userData.loading && <Loader/>}

         <div className={`${editpage?'d-none':''} container-fluid col-lg-9  col-md-7 col-sm-12`}>
          {profileData.profileinfo!==undefined &&  <EditProfilePage profileData={profileData} toggleeditpage={toggleeditpage}/>}
         </div>
       </div>
     </div>
  );
};

export default Profile;