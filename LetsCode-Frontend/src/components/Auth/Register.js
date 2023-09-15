import React, { useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  userRegister } from '../../store/actions/authActions';
import { faEye, faEyeSlash ,faUser,faLock,faEnvelope} from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import Loader from '../Common/Loading';

const Register = () => {
  
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    name: '',
    email: '',
  });

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const userData = useSelector((state) => (state.auth));

  const errorCode=userData.errorCode;

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    dispatch(userRegister(formData.username,formData.password,formData.email,formData.name,navigate))
  }


  return (
    <div className={styles.loginContainer}>
      {
        userData.loading?<Loader/>:
        <Container>
        <Row className='d-flex justify-content-center'>

          <Col md={12} className={styles.registerForm}>
            <Form className={styles.RegisterForm}  onSubmit={handleSubmit}>
              <div><h2 className={styles.title}>Let's Code</h2></div>
              <Form.Group controlId="formBasicUsername">
                <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                  <input
                    className={`${styles.inputFiled} ${errorCode === '5' ? styles.errorInput : ''}`}
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleInputChange}
                    placeholder="Username"
                    required
                  />
                </div>
              </Form.Group>

              <Form.Group controlId="formBasicUsername">
              <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                <input
                    className={`${styles.inputFiled} ${errorCode === '3' ? styles.errorInput : ''}`}
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    placeholder="Enter Your Name"
                    required
                  />
                </div>
              </Form.Group>

              <Form.Group controlId="formBasicUsername">
              <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faEnvelope} className={styles.icon} />
                <input
                    className={`${styles.inputFiled} ${errorCode === '3' || errorCode === '12' ? styles.errorInput : ''}`}
                    type="text"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Enter Your Email"
                    required
                  />
                </div>
              </Form.Group>

              <Form.Group controlId="formBasicPassword">

                <div className={styles.passwordContainer}>
                <FontAwesomeIcon icon={faLock} className={styles.icon} />
                <input
                    className={`${styles.inputFiled} ${errorCode === '4' ? styles.errorInput : ''}`}
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
              {errorCode=='5' && <div className={styles.error}>Username is already taken</div>} 
              {errorCode=='12' && <div className={styles.error}>The email address provided doesn't exist</div>} 
              <Button variant="primary" className={styles.Button} type="submit">
                Sign Up
              </Button>
              <div className={styles.loginOptions}>
                <span>Have an account? </span>
                <Link to="/login" style={{color:"#00a2f8"}}>Sign In</Link>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>
      }
    </div>
  );
};

export default Register;
