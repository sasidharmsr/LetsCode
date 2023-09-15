import React, { useEffect, useState } from "react";
import Icon from "../../Images/Icon.svg";
import Profile from "../../Images/profile.png";
import Home from "../../Images/home.svg";
import Search from "../../Images/search.svg";
import Problems from "../../Images/problems.svg";
import Contests from "../../Images/contests.svg";
import Submissions from "../../Images/submissions.svg";
import Logout from "../../Images/logout.svg";
import { useLocation ,Link, useNavigation, useNavigate} from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../store/actions/authActions";
import UserSearchComponent from "./Searchbar";
import { getHomedetails } from "../../store/actions/userActions";
import { getPosts } from "../../store/actions/postActions";

const Sidebar = () => {
    const location = useLocation();

    const userData=useSelector(state=>Object(state.auth))
    const users=useSelector(state=>Object(state.home).users)

    const [closeMenu, setCloseMenu] = useState(false);
    const [searchbar,setsearchbar]=useState(false)

    const handleCloseMenu = () => {
        setCloseMenu(!closeMenu);
    };

    const togglesearch=()=>{
        setsearchbar(!searchbar)
    }
    const dispatch=useDispatch()
    const navigate=useNavigate()
    const userLogout=()=>{
        dispatch(logout())
    }

    useEffect(()=>{
        if(users.length===0){
        dispatch(getHomedetails(navigate))
        dispatch(getPosts(0,0,navigate))  
        }
      },[])


    const routesWithoutNavBar = ['profile','contests','submissions','problems','editprofile']; 
    let check=routesWithoutNavBar.includes(location.pathname.split('/')[1]);
    check|=searchbar

    return (
        <>
        { searchbar && <UserSearchComponent users={users} searchbar={togglesearch}/>}
        <div className={(!closeMenu && !check) ? "sidebar" : "sidebar active"}>
            <div
                className={
                    (!closeMenu && !check)
                        ? "logoContainer"
                        : "logoContainer active"
                }
            >
                 <Link to="/"><img src={Icon} alt="icon" className="logo" /></Link>
               {(!closeMenu && !check) &&  <Link to="/" style={{textDecoration:"none"}}><h2 className="title">Lets'Code</h2></Link>}
            </div>
            <div
                className={
                    !check ? 
                    ((!closeMenu)
                        ? "burgerContainer"
                        : "burgerContainer active"):""
                }
            >
                <div
                    className="burgerTrigger"
                    onClick={() => {
                        handleCloseMenu();
                    }}
                ></div>
                <div className="burgerMenu"></div>
            </div>
            <div
                className={
                    (closeMenu && !check)
                        ? "profileContainer"
                        : "profileContainer active"
                }
            >
               <Link to={`/profile/${userData.userName}`}><img src={userData.pic} alt="profile" className="profile" onClick={()=>setsearchbar(false)}/></Link> 
                <div className="profileContents">
                    <p className="name">Hello, {userData.userName}👋</p>
                    <p>specialist</p>
                </div>
            </div>
            <div
                className={
                    closeMenu === false
                        ? "contentsContainer"
                        : "contentsContainer active"
                }
            >
                <ul>
                    <li className={location.pathname === "/home" ? "active" : ""}  onClick={()=>setsearchbar(false)}>
                       <Link to="/home"><img src={Home} alt="home" /></Link> 
                        {(!closeMenu && !check) && <Link to="/home" className="link">Home</Link>}
                    </li>
                    <li
                        className={
                            location.pathname === "/seacrh"
                                ? "active"
                                : ""
                        }
                        onClick={()=>setsearchbar(!searchbar)}
                    >
                        <img src={Search} alt="seacrh" />
                        {(!closeMenu && !check)&& <h5 to="/" className="link">Search</h5>}
                    </li>
                    <li
                        className={
                            location.pathname === "/problems" ? "active" : ""
                        }
                        onClick={()=>setsearchbar(false)}
                    >
                        <Link to="/problems"><img src={Problems} alt="problems" /></Link>
                        {(!closeMenu && !check)&&<Link to="/problems" className="link">Problems</Link>}
                    </li>
                    <li
                        className={
                            location.pathname === "/Contests" ? "active" : ""
                        }
                        onClick={()=>setsearchbar(false)}
                    >

                        <Link to="/contests"><img src={Contests} alt="Contests" /></Link>
                        {(!closeMenu && !check)&&<Link to="/contests" className="link">Contests</Link>}
                    </li>
                    <li
                        className={
                            location.pathname.includes("submissions")? "active" : ""
                        }
                        onClick={()=>setsearchbar(false)}
                    >
                        <Link to="/submissions/all"><img src={Submissions} alt="Submissions" /></Link>
                        {(!closeMenu && !check)&&<Link to="/submissions/all" className="link">Submissions</Link>}
                    </li>
                    <li
                        onClick={userLogout}
                        className={
                            location.pathname === "/Logout" ? "active" : ""
                        }
                    >
                        <Link  to="/login"><img src={Logout} alt="Logout" /></Link>
                        {(!closeMenu && !check)&&<Link to="/login" className="link">Logout</Link>}
                    </li>
                </ul>
            </div>
        </div>
        </>
    );
};

export default Sidebar;