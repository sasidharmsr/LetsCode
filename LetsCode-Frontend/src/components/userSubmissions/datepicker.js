import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Button } from 'reactstrap';
import styles from '../../assets/contests.module.css';
import { useNavigate } from 'react-router-dom';

function DateInputComponent({handlesearch}) {
    const initialYear = new Date().getFullYear();
    const initialMonth = new Date().getMonth();
    const initialDay = new Date().getDate();

    const [date, setdate] = useState({ year: initialYear, month: initialMonth, day: initialDay });

    const handleAddDate = () => {
        let formattedDate = '';
    
        if (date.year !== null) {
            formattedDate += date.year;
        } else {
            formattedDate += '$$$$';
        }
    
        if (date.month !== null) {
            formattedDate += `-${(date.month + 1).toString().padStart(2, '0')}`;
        } else {
            formattedDate += '-$$';
        }
    
        if (date.day !== null) {
            formattedDate += `-${(date.day).toString().padStart(2, '0')}`;
        } else {
            formattedDate += '-$$';
        }
        handlesearch(formattedDate);
    };
    

    const setcrtYear = (data) => {
        if (data !== null && data.getFullYear() >= 1970) {
            setdate({ year: data.getFullYear(), month: null, day: null });
        }
    };

    const setcrtMonth = (data) => {
        if (data !== null) {
            setdate({ year: date.year, month: data.getMonth(), day: date.day });
        }
    };

    const setcrtDay = (data) => {
        if (data !== null) {
            setdate({ year: date.year, month: date.month, day: data.getDate() });
        }
    };

    return (
        <div>
        <div className={styles.dateInputContainer}>
            <div className={styles.datediv}>
                <DatePicker
                    selected={new Date(date.year, 1, 1)}
                    onChange={(year) => setcrtYear(year)}
                    dateFormat="yyyy"
                    showYearPicker
                    scrollableYearDropdown
                    placeholderText="Select year"
                    noFocus
                />
            </div>
            <div className={styles.datediv}>
                <DatePicker
                    selected={
                        date.month !== null
                            ? new Date(date.year, date.month, 1)
                            : null
                    }
                    onChange={(data) => setcrtMonth(data)}
                    dateFormat="MMMM"
                    showMonthYearPicker
                    placeholderText="Select month and year"
                    noFocus
                />
            </div>
            <div className={styles.datediv}>
                <DatePicker
                    selected={
                        date.day !== null && date.month !== null
                            ? new Date(date.year, date.month, date.day)
                            : null
                    }
                    onChange={(data) => setcrtDay(data)}
                    dateFormat="dd"
                    placeholderText="Select day"
                    noFocus
                />
            </div>
            <div className='d-none d-md-block'>    
                <Button onClick={handleAddDate} style={{ marginRight: 15 }}>Search</Button>
            </div>
        </div>
           <div className='d-md-none d-flex justify-content-center'>       
           <Button onClick={handleAddDate} style={{ marginRight: 15 }}>Search</Button>
       </div>
       </div>
    );
}

export default DateInputComponent;
