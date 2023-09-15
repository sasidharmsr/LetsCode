package com.example.Let.sCode.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.Let.sCode.Entitys.userStatisticsEntity;

import lombok.Data;

@Data
public class profileResponse {
    
    private Map<String,Object> profileinfo;
    private Map<String,Long> calendarData;
    private List<Map<String,Object>> userPosts;
    private Map<String,Object> totalQuestions;
    private Map<String,Object> SolvedCfQuestions;
    private List<followers> followers;
    private List<followers> following;
    private Set<Integer> years;
    private Map<String,Object> yearsdata;
    private userStatisticsEntity userstats;
    private boolean followerCheck;
    private Long postsCount;

    public profileResponse(Object[] dbData){
        String query="cf_count,lc_count,at_count,cf_rating,lc_rank,lc_rating,"+
        "easy_count,medium_count,hard_count,cf_rank,school,company,linkedin_url,"+
        "github_url,city,country,cf_photo,lc_photo,cf_school,name,email_id,lc_id,at_id,cf_id,user_name,user_id,phone_number,pic,role";
        String[] columns=query.split(",");int index=0;
        profileinfo=new HashMap<>();
        for(String column:columns){
            profileinfo.put(column,dbData[index]);
            index++;
        }
    }

    public void addTotalQuestion(List<Object[]> dbData){
        totalQuestions=new HashMap<>();
        totalQuestions.put("EasyCf", dbData.get(0)[2]);
        totalQuestions.put("MediumCf", dbData.get(0)[3]);
        totalQuestions.put("HardCf", dbData.get(0)[4]);
        totalQuestions.put("EasyLc", dbData.get(0)[1]);
        totalQuestions.put("MediumLc", dbData.get(1)[1]);
        totalQuestions.put("HardLc", dbData.get(2)[1]);
    }

    public void cfsolvedQuestion(List<Object[]> dbData){
        SolvedCfQuestions=new HashMap<>();
        SolvedCfQuestions.put("Easy", dbData.get(0)[0]);
        SolvedCfQuestions.put("Medium", dbData.get(0)[1]);
        SolvedCfQuestions.put("Hard", dbData.get(0)[2]);
    }

}



