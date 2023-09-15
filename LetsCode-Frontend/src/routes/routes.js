import Login from '../components/Auth/Login';
import { useNavigate } from 'react-router-dom';
import Home from '../components/Home/Home';
import Register from '../components/Auth/Register';
import BoxSelector from '../components/Auth/Topics';
import UserIds from '../components/Auth/UserIds';
import Profile from '../components/Profile/Profile';
import { useEffect } from 'react';
import ContestComponent from '../components/Contests/contest';
import UserSubmissions from '../components/userSubmissions/UserSubmissions';
import ProblemComponent from '../components/Problems/Problems';
import EditProfile from '../components/Profile/EditProfile';
import Forgotten from '../components/Auth/Forgotten';
import Recovery from '../components/Auth/Recovery';

const userRoutes = [
  { path: "/home",  component:  <Home/>},
  { path:"/register/userIds",component:<UserIds/>},
  { path:"/selection/topics",component:<BoxSelector/>},
  {path:"/profile/:userName",component:<Profile/>},
  {path:"/contests",component:<ContestComponent/>},
  {path:"/submissions/:type",component:<UserSubmissions/>},
  {path:"/problems",component:<ProblemComponent/>},
  {path:"/editprofile",component:<EditProfile/>},
  {path:"/",component:<Home/>},
  {path:"",component:<Home/>},
];

const authRoutes = [
  { path: "/login", component: <Login/> },
  { path:"/register" ,component:<Register/>},
  { path:"/forgot",component:<Forgotten/>},
  { path:"/recovery",component:<Recovery/>},
];


export { userRoutes, authRoutes };
