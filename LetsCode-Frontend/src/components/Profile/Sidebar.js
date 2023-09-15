import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom'; // Use your routing library
import { Modal, Button } from 'react-bootstrap';
import styles from '../../assets/Profile.module.css';
import codeForceslogo from '../../Images/codeforces -logo.png'
import leetcode_icon from '../../Images/logo-leetcode.png'
import atcoder_icon from '../../Images/download_atcoder.jpeg'
import profileIcon from '../../Images/profile.png'
import statsicon from '../../Images/problems.svg'
import editicon from '../../Images/Edit_User.svg'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCalendar, faCheckCircle, faChessKing, faChessQueen, faStar } from '@fortawesome/free-solid-svg-icons';

const UserSidebar = ({profileData,curUser,toggleFollower,toggleeditpage,togglePosts,showmore}) => {
  const [showModal, setShowModal] = useState(false);
  const [modaltype,setmodaltype]=useState('')

  const profileinfo=profileData.profileinfo
  const handleModalOpen = (value) => {
    setShowModal(true);
    setmodaltype(value)
  };

  const handleModalClose = () => {
    setShowModal(false);
  };

  const UserCard = ({ icon, title ,link,type}) => (
    link==""?(<div className={styles.userCard}>
    <img src={icon} alt="User Icon" className={styles.userIcon} />
    <h4 style={{marginLeft:"10px"}}className={styles.userIdtitle}>{title}</h4>
  </div>):
    <div className={styles.userCard}>
    <img src={icon} alt="User Icon" className={styles.userIcon} />
    {type===1? (<div style={{cursor:"pointer"}} onClick={()=>toggleeditpage()}>
    <h4 style={{marginLeft:"10px"}}className={styles.userIdtitle}>{title}</h4>
    </div>):(<a style={{textDecoration:"none",color:"white"}} href={link} target="_blank">
    <h4 style={{marginLeft:"10px"}}className={styles.userIdtitle}>{title}</h4>
    </a>)}
    </div>
  );


  const calculateFontSize = (name) => {
    const nameLength = Math.min(name.length, 30);
    const minFontSize = 17; // Minimum font size in pixels
    const maxFontSize = 24; // Maximum font size in pixels
    const minLength = 3; // Minimum name length
    const maxLength = 30; // Maximum name length
  
    // Calculate the font size using linear interpolation
    const fontSize = minFontSize + ((maxFontSize - minFontSize) * (nameLength - minLength)) / (maxLength - minLength);
  
    // Ensure that the calculated font size is within the specified range
    const clampedFontSize = Math.min(Math.max(fontSize, minFontSize), maxFontSize);
  
    return `${clampedFontSize}px`;
  };


  const FollowerList = ({value}) => {
    let data=profileData.followers
    if(value==="following")data=profileData.following
    return(
    <Modal show={showModal} onHide={handleModalClose} centered  dialogClassName={styles.modalDialog}>
      <Modal.Header closeButton className={styles.modalHeader} >
        <Modal.Title>{modaltype==="followers"?"Followers":"Following"}</Modal.Title>
      </Modal.Header>
      <Modal.Body className={styles.modalBody}>
      {data.map((follower,index) => (
        <div key={index} className={styles.followerItem}>
        <div className='d-flex justify-content-between' onClick={()=>handleModalClose()}>
            <div>
          <Link to={`/profile/${follower["user_name"]}`} onClick={()=>togglePosts(0)}><img src={follower.pic} alt="Follower Icon" className={styles.followerIcon} onClick={()=>handleModalClose()}/></Link></div>
          <div className='d-flex flex-column' >
          <Link to={`/profile/${follower["user_name"]}`} onClick={()=>togglePosts(0)} style={{textDecoration:"none",color:"white"}}><h4 onClick={()=>handleModalClose()} className={styles.userNametitle}>{follower["user_name"]}</h4></Link>
          <h6 className={styles.usertitle}>{follower["name"]}</h6>
          </div>
          </div>
           {follower["user_name"] !== curUser &&
           <div>
    {
 ( curUser === profileData.userName && follower.tag === null && modaltype==="followers") ? (
    <Button disabled style={{ width: "140px", backgroundColor: "#0095f6" }}>Removed</Button>
  ) : (
    <Button
      onClick={() => toggleFollower(follower["user_id"], curUser === profileData.userName && modaltype === "followers" ?true:follower.tag !== "NONE", modaltype)}
      style={{
        width: "140px",
        backgroundColor: follower.tag !== "NONE" ? "#474747" : "#0095f6",
      }}
    >
      {curUser === profileData.userName && modaltype === "followers" ? "Remove" : follower.tag === "NONE" ? "Follow" : "Following"}
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
    <>
    { Object.keys(profileinfo).length 
    ?(<div className={styles.userProfile}>
      <div className={styles.userInfo}>
       <div to="/user-profile" className={styles.profileLink}>
        <img src={profileinfo.pic} alt="Profile Icon" className={styles.profileIcon} />
         {profileinfo.name!==undefined && <div className='d-flex align-items-center'><h2 className={styles.profileUserName} style={{ fontSize: calculateFontSize(profileinfo.name==null ?profileinfo["user_name"]:profileinfo.name) }}>{curUser!=profileData.userName ?profileinfo["user_name"]:profileinfo.name}</h2><span>{profileinfo.role==="ADMIN"&&<img width="20" height="20" src="https://img.icons8.com/color/48/verified-badge.png" alt="verified-badge" style={{marginLeft:"10px",marginBottom:"10px"}}/>}</span></div>}
          {curUser==profileData.userName && profileinfo.name!==undefined ? <h5 className={styles.profileName} style={{ fontSize: calculateFontSize(profileinfo["user_name"])}}>{profileinfo["user_name"]}</h5>
          :<Button onClick={()=>toggleFollower(profileinfo["user_id"],profileData.followerCheck,"followerCheck")}>{!profileData.followerCheck?"Follow":"Following"}</Button>}
        </div>
        <div className='d-flex justify-content-around w-100'>
        <div className={styles.followersLink} onClick={()=>togglePosts(1)}>
        <div className={styles.customDiv}>
            <h3 className={styles.countTitle}>{profileData.postsCount}</h3>
            <h5 className={styles.subtitle}>Posts</h5>
          </div>
        </div>
        <div className={styles.followersLink} onClick={()=>profileData.followersCount && handleModalOpen("followers")}>
          <div className={styles.customDiv}>
            <h3 className={styles.countTitle}>{profileData.followersCount}</h3>
            <h5 className={styles.subtitle}>Followers</h5>
          </div>
        </div>
        <div className={styles.followingLink} onClick={()=>profileData.followingCount && handleModalOpen("following")}>
          <div className={styles.customDiv}>
          <h3 className={styles.countTitle}>{profileData.followingCount}</h3>
          <h5 className={styles.subtitle}>Following</h5>
          </div>
        </div>
        </div>
      </div>
      {
        showmore && <div className='w-100'>
        <div style={{width:"90%",border:"1px solid #2cbb5d",margin:"10px"}}></div>
        <div className={styles.userStats}>
          <div className={styles.userCard}>
          <FontAwesomeIcon icon={faStar} className={styles.jhb} />
          <h4 style={{marginLeft:"10px"}}className={styles.userIdtitle}>Rating | {profileinfo.cf_rating}</h4>
          </div>
          <div className={styles.userCard}>
          <FontAwesomeIcon icon={faChessKing} className={styles.jhb} />
          <h4 style={{marginLeft:"10px"}}className={styles.userIdtitle}>{profileinfo.cf_rank}</h4>
          </div>
          <div className={styles.userCard}>
          <FontAwesomeIcon icon={faChessQueen} className={styles.jhb} />
          <h4 style={{marginLeft:"10px"}}className={styles.userIdtitle}>Rank | {profileinfo.lc_rank}</h4>
          </div>
          <UserCard icon={codeForceslogo} title={profileinfo.cf_id} link={`https://codeforces.com/profile/${profileinfo.cf_id}`} type={0} />
          <UserCard  icon={leetcode_icon} title={profileinfo.lc_id} link={`https://leetcode.com/${profileinfo.lc_id}`} type={0} />
          <UserCard  icon={atcoder_icon} title={profileinfo.at_id} link={`https://atcoder.jp/users/${profileinfo.at_id}`} type={0}  />
          {/* <UserCard   icon={statsicon} title="Stats" link="" type={0} /> */}
         {curUser === profileData.userName && <UserCard  icon={editicon} title="Edit Profile" link="/editprofile" type={1} />}
        </div>
        </div>
      }
      <FollowerList value={modaltype}/>
    </div>)
    :(<div className={styles.userProfile}>
    </div>)}
    </>
  );
};

export default UserSidebar;
