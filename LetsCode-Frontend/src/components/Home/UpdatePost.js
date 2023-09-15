import React, { useEffect, useState } from 'react';
import { Modal, Button, Input, Dropdown, Menu, Upload, message } from 'antd';
import { UserOutlined, CameraOutlined } from '@ant-design/icons';
import styles from '../../assets/OptionsComponent.module.css';
import Profile from '../../Images/profile.png';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { updatPostDetails, updatePost } from '../../store/actions/postActions';
const UpdatePost = ({postId,photo,isModalVisible,setIsModalVisible,pastdescription,setImagedata,imagedata,selectAudience,setSelectAudience}) => {

  const [description,setdescription] =useState(pastdescription);
  const [uploadKey, setUploadKey] = useState(Date.now()); 
  const [error,seterror]=useState('')

  const dispatch=useDispatch();
  const navigate=useNavigate()

  const handleCancel = () => {
    setIsModalVisible(false);
  };



  const handleImageChange = (info) => {
    console.log(info)
    const removedFile = imagedata.find((file) => !info.fileList.some((newFile) => newFile.uid === file.uid));

  // If a file was removed, update the imagedata state
  if (removedFile) {
    const newImagedata = imagedata.filter((file) => file.uid !== removedFile.uid);
    setImagedata(newImagedata);
  } else {
    setImagedata(info.fileList);
  }
  };

  const uploadProps = {
    beforeUpload: () => false, 
    onChange: handleImageChange,
  };

  
  const audiencemap={"Post to Anyone":"public","Post to Followers":"private"}
  const handlePostSubmit = () => {
    // Replace with logic to handle post submission
    if(imagedata.length>1){
      seterror('You have to Select only one Pic!');
      return;
    }
    else{
      message.success("Your post is Updated Posted!")
      console.log(postId,description,imagedata.length===0?null:imagedata[0].originFileObj,audiencemap[selectAudience])
      updatPostDetails(postId,description,imagedata.length===0?null:imagedata[0].originFileObj,audiencemap[selectAudience],dispatch,navigate)
    }
    setIsModalVisible(false);
  };

  const menu = (
    <Menu>
      <Menu.Item key="1" onClick={() => setSelectAudience('Post to Anyone')}>
        Post to Anyone
      </Menu.Item>
      <Menu.Item
        key="2"
        onClick={() => setSelectAudience('Post to Followers')}
      >
        Post to Followers
      </Menu.Item>
    </Menu>
  );

  return (
      <Modal
        title="Update Post"
        open={isModalVisible}
        onOk={()=>handlePostSubmit()}
        onCancel={handleCancel}
      >
        <div className={styles['post-options']}>
          <div className={styles['profile-icon']}>
            <img
              src={photo}
              alt="Profile"
              style={{ height: '3rem',width:'3rem',borderRadius:'50%' }}
            />
          </div>
          <Dropdown overlay={menu} placement="bottomLeft">
            <Button >{selectAudience}</Button>
          </Dropdown>
        </div>
        <Input.TextArea
          placeholder="Write your post here..."
          rows={4}
          value={description}
          style={{ marginBottom: '10px',whiteSpace:'pre-line', height: '200px',overflowY: 'auto',  }}
          onChange={(e)=>setdescription(e.target.value)}
        />
        <Upload {...uploadProps} key={uploadKey} fileList={imagedata}>
          <Button icon={<CameraOutlined />}>Attach Photo</Button>
        </Upload>
        {
          error!='' &&
          <div style={{color:"red",fontSize:14}}>
            {error}
          </div>
        }
      </Modal>
  );
};

export default UpdatePost;
