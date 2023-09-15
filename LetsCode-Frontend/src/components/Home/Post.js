import React, { useEffect, useState } from 'react';
import { Avatar, Comment, Tooltip, Divider, Image, message } from 'antd';
import { UserOutlined, HeartOutlined, CommentOutlined, HeartFilled } from '@ant-design/icons';
import styles from '../../assets/OptionsComponent.module.css'
import Profile from '../../Images/profile.png'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEllipsisV, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { deleteLike, deletePost, getCommentsforPost, getLikesforPost, postComment, postLike } from '../../store/actions/postActions';
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import { Dropdown } from "react-bootstrap";
import DropdownToggle from "react-bootstrap/esm/DropdownToggle";
import { Modal, Button } from 'react-bootstrap';
import DropdownItem from "react-bootstrap/esm/DropdownItem";
import UpdatePost from './UpdatePost';
import { followUser, unfollowUser } from '../../store/actions/userActions';
import CommentComponent from './Comment';

const Post = ({subcheck,index,commentsCount,showComment,CommentAddCheck,showallComments,singlePostRender,showfulldescription ,profilePic,postId,userName,description,imageSrc,isLiked,likesCount,check,postType,userData}) => {
  const [showFullDescription, setShowFullDescription] = useState(showfulldescription);
  const [liked,setliked] =useState(isLiked)
  const [isModalVisible, setIsModalVisible] = useState(true);
  const [imagedata, setImagedata] = useState([]);
  const audiencemap={"public":"Post to Anyone","private":"Post to Followers"}
  const [selectAudience, setSelectAudience] = useState(audiencemap[postType]);
  const [type,settype]=useState(false)
  const [comment,setcomment]=useState(showComment)
  const toggleDescription = () => {
    setShowFullDescription(!showFullDescription);
  };

  
  const likedUsers=useSelector(state=>(state.topics.likesUsers))

  
  const dbComments=useSelector(state=>(state.topics.comments))

  const [commentText,setcommentText]=useState('')
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggleDropdown = () => setDropdownOpen(!dropdownOpen);
  const [showModal, setShowModal] = useState(false);
  const [offset,setoffset]=useState(0)
  const [commetsHide,setcommetsHide]=useState(showallComments)

  const dispatch=useDispatch()
  const navigate=useNavigate()
  const togglelike=()=>{
    if(liked) dispatch(deleteLike(postId,CommentAddCheck,navigate))
    else dispatch(postLike(postId,CommentAddCheck,navigate))
    setliked(!liked)
  }

  const dummyFile = {
    uid: '-143',
    name: 'Pic',
    status: 'done', 
    url: imageSrc, 
  }

  const handleModalOpen = () => {
    setShowModal(true);
  };

  const handleModalClose = () => {
    setShowModal(false);
  };

  function getDurationDifference(timestamp) {
    const now = Math.floor(Date.now() / 1000); 
    const difference = now - timestamp;
  
    if (difference < 60) {
      return `${difference}s`;
    } else if (difference < 3600) {
      return `${Math.floor(difference / 60)}m`;
    } else if (difference < 86400) {
      return `${Math.floor(difference / 3600)}h`;
    } else if (difference < 604800) {
      return `${Math.floor(difference / 86400)}d`;
    } else if (difference < 2419200) {
      return `${Math.floor(difference / 604800)}w`;
    } else if (difference < 29030400) {
      return `${Math.floor(difference / 2419200)}m`;
    } else {
      return `${Math.floor(difference / 29030400)}y`;
    }
  }


  const updatefun =()=>{
    if(!type)settype(true)
    if(imageSrc!==null && imageSrc!='')setImagedata([dummyFile])
    if(!isModalVisible){
      setSelectAudience(audiencemap[postType])
      setIsModalVisible(true)
    }
    
  }
  const deletepost=()=>{
    message.success("Your Post is Successfully Deleted")
    dispatch(deletePost(postId,navigate))
  }

  const getComments=(type)=>{
    if(type===1)dispatch(getCommentsforPost(postId,0,setoffset,type,navigate))
    else dispatch(getCommentsforPost(postId,offset+3,setoffset,type,navigate))
  }

  const toggleComments=()=>{
    if(subcheck){
      singlePostRender(index,2)
      if(commetsHide===true){
        getComments(1)
      }
      setcommetsHide(!commetsHide)
    }
    if(commetsHide===true){
      getComments(1)
      setcomment(true)
    }else{
      setcomment(false)
    }
    setcommetsHide(!commetsHide)
  }

  const toggleFollower=(followerId,type,follower)=>{
      if(type===true)
        dispatch(followUser(userData.userId,followerId,"likes",userData,follower,navigate))
      else 
        dispatch(unfollowUser(userData.userId,followerId,"likes",userData,follower,navigate))
  }

  const sendComemnt=()=>{
    message.success("Your Comment is Successfully Posted")
    dispatch(postComment(postId,commentText,userData,CommentAddCheck,navigate))
    if(commetsHide)setcomment(false)
    setcommentText('')
  }

  const getuserLikes=()=>{
    dispatch(getLikesforPost(postId,navigate,handleModalOpen))
  }

const MAX_LEtters = 50;

const  splitString=(text) =>{
  const lines = text.split('\n');
  const result = [];

  for (const line of lines) {
    const words = line.split(' ');
    let currentLine = '';

    for (const word of words) {
      while (word.length > MAX_LEtters) {
        currentLine += word.substring(0, MAX_LEtters) + '-';
        word = word.substring(MAX_LEtters);
        result.push(currentLine);
        currentLine = '';
      }

      if (currentLine.length + word.length + 1 <= MAX_LEtters) {
        if (currentLine.length > 0) {
          currentLine += ' ';
        }
        currentLine += word;
      } else {
        result.push(currentLine);
        currentLine = word;
      }
    }

    if (currentLine.length > 0) {
      result.push(currentLine);
    }
  }

  return result;
}
const renderDescription = () => {
  const lines = splitString(description)
  const truncatedLines = showFullDescription ? lines : lines.slice(0, 2);
  return (
    <div className={ `${styles.description} ${subcheck ? styles.des : '' }`} >
      {truncatedLines.map((line, index) => (
        <p style={{fontSize:"18px",wordWrap:"break-word"}} key={index}>{line}</p>
      ))}
      {!showFullDescription && (lines.length > truncatedLines.length ) && (
        <p className={styles.seeMore} onClick={()=>!subcheck?toggleDescription():singlePostRender(index,0)}>
          ... See more
        </p>
      )}
      {showFullDescription && (lines.length > 2) && (
        <p className={styles.seeMore} onClick={toggleDescription}>
          ... See Less
        </p>
      )}
    </div>
  );
};


const FollowerList = () => {
  return(
  <Modal show={showModal} onHide={handleModalClose} centered  dialogClassName={styles.modalDialog}>
    <Modal.Header closeButton className={styles.modalHeader} >
      <Modal.Title>{"Liked Users"}</Modal.Title>
    </Modal.Header>
    <Modal.Body className={styles.modalBody}>
    {likedUsers.map((follower,index) => (
      <div key={index} className={styles.followerItem}>
      <div className='d-flex justify-content-between' onClick={()=>handleModalClose()}>
          <div>
        <Link to={`/profile/${follower["user_name"]}`} ><img src={follower.pic} alt="Follower Icon" className={styles.followerIcon} onClick={()=>handleModalClose()}/></Link></div>
        <div className='d-flex flex-column' >
        <Link to={`/profile/${follower["user_name"]}`} style={{textDecoration:"none",color:"white"}}><h4 onClick={()=>handleModalClose()} className={styles.userNametitle}>{follower["user_name"]}</h4></Link>
        <h6 className={styles.usertitle}>{follower["name"]}</h6>
        </div>
        </div>
         {follower["user_name"] !== userData.userName &&
         <div>
  {
(
  <Button
    style={{
      width: "140px",
      backgroundColor: follower.tag !== "NONE" ? "#474747" : "#0095f6",
    }}
    onClick={()=>toggleFollower(follower["user_id"],follower.tag==="NONE",follower)}
  >
    { follower.tag === "NONE" ? "Follow" : "Following"}
  </Button>
)
}

</div>}
      </div>
    ))}
    </Modal.Body>
  </Modal>)
};

  return (
    <div className={styles.post}>
      <div className={styles['profile-icon']}>
      {type &&  <UpdatePost postId={postId} selectAudience={selectAudience} setSelectAudience={setSelectAudience} postType={postType} imagedata={imagedata} setImagedata={setImagedata} isModalVisible={isModalVisible} imageSrc={imageSrc} pastdescription={description}photo={profilePic} setIsModalVisible={setIsModalVisible}/>}
        <div>
       <Link to={`/profile/${userName}`}><img src={profilePic} alt='Profile' style={{height:"50px",width:"50px",borderRadius:"50%"}}/></Link>
       <Link style={{textDecoration:"none",color:"white"}} to={`/profile/${userName}`}><span className={styles.userID}>{userName}</span></Link>
       </div>
       <div>
      { check &&  <Dropdown isOpen={dropdownOpen} toggle={()=>toggleDropdown()} style={{backgroundColor:"#282828"}}>
            <DropdownToggle className={styles.dropdown} >
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"white",color:"black"}}>
                <DropdownItem   onClick={() => updatefun()}>Update</DropdownItem>
               <DropdownItem    onClick={() => deletepost()}>Delete</DropdownItem>
            </DropdownMenu>
        </Dropdown>}
       </div>
      </div>

      {renderDescription()}

      {imageSrc && (
        <Image src={imageSrc} alt="Post Image"  style={{ width: '100%',margin: '10px 0'}}/>
      )}


      <div className={styles.actions}>
        <div className={styles.like}>
          <Tooltip className='d-flex align-items-center'>
            {!liked && <HeartOutlined style={{fontSize:"20px"}} onClick={togglelike}/>}
            {liked && <HeartFilled style={{fontSize:"20px"}} onClick={togglelike}/>}
           {likesCount>1 &&  <span className={styles.LikesTitle} onClick={()=>getuserLikes()}>
          {`${likesCount} likes`}
        </span>}
          </Tooltip>
        </div>
        <div className={styles.comment}>
          <Tooltip  className='d-flex align-items-center'>
            <CommentOutlined style={{fontSize:"20px"}}  onClick={()=> !subcheck ? commetsHide?setcomment(!comment):'':singlePostRender(index,1)}/>
            <span className={styles.LikesTitle} onClick={()=>toggleComments()} >
          {commentsCount>0 ? commentsCount===1?'1 Comment':`${commentsCount} Comments`:'' } 
        </span>
          </Tooltip>
        </div>
      </div>
  
   { comment && subcheck===false &&  <div className={styles['add-comment']}>
      <div className={styles['profile-icon']}>
        <img src={userData.pic} alt="Profile" style={{ height: '3rem',width:'3rem',borderRadius:'50%' }} />
      </div>
      <input className={styles['start-comment']} value={commentText} placeholder=' Add a Comment' onChange={(e)=>setcommentText(e.target.value)} / >
      <FontAwesomeIcon icon={faPaperPlane}  style={{cursor:"pointer",marginRight:"auto"}} onClick={()=>sendComemnt()}/> 
      </div>

   }


   {commetsHide===false && subcheck===false &&
   <>
  {dbComments.map((com)=>(
    
    <CommentComponent deleteCheck={com.userName===userData.userName} CommentAddCheck={CommentAddCheck} postId={postId} profilePic={com.pic} username={com.userName} commentId={com.commentId} timeAgo={getDurationDifference(com.timeStamp)} commentText={com.commentText} />
   ))}
   <h3 style={{fontSize:15,cursor:"pointer",fontWeight:400}} onClick={()=>getComments(2)}>Load more comments</h3>
   </>
   }

   <FollowerList />

      <Divider className={styles.divider} />
    
    </div>
  );
};

export default Post;
