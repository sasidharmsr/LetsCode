import React, { useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  forgotPassword, userRegister } from '../../store/actions/authActions';
import { faEye, faEyeSlash ,faUser,faLock,faEnvelope} from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import lockIcon from '../../Images/lock-icon-29056.png'
import Success from './Success';
import Loader from '../Common/Loading';

const Forgotten = () => {
  
  const [success, setsuccess] = useState(false);
  const [email,setemail]=useState('')
  const [formData, setFormData] = useState({
    username: '',
  });

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const errorCode=useSelector((state)=>state.auth.errorCode);

  const userData=useSelector(state=>Object(state.auth))
  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const successful=(email)=>{
    setemail(email)
    setsuccess(true)
  }

  function isValidEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
  }
  function isPhoneNumber(input) {
    const phoneNumberPattern = /^\d{10}$/;
    return phoneNumberPattern.test(input);
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    let email=null,phone_number=null,user_name=null;
    if(isPhoneNumber(formData.username))phone_number=formData.username
    else if(isValidEmail(formData.username))email=formData.username
    else user_name=formData.username
    dispatch(forgotPassword(user_name,phone_number,email,successful))
  }


  return (
    <>
    {userData.loading===false ?
    <div className={styles.loginContainer}>
     { !success ? <Container>
        <Row className='d-flex justify-content-center'>

          <Col md={12} className={styles.registerForm}>
            <Form className={styles.RegisterForm}  onSubmit={handleSubmit}>
              <div><h2 className={styles.title}>Let's Code</h2></div>
              <div className='d-flex justify-content-center'>
              <img src={lockIcon} style={{height:50,width:50}}/>
              </div>
              <h3 style={{fontSize:20,fontWeight:500}} className='d-flex justify-content-center'>
              Trouble with logging in?
              </h3>
              <div>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              Enter your email address, phone number or
              </h5>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              username, and we'll send you a link to get back
              </h5>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              into your account.
              </h5>
                </div>
              <Form.Group controlId="formBasicUsername">
                <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                  <input
                    className={`${styles.inputFiled} ${errorCode !== '-99' ? styles.errorInput : ''}`}
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleInputChange}
                    placeholder="Username ,Email or Phone number"
                    required
                  />
                </div>
              </Form.Group>

              {errorCode=='5' && <div className={styles.error}>Username Not Exists</div>} 
              {errorCode=='6' && <div className={styles.error}>Email Id Not Linked With Any Account</div>} 
              {errorCode=='7' && <div className={styles.error}>Phone Number Not Linked With Any Account</div>} 
              <Button variant="primary" className={styles.Button} type="submit">
                Send Login Link
              </Button>
              <div className={styles.loginOptions}>
              <span>Back to </span>
                <Link to="/login" style={{color:"#00a2f8"}}>Login</Link>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>:
      <Success email={email}/>}
    </div>:
    <Loader/>}
    </>
  );
};

export default Forgotten;
