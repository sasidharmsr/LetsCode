import React, { useState,useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from '../../assets/OptionsComponent.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { useSelector } from 'react-redux';

const CalendarGrid = ({calenderData}) => {
  // Get the current date, month, and year
  const today = new Date();
  const currentMonth = today.getMonth();
  const currentYear = today.getFullYear();
  const currentDate = today.getDate();
  const navigate=useNavigate()

  const [curMonth,setcurmonth]=useState(currentMonth);
  const [curYear,setcurYear]=useState(currentYear);

  // Get the current time in Kolkata (UTC+5:30)
  const kolkataTime = new Date(today.toLocaleString('en-US', { timeZone: 'Asia/Kolkata' }));
  const hoursLeft = 23 - kolkataTime.getHours();
  const minutesLeft = 59 - kolkataTime.getMinutes();
  const secondsLeft = 59 - kolkataTime.getSeconds();

  // Get the first day of the month
  const firstDayOfMonth = new Date(curYear, curMonth, 1);
  const startingDay = firstDayOfMonth.getDay(); // 0 for Sunday, 1 for Monday, ..., 6 for Saturday

  const lastDayOfMonth = new Date(curYear, curMonth + 1, 0).getDate();

  const dates = Array.from({ length: lastDayOfMonth }, (_, index) => index + 1);

  const [hoveredDate, setHoveredDate] = useState(null);
  const [timeLeft, setTimeLeft] = useState({ hours: hoursLeft, minutes: minutesLeft, seconds: secondsLeft });

  // Update the time every second
  const monthNames = [
    'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN',
    'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'
  ];
  const presentDate=(curdate)=>{
    let date=curYear+"-";
    date+=(curMonth.toString().length==1?("0"+(1+curMonth)):(curMonth+1))+"-";
    date+=curdate.toString().length==1?("0"+curdate):curdate;
    return date;
  }


  function isWithinLastSixMonths(year, month) {
    const currentDate = new Date();
    const sixMonthsAgo = new Date();
    sixMonthsAgo.setMonth(currentDate.getMonth() - 5); 
    const providedDate = new Date(year, month - 1, 1);
    return providedDate >= sixMonthsAgo && providedDate <= currentDate;
}


  const iconfun=(e)=>{
    if(e==="left"){
      if(!isWithinLastSixMonths(curYear,curMonth))return;
      if(curMonth==0){
        setcurmonth(11);setcurYear(curYear-1);
      }
      else
        setcurmonth(curMonth-1)
    } 
    else{
      if(curMonth>=currentMonth && curYear==currentYear)return;
      if(curMonth==11){
        setcurmonth(0);setcurYear(curYear+1);
      }
      else
        setcurmonth(curMonth+1)
    }
  }
  const getdata=(date)=>{
    const data=calenderData[presentDate(date)]
    if(data===undefined)return  "No Progress";
    return data+" Problems Solved";
  }

  const setHoveredDatefun=(date)=>{
    if(curMonth===currentMonth && date>currentDate && curYear===currentYear)setHoveredDate(null);
    else setHoveredDate(date);
  }

  const navigatetosubmission = (date) => {
    let mmonth = curMonth+1;
    let ddate = date;

    if (curMonth.toString().length === 1) {
        mmonth = "0" + mmonth;
    }

    if (date.toString().length === 1) {
        ddate = "0" + date;
    }

    navigate(`/submissions/all?date=${curYear}-${mmonth}-${ddate}`);
};


  
  useEffect(() => {
    const countdownInterval = setInterval(() => {
      const updatedTime = new Date(today.toLocaleString('en-US', { timeZone: 'Asia/Kolkata' }));
      const hours = 23 - updatedTime.getHours();
      const minutes = 59 - updatedTime.getMinutes();
      const seconds = 59 - updatedTime.getSeconds();
      setTimeLeft({ hours, minutes, seconds });
    }, 1000);

    return () => clearInterval(countdownInterval);
  }, [today]);


  return (calenderData.length!=0 &&
    <div className={styles.calendar}>

    <div className={styles.header}>
        <span className={styles.day}>{`${calenderData[curMonth+1]===undefined?0:calenderData[curMonth+1]} Done`}</span>
        <span className={styles.time}>
          {timeLeft.hours.toString().length==1?'0':''}{timeLeft.hours}: {timeLeft.minutes.toString().length==1?'0':''}{timeLeft.minutes}: {timeLeft.seconds.toString().length==1?'0':''}{timeLeft.seconds} left
        </span>
        <div className='text-center'>
        <span className={`${styles.icon}`} style={{marginLeft:"25px"}} onClick={()=>iconfun("left")}><FontAwesomeIcon icon={faChevronLeft} /></span>
          <span className={styles.month}>{monthNames[curMonth]}</span>
        <span style={{fontSize:"smaller"}}  className={styles.icon} onClick={()=>iconfun("right")}><FontAwesomeIcon icon={faChevronRight} /></span>
        </div>
      </div>
      {/* Display the days of the week */}
      <div className={styles.daysOfWeek}>
        {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((day, index) => (
          <span key={index} className={styles.dayOfWeek}>
            {day}
          </span>
        ))}
      </div>

      <div className={styles.dates}>
        {Array.from({ length: startingDay }, (_, index) => (
          <div key={`empty-${index}`} className={styles.emptyCell} />
        ))}

        {/* Dates for the current month */}
        {dates.map((date) => (
          <div
            key={date}
            className={`${styles.date} ${(date==currentDate && currentMonth==curMonth && curYear==currentYear)?styles.curdate:(getdata(date)==="No Progress"?'':styles.activedate)}`}
            onClick={() => {
              navigatetosubmission(date)
            }}
            onMouseEnter={() => setHoveredDatefun(date)}
            onMouseLeave={() => setHoveredDate(null)}
          >
          {date}
            {hoveredDate === date && (
              <span className={styles.number}>{getdata(date)}</span> 
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default CalendarGrid;
