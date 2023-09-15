import React, { useEffect, useState } from 'react'
import CalendarGrid from './CalenderGrid'
import { useDispatch, useSelector } from 'react-redux'
import CreatePost from './CreatePosts';
import Post from './Post';
import styles from '../../assets/OptionsComponent.module.css'
import HomeStats from './HomeStats';
import { useNavigate } from 'react-router-dom';
import { getPosts, startLoading } from '../../store/actions/postActions';
import Loader from '../Common/Loading';

const Home = () => {
  const posts=useSelector(state=>(state.home.posts))
  const homeData=useSelector(state=>Object(state.home))
  const userData=useSelector(state=>Object(state.auth))
  const [offset,setoffset]=useState(10)

  const dispatch=useDispatch()
  const navigate=useNavigate()
  const handleScroll = () => {
    const scrollPosition = window.scrollY;
    const pageHeight = document.body.scrollHeight;
    const windowHeight = window.innerHeight;
    const scrollPercentage = (scrollPosition / (pageHeight - windowHeight)) * 100;
    if (scrollPercentage >= 75 && !homeData.isLoading && homeData.totalPosts>(offset)) {
      console.log("fetchin call")
      dispatch(getPosts(offset,1,navigate));
      setoffset(offset+10)
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [homeData.isLoading,homeData.totalPosts]);

  return (
    <div  style={{minHeight:"100vh",backgroundColor:"#1a1a1a"}} className=' container-fluid'>
      <div className='row  d-flex p-md-5 justify-content-center align-items-start'>
        <div className='col-0 col-md-3'></div>
      <div className='col-12 col-md-6 d-flex flex-column flex-grow-2' style={{minHeight:"100vh",position:"relative"}}>
        <CreatePost photo={userData.pic} userData={userData}/>
        <hr style={{color:"#bfbebb",fontSize:"90px"}} className={styles.horizontalLine}/>
        {
        posts.map((post, idx) => {
          return (
            <Post
              key={idx}
              userData={userData}
              subcheck={false}
              showComment={false}
              postId={post["post_id"]}
              isLiked={post["is_liked"]}
              profilePic={post["pic"]}
              userName={post["user_name"]}
              description={post["post_description"]}
              imageSrc={post["post_pic"]}
              likesCount={post["likes_count"]}
              commentsCount={post["comments_count"]}
              check={false}
              showfulldescription={false}
              showallComments={true}
              CommentAddCheck={false}
            />
          );
        })
      }
      {homeData.isLoading && 
      <div style={{height:70}}>
       <div className={styles.loader}>
          <div className={styles.spinner}></div>
        </div>
        </div>}
      </div>
      {userData.loading && <Loader/>}
      <div className='d-none d-flex flex-column align-items-center col-12 col-md-3 d-md-flex ' style={{ position: "sticky", top: "40px", height: "100vh" }}>
        <CalendarGrid calenderData={homeData.calenderData}/>
       { homeData.counts.lenth!==0 && <HomeStats counts={homeData.counts} totalProblems={2800} solvedProblems={750}/>}
        </div>
    </div>
    </div>
  )
}

export default Home