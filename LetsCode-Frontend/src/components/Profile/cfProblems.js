import React from 'react'
import styles from '../../assets/Profile.module.css'
const CfProblems = ({profileinfo,totalProblems,cfProblems}) => {
    
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
        <h4 className={styles.lctitle}>CodeForces Problems</h4>
        <div className='row d-flex' style={{height:"200px"}}>
            <div className='col-4 d-flex justify-content-center align-items-center'>
            <div className={styles.pie} style={mapping} >
                {profileinfo.cf_count}
            </div>
            </div>
            <div className={`${styles.problemsdiv} col-7`}>
            <div className='d-flex flex-column w-100' >
                <div className='d-flex justify-content-between align-items-center'>
                    <h5 className={styles.difficultyTitle}>Easy<span className={styles.spantitle}>{"<1300"}</span></h5>
                    <h4 className={styles.countTitle}>{cfProblems.Easy}<span className={styles.spantitle}>/{totalProblems.EasyCf}</span></h4>
                </div>
                <div className={styles.progressContainer}>
                    <div style={createProgressStyle(cfProblems.Easy, totalProblems.EasyCf,"#00b8a3","#294d35")}></div>
                </div>
            </div>
            <div className='d-flex flex-column w-100'>
                <div className='d-flex justify-content-between align-items-center'>
                    <h5 className={styles.difficultyTitle}>Medium<span className={styles.spantitle}> {"<1800"}</span></h5>
                    <h4 className={styles.countTitle}>{cfProblems.Medium}<span className={styles.spantitle}>/{totalProblems.MediumCf}</span></h4>
                </div>
                <div className={styles.progressContainer}>
                    <div style={createProgressStyle(cfProblems.Medium, totalProblems.MediumCf,"#ffc01e","#5e4e26")} className={styles.lineElement}></div>
                </div>
            </div>
            <div className='d-flex flex-column w-100'>
                <div className='d-flex justify-content-between align-items-center'>
                    <h5 className={styles.difficultyTitle}>Hard<span className={styles.spantitle}>{"<3500"}</span></h5>
                    <h4  className={styles.countTitle}>{cfProblems.Hard}<span className={styles.spantitle}>/{totalProblems.HardCf}</span></h4>
                </div>
                <div className={styles.progressContainer}>
                    <div style={createProgressStyle(cfProblems.Hard, totalProblems.HardCf,"#ef4743","#5a302f")}></div>
                </div>
                </div>
            </div>
        </div>
    </div>
  )
}

export default CfProblems