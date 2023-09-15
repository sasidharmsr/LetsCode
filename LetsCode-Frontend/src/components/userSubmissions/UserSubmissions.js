import React, { useEffect, useRef, useState } from "react";
import styles from "../../assets/contests.module.css"
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import { Dropdown } from "react-bootstrap";
import DropdownToggle from "react-bootstrap/esm/DropdownToggle";
import DropdownItem from "react-bootstrap/esm/DropdownItem";
import Switch from "react-switch";
import Pagination from "react-bootstrap/Pagination";
import { useDispatch, useSelector } from "react-redux";
import { getContests, getUserSubmissions } from "../../store/actions/userActions";
import { useLocation, useNavigate } from "react-router-dom";
import DateInputComponent from "./datepicker";
import Loader from "../Common/Loading";

const UserSubmissions = () => {

  const [selectedPlatform, setSelectedPlatform] = useState("ALL");
  

  const urlParams = new URLSearchParams(window.location.search);
  const date = urlParams.get("date");
  const userData=useSelector(state=>Object(state.auth))
  const submissionData=useSelector(state=>Object(state.topics));

  const dispatch=useDispatch();
  const navigate=useNavigate();

  const [currentPage, setCurrentPage] = useState(1);
  const submissionsPerPage = 20; 

    const map={"ALL":"all","LeetCode":"lc","Codeforces":"cf","AtCoder":"at"}

    const revmap={"lc":"LeetCode","cf":"CodeForces","at":"AtCoder"}

    const colmap={"lc":1,"at":3,"cf":6}

  useEffect(()=>{
    dispatch(getUserSubmissions(map[selectedPlatform],date,0,navigate))
  },[selectedPlatform,date])

  const handlesearch=(searchdate)=>{
    navigate(`/submissions/all?date=${searchdate}`)
    dispatch(getUserSubmissions(map[selectedPlatform],searchdate,0,navigate))
  }
  const totalProblems=submissionData.total;

  const handlePlatformChange = (curplatform) => {

    setSelectedPlatform(curplatform);
  };

  const handlePageChange = (pageNumber,event) => {
    event.preventDefault();
    if(pageNumber===currentPage)return
    dispatch(getUserSubmissions(map[selectedPlatform],date,(pageNumber-1)*submissionsPerPage,navigate))
    setCurrentPage(pageNumber);
  };


  const modify=(path,type)=>{
    if(path===null || path===undefined)return
    if(type=='lc'){
        return `https://leetcode.com/problems/${path}/`;
    }
    else if(type=='cf'){
        const parts = path.split("/");
        return `https://codeforces.com/contest/${parts[0]}/problem/${parts[1]}`
    }
    const parts = path.split("_");
    return `https://atcoder.jp/contests/${parts[0]}/tasks/${path}`
  }

  const modifysubmission=(stamp,path,type)=>{ 
    if(path===null || path===undefined)return
    if(type=='lc'){
        return `https://leetcode.com/submissions/detail/${stamp}/`;
    }
    else if(type=='cf'){
        const parts = path.split("/");
        return `https://codeforces.com/contest/${parts[0]}/submission/215629974`
    }
    const parts = path.split("_");
    return `https://atcoder.jp/contests/${parts[0]}/submissions/${stamp}`
  }

  const cftitle=(path,title)=>{
    const parts = path.split("/");
    return parts[1]+". "+title;
  }


  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggleDropdown = () => setDropdownOpen(!dropdownOpen);

 
  return (
    <div className={styles["contest-component"]}>
      <div className={styles.header}>
        <div className='d-flex'>
        <div className={styles["submission-title"]}  style={{paddingTop:14}} >{`${selectedPlatform} Submissions(${submissionData.total})`}</div>
         <div className="d-md-none">
            <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={ styles.dropdown} style={{backgroundColor:"#555555"}}>
                {selectedPlatform}
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
            <DropdownItem  className={selectedPlatform=="ALL" ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("ALL")}>ALL</DropdownItem>
            <DropdownItem   className={selectedPlatform== "LeetCode" ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("LeetCode")}>LeetCode</DropdownItem>
                <DropdownItem   className={selectedPlatform=="Codeforces"  ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("Codeforces")}>Codeforces</DropdownItem>
               <DropdownItem  className={selectedPlatform=="AtCoder" ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("AtCoder")}>AtCoder</DropdownItem>
            </DropdownMenu>
        </Dropdown>
            </div>
        </div>
        <div className="d-flex ">
            <DateInputComponent handlesearch={handlesearch}/>
            <div className="d-none d-md-block" style={{marginRight:-20}}>
            <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={ styles.dropdown} style={{backgroundColor:"#555555"}}>
                {selectedPlatform}
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
            <DropdownItem  className={selectedPlatform=="ALL" ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("ALL")}>ALL</DropdownItem>
            <DropdownItem   className={selectedPlatform== "LeetCode" ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("LeetCode")}>LeetCode</DropdownItem>
                <DropdownItem   className={selectedPlatform=="Codeforces"  ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("Codeforces")}>Codeforces</DropdownItem>
               <DropdownItem  className={selectedPlatform=="AtCoder" ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange("AtCoder")}>AtCoder</DropdownItem>
            </DropdownMenu>
        </Dropdown>
            </div>
        </div>
      </div>
      <div className={styles.content}>
        <table>
          <thead>
            <tr className="d-none d-md-table-row">
              <th>Date</th>
              <th> Problem Name</th>
              <th>PlatForm</th>
              <th>Detail</th>
            </tr>
          </thead>
          <tbody>
            {
                submissionData.userSubmissions.map((submission)=>{
                  if(submission.uniqureId===null && submission.uniqureId===undefined)return; 
                   return (<tr className={styles.tableRow} key={submission.uniqureId}>
                    <th className={styles.cell}>{submission.date}</th>
                    <th className={styles.cell}><a className={`${styles.celllink}` } href={modify(submission.uniqureId,submission.type)} target="_blank">{submission.type=='cf'? cftitle(submission.uniqureId,submission.title) : submission.title}</a></th>
                    <th className={`${styles.cell} ${styles[`cell-${colmap[submission.type]}`]}`} >{revmap[submission.type]}</th>
                    <th className={styles.cell}><a className={`${styles.celllink}` } href={modifysubmission(submission.submissionId,submission.uniqureId,submission.type)} target="_blank">Detail</a></th>
                  </tr>)
                })
            }
          </tbody>
        </table>
      </div>
      <div className={styles.pagination}>
      {userData.loading && <Loader/>}
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
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage - 2)}>
            {currentPage - 2}
          </Pagination.Item>
        )}
        {currentPage > 1 && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage - 1)}>
            {currentPage - 1}
          </Pagination.Item>
        )}
        <Pagination.Item active >{currentPage}</Pagination.Item>
        {currentPage < Math.ceil(totalProblems / submissionsPerPage) && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage + 1)}>
            {currentPage + 1}
          </Pagination.Item>
        )}
        {currentPage < Math.ceil(totalProblems / submissionsPerPage) - 1 && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage + 2)}>
            {currentPage + 2}
          </Pagination.Item>
        )}
        {currentPage < Math.ceil(totalProblems / submissionsPerPage) - 2 && <Pagination.Ellipsis disabled />}
        {currentPage < Math.ceil(totalProblems / submissionsPerPage) - 2 && (
          <Pagination.Item onClick={handlePageChange.bind(null, Math.ceil(totalProblems / submissionsPerPage))}>
            {Math.ceil(totalProblems / submissionsPerPage)}
          </Pagination.Item>
        )}
          <Pagination.Next
            onClick={handlePageChange.bind(null, currentPage + 1)}
            disabled={currentPage === Math.ceil(totalProblems / submissionsPerPage)}
          />
        </Pagination>
      </div>
    </div>
  );
};

export default UserSubmissions;
