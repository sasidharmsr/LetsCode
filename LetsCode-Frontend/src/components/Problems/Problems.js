import React, { useEffect, useRef, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faCheckCircle, faRandom, faSearch, faTimes } from '@fortawesome/free-solid-svg-icons';
import styles from '../../assets/problems.module.css';
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import { Dropdown ,DropdownButton, Pagination} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/esm/DropdownToggle";
import DropdownItem from "react-bootstrap/esm/DropdownItem";
import { Input } from 'reactstrap';
import FilterComponent from './Filters';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { getProblems } from '../../store/actions/postActions';
import Loader from '../Common/Loading';

const ProblemComponent = () => {

    const dispatch=useDispatch()
    const navigate=useNavigate()
    const [selectedPlatform,setSelectedPlatform]=useState("Codeforces")
    const [tags,settags]=useState(false)
    const [difficulty,setdifficultyy]=useState('')
    const [status,setstatus]=useState(0)
    const [firstPage,setfirstPage]=useState(true)
    const [inputvalue,setinputvalue]=useState('')
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const problems=useSelector((state)=>(Object(state.topics.problems)))
    const totalProblems=useSelector((state)=>(Object(state.topics.count)))
    const topics=useSelector((state)=>(Object(state.topics.topics)))
    const map={"LeetCode":"lc","Codeforces":"cf","AtCoder":"at"}
    const difmap={"Easy":1,"Medium":2,"Hard":3,"":0,"#":0}

    const [currentPage, setCurrentPage] = useState(1);
    const problemsPerPage = 30; 
    const userData=useSelector(state=>Object(state.auth))


  const toggleDropdown = () => setDropdownOpen(!dropdownOpen);
  const obj={}
  topics.map((topic)=>{
    obj[topic.id]=topic.topicName;
  })
    useEffect(()=>{
      if(map[selectedPlatform]==="cf")
        dispatch(getProblems(map[selectedPlatform],status,"",false,0,3500,0,0,inputvalue,navigate))
      else if(map[selectedPlatform]==="lc")
        dispatch(getProblems(map[selectedPlatform],status,"",false,0,100,0,0,inputvalue,navigate))
    },[selectedPlatform])
    const handlePlatformChange = (platform) => {
      setSelectedPlatform(platform);
      setdifficultyy('')
      setCurrentPage(1)
      settags(false);setstatus(0);
    };
    const handleInputChange=(e)=>{
      setinputvalue(e.target.value)
      if(e.target.value===''){
        if(selectedPlatform==='lc')
        dispatch(getProblems(map[selectedPlatform],status,"",false,0,100,0,0,"",navigate))
      else
        dispatch(getProblems(map[selectedPlatform],status,"",false,0,3500,0,0,"",navigate))
      }
    }

    const handlePageChange = (pageNumber,event) => {
      event.preventDefault();
      if(pageNumber===currentPage)return
      setCurrentPage(pageNumber);
    };

    const openRandomProblem = () => {
      const randomIndex = Math.floor(Math.random() * problems.length);
      const randomProblem = problems[randomIndex];
      let url=""
      if(map[selectedPlatform]==='lc')url=`https://leetcode.com/problems/${randomProblem.path}/`
      else url=`https://codeforces.com/problemset/problem/${randomProblem.contestId}/${randomProblem.indicator}`
      window.open(url, '_blank');
    };

  const topicnames=(value)=>{
      value=value.split(",")
      let topicNames=""
      for(const val of value){
        topicNames+=obj[val]+", ";
      }
      return topicNames.substring(0,topicNames.length-2);
  }
  const addtopic=(curtopics,and,ratingL,ratingR)=>{
      let res=""
      curtopics.map((element,index) =>{
        if(element)res+=index+1+","
      });
      res=res.substring(0,res.length-1)
      dispatch(getProblems(map[selectedPlatform],status,res,and,ratingL,ratingR,difmap[difficulty],0,inputvalue,navigate))
      setfirstPage(false)
      setCurrentPage(1)
  }
  const filterbyRating=(res,and,ratingL,ratingR,val)=>{
    
    if(val!==4)
      dispatch(getProblems(map[selectedPlatform],status,res,and,ratingL,ratingR,difmap[difficulty],0,inputvalue,navigate))
    else if(firstPage)
      dispatch(getProblems(map[selectedPlatform],status,res,and,ratingL,ratingR,difmap[difficulty],(currentPage-1)*problemsPerPage,inputvalue,navigate))
    if(val===0 && selectedPlatform==='Codeforces')setdifficultyy('')
    if(val!==4){
      setfirstPage(false)
      setCurrentPage(1)
    }else{
      setfirstPage(true)
    }
  }
  const setdifficulty=(value)=>{
    if(difficulty===value)setdifficultyy('#')
    else setdifficultyy(value)
  }
  const clearInput = () => {
    if(selectedPlatform==='lc')
    dispatch(getProblems(map[selectedPlatform],status,"",false,0,100,0,0,"",navigate))
  else
    dispatch(getProblems(map[selectedPlatform],status,"",false,0,3500,0,0,"",navigate))
    setinputvalue('');
  };
  const handleSubmit=()=>{
    if(selectedPlatform==='lc')
      dispatch(getProblems(map[selectedPlatform],status,"",false,0,100,0,0,inputvalue,navigate))
    else
      dispatch(getProblems(map[selectedPlatform],status,"",false,0,3500,0,0,inputvalue,navigate))
  }
  return (
    <div className='fluid-container' style={{backgroundColor:"#1a1a1a"}}>
    <div className='row'>
    <div className={`${styles.container} col-12 col-lg-9`}>
      <div className={styles.header}>
        <div className={`${styles.row} row mt-3`}>
          <div className="col-md-6 col-12">
            <h2 className={styles.maintitle}>{selectedPlatform} Problems</h2>
          </div>
          <div className="col-md-6 d-none d-md-flex align-items-center justify-content-end" >
          <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={`${styles.maindropdown} `} style={{backgroundColor:"#555555"}}>
                {selectedPlatform}
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#3a3a3a",color:"white"}}>
            <DropdownItem  className={selectedPlatform== "LeetCode"? styles["red-text"] : styles["white-text"]}   onClick={() => handlePlatformChange("LeetCode")}>LeetCode</DropdownItem>
                <DropdownItem   className={selectedPlatform== "Codeforces" ? styles["red-text"] : styles["white-text"]}   onClick={() => handlePlatformChange("Codeforces")}>Codeforces</DropdownItem>
            </DropdownMenu>
        </Dropdown>
          </div>
        </div>
      </div>
      <div className={`${styles.row} row`}>
        <div className="col-md-2 col-6">
        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={`${styles.dropdowns} `} style={{backgroundColor:"#3a3a3a"}}>
                Status
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#3a3a3a"}}>
                <DropdownItem  className={styles["white-text"]}   onClick={() =>setstatus(0)}>
                <div className='d-flex justify-content-between align-items-center'><span>Show Solved</span>
                  {status===0 &&<FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div></DropdownItem>
               <DropdownItem className={styles["white-text"]}  onClick={() => setstatus(2)}>
               <div className='d-flex justify-content-between align-items-center'><span>Hide Solved</span>
                  {status===2 && <FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div>
               </DropdownItem>
            </DropdownMenu>
        </Dropdown>
        </div>
        <div className="col-6 d-md-none " >
          <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={`${styles.maindropdown} `} style={{backgroundColor:"#555555"}}>
                {selectedPlatform}
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#3a3a3a",color:"white"}}>
            <DropdownItem  className={selectedPlatform== "LeetCode"? styles["red-text"] : styles["white-text"]}   onClick={() => handlePlatformChange("LeetCode")}>LeetCode</DropdownItem>
                <DropdownItem   className={selectedPlatform== "Codeforces" ? styles["red-text"] : styles["white-text"]}   onClick={() => handlePlatformChange("Codeforces")}>Codeforces</DropdownItem>
            </DropdownMenu>
        </Dropdown>
          </div>
        <div className="col-md-2 col-6">
        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={`${styles.dropdowns} `} style={{backgroundColor:"#3a3a3a"}}>
                Difficulty 
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#3a3a3a",color:"white"}}>
                <DropdownItem   className={ styles["green-text"]}  onClick={() =>setdifficulty("Easy")}>
                  <div className='d-flex justify-content-between align-items-center'><span>Easy</span>
                  {difficulty==='Easy' && <FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div>
                </DropdownItem>
               <DropdownItem  className={styles["yellow-text"] }   onClick={() => setdifficulty("Medium")}>
               <div className='d-flex justify-content-between align-items-center'><span>Medium</span>
               {difficulty==='Medium' && <FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div>
                </DropdownItem>
               <DropdownItem  className={ styles["red-text"]}   onClick={() => setdifficulty("Hard")}>
               <div className='d-flex justify-content-between align-items-center'><span>Hard</span>
               {difficulty==='Hard' &&  <FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div>
               </DropdownItem>
            </DropdownMenu>
        </Dropdown>
        </div>
        <div className="col-md-2 col-6 d-none d-md-block">
        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={`${styles.dropdowns} `} style={{backgroundColor:"#3a3a3a"}}>
                Tags
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#3a3a3a",color:"white"}}>
                <DropdownItem   className={ styles["white-text"]}   onClick={() =>settags(true)}>
                <div className='d-flex justify-content-between align-items-center'><span>Show Tags</span>
                 {tags && <FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div>
                </DropdownItem>
               <DropdownItem  className={styles["white-text"]}   onClick={() => settags(false)}>
               <div className='d-flex justify-content-between align-items-center'><span>Hide Tags</span>
                  {!tags && <FontAwesomeIcon icon={faCheck} style={{ fontSize:18,color: '#0a84ff' }} />}</div>
               </DropdownItem>
            </DropdownMenu>
        </Dropdown>
        </div>
        <div className="col-md-2 col-6 d-md-none">
          <div className={styles.iconText} onClick={openRandomProblem}>
            <FontAwesomeIcon icon={faRandom} className={styles.icon} />
            <span style={{marginTop:10}}>Pick one</span>
          </div>
        </div>
        <div className="col-md-4 d-flex align-items-center ">
        <Input
        type="text"
        className={styles.input}
        value={inputvalue}
        onChange={(e)=>handleInputChange(e)}
        placeholder="Search Question"
      />
       {inputvalue!=='' && (
        <div className={styles.clearIconContainer}>
          <FontAwesomeIcon
            icon={faTimes}
            className={styles.clearIcon}
            onClick={clearInput}
          />
        </div>
      )}
         <FontAwesomeIcon icon={faSearch} className={styles.searchIcon} onClick={()=>handleSubmit()}/>
        </div>
        <div className="col-md-2 d-md-block d-none">
          <div className={styles.iconText} onClick={openRandomProblem}>
            <FontAwesomeIcon icon={faRandom} className={styles.icon} />
            <span style={{marginTop:10}}>Pick one</span>
          </div>
        </div>
      </div>
      <div className={styles.tableContainer}>
        <table >
          <thead>
            {
              map[selectedPlatform]==='cf'?
              <tr>
              <th style={{textAlign:"center"}}>#</th>
              <th>Title</th>
              <th>Rating</th>
              <th>Status</th>
            </tr>:
              <tr>
              <th style={{textAlign:"center"}}>Id</th>
              <th>Title</th>
              <th>Difficulty</th>
              <th>AcRate</th>
              <th>Status</th>
            </tr>
            }

          </thead>
          <tbody>
            {
              problems.map((problem)=>{
                if(map[selectedPlatform]==='lc' && problem.acRate!==undefined)return(
                  <tr>
                    <td style={{width:"10%",textAlign:"center"}} className={styles.cell}>{problem.id}</td>
                    <td className={`${styles.cell}`} >
                      <div className='w-100 d-flex justify-conetent-between align-items-center'>
                      <span className={styles.ProblemName}>
                        <a  href={`https://leetcode.com/problems/${problem.path}/`} style={{textDecoration:"none",color:"white"}} target='_blank'>{problem.title}</a>
                        </span>
                      {tags && <span className={styles.topicNames}>{topicnames(problem.topicsIds)}</span>}
                      </div>
                      </td>
                    <td style={{width:"10%",textAlign:"center"}}  className={`${styles.cell} ${styles[problem.difficulty]}` }>{problem.difficulty}</td>
                    <td style={{width:"10%",textAlign:"center"}}  className={styles.cell}>{problem.acRate===undefined?'':problem.acRate.toFixed(2)}</td>
                    <td  style={{width:"10%",textAlign:"center"}} className={styles.cell}>
                     {problem.submissionId===null ? <></> :<FontAwesomeIcon icon={faCheckCircle} style={{ color: 'green' }} />}
                      </td>
                </tr>
                )
                else if(map[selectedPlatform]==='cf'  && problem.rating!==undefined)
                  return(
                    <tr>
                      <td style={{width:"10%",textAlign:"center"}} className={styles.cell}>{problem.contestId}</td>
                      <td className={`${styles.cell}`} >
                        <div className='w-100 d-flex justify-conetent-between align-items-center'>
                        <span className={styles.ProblemName}>
                          <a href={`https://codeforces.com/problemset/problem/${problem.contestId}/${problem.indicator}`} style={{textDecoration:"none",color:"white"}} target='_blank'>{problem.indicator+". "+problem.title}</a>
                          </span>
                        {tags && <span className={styles.topicNames}>{topicnames(problem.topicsIds)}</span>}
                        </div>
                        </td>
                      <td style={{width:"10%",textAlign:"center"}}  className={`${styles.cell} ${problem.rating<1400 ? styles["Easy"] : problem.rating<1900 ?styles["Medium"]:styles["Hard"]}`}>{problem.rating}</td>
                      <td  style={{width:"10%",textAlign:"center"}} className={styles.cell}>
                       {problem.submissionId===null ? <></> :<FontAwesomeIcon icon={faCheckCircle} style={{ color: 'green' }} />}
                        </td>
                  </tr>
                  )
              })
            }
          </tbody>
        </table>
      </div>
      {userData.loading && <Loader/>}
      <div className={styles.pagination}>
        <Pagination>
          <Pagination.Prev
            onClick={handlePageChange.bind(null, currentPage - 1)}
            disabled={currentPage === 1}
          />
        {currentPage > 3 && (
          <Pagination.Item onClick={handlePageChange.bind(null, 1)}>
            1
          </Pagination.Item>
        )}
        {currentPage > 3 && <Pagination.Ellipsis disabled />}
        {currentPage > 2 && (
          <Pagination.Item  onClick={handlePageChange.bind(null, currentPage - 2)}>
            {currentPage - 2}
          </Pagination.Item>
        )}
        {currentPage > 1 && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage - 1)}>
            {currentPage - 1}
          </Pagination.Item>
        )}
        <Pagination.Item active >{currentPage}</Pagination.Item>
        {currentPage < Math.ceil(totalProblems / problemsPerPage) && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage + 1)}>
            {currentPage + 1}
          </Pagination.Item>
        )}
        {currentPage < Math.ceil(totalProblems / problemsPerPage) - 1 && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage + 2)}>
            {currentPage + 2}
          </Pagination.Item>
        )}
        {currentPage < Math.ceil(totalProblems / problemsPerPage) - 2 && <Pagination.Ellipsis disabled />}
        {currentPage < Math.ceil(totalProblems / problemsPerPage) - 2 && (
          <Pagination.Item onClick={handlePageChange.bind(null, Math.ceil(totalProblems / problemsPerPage))}>
            {Math.ceil(totalProblems / problemsPerPage)}
          </Pagination.Item>
        )}
          <Pagination.Next
            onClick={handlePageChange.bind(null, currentPage + 1)}
            disabled={currentPage === Math.ceil(totalProblems / problemsPerPage)}
          />
        </Pagination>
      </div>
    </div>
    <div className='col-lg-3' style={{borderLeft:"1px solid white"}}>
        <FilterComponent currentPage={currentPage} selectedPlatform={map[selectedPlatform]} difficulty={difficulty} status={status} filterbyRating={filterbyRating} addtopic={addtopic} topics={topics}/>
    </div>
    </div>
    </div>
  );
};

export default ProblemComponent;
