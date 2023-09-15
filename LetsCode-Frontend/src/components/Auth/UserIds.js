import React, { useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import styles from '../../assets/Login.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { userRegisterUserIds } from '../../store/actions/authActions';
import Loader from '../Common/Loading';

const UserIds = () => {
  const [formData, setFormData] = useState({
    leetCodeUserId: '',
    codeforcesUserId: '',
    atCodersUserId: '',
  });

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
    dispatch(userRegisterUserIds(formData.leetCodeUserId,formData.codeforcesUserId,formData.atCodersUserId,navigate))
  };

  return (
    <div className={styles.loginContainer}>
      {
        userData.loading ? <Loader/>
        :
        <Container>
        <Row className='d-flex justify-content-center'>
          <Col md={12} className={styles.userIdForm}>
            <Form className={styles.Form} onSubmit={handleSubmit}>
              <div><h2 className={styles.title}>Let's Code</h2></div>
              
              {/* LeetCode User ID */}
              <div className={`${styles.label} ${errorCode.includes('0')? styles.errorLabel : ''}`}>
                LeetCode ID
              </div>
              <Form.Group controlId="formLeetCodeUsername">
                <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                  <input
                    className={`${styles.inputFiled} ${errorCode.includes('0') ? styles.errorInput : ''}`}
                    type="text"
                    name="leetCodeUserId"
                    value={formData.leetCodeUserId}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </Form.Group>
              
              {/* Codeforces User ID */}
              <div className={`${styles.label} ${errorCode.includes('1') ? styles.errorLabel : ''}`}>
                Codeforces ID
              </div>
              <Form.Group controlId="formCodeforcesUsername">
                <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                  <input
                    className={`${styles.inputFiled} ${errorCode.includes('1')? styles.errorInput : ''}`}
                    type="text"
                    name="codeforcesUserId"
                    value={formData.codeforcesUserId}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </Form.Group>
              
              {/* atCoders User ID */}
              <div className={`${styles.label} ${errorCode.includes('2') ? styles.errorLabel : ''}`}>
                AtCoders ID
              </div>
              <Form.Group controlId="formatCodersUsername">
                <div className={styles.inputContainer}>
                  <FontAwesomeIcon icon={faUser} className={styles.icon} />
                  <input
                    className={`${styles.inputFiled} ${errorCode.includes('2') ? styles.errorInput : ''}`}
                    type="text"
                    name="atCodersUserId"
                    value={formData.atCodersUserId}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </Form.Group>
              {errorCode!="-99" && <div className={styles.error}>UserId's Are Not Exist</div>} 
              
              
              <Button variant="primary" className={styles.Button} type="submit">
                Continue
              </Button>

              <div className={styles.loginOptions} >
              <span style={{fontSize:14,fontWeight:400}}>If you dont have accounts on above platform signup here</span>
              </div>

              <div className='d-flex justify-content-between mt-1'>
              <a href="https://codeforces.com/register" target="_blank" style={{color:"#00a2f8",textDecoration:"none"}}>Codeforces</a>
              <a href="https://leetcode.com/accounts/signup/" target="_blank" style={{color:"#00a2f8",textDecoration:"none"}}>Leetcode</a>
              <a href="https://atcoder.jp/register" target="_blank" style={{color:"#00a2f8",textDecoration:"none"}}>Atcoder</a>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>

      }
    </div>
  );
};

export default UserIds;
