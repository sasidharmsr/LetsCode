import React,{useState} from 'react';
import styles from '../../assets/OptionsComponent.module.css'// Import the CSS module for styling
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { useDispatch, useSelector } from 'react-redux';
import { submitTopics } from '../../store/actions/authActions';
import { useNavigate } from 'react-router-dom';

const TwoColumnLayout = () => {
    const [selectedBoxes, setSelectedBoxes] = useState([]);
    const [error,setError] =useState(false);
    const maxSelection = 5; 
    const topics=useSelector(state=>Object(state.topics.topics))
    const navigate=useNavigate()
    const dispatch=useDispatch()
  
    const handleBoxClick = (boxId) => {
      if (selectedBoxes.includes(boxId)) {
        setSelectedBoxes(selectedBoxes.filter((id) => id !== boxId));
      } else {
        if (selectedBoxes.length < maxSelection) {
          setSelectedBoxes([...selectedBoxes, boxId]);
        }
      }
    };

    const handleSubmit = () => {
        if (selectedBoxes.length >= 5) {
         const selectedTopics=[]
         selectedBoxes.map((i)=>selectedTopics.push(topics[i]));
         dispatch(submitTopics(selectedTopics,navigate))
        }
        else{
            setError(true);
            setTimeout(()=>{
                setError(false);
            },3000)
        }
      };
  

    const renderBoxes = () => {
        const boxes = [];
        if(topics==undefined)return boxes;
        for (let i = 0; i < Math.min(18,topics.length); i++) {
          const isSelected = selectedBoxes.includes(i);
          const boxClass = isSelected ? styles.selectedWord : styles.word;
          boxes.push(
            <div
              key={i}
              className={boxClass}
              onClick={() => handleBoxClick(i)}
            >
              {topics[i].topicName}
            </div>
          );
        }
        return boxes;
      };

  return (
   <div className='container'>
    <div className={` ${styles.container}`}>
      {/* Left Side */}
      <div className={` ${styles.left}`}>
        <h2 className={styles.title}>Hello Coder!</h2>
        <p className={styles.description}>We want some of the topics that your are intrested in, So Please select 5 topics from right side.....</p>
        <button className={styles.button}
        onClick={()=>handleSubmit()}>
          Continue
          <FontAwesomeIcon icon={faArrowRight} className={styles.icon} />
        </button>

        {error ?<div className={styles.error}>*Select 5 topics</div>:<></>}
        
      </div>
      
      {/* Right Side */}
      <div className={` ${styles.right}`}>
                {renderBoxes()}
          {/* Add more rows here */}
      </div>
    </div>
   </div>
    
  );
};

export default TwoColumnLayout;
