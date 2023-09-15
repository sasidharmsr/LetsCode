import React from 'react';
import styles from '../../assets/Profile.module.css';
import Location from '../../Images/location.svg'
import GithubOutlined from '../../Images/github.svg'
import LinkedinOutlined from '../../Images/linkedin.svg'
import School from '../../Images/school.svg'
import { faBriefcase } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
const UserDetails = ({profileinfo}) => {


  var schoolname=profileinfo.school;
  if(schoolname==="null")schoolname=profileinfo.cf_school;
  const giturl = profileinfo.github_url;
  let git_username="",linkedin_username="";
  if(giturl!==undefined && giturl!=="null"){
    let parts = giturl.split("/"); 
    git_username = parts[parts.length - 1];
  }
  const linkeinurl = profileinfo.linkedin_url;
  if(linkeinurl!==undefined && linkeinurl!=="null"){
    let parts = giturl.split("/"); 
    linkedin_username = parts[parts.length - 1];
  }
  
  return (
    <div className={styles.userContainer}>
      <div className={styles.userColumn}>
        <div className={styles.userDetail}>
        <FontAwesomeIcon icon={faBriefcase} className={styles.icon}   />
          <div className={styles.name}>{profileinfo.company==="null"?'Student':`${profileinfo.company}`}</div>
        </div>
        <div className={styles.userDetail}>
        <img  src={School}/>
          <div className={styles.name}>{schoolname==='null'?'No Education Info':schoolname}</div>
        </div>
        <div className={styles.userDetail}>
        <img  src={GithubOutlined}/>
        <a style={{textDecoration:"none"}} href={profileinfo.github_url} target="_blank"><div className={styles.name}>{git_username===''?'Not Provided':git_username}</div></a>
        </div>
     
        <div className={styles.userDetail}>
        <img  src={LinkedinOutlined}/>
          <a style={{textDecoration:"none"}} href={profileinfo.linkedin_url} target="_blank"><div className={styles.name}>{linkedin_username===''?'Not Provided':linkedin_username}</div></a>
        </div>
        <div className={styles.userDetail}>
        <img  src={Location}/>
          <div className={styles.name}>{(profileinfo.country==="null" || profileinfo.city==="null")?'No Location Info': `${profileinfo.country}|${profileinfo.city}`}</div>
        </div>
        </div>
      </div>
  );
};

export default UserDetails;
