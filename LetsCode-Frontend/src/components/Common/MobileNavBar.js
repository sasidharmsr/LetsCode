import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Profile from "../../Images/profile.png";
import Home from "../../Images/home.svg";
import Search from "../../Images/search.svg";
import Problems from "../../Images/problems.svg";
import Contests from "../../Images/contests.svg";
import Submissions from "../../Images/submissions.svg";
import Logout from "../../Images/logout.svg";
import NavDropdown from 'react-bootstrap/NavDropdown';
import Offcanvas from 'react-bootstrap/Offcanvas';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import UserSearchComponent from './Searchbar';
import { logout } from '../../store/actions/authActions';

function MobileNavBar() {
  const [showOffcanvas, setShowOffcanvas] = useState(false);
  const [search, setsearch] = useState(false);

  const users=useSelector(state=>Object(state.home).users)
  const userData=useSelector(state=>Object(state.auth))
  const handleClose = () => {
    setShowOffcanvas(false);
  };
  
  const dispatch=useDispatch()
  const handleSearch=(val)=>{
    setsearch(!search)
    if(val===1){
      setShowOffcanvas(false);
    }
  }
  const userLogout=()=>{
    dispatch(logout())
}

  return (
    <>
      {[false].map((expand) => (
        <Navbar key={expand}    style={{backgroundColor:"#1a1a1a"}} expand={expand}>
          <Container fluid>
            <Navbar.Brand href="#" style={{ color: 'white' }}>
              Let's Code
            </Navbar.Brand>
            <Navbar.Toggle
              onClick={() => setShowOffcanvas(true)}
              style={{ backgroundColor: 'white' }} 
            />
            <Navbar.Offcanvas show={showOffcanvas} onHide={handleClose} placement="end"
              id={`offcanvasNavbar-expand-${expand}`}
              style={{backgroundColor:"#1a1a1a"}}
            >
              {
                search ?<></>:<Offcanvas.Header>
                <div className='d-flex align-items-center' style={{height:40,margin:10}} onClick={()=>setShowOffcanvas(false)}>
                <Link to={`/profile/${userData.userName}`}><img src={userData.pic} alt="profile" style={{height:40,width:40,borderRadius:"50%",cursor:"pointer"}}/></Link>
                    <Link to={`/profile/${userData.userName}`} style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }}>
                      {userData.userName}
                    </Link>
                  </div>
                  <button
                    type="button"
                    className="btn btn-link text-white"
                    aria-label="Close"
                    onClick={handleClose}
                    style={{ position: "absolute", top: "10px", right: "10px", background: "none", border: "none" }}
                  >
                    <FontAwesomeIcon icon={faTimes} />
                  </button>
                </Offcanvas.Header>
              }
            {search ?(<Offcanvas.Body>
                <UserSearchComponent users={users} searchbar={handleSearch}/>
              </Offcanvas.Body>):
           (
              <Offcanvas.Body>
                <Nav className="justify-content-end  flex-grow-1 pe-3">
                  <div className='d-flex flex-column justify-content-between' style={{height:"50%"}}>
                  <div className='d-flex align-items-center' style={{height:40,margin:10}} onClick={()=>setShowOffcanvas(false)}>
                <Link to="/home"><img src={Home} alt="home" /></Link>
                  <Link to="/home" style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }}>
                    Home
                  </Link>
                </div>
                <div className='d-flex align-items-center' onClick={()=>handleSearch()} style={{height:40,margin:10,cursor:"pointer"}} >
                <img src={Search} alt="search" />
                  <div style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }}>
                    Search
                  </div>
                </div>
                <div className='d-flex align-items-center' style={{height:40,margin:10}} onClick={()=>setShowOffcanvas(false)}>
                <Link to="/problems"><img src={Problems} alt="problems" /></Link>
                  <Link to="/problems" style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }}>
                    Problems
                  </Link>
                </div>
                <div className='d-flex align-items-center' style={{height:40,margin:10}} onClick={()=>setShowOffcanvas(false)}>
                <Link to="/contests"><img src={Contests} alt="contests" /></Link>
                  <Link to="/contests" style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }}>
                    Contests
                  </Link>
                </div>
                <div className='d-flex align-items-center' style={{height:40,margin:10}} onClick={()=>setShowOffcanvas(false)}>
                <Link to="/submissions/all"><img src={Submissions} alt="submissions" /></Link>
                  <Link to="/submissions/all" style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }}>
                    Submissions
                  </Link>
                </div>
                <div className='d-flex ' style={{height:40,margin:10}} onClick={userLogout}>
                <Link to="/login"><img src={Logout} alt="login" /></Link>
                  <Link to="/login" style={{ color: 'white',textDecoration:"none",fontSize:20,marginLeft:12 }} >
                    Logout
                  </Link>
                </div>
                  </div>
                </Nav>
              </Offcanvas.Body>)}
            </Navbar.Offcanvas>
          </Container>
        </Navbar>
      ))}
    </>
  );
}

export default MobileNavBar;
