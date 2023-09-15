import React from 'react';
import Chart from 'react-google-charts';
import styles from '../../assets/Profile.module.css'

const PieChart = ({chartData}) => {
  // Create an array with the custom legend labels
  const chartDataWithLabels = [
    ['Platform', 'Problems Count'],
    ['Codeforces', chartData.cf],
    ['LeetCode', chartData.lc],
    ['AtCoder', chartData.at],
  ].map((entry) => [`${entry[0]} (${entry[1]})`, entry[1]]);


  // Define colors based on background color
  const backgroundColor = '#282828';
  const chartColors = ['#FAC74E', '#59A5D8', '#E76F51'];

  const chartContainerStyle = {
    width: '100%',
    height: '260px',
    borderRadius: '10px',
    overflow: 'hidden', 
  };

  console.log("chart........")

  return (
    <div style={chartContainerStyle}>
      <Chart
        width={'100%'}
        height={'260px'}
        chartType="PieChart"
        loader={
          <div className='d-flex justify-content-center align-items-center mb-3' style={{color:"white"}}>
        Loading Chart.....
      </div>}
        data={chartDataWithLabels} 
        options={{
          title: 'Solved Problems',
          backgroundColor,
          pieSliceBorderColor: backgroundColor,
          titleTextStyle: {
            color: '#9fa1a4',
            fontSize: 16,
          },
          legend: {
            position: 'right', 
            alignment: 'center',
            textStyle: {
              color: 'white',
              fontSize: 14,
            },
          },
          slices: chartColors.map((color) => ({ color })),
        }}
        rootProps={{ 'data-testid': '1' }}
      />
    </div>
  );
};

export default PieChart;
