import React from 'react';

import styles from '../../assets/Profile.module.css'

const ImageGrid = ({ posts,singlePostRender }) => {
    const images=[]
    posts.map((post,idx)=>{
      if((post["post_pic"]===null || post["post_pic"]===""))return;
        images.push({url:post["post_pic"],index:idx})
    })
    console.log(images)
    return (
      <div className={styles['image-container']}>
        {images.map((post, index) => (
          <div key={index} className={styles['image-item']} onClick={()=>singlePostRender(post.index)}>
            <img src={post.url} alt={`Image ${index + 1}`} />
          </div>
        ))}
      </div>
    );
  };

export default ImageGrid;
