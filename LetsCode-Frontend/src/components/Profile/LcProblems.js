import React from 'react'
import styles from '../../assets/Profile.module.css'
const LcProblems = ({profileinfo,totalProblems}) => {
    
    const angle=43
    const mapping={
        '--p': angle,
        '--b': "5px",
        '--c': '#ffa116',
    }
    function createProgressStyle(numerator, denominator,fillColor,backgroundColor) {
        const fractionPercentage = (numerator / denominator) * 100;
        return {
            width: `100%`,
            background: `linear-gradient(90deg, ${fillColor} ${fractionPercentage}%, ${backgroundColor} ${fractionPercentage}%)`,
            height: '100%',
        };
    }
  return (
    <div className={styles.lcdiv}>
        <h4 className={styles.lctitle}>LeetCode Problems</h4>
        <div className='row d-flex' style={{height:"200px"}}>
            <div className='col-4 d-flex justify-content-center align-items-center'>
            <div className={styles.pie} style={mapping}>
                {profileinfo.lc_count}
            </div>
            </div>
            <div className={`${styles.problemsdiv} col-7`}>
            <div className='d-flex flex-column w-100' >
                <div className='d-flex justify-content-between align-items-center'>
                    <h5 className={styles.difficultyTitle}>Easy</h5>
                    <h4 className={styles.countTitle}>{profileinfo.easy_count}<span className={styles.spantitle}>/{totalProblems.EasyLc}</span></h4>
                </div>
                <div className={styles.progressContainer}>
                    <div style={createProgressStyle(profileinfo.easy_count, totalProblems.EasyLc,"#00b8a3","#294d35")}></div>
                </div>
            </div>
            <div className='d-flex flex-column w-100'>
                <div className='d-flex justify-content-between align-items-center'>
                    <h5 className={styles.difficultyTitle}>Medium</h5>
                    <h4 className={styles.countTitle}>{profileinfo.medium_count}<span className={styles.spantitle}>/{totalProblems.MediumLc}</span></h4>
                </div>
                <div className={styles.progressContainer}>
                    <div style={createProgressStyle(profileinfo.medium_count, totalProblems.MediumLc,"#ffc01e","#5e4e26")} className={styles.lineElement}></div>
                </div>
            </div>
            <div className='d-flex flex-column w-100'>
                <div className='d-flex justify-content-between align-items-center'>
                    <h5 className={styles.difficultyTitle}>Hard</h5>
                    <h4  className={styles.countTitle}>{profileinfo.hard_count}<span className={styles.spantitle}>/{totalProblems.HardLc}</span></h4>
                </div>
                <div className={styles.progressContainer}>
                    <div style={createProgressStyle(profileinfo.hard_count, totalProblems.HardLc,"#ef4743","#5a302f")}></div>
                </div>
                </div>
            </div>
        </div>
    </div>
  )
}

export default LcProblems