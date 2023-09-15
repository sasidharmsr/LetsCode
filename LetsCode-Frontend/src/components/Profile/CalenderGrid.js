import React, { useState } from "react";

import CalendarHeatmap from "react-calendar-heatmap";
import ReactTooltip from "react-tooltip";

import "./calender.css";
import styles from "../../assets/Profile.module.css"

const today = new Date();

function Heatmap({calendarData,years,calendarDescription}) {
  
  const [presentYear,setYear]=useState(years[0]);
  const problemsData=[];
  const presentDate=new Date(presentYear,today.getMonth(),today.getDate())
  calendarData=Object(calendarData)
  for(const key in calendarData){
    problemsData.push({
      date:key,
      count:calendarData[key]
    })
  }
  
  const data = getRange(365).map((index) => {
    const dateObject=shiftDate(presentDate, -index)
    const year = dateObject.getFullYear();
    const month = String(dateObject.getMonth() + 1).padStart(2, '0');
    const day = String(dateObject.getDate()).padStart(2, '0');
    const formattedDateString = `${year}-${month}-${day}`;
    let count=0
    if(calendarData.hasOwnProperty(formattedDateString))count=calendarData[formattedDateString]
    return {
      date: dateObject,
      count: count
    };
  });


  return (
    <div className={`container ${styles.calcContainer}`} >
      <div className={`row  ${styles.statsContainer}`}>
      <div className="col-12 col-md-6 d-flex justify-content-between align-items-center ">
      <h4 className={`${styles.countTitle}`}>
        {calendarDescription.submissions[presentYear]} <span className={styles.spanyeartitle}>{`submissions in ${presentYear}`}</span>
      </h4>
      <div className={`${styles.statItems} d-md-none mb-3`}>
        <select className={styles.dropdown} onChange={(event) => setYear(event.target.value)}>
            {years.map((year) => (
              <option key={year} value={year}>
                {year}
              </option>
            ))}
          </select>
        </div>
      </div>
      <div className={`col-12 col-md-6 ${styles.statItem}`}>
        <h4 className={styles.spanyeartitle}>
          Total active days: {calendarDescription.activedays[presentYear]}
        </h4>
        <h4 className={styles.spanyeartitle}>
          Max streak: {calendarDescription.streak[presentYear]}
        </h4>
        <div className={`${styles.statItems} d-none d-md-flex`}>
        <select className={styles.dropdown} onChange={(event) => setYear(event.target.value)}>
            {years.map((year) => (
              <option key={year} value={year}>
                {year}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
    <div className={` {styles.Calendarr}`}>
    <div className={styles.CalendarContainer}>
    <CalendarHeatmap
        startDate={shiftDate(presentDate, -365)}
        endDate={presentDate}
        values={data}
        classForValue={(value) => {
          if (!value) {
            return "color-empty";
          }
          return `${value.count}` < 5
            ? `color-github-${value.count}`
            : `color-github-5`;
        }}
        tooltipDataAttrs={(value) => {
          return {
            "data-tip": `${
              value.count
            } submissions on ${value.date.toString().slice(4, 15)}`
          };
        }}
        gutterSize={4}
        showWeekdayLabels={false}
      />
      <ReactTooltip />
      </div>
    </div>
    </div>
  );
}

function shiftDate(date, numDays) {
  const newDate = new Date(date);
  newDate.setDate(newDate.getDate() + numDays);
  return newDate;
}

function getRange(count) {
  return Array.from({ length: count }, (_, i) => i);
}

function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

export default Heatmap;
