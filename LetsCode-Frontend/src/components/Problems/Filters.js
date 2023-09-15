import React, { useEffect, useState } from 'react';
import { Input, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleLeft, faAngleRight, faTimes } from '@fortawesome/free-solid-svg-icons';
import styles from '../../assets/fileters.module.css'
import Switch from "react-switch";

const FilterComponent = ({topics,currentPage,selectedPlatform,difficulty,status,addtopic,filterbyRating}) => {
 const [and,setand] =useState(true);
 const [topicbtn,settopicbtn]=useState(topics.map(() => false))
 const [rating,setrating]=useState({left:0,right:selectedPlatform==='cf'?3500:100})
 const [input,setinputval] =useState('')
 const [clearrating,setclearrating]=useState(false);
 const [error,seterror]=useState(false)
 const [stopintialcal,setstopintialcal]=useState(false)
  const toggleAnd=()=>{
    let res=""
    topicbtn.map((element,index) =>{
      if(element)res+=index+1+","
    });
    if(res!=="")
      filterbyRating(res,!and,rating.left,rating.right,1)
    setand(!and)
 }

 useEffect(()=>{
  if(selectedPlatform==='lc') setrating({left:0,right:100})
  else setrating({left:0,right:3500})
  settopicbtn(topics.map(() => false))
 },[selectedPlatform])

  useEffect(()=>{
    if(stopintialcal){
    let res=""
    topicbtn.map((element,index) =>{
      if(element)res+=index+1+","
    });
      filterbyRating(res,and,rating.left,rating.right,1)
    }
    else{
      setTimeout(()=>{
        console.log("i wake up")
        setstopintialcal(true)
      },[2000])
  }
  },[status])

  useEffect(()=>{
      if(stopintialcal){
        let res=""
        topicbtn.map((element,index) =>{
          if(element)res+=index+1+","
        });
      filterbyRating(res,and,rating.left,rating.right,4)
      }
  },[currentPage])

  useEffect(()=>{
    if(difficulty!=='' && selectedPlatform==='cf'){
        let res=""
      topicbtn.map((element,index) =>{
        if(element)res+=index+1+","
      });
      if(difficulty==='#'){
        setrating({left:0,right:3500})
        filterbyRating(res,and,0,3500,1);
      }else if(difficulty==="Easy"){
        setrating({left:800,right:1300})
        filterbyRating(res,and,0,1300,1);
      }
      else if(difficulty==="Medium"){
        setrating({left:1400,right:1800})
        filterbyRating(res,and,1301,1800,1);
      }
      else if(difficulty==="Hard"){
        setrating({left:1900,right:3500})
        filterbyRating(res,and,1801,3500,1);
      }
      if(clearrating)setclearrating(false)
    }
    else if(difficulty!=='' && selectedPlatform=='lc'){
        let res=""
      topicbtn.map((element,index) =>{
        if(element)res+=index+1+","
      });
      filterbyRating(res,and,rating.left,rating.right,0)
    }
  },[difficulty])

 const filterRating=()=>{
  if(parseInt(rating.left)>parseInt(rating.right)){
    seterror(true)
    setTimeout(()=>{
      seterror(false)
    },2000)
  }else{
    let res=""
    topicbtn.map((element,index) =>{
      if(element)res+=index+1+","
    });
    setclearrating(true)
    console.log(rating)
    filterbyRating(res,and,rating.left,rating.right,0)
  }
 }
 const clearratingfun=()=>{
  if(selectedPlatform==='cf')
    setrating({left:0,right:3500});
  else setrating({left:0,right:100});
  setclearrating(false)
  let res=""
    topicbtn.map((element,index) =>{
      if(element)res+=index+1+","
    });
    if(selectedPlatform==='cf')filterbyRating(res,and,0,3500,1)
    else filterbyRating(res,and,0,100,1)
 }
 const toggletopic=(id)=>{
    const updatedTopicbtn = [...topicbtn];
    updatedTopicbtn[id] = !updatedTopicbtn[id];
    addtopic(updatedTopicbtn,and,rating.left,rating.right)
    settopicbtn(updatedTopicbtn);
 }

  return (
    <div className={styles.mainContainer}>

      <div className={styles.componentContainer}>
        <div className={styles.componentHeader}>
          <h3 style={{fontSize:23,fontWeight:500}}>{`Filter ${selectedPlatform=='cf' ? "CF":"LC"} Problems`}</h3>
        </div>
        <div className={styles.componentBody}>
          <div className={styles.filterRow}>
            <div className='w-100 d-flex justify-content-between'>
            <h5>{`${selectedPlatform==='lc'?'Acceptance Rate':'Problem Rating'} `}</h5>
            {clearrating && <FontAwesomeIcon onClick={()=>clearratingfun()} icon={faTimes} style={{ cursor:"pointer",color: 'red', fontSize: '24px',marginRight:10 }} />}
            </div>
            <div className='d-flex align-items-center'>
            <Input type="text" inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={rating.left} onChange={(e)=>{const numericValue = e.target.value.replace(/\D/g, '');setrating({left:numericValue,right:rating.right})}}/>
            <Input type="text" inputMode="numeric" pattern="[0-9]*" className={styles.inputField} value={rating.right} onChange={(e)=>{const numericValue = e.target.value.replace(/\D/g, '');setrating({right:numericValue,left:rating.left})}}/>
            <Button  className={styles.Button} onClick={()=>filterRating()}>Apply</Button>
            </div>
            {error && <div style={{color:"red",fontSize:15}}>*Larger value on the Left</div>}
          </div>
        </div>
      </div>

      <div className={styles.componentContainer}>
      <div className={styles.componentHeader}>
        <h4>Filter Topics</h4>
      </div>
      <div style={{ marginTop: 10 }}>
        <Input type="text" placeholder="Search" className={styles.searchBar} onChange={(e)=>setinputval(e.target.value)} />
      </div>
      <div className='d-flex mt-3 justify-content-between'>
        <h5>Apply AND</h5>
        <Switch
          onChange={toggleAnd}
          checked={and}
          onColor="#86d3ff"
          onHandleColor="#2693e6"
          handleDiameter={25}
          uncheckedIcon={false}
          checkedIcon={false}
          height={20}
          width={48}
        />
      </div>
      <div className={styles.componentbelowBody}>
        <div
          className={styles.maintopics}
        >
          <div className={styles.topicList}>{topics.map((topic, index) => {
            if(topic.cfName==="" && selectedPlatform==="cf")return;
            if(topic.slug==="" && selectedPlatform==="lc")return;
            if(topic.topicName.toLowerCase().includes(input.toLowerCase())===false)return;
            return(
              <div key={index} className={topicbtn[topic.id-1] ? styles.activetopicButton: styles.topicButton} onClick={()=>toggletopic(topic.id-1)}>
                {topic.topicName}
              </div>)
            })}
          </div>
        </div>
      </div>
    </div>
    </div>
  );
};

export default FilterComponent;
