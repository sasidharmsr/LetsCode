import React from "react";
import { Outlet, Navigate, useLocation } from "react-router-dom";
import NavScroll from "../components/Common/NavBar";
import MobileNavBar from "../components/Common/MobileNavBar";
import { useSelector } from "react-redux";

const Authmiddleware = () => {

  const auth=localStorage.getItem("msr_access_token");
  
  const location = useLocation();
  
  const userData=useSelector(state=>Object(state.auth))

  const routesWithoutNavBar = ['/selection/topics', '/register/userIds']; 
  const hideNavBar = routesWithoutNavBar.includes(location.pathname);

  return(
    auth?
    (userData.errorCode!=='43'?
    <React.Fragment>
      <div className="d-none d-md-block">
      {!hideNavBar && <NavScroll/>}
      </div>
      <div className="d-md-none"><MobileNavBar/></div>
      <Outlet/>
    </React.Fragment>:<Navigate to="/register/userIds" />)
    :<Navigate to="/login" />
  )
}

export default Authmiddleware;
