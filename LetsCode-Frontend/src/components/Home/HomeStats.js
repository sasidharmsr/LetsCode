import React from 'react';
import styles from '../../assets/homeStats.module.css';

const HomeStats = ({ counts }) => {

    const StatDiv=({solvedProblems,totalProblems})=>{
        const angle=((solvedProblems/totalProblems)*100)
        const mapping={
            '--p': angle,
            '--b': "4px",
            '--c': '#ffa116',
        }
        return(
        <div className={styles.pie} style={mapping}>{solvedProblems}</div>)
    }

   return (
    <div className={styles.maindiv}>
        <div className='d-flex align-items-center justify-content-between'>
        <h1 className={styles.heading}>Solved Problems</h1></div>
        <div className='d-flex align-items-center justify-content-between'>
            <h2 className={styles.title}>Leetcode</h2>
            <StatDiv solvedProblems={counts.Lc} totalProblems={2700}/>
        </div>
        <div className='d-flex align-items-center justify-content-between'>
            <h2 className={styles.title}>Codeforces</h2>
            <StatDiv solvedProblems={counts.Cf}  totalProblems={8000}/>
        </div>
        <div className='d-flex align-items-center justify-content-between'>
            <h2 className={styles.title}>Atcoder</h2>
            <StatDiv solvedProblems={counts.At}  totalProblems={3000}/>
        </div>
    </div>
   )
  };

export default HomeStats;
