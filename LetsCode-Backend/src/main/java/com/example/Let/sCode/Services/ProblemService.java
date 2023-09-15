package com.example.Let.sCode.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Let.sCode.Dao.LcSubmissionsDao;
import com.example.Let.sCode.Dao.cfProblemsDao;
import com.example.Let.sCode.Dao.lcProblemsDao;
import com.example.Let.sCode.Dao.userSubmissionsDao;
import com.example.Let.sCode.Security.JwtRequestFilter;
import com.example.Let.sCode.json.profileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {
    
    private final userSubmissionsDao userSubmissionsDao;
    private final LcSubmissionsDao lcSubmissionsDao;
    private final cfProblemsDao cfProblemsDao;
    private final lcProblemsDao lcProblemsDao;

    public  Map<String,Long> getLatestmonthSubmissions(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> result = userSubmissionsDao.getCountByDateForUser(JwtRequestFilter.userId);
        Map<String,Long> res=new HashMap<>();
        for (Object[] row : result) {
            Date date = (Date) row[0];
            Long count = (Long) row[1];
            res.put(dateFormat.format(date), count);
        }
        return res;
 }


  public  profileResponse getLatestyearSubmissions(profileResponse response,Long userId) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> result = userSubmissionsDao.getCountByDateForYearofUser(userId);
        List<Object[]> lcList=lcSubmissionsDao.getSubmissions(userId);
        Set<String> dates = new TreeSet<>(Comparator.reverseOrder());
        Set<Integer> years=new HashSet<>();
        Map<String,Object> yearsData=new HashMap<>();
        Map<Integer,Integer> activedays=new HashMap<>();
        Map<Integer,Long> submissions=new HashMap<>();
        Map<String,Long> res=new HashMap<>();
        for (Object[] row : result) {
            Date date = (Date) row[0];
            Long count = (Long) row[1];
            res.put(dateFormat.format(date), count);
            dates.add(dateFormat.format(date));
            years.add(getYearFromDate(date));
            int year=getYearFromDate(date);
            if(activedays.containsKey(year))activedays.put(year,1+activedays.get(year));
            else activedays.put(year,1);
            if(submissions.containsKey(year))submissions.put(year,count+submissions.get(year));
            else submissions.put(year,count);
        }
        for(Object[] row:lcList){
            Date date = (Date) row[0];
            years.add(getYearFromDate(date));
            dates.add(dateFormat.format(date));
            Long count = (Long) row[1];
            int year=getYearFromDate(date);
            if(res.containsKey(dateFormat.format(date))){
                res.put(dateFormat.format(date),count+res.get(dateFormat.format(date)));
                submissions.put(year,count+submissions.get(year));
            }
            else{
                res.put(dateFormat.format(date),count);
                if(activedays.containsKey(year))activedays.put(year,1+activedays.get(year));
                else activedays.put(year,1);
                if(submissions.containsKey(year))submissions.put(year,count+submissions.get(year));
                else submissions.put(year,count);
            }
        }
        Map<Integer, Integer> maxStreaksPerYear = new HashMap<>();
        int currentYear = -1;
        int currentStreak = 0;
        int maxStreak = 0;
        Date prevDate = null;
        for(String date:dates){
            LocalDate localDate = LocalDate.parse(date);
            int year = localDate.getYear();
            if (currentYear == -1) {
                currentYear = year;
                currentStreak = 1;
                maxStreak = 1;
            } else if (currentYear == year) {
                if (prevDate != null && daysBetween(prevDate, dateFormat.parse(date)) == 1) {
                    currentStreak++;
                    maxStreak = Math.max(maxStreak, currentStreak);
                    maxStreaksPerYear.put(currentYear, maxStreak);
                } else {
                    currentStreak = 1;
                }
            } else {
                maxStreaksPerYear.put(currentYear, maxStreak);
                currentYear = year;
                currentStreak = 1;
                maxStreak = 1;
            }
            prevDate = dateFormat.parse(date);
        }
        yearsData.put("activedays",activedays); yearsData.put("streak",maxStreaksPerYear);
        yearsData.put("submissions",submissions);
        response.setYearsdata(yearsData);
        System.out.println(yearsData);
        response.setCalendarData(res);
        response.setYears(years);
        return response;
 }
    
        private int daysBetween(Date date1, Date date2) {
            long difference = Math.abs(date2.getTime() - date1.getTime());
            return (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        }

    public List<Map<String,Object>> getcfContestsdata(int type,Long userId,Long contestId){
        List<Object[]> res= userSubmissionsDao.getProblemDataForUserAndContestCf(type,userId,contestId);
        int count=userSubmissionsDao.countCfContestsWithType(type);
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> response=new ArrayList<>();
        map.put("total", count);response.add(map);
        for(Object[] row:res){
            map=new HashMap<>();
            map.put("contest_id",row[0]);map.put("contest_title",row[1]);
            map.put("index",row[2]);map.put("title",row[3]);
            map.put("submission_id",row[4]);
            response.add(map);
        }
        return response;
    }
    public int getYearFromDate(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String yearString = yearFormat.format(date);
        return Integer.parseInt(yearString);
    }

     public List<Map<String,Object>> getatContestsdata(String type,Long userId,String contestId){
        List<Object[]> res= userSubmissionsDao.getProblemDataForUserAndContestAt(type,userId,contestId);
        Integer count=Integer.parseInt(userSubmissionsDao.countAtContestsWithType(type).substring(3));
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> response=new ArrayList<>();
        map.put("total", count);response.add(map);
        for(Object[] row:res){
            map=new HashMap<>();
            map.put("contest_id",row[0]);map.put("contest_title",row[1]);
            map.put("index",row[2]);map.put("title",row[3]);
            map.put("submission_id",row[4]);
            response.add(map);
        }
        return response;
    }

    public List<Map<String,Object>> getUserSubmissions(Long userId,String type,String pattern,Long offset){
        List<Map<String,Object>> result=new ArrayList<>();
        Map<String,Object> res=new HashMap<>();res.put("total",userSubmissionsDao.findUserSubmissionscount(userId, type, pattern));
        result.add(res);
        List<Object[]> list=userSubmissionsDao.findUserSubmissionsWithDates(userId, type, pattern,offset);
        for(Object[] i:list){
            res=new HashMap<>();
            res.put("date",(Date)i[0]);
            res.put("title",(String)i[1]);
            res.put("uniqureId",(String)i[2]);
            res.put("type",(String)i[3]);
            res.put("submissionId",(Integer)i[4]);
            result.add(res);
        }
        return result;
    }

    public Map<String,Object> getCfProblems(Long userId,int condition,Long offset,int ratingL,int ratingR,String title){
        List<Map<String,Object>> result=new ArrayList<>();
        Object total=0;
        List<Object[]> list=cfProblemsDao.getFilteredCfProblems(userId,condition,offset,ratingL,ratingR,title);
        total=cfProblemsDao.getFilteredCfProblemscount(userId,condition,ratingL,ratingR,title);
        for(Object[] i:list){
            Map<String,Object> res=new HashMap<>();
            res.put("contestId",(Integer)i[0]);
            res.put("indicator",(String)i[1]);
            res.put("title",(String)i[2]);
            res.put("rating",(Integer)i[3]);
            res.put("topicsIds",i[4]);res.put("submissionId",i[5]);
            result.add(res);
        }
        Map<String,Object> finalResult=new HashMap<>();;
        finalResult.put("total",total);
        finalResult.put("questions",result);
        return finalResult;
    }

    public Map<String,Object> getCfProblemsWithOrtopics(Long userId,int condition,Long offset,String topics,int ratingL,int ratingR,String title){
        List<Map<String,Object>> result=new ArrayList<>();
        Object total=0;
        List<Object[]> list=cfProblemsDao.getFilteredCfProblemswithOrTopics(userId,condition,topics,offset,ratingL,ratingR,title);
        total=cfProblemsDao.getFilteredCfProblemswithOrTopicscount(userId,condition,topics,ratingL,ratingR,title);
        for(Object[] i:list){
            Map<String,Object> res=new HashMap<>();
            res.put("contestId",(Integer)i[0]);
            res.put("indicator",(String)i[1]);
            res.put("title",(String)i[2]);
            res.put("rating",(Integer)i[3]);
            res.put("topicsIds",i[4]);res.put("submissionId",i[5]);
            result.add(res);
        }
        Map<String,Object> finalResult=new HashMap<>();;
        finalResult.put("total",total);
        finalResult.put("questions",result);
        return finalResult;
    }

    public Map<String,Object> getCfProblemsWithAndtopics(Long userId,int condition,Long offset,String topics,int topicCount,int ratingL,int ratingR,String title){
        List<Map<String,Object>> result=new ArrayList<>();
        Object total=0;
        List<Object[]> list=cfProblemsDao.getFilteredCfProblemswithAndTopics(userId,condition,topics,topicCount,offset,ratingL,ratingR,title);
        total=cfProblemsDao.getFilteredCfProblemswithAndTopicscount(userId,condition,topics,topicCount,ratingL,ratingR,title);
        for(Object[] i:list){
            Map<String,Object> res=new HashMap<>();
            res.put("contestId",(Integer)i[0]);
            res.put("indicator",(String)i[1]);
            res.put("title",(String)i[2]);
            res.put("rating",(Integer)i[3]);
            res.put("topicsIds",i[4]);res.put("submissionId",i[5]);
            result.add(res);
        }
        Map<String,Object> finalResult=new HashMap<>();;
        finalResult.put("total",total);
        finalResult.put("questions",result);
        return finalResult;
    }

     public Map<String,Object> getLcProblems(Long userId,int condition,Long offset,int ratingL,int ratingR,int difficult,String title){
        List<Map<String,Object>> result=new ArrayList<>();
        Object total=0;
        List<Object[]> list=lcProblemsDao.getFilteredLcProblems(userId,condition,offset,ratingL,ratingR,difficult,title);
        total=lcProblemsDao.getFilteredLcProblemscount(userId,condition,ratingL,ratingR,difficult,title);
        for(Object[] i:list){
            Map<String,Object> res=new HashMap<>();
            res.put("id",(Integer)i[0]);
            res.put("path",(String)i[1]);
            res.put("title",(String)i[2]);
            res.put("difficulty",(String)i[3]);
            res.put("acRate",i[4]);
            res.put("topicsIds",i[5]);res.put("submissionId",i[6]);
            result.add(res);
        }
        Map<String,Object> finalResult=new HashMap<>();;
        finalResult.put("total",total);
        finalResult.put("questions",result);
        return finalResult;
    }

    public Map<String,Object> getLcProblemsWithOrtopics(Long userId,int condition,Long offset,String topics,int ratingL,int ratingR,int difficult,String title){
        List<Map<String,Object>> result=new ArrayList<>();
        Object total=0;
        List<Object[]> list=lcProblemsDao.getFilteredLcProblemswithOrTopics(userId,condition,topics,offset,ratingL,ratingR,difficult,title);
        total=lcProblemsDao.getFilteredLcProblemswithOrTopicscount(userId,condition,topics,ratingL,ratingR,difficult,title);
        for(Object[] i:list){
            Map<String,Object> res=new HashMap<>();
            res.put("id",(Integer)i[0]);
            res.put("path",(String)i[1]);
            res.put("title",(String)i[2]);
            res.put("difficulty",(String)i[3]);
            res.put("acRate",i[4]);
            res.put("topicsIds",i[5]);res.put("submissionId",i[6]);
            result.add(res);
        }
        Map<String,Object> finalResult=new HashMap<>();;
        finalResult.put("total",total);
        finalResult.put("questions",result);
        return finalResult;
    }

    public Map<String,Object> getLcProblemsWithAndtopics(Long userId,int condition,Long offset,String topics,int topicCount,int ratingL,int ratingR,int difficulty,String title){
        List<Map<String,Object>> result=new ArrayList<>();
        Object total=0;
        List<Object[]> list=lcProblemsDao.getFilteredLcProblemswithAndTopics(userId,condition,topics,topicCount,offset,ratingL,ratingR,difficulty,title);
        total=lcProblemsDao.getFilteredLcProblemswithAndTopicscount(userId,condition,topics,topicCount,ratingL,ratingR,difficulty,title);
        for(Object[] i:list){
            Map<String,Object> res=new HashMap<>();
            res.put("id",(Integer)i[0]);
            res.put("path",(String)i[1]);
            res.put("title",(String)i[2]);
            res.put("difficulty",(String)i[3]);
            res.put("acRate",i[4]);
            res.put("topicsIds",i[5]);res.put("submissionId",i[6]);
            result.add(res);
        }
        Map<String,Object> finalResult=new HashMap<>();;
        finalResult.put("total",total);
        finalResult.put("questions",result);
        return finalResult;
    }
    

}
