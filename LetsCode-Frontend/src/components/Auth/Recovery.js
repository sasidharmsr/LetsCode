import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  passwordChange, userRegister, validateToken } from '../../store/actions/authActions';
import { faEye, faEyeSlash ,faUser,faLock,faEnvelope} from '@fortawesome/free-solid-svg-icons';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import lockIcon from '../../Images/lock-icon-29056.png'
import Loader from '../Common/Loading';
import Error from './Error';

const Recovery = () => {
  
  const [showPassword, setShowPassword] = useState(false);
  const [recoverypage,setrecoverypage]=useState(true)
  const [userName,setuserName]=useState('')
  const [formData, setFormData] = useState({
    password: '',
    conformpassword: '',
  });

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [errorCode,seterrorcode]=useState(false)
  const userData=useSelector(state=>Object(state.auth))

  const urlParams = new URLSearchParams(window.location.search);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  useEffect(()=>{
    dispatch(validateToken(urlParams.get('token'),setrecoverypage,setuserName))
  },[])
  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log(formData.password!==formData.conformpassword)
    if(formData.password!==formData.conformpassword){
        seterrorcode(true)
        setTimeout(()=>{
            seterrorcode(false)
        },2000)
        return
    }
   dispatch(passwordChange(userName,formData.password,navigate))
  }

  return (
    <div>
      {!userData.loading?
        recoverypage?(
        <div className={styles.loginContainer}>
        <Container>
        <Row className='d-flex justify-content-center'>

          <Col md={12} className={styles.registerForm}>
            <Form className={styles.RegisterForm}  onSubmit={handleSubmit}>
              <div><h2 className={styles.title}>Let's Code</h2></div>
              
              <div className='d-flex justify-content-center'>
              <img src={lockIcon} style={{height:50,width:50}}/>
              </div>

              <h3 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
                Hi {userName}! You can reset your password
              </h3>

              <Form.Group controlId="formBasicPassword">

                <div className={styles.passwordContainer}>
                <FontAwesomeIcon icon={faLock} className={styles.icon} />
                <input
                    className={`${styles.inputFiled} ${errorCode ? styles.errorInput : ''}`}
                    type={showPassword ? 'text' : 'password'}
                    name="password"
                    value={formData.password}
                    onChange={handleInputChange}
                    placeholder="Password"
                    required
                  />
                  <FontAwesomeIcon
                    icon={showPassword ? faEyeSlash : faEye}
                    className={styles.showPasswordIcon}
                    onClick={togglePasswordVisibility}
                  />
                </div>
              </Form.Group>

              <Form.Group controlId="formBasicPassword">
                <div className={styles.passwordContainer}>
                <FontAwesomeIcon icon={faLock} className={styles.icon} />
                <input
                    className={`${styles.inputFiled} ${errorCode ? styles.errorInput : ''}`}
                    type={'password'}
                    name="conformpassword"
                    value={formData.conformpassword}
                    onChange={handleInputChange}
                    placeholder="Conform Password"
                    required
                />
                </div>
            </Form.Group>
              {errorCode && <div className={styles.error}>Password are not Matched</div>} 
              <Button variant="primary" className={styles.Button} type="submit">
                Submit
              </Button>
              <div className={styles.loginOptions}>
                <span>Have an account? </span>
                <Link to="/login" style={{color:"#00a2f8"}}>Sign In</Link>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>
      </div>)
      :(<Error/>)
      :
      <Loader/>}
    </div>
  );
};

export default Recovery;
