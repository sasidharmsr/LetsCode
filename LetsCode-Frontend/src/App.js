import { Routes, Route, BrowserRouter as Router } from "react-router-dom";
import './App.css';
import "../src/styles/main.scss";
import React, { useEffect } from "react";
import { userRoutes, authRoutes } from "./routes/routes";
import Authmiddleware from "./routes/middleware";
import { loadUserDetails } from "./store/actions/authActions";
import { useDispatch } from "react-redux";
import Home from "./components/Home/Home";
import NavScroll from "./components/Common/NavBar"
function App() {

  const dispatch=useDispatch()
  useEffect(()=>{
    if(localStorage.getItem("msr_access_token")){
       loadUserDetails(dispatch)
    }
  },[])
  return (
    <React.Fragment>
      <Router>
        <Routes>
        {authRoutes.map((route,idx)=>{
          return (
              <Route key={idx} path={route.path} element={route.component}/>)
            })}
          <Route element={<Authmiddleware/>}>
            {userRoutes.map((route,idx)=>{
              return (<Route key={idx} path={route.path} element={route.component}/>)
            })}
          </Route>
        </Routes>
      </Router>
    </React.Fragment>
  );
}

export default App;


// {authRoutes.map((route, idx) => (
//   <Route
//     key={idx}
//     path={route.path}
//     element={<Authmiddleware component={route.component} isAuthProtected={true} />}
//   />
// ))}

// {userRoutes.map((route, idx) => (
//   <Route
//     key={idx}
//     path={route.path}
//     element={<Authmiddleware component={route.component} isAuthProtected={false} />}
//   />
// ))}