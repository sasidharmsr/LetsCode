import React, { useState } from 'react';
import { Modal, Button, Input, Dropdown, Menu, Upload, message } from 'antd';
import { UserOutlined, CameraOutlined } from '@ant-design/icons';
import styles from '../../assets/OptionsComponent.module.css';
import Profile from '../../Images/profile.png';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { postDetails } from '../../store/actions/postActions';
const CreatePost = ({photo,userData}) => {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectAudience, setSelectAudience] = useState('Select audience');
  const [imagedata, setImagedata] = useState({count:0,file:null});
  const [description,setdescription] =useState('');
  const [uploadKey, setUploadKey] = useState(Date.now()); 
  const [error,seterror]=useState('')

  const dispatch=useDispatch();
  const navigate=useNavigate()
  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setdescription('')
    setImagedata({count:0,file:null})
    setUploadKey(Date.now())
    setIsModalVisible(false);
  };

  const handleImageChange = (info) => {
    setImagedata({file:info.file,count:info.fileList.length});
  };

  const uploadProps = {
    beforeUpload: () => false, 
    onChange: handleImageChange,
  };
  
  const audiencemap={"Post to Anyone":"public","Post to Followers":"private"}
  const handlePostSubmit = () => {
    // Replace with logic to handle post submission
    if(imagedata.count>1){
      seterror('You have to Select only one Pic!');
      return;
    }
    else if(selectAudience==='Select audience'){
      seterror("You have to select Audience")
      setTimeout(()=>{
        seterror('')
      },2000)
      return;
    }
    else{
      message.success("Your post is Successfully Posted")
     // console.log(imagedata.file)
      postDetails(imagedata.file,description,audiencemap[selectAudience],userData,dispatch,navigate)
    }
    setdescription('');setImagedata({count:0,file:null})
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
    <div className={styles['create-post']}>
      <div className={styles['profile-icon']}>
        <img src={photo} alt="Profile" style={{ height: '3rem',width:'3rem',borderRadius:'50%' }} />
      </div>
      <div className={styles['start-post']} onClick={showModal}>
        Start a post
      </div>

      <Modal
        title="Create Post"
        open={isModalVisible}
        onOk={handlePostSubmit}
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
          style={{ marginBottom: '10px',whiteSpace:'pre-line' }}
          onChange={(e)=>setdescription(e.target.value)}
        />
        <Upload {...uploadProps} key={uploadKey}>
          <Button icon={<CameraOutlined />}>Attach Photo</Button>
        </Upload>
        {
          error!='' &&
          <div style={{color:"red",fontSize:14}}>
            {error}
          </div>
        }
      </Modal>
    </div>
  );
};

export default CreatePost;
