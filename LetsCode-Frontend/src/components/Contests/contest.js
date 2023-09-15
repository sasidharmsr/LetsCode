import React, { useEffect, useRef, useState } from "react";
import styles from "../../assets/contests.module.css"
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import { Dropdown } from "react-bootstrap";
import DropdownToggle from "react-bootstrap/esm/DropdownToggle";
import DropdownItem from "react-bootstrap/esm/DropdownItem";
import Switch from "react-switch";
import Pagination from "react-bootstrap/Pagination";
import { useDispatch, useSelector } from "react-redux";
import { getContests } from "../../store/actions/userActions";
import { useNavigate } from "react-router-dom";
import Loader from "../Common/Loading";

const ContestComponent = () => {

  const [selectedPlatform, setSelectedPlatform] = useState(true);
  const [type,setType] =useState(2)
  const [attype ,setattype]=useState(0);

  const contestsData=useSelector(state=>Object(state.home.contests));
  const userData=useSelector(state=>Object(state.auth))

  const dispatch=useDispatch();
  const navigate=useNavigate();

  const typeObj={"2":"Div 3","1":"Educational Round","3":"Div 4","4":"Div 2","6":"Div 1"}
  const attypeObj={"0":"Beginer","1":"Regular","2":"ABC","3":"ARC"}

  useEffect(()=>{
    if(selectedPlatform)dispatch(getContests(selectedPlatform,123456,type,navigate))
    else {
      if(attype) dispatch(getContests(selectedPlatform,"arc9999",attype+1,navigate))
      else  dispatch(getContests(selectedPlatform,"abc9999",attype+1,navigate))
    }
      
  },[type,attype])

  const totalProblems=contestsData.total;

  const [currentPage, setCurrentPage] = useState(1);
  const contestsPerPage = 20; 

  const handlePlatformChange = () => {
    if(selectedPlatform){
      if(attype) dispatch(getContests(!selectedPlatform,"arc999",attype+1,navigate))
      else  dispatch(getContests(!selectedPlatform,"abc999",attype+1,navigate))
    }
    else
     dispatch(getContests(!selectedPlatform,123456,type,navigate))
    setSelectedPlatform(!selectedPlatform);
  };

  const handlePageChange = (pageNumber,event) => {
    event.preventDefault();
    if(pageNumber===currentPage)return
    if(selectedPlatform)
      dispatch(getContests(selectedPlatform,(contestsData.contest_id+1)-((pageNumber-currentPage-1)*230),type,navigate))
    else{
      if(!attype)dispatch(getContests(selectedPlatform,"abc"+(contestsData.contest_id-((pageNumber-currentPage)*(contestsPerPage-1))),attype+1,navigate))
      else dispatch(getContests(selectedPlatform,"arc"+contestsData.contest_id-((pageNumber-currentPage)*(contestsPerPage-1)),attype+1,navigate))
    }
    setCurrentPage(pageNumber);
  };

//   const componentRef = useRef();

//   useEffect(() => {
//     // Scroll to the top of the component after the page changes
//     if (componentRef.current) {
//       componentRef.current.scrollIntoView({ behavior: "smooth" });
//     }
//   }, [currentPage]);



  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggleDropdown = () => setDropdownOpen(!dropdownOpen);

  const [solved, setsolevd] = useState(false);

  const handleChange = () => {
    setsolevd(!solved);
  };
  return (
    <div className={`${styles["contest-component"]}`}>
      <div className={`${styles.header} row`}>
        <div className="d-flex col-12 col-lg-6">
          {userData.loading && <Loader/>}
        <div className={styles["contest-title"]} >{`${selectedPlatform?"Codeforces":"AtCoder"} ${selectedPlatform?typeObj[type]:attypeObj[attype]} Contests`}</div>
        <div className="d-none d-md-block">
        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={type=="1" ?`${styles.dropdown} ${styles.eddropdown}`:styles.dropdown} style={{backgroundColor:"#555555"}}>
                {selectedPlatform?typeObj[type]:attypeObj[attype+2]}
            </DropdownToggle>
            { selectedPlatform ?
              <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
             <DropdownItem   className={typeObj[type] == "Div 1" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(6)}>Div 1</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Div 2" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(4)}>Div 2</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Div 3" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(2)}>Div 3</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Div 4" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(3)}>Div 4</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Educational Round" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(1)}>Educational Round</DropdownItem>
           </DropdownMenu>
         :
         <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
            <DropdownItem   className={attypeObj[type] == "Div 1" ? styles["red-text"] : styles["green-text"]}   onClick={() => setattype(0)}>ABC</DropdownItem>
              <DropdownItem  className={attypeObj[type] == "Div 2" ? styles["red-text"] : styles["green-text"]}   onClick={() => setattype(1)}>ARC</DropdownItem>
          </DropdownMenu>
            }
        </Dropdown>
        
        </div>
        </div>
        <div className="d-flex d-md-none justify-content-start col-12">
        <div className="d-md-none col-6 d-block">
        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={type=="1" ?`${styles.dropdown} ${styles.eddropdown}`:styles.dropdown} style={{backgroundColor:"#555555"}}>
                {selectedPlatform?typeObj[type]:attypeObj[attype+2]}
            </DropdownToggle>
            { selectedPlatform ?
              <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
             <DropdownItem   className={typeObj[type] == "Div 1" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(6)}>Div 1</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Div 2" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(4)}>Div 2</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Div 3" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(2)}>Div 3</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Div 4" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(3)}>Div 4</DropdownItem>
             <DropdownItem  className={typeObj[type] == "Educational Round" ? styles["red-text"] : styles["green-text"]}   onClick={() => setType(1)}>Educational Round</DropdownItem>
           </DropdownMenu>
         :
         <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
            <DropdownItem   className={attypeObj[type] == "Div 1" ? styles["red-text"] : styles["green-text"]}   onClick={() => setattype(0)}>ABC</DropdownItem>
              <DropdownItem  className={attypeObj[type] == "Div 2" ? styles["red-text"] : styles["green-text"]}   onClick={() => setattype(1)}>ARC</DropdownItem>
          </DropdownMenu>
            }
        </Dropdown>
        </div>
        <div className="d-md-none col-6 d-block">
            <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={type=="1" ?`${styles.dropdown} ${styles.eddropdown}`:styles.dropdown} style={{backgroundColor:"#555555"}}>
                {selectedPlatform?"CodeForces ":"AtCoder "}
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
                <DropdownItem   className={selectedPlatform== true ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange()}>Codeforces</DropdownItem>
               <DropdownItem  className={selectedPlatform== false? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange()}>AtCoder</DropdownItem>
            </DropdownMenu>
        </Dropdown>
            </div>
        </div>
        <div className="d-flex col-12 col-lg-6 justify-content-around">
        <div className="d-flex" style={{paddingTop:15,marginRight:10}}>
            <h5 className={styles.subtitle}>Show Solved</h5>
                <Switch
                onChange={handleChange}
                checked={solved}
                onColor="#86d3ff"
                onHandleColor="#2693e6"
                handleDiameter={30}
                uncheckedIcon={false}
                checkedIcon={false}
                height={20}
                width={48}
            />
            </div>
            <div className="d-md-flex d-none">
            <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
            <DropdownToggle caret className={type=="1" ?`${styles.dropdown} ${styles.eddropdown}`:styles.dropdown} style={{backgroundColor:"#555555"}}>
                {selectedPlatform?"CodeForces ":"AtCoder "}
            </DropdownToggle>
            <DropdownMenu style={{backgroundColor:"#282828",color:"white"}}>
                <DropdownItem   className={selectedPlatform== true ? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange()}>Codeforces</DropdownItem>
               <DropdownItem  className={selectedPlatform== false? styles["red-text"] : styles["green-text"]}   onClick={() => handlePlatformChange()}>AtCoder</DropdownItem>
            </DropdownMenu>
        </Dropdown>
            </div>
        </div>
      </div>
      <div className={`${styles.content} row`}>
        <table>
          <thead>
            <tr className="d-none d-md-table-row">
              <th>Contest</th>
              <th>Problem A</th>
              <th>Problem B</th>
              <th>Problem C</th>
              <th>Problem D</th>
              <th>Problem E</th>
              <th>Problem F</th>
             {((selectedPlatform && type<4)||(!selectedPlatform && !attype)) && <th>Problem G</th>}
            </tr>
          </thead>
          <tbody>
            {
                Object.keys(contestsData).map((contestKey)=>{
                    if(contestKey!="total" && contestKey!="contest_id" && ( !selectedPlatform || (selectedPlatform && contestKey!=(-1*contestsData.contest_id)))){
                        let count=0;
                        if(contestsData[contestKey].length<5)return;
                        return selectedPlatform ?
                        (
                        <tr className={styles.tableRow} key={contestKey}>
                       <td style={{ textOverflow: 'ellipsis'}}  className={styles.cell}> <a className={styles.celllink} href={`https://codeforces.com/contest/${-1*contestKey}`} target="_blank">{type=="1"?contestsData[contestKey][0][2].substring(23):contestsData[contestKey][0][2].substring(11)}</a></td>
                        {contestsData[contestKey].map((data)=>{
                            count++;
                            if(count>7||(type>3 && count>6)){
                                return(
                                    <></>
                                )
                            }
                            return(
                                <td key={data[0]} style={{ textOverflow: 'ellipsis'}}  className={(solved && data[3]!==null) ?`${styles.cell} ${styles["bg-cell"]}`:styles.cell}><a className={`${styles.celllink} ${styles[`cell-${count}`]}`} href={`https://codeforces.com/contest/${-1*contestKey}/problem/${data[0]}`} target="_blank">{data[0]+". "+data[1]}</a></td>
                            )
                        })}
                        </tr>)
                        :
                      (
                        <tr key={contestKey} className={styles.tableRow}>
                      <td style={{ textOverflow: 'ellipsis'}}  className={styles.cell}> <a className={styles.celllink} href={`https://atcoder.jp/contests/${contestKey}/tasks`} target="_blank">{contestKey}</a></td>
                        {contestsData[contestKey].map((data)=>{
                            count++;
                            if(count>7){
                                return(
                                    <></>
                                )
                            }
                            return(
                                <td key={data[0]} style={{ textOverflow: 'ellipsis'}}  className={(solved && data[3]!==null) ?`${styles.cell} ${styles["bg-cell"]}`:styles.cell}><a className={`${styles.celllink} ${styles[`cell-${count}`]}`} href={`https://atcoder.jp/contests/${contestKey}/tasks/${data[2]}`}  target="_blank">{data[1]}</a></td>
                            )
                        })}
                        </tr>)
                    }
                })
            }
          </tbody>
        </table>
      </div>
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
        {currentPage < Math.ceil(totalProblems / contestsPerPage) && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage + 1)}>
            {currentPage + 1}
          </Pagination.Item>
        )}
        {currentPage < Math.ceil(totalProblems / contestsPerPage) - 1 && (
          <Pagination.Item onClick={handlePageChange.bind(null, currentPage + 2)}>
            {currentPage + 2}
          </Pagination.Item>
        )}
        {currentPage < Math.ceil(totalProblems / contestsPerPage) - 2 && <Pagination.Ellipsis disabled />}
        {currentPage < Math.ceil(totalProblems / contestsPerPage) - 2 && (
          <Pagination.Item onClick={handlePageChange.bind(null, Math.ceil(totalProblems / contestsPerPage))}>
            {Math.ceil(totalProblems / contestsPerPage)}
          </Pagination.Item>
        )}
          <Pagination.Next
            onClick={handlePageChange.bind(null, currentPage + 1)}
            disabled={currentPage === Math.ceil(totalProblems / contestsPerPage)}
          />
        </Pagination>
      </div>
    </div>
  );
};

export default ContestComponent;
