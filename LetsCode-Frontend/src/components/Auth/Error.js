import React, { useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { Link, useNavigate } from 'react-router-dom';
import SorryIcon from '../../Images/sorry.png'

const Error = () => {
  
  return (
    <div className={styles.loginContainer}>
      <Container>
        <Row className='d-flex justify-content-center'>

          <Col md={12} className={styles.registerForm}>
            <Form className={styles.RegisterForm} >
              <div><h2 className={styles.title}>Let's Code</h2></div>
              <div className='d-flex justify-content-center'>
              <img src={SorryIcon} style={{height:140,width:140}}/>
              </div>
              <h3 style={{fontSize:20,fontWeight:500}} className='d-flex justify-content-center'>
              Link Expired!
              </h3>
              <div>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              Sorry the Link is Expired,
              </h5>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              You Can raise the new link.
              </h5>
              <h5 style={{fontSize:15,fontWeight:400}} className='d-flex justify-content-center'>
              (link is active for only 30 min)
              </h5>
                </div>
 
              <div className={styles.loginOptions}>
              <span>Raise a New </span>
                <Link to="/forgot" style={{color:"#00a2f8"}}>Link</Link>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Error;
