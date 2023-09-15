import { faEllipsisH, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';

import styles from '../../assets/OptionsComponent.module.css'
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { deleteComment } from '../../store/actions/postActions';

const CommentComponent = ({ deleteCheck,profilePic,CommentAddCheck, username,postId, commentId, timeAgo, commentText }) => {

  const dispatch=useDispatch()
  const navigate=useNavigate()
  const handleDeleteClick = () => {
    dispatch(deleteComment(commentId,postId,CommentAddCheck,navigate))
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'row', marginBottom: '16px',marginTop:'10px' }}>
      <div>
      <Link to={`/profile/${username}`} style={{textDecoration:"none",color:"white"}}><img src={profilePic} alt="Profile Pic" style={{ width: '48px', height: '48px', borderRadius: '50%' }} /></Link>
      </div>
      <div style={{ marginLeft: '12px', flex: '1' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <div>
           <Link to={`/profile/${username}`} style={{textDecoration:"none",color:"white"}}> <strong>{username}</strong></Link>
            <span style={{ marginLeft: '8px', fontSize: '12px', color: '#888888' }}>{timeAgo}</span>
          </div>
          <div>
         {deleteCheck && <span className="font-icon" onClick={handleDeleteClick} style={{cursor:"pointer"}}><FontAwesomeIcon icon={faTrash}/> </span>}
          </div>
        </div>
        <div>
          <p>{commentText}</p>
        </div>
      </div>
    </div>
  );
};

export default CommentComponent;
