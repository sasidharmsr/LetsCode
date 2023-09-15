import React, { useEffect, useState } from 'react';
import styles from '../../assets/ediprofile.module.css'

import Profile from '../../Images/profile.png'
import { useRef } from 'react';

import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import { Dropdown } from "react-bootstrap";
import DropdownToggle from "react-bootstrap/esm/DropdownToggle";
import DropdownItem from "react-bootstrap/esm/DropdownItem";
import { Input, Switch, message } from 'antd';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faL, faTimes } from '@fortawesome/free-solid-svg-icons';
import { useDispatch } from 'react-redux';
import { postProfilePic, saveuserStats, updateuser, updateuserinfo } from '../../store/actions/userActions';
import { useNavigate } from 'react-router-dom';


const EditProfilePage = ({toggleeditpage,profileData}) => {
  const [activeSection, setActiveSection] = useState(1);
  const [whatsappdata,setwhatsappdata]=useState({enabled:false,difficulty:'ALL',atType:'ALL'})
  const [profileinfo,setprofileinfo]=useState({})
  const [profiledata,setprofiledata]=useState({name:profileData.profileinfo.name,email:profileData.profileinfo.email_id,phoneNumber:profileinfo.phone_number})

  const fileInputRef = useRef(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggleDropdown = () => setDropdownOpen(!dropdownOpen);
  const navigate=useNavigate()

  const dispatch=useDispatch()
  const handleTextClick = () => {
    fileInputRef.current.click();
  };

  const setwhatsapp=()=>{
  
     if(whatsappdata.enabled===false)
      setwhatsappdata({...whatsappdata,enabled:true});
    else{
      setwhatsappdata({...whatsappdata,enabled:false});
    }
  }
  const checknullValues=(val)=>{
    if(val===null||val==='null')return '';
    return val;
  }

  const difmap={"ALL":0,"Easy":1,"Medium":2,"Hard":3,"0":"ALL","2":"Medium","1":"Easy","3":"Hard"}
  const attypemap={"ALL":"ALL","Problem A":"A","Problem B":"B","Problem C":"C","Problem D":"D","Problem E":"E","Problem F":"F","Problem G":"G"}
  const revatType={
    "ALL": "ALL",
    "A": "Problem A",
    "B": "Problem B",
    "C": "Problem C",
    "D": "Problem D",
    "E": "Problem E",
    "F": "Problem F",
    "G": "Problem G"
}

  useEffect(()=>{
    setprofiledata({name:profileData.profileinfo.name,email:profileData.profileinfo.email_id,phoneNumber:profileData.profileinfo.phone_number})
    setprofileinfo({
      linkedinUrl:checknullValues(profileData.profileinfo.linkedin_url),
      githubUrl: checknullValues(profileData.profileinfo.github_url),
      school: checknullValues(profileData.profileinfo.cf_school),
      company: checknullValues(profileData.profileinfo.company==="null"?"student":profileData.profileinfo.company),
      country: checknullValues(profileData.profileinfo.country),
      city: checknullValues(profileData.profileinfo.city),
    });
    if(profileData.userStats!==null)
    setwhatsappdata({"enabled":profileData.userStats.enabled,"cfCount":profileData.userStats.cfCount,
    "lcCount":profileData.userStats.lcCount,"atCount":profileData.userStats.acCount,"rating":profileData.userStats.rating,
    "difficulty":difmap[profileData.userStats.difficulty],"atType":revatType[profileData.userStats.atType]})
    else setwhatsappdata({enabled:false,difficulty:'ALL',atType:'ALL'})

  },[profileData])

  const saveusersdata=()=>{
    //console.log(profiledata)
    dispatch(updateuser(profiledata.name,profiledata.email,profiledata.phoneNumber,message,navigate))
    toggleeditpage()
  }

  const saveuserInfo=()=>{
    dispatch(updateuserinfo(profileinfo.linkedinUrl,profileinfo.githubUrl,profileinfo.school,
      profileinfo.city,profileinfo.country,profileinfo.company,message,navigate))
      toggleeditpage()
  }

  const saveuserstat=()=>{
    if(whatsappdata.cfCount==='' || whatsappdata.atCount===''|| whatsappdata.lcCount===''){
      message.error('Provide the Number of Problems in all Plactform')
      return
    }
    dispatch(saveuserStats(whatsappdata.enabled?1:0,profileData.profileinfo.user_id,whatsappdata.cfCount,whatsappdata.lcCount,whatsappdata.atCount,whatsappdata.rating,difmap[whatsappdata.difficulty],attypemap[whatsappdata.atType],message,navigate))
      toggleeditpage()
  }

  const activeSectionmap={1:"Profile Settings",2:"Info Settings",3:"Telegram Settings"};

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      postProfilePic(selectedFile,dispatch,message,navigate)
    }
  };


  return (
    <div className={styles.editProfilePage}>
      <div className='d-flex justify-content-end'>
        <button
          type="button"
          className="btn btn-link text-white"
          aria-label="Close"
          onClick={()=>toggleeditpage()}
        >
          <FontAwesomeIcon icon={faTimes} />
        </button>
        </div>
      <div className={styles.rightPanel}>
        <div className='d-flex justify-content-between align-items-center'>
        <h2 className={` d-none d-md-block ${styles.mainTitle}`}>{activeSectionmap[activeSection]}</h2>
        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={styles.dropdown} style={{backgroundColor:"#555555"}}>
                {activeSectionmap[activeSection]}
            </DropdownToggle>
              <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
             <DropdownItem   className={activeSection == 1 ? styles["red-text"] : styles["white-text"]}   onClick={() => setActiveSection(1)}>Profile Settings</DropdownItem>
             <DropdownItem  className={activeSection == 2? styles["red-text"] : styles["white-text"]}   onClick={() => setActiveSection(2)}>Info Settings</DropdownItem>
             <DropdownItem  className={activeSection == 3 ? styles["red-text"] : styles["white-text"]}   onClick={() => setActiveSection(3)}>Telegram Settings</DropdownItem>
           </DropdownMenu>
        </Dropdown>

        </div>
        {activeSection === 1 && (
             <div className={styles.profileUpdate}>
               <div className={styles.profileImage}>
                 <img src={profileData.profileinfo.pic} alt="Profile"   style={{height:60,width:60,borderRadius:"50%"}}/>
                 <div className='d-flex flex-column justify-content-around'>
                  <div className={styles.userName}>{profileData.userName}</div>
                  <div className={styles.changeProfileText} onClick={handleTextClick}>
                    Change Profile Photo
                  </div>
                 </div>
                  <input
                    type="file"
                    id="profileImageInput"
                    accept="image/*"
                    style={{ display: 'none' }}
                    onChange={handleFileChange}
                    ref={fileInputRef}
                  />
               </div>
               <div className={styles.inputRow}>
                 <div className={styles.inputGroup}>
                   <label > Name</label>
                   <input type="text" value={profiledata.name}  onChange={(e)=>setprofiledata({...profiledata,name:e.target.value})}/>
                 </div>
                 <div className={styles.inputGroup}>
                   <label> Email</label>
                   <input value={profiledata.email} type="email" onChange={(e)=>setprofiledata({...profiledata,email:e.target.value})}/>
                 </div>
                 <div className={styles.inputGroup}>
                   <label> Phone Number</label>
                   <Input type="text"placeholder={"Add Phone Number"} inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={profiledata.phoneNumber}  onChange={(e)=>setprofiledata({...profiledata,phoneNumber:e.target.value})} />
                 </div>
                 <div className={styles.inputGroup} >
                  <button onClick={()=>saveusersdata()}>Save</button>
                  </div>
               </div>
             </div>
        )}
        {activeSection === 2 && (
          <div className={styles.sectionContent}>
            <div className={styles.inputGroup}>
              <label> LinkedIn Profile</label>
              <input type="text" value={profileinfo.linkedinUrl} onChange={(e)=>setprofileinfo({...profileinfo,linkedinUrl:e.target.value})}/>
            </div>
            <div className={styles.inputGroup}>
              <label> GitHub Profile</label>
              <input type="text" value={profileinfo.githubUrl} onChange={(e)=>setprofileinfo({...profileinfo,githubUrl:e.target.value})}/>
            </div>
            <div className={styles.inputGroup}>
              <label>College </label>
              <input type="text" value={profileinfo.school} onChange={(e)=>setprofileinfo({...profileinfo,school:e.target.value})}/>
            </div>
            <div className={styles.inputGroup}>
              <label>Company</label>
              <input type="text" value={profileinfo.company} onChange={(e)=>setprofileinfo({...profileinfo,company:e.target.value})}/>
            </div>
            <div className={styles.inputGroup}>
              <label>Country</label>
              <input type="text" value={profileinfo.country} onChange={(e)=>setprofileinfo({...profileinfo,country:e.target.value})}/>
            </div>
            <div className={styles.inputGroup}>
              <label>City</label>
              <input type="text" value={profileinfo.city} onChange={(e)=>setprofileinfo({...profileinfo,city:e.target.value})}/>
            </div>
             <div className={styles.inputGroup} >
                  <button onClick={()=>saveuserInfo()}>Save</button>
                  </div>
          </div>
        )}
        {activeSection === 3 && (
          <div className={styles.sectionContent}>
          {whatsappdata.enabled===false &&  <p>Receive daily problem-solving challenges via Telegram bot and customize the number of problems you'd like to tackle on various platforms.</p>}
             <div className="d-flex" style={{paddingTop:15,marginRight:10}}>
            <h5 className={styles.subtitle}>Enable Telegram Messages</h5>
                <Switch
                onChange={()=>setwhatsapp()}
                checked={whatsappdata.enabled}
                onColor="#86d3ff"
                onHandleColor="#2693e6"
                handleDiameter={30}
                uncheckedIcon={false}
                checkedIcon={false}
                height={20}
                width={48}
            />
            </div>
          {whatsappdata.enabled && <div>
            <p className={styles.paratopic}>
            To receive Telegram messages, kindly initiate contact by sending the command Start to <a style={{textDecoration:'none',color:"#4096ff"}} href='https://t.me/LetsCodersBot' target='_blank'>LetsCodersBot</a> .
            </p>
           <h2 className={styles.titletopic}>
              CodeForces:
            </h2>
            <div className='d-md-flex justify-content-around align-items-center'>
            <div className={styles.inputGroup}>
              <label>Count</label>
              <Input type="text" placeholder={"Problems Count"}  inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={whatsappdata.cfCount} onChange={(e)=>{const numericValue = e.target.value.replace(/\D/g, '');setwhatsappdata({...whatsappdata,cfCount:numericValue})}}/>
            </div>
            <div className={styles.inputGroup}>
              <label>Rating</label>
              <Input type="text" placeholder={"Enter Rating"} inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={whatsappdata.rating} onChange={(e)=>{const numericValue = e.target.value.replace(/\D/g, '');setwhatsappdata({...whatsappdata,rating:numericValue})}}/>
            </div>
            </div>
            <h2 className={styles.titletopic}>
              LeetCode:
            </h2>
            <div className='d-md-flex justify-content-around align-items-center'>
            <div className={styles.inputGroup}>
              <label>Count</label>
              <Input type="text"placeholder={"Problems Count"} inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={whatsappdata.lcCount} onChange={(e)=>{const numericValue = e.target.value.replace(/\D/g, '');setwhatsappdata({...whatsappdata,lcCount:numericValue})}}/>
            </div>
            <div className={styles.inputGroup}>
            <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
                <DropdownToggle caret className={`${styles.dropdowns} `} style={{backgroundColor:"#3a3a3a"}}>
                    {whatsappdata.difficulty} 
                </DropdownToggle>
                <DropdownMenu style={{backgroundColor:"#3a3a3a",color:"white"}}>
                <DropdownItem  className={ styles["white-text"]}  onClick={() =>setwhatsappdata({...whatsappdata,difficulty:"ALL"})}>
                    ALL
                    </DropdownItem>
                    <DropdownItem   className={ styles["green-text"]} onClick={() =>setwhatsappdata({...whatsappdata,difficulty:"Easy"})}>
                    Easy
                    </DropdownItem>
                  <DropdownItem  className={styles["yellow-text"] }   onClick={() =>setwhatsappdata({...whatsappdata,difficulty:"Medium"})}>
                    Medium
                    </DropdownItem>
                  <DropdownItem  className={ styles["red-text"]}   onClick={() =>setwhatsappdata({...whatsappdata,difficulty:"Hard"})}>
                  Hard
                  </DropdownItem>
                </DropdownMenu>
            </Dropdown>
            </div>
            </div>
            <h2 className={styles.titletopic}>
              AtCoder:
            </h2>
            <div className='d-md-flex justify-content-around align-items-center'>
            <div className={styles.inputGroup}>
              <label>Count</label>
              <Input type="text" placeholder={"Problems Count"} inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={whatsappdata.atCount} onChange={(e)=>{const numericValue = e.target.value.replace(/\D/g, '');setwhatsappdata({...whatsappdata,atCount:numericValue})}}/>
            </div>
            <div className={styles.inputGroup}>
            <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
                <DropdownToggle caret className={`${styles.dropdowns} `} style={{backgroundColor:"#3a3a3a"}}>
                    {whatsappdata.atType} 
                </DropdownToggle>
                <DropdownMenu style={{backgroundColor:"#3a3a3a",color:"white"}}>
                <DropdownItem  className={ styles["white-text"]}  onClick={() =>setwhatsappdata({...whatsappdata,atType:"ALL"})}>
                    ALL
                    </DropdownItem>
                    <DropdownItem   className={ styles["green-text"]} onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem A"})}>
                    Problem A
                    </DropdownItem>
                  <DropdownItem  className={styles["green-text"] } onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem B"})}>
                  Problem B
                    </DropdownItem>
                  <DropdownItem  className={ styles["green-text"]}  onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem C"})}>
                  Problem C
                  </DropdownItem>
                  <DropdownItem  className={ styles["yellow-text"]}  onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem D"})}>
                  Problem D
                  </DropdownItem>
                  <DropdownItem  className={ styles["yellow-text"]}   onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem E"})}>
                  Problem E
                  </DropdownItem>
                  <DropdownItem  className={ styles["red-text"]}    onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem F"})}>
                  Problem F
                  </DropdownItem>
                  <DropdownItem  className={ styles["red-text"]}    onClick={() =>setwhatsappdata({...whatsappdata,atType:"Problem G"})}>
                  Problem G
                  </DropdownItem>
                </DropdownMenu>
            </Dropdown>
            </div>
            </div>
  
           </div>}
            <div className={styles.inputGroup} >
                  <button onClick={()=>saveuserstat()}>Save</button>
                  </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default EditProfilePage;
