import React, { useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { Link, useNavigate } from 'react-router-dom';
import SuccessIcon from '../../Images/tickmark1.png'

const Success = ({email}) => {
  
  return (
    <div className={styles.loginContainer}>
      <Container>
        <Row className='d-flex justify-content-center'>

          <Col md={12} className={styles.registerForm}>
            <Form className={styles.RegisterForm} >
              <div><h2 className={styles.title}>Let's Code</h2></div>
              <div className='d-flex justify-content-center'>
              <img src={SuccessIcon} style={{height:70,width:70}}/>
              </div>
              <h3 style={{fontSize:20,fontWeight:500}} className='d-flex justify-content-center'>
              Email was successfully sent
              </h3>
              <div>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              we send you a link to your
              </h5>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              email {email} 
              </h5>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              kindly reset your password with in 30 min.
              </h5>
                </div>
 
              <div className={styles.loginOptions}>
              <span>Back to </span>
                <Link to="/login" style={{color:"#00a2f8"}}>Login</Link>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Success;
