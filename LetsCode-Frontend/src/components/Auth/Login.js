import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash, faUser, faLock } from '@fortawesome/free-solid-svg-icons';
import { useDispatch, useSelector } from 'react-redux';
import { userLogin } from '../../store/actions/authActions';
import { Link, useNavigate } from 'react-router-dom';

const Login = (props) => {
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const errorCode=useSelector((state)=>state.auth.errorCode);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };


  const handleSubmit = async (event) => {
    event.preventDefault();
      dispatch(userLogin(formData.username, formData.password, navigate));
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <>
    <div className={styles.loginContainer}>
      <Container>
        <Row className='d-flex justify-content-center'>
          <Col md={4} className={`d-flex  justify-content-end ${styles.hiddenOnSmall}`}>
            <img className={styles.bgImage} src="https://i.ibb.co/rvnsX5C/Picsart-23-08-11-15-45-02-088.jpg" alt="Background" />
          </Col>

          <Col md={6} className={styles.loginForm}>
            <Form className={styles.Form} onSubmit={handleSubmit}>
              <div><h2 className={styles.title}>Let's Code</h2></div>
              <Form.Group controlId="formBasicUsername">
              <div className={`${styles.inputContainer} ${errorCode === '3' ? styles.error : ''}`}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                  <input
                    className={`${styles.inputFiled} ${errorCode === '3' ? styles.errorInput : ''}`}
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleInputChange}
                    placeholder="Username"
                    required
                  />
                </div>
              </Form.Group>

              <Form.Group controlId="formBasicPassword">
              <div className={`${styles.passwordContainer} ${errorCode === '4' ? styles.error : ''}`}>
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

              {errorCode=='3' && <div className={styles.error}>Username is incorrect</div>} 
              {errorCode=='4' && <div className={styles.error}>Password is incorrect</div>} 
              <Button variant="primary" className={styles.Button} type="submit">
                Login
              </Button>
              <div className={styles.loginOptions}>
                <Link to="/forgot" style={{color:"black",fontSize:15,fontWeight:400}}>Forgotten your password? </Link>
              </div>
            </Form>
            <div className={styles.registerdiv}>
            <div className={styles.loginOptions}>
            <span>Don't have an account? </span>
            <Link to="/register" style={{color:"#00a2f8"}}>Sign Up</Link>
            </div>
            </div>
          </Col>
        </Row>
      </Container>
    </div>
    </>
  );
};

export default Login;
