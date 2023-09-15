package com.example.Let.sCode.Services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.Let.sCode.Dao.LcSubmissionsDao;
import com.example.Let.sCode.Dao.lcProblemsDao;
import com.example.Let.sCode.Entitys.LcProblemsEntity;
import com.example.Let.sCode.Entitys.LcSubmissionsEntity;
import com.example.Let.sCode.Entitys.UserInfoEntity;
import com.example.Let.sCode.Entitys.UserSubmissionId;
import com.example.Let.sCode.Entitys.UsersDataEntity;
import com.example.Let.sCode.Entitys.UsersSubmissionsEntity;
import com.example.Let.sCode.Entitys.userEntity;
import com.example.Let.sCode.Exceptions.InternalErrorCodes;
import com.example.Let.sCode.Exceptions.UserIdNotFoundExeption;
import com.example.Let.sCode.Security.JwtRequestFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeetCodeService {
    
    private final lcProblemsDao lcProblemsDao;
    private final LcSubmissionsDao lcSubmissionsDao;
    private final RedisService redisService;

    public JsonNode userIdCheck(userEntity user) throws JsonProcessingException,UsernameNotFoundException{
        WebClient.Builder builder = WebClient.builder();
        String userId=user.getLcUserId();
        String  query= "{ userContestRanking(username:$username){rating} allQuestionsCount {difficulty count } matchedUser(username: $username) {  username linkedinUrl githubUrl contributions { points } profile { reputation userAvatar school company ranking } submitStats  {   acSubmissionNum { difficulty count submissions }  } }  }";
        query = query.replaceAll("\\$username", "\"" + userId + "\"");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("query",query);
        String url = "https://leetcode.com/graphql";
        String graphql=mapToString(jsonMap);
        try {
            // Create HttpHeaders with the desired headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Make a POST request with body and headers
            String response = builder.build()
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(graphql))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
             String firstLetterCapital = userId.substring(0, 1).toUpperCase() + userId.substring(1);
             String firstLetterSmall = userId.substring(0, 1).toLowerCase() + userId.substring(1);
             
            if(!(userService.jsonToString(jsonNode.get("data").get("matchedUser").get("username")).contains(firstLetterCapital)) && 
            !(userService.jsonToString(jsonNode.get("data").get("matchedUser").get("username")).contains(firstLetterSmall))
            )
            {
                throw new UserIdNotFoundExeption(InternalErrorCodes.LcUserIdNotFound.getCode());
            }
            return jsonNode;
        }
        catch(Exception e){
            throw e;
        }
    }

    public UserInfoEntity setlcProfile(JsonNode data,UserInfoEntity userInfoEntity) throws JsonProcessingException
    {
        JsonNode matchedUser=data.get("data").get("matchedUser");
        JsonNode profile=matchedUser.get("profile");
        userInfoEntity.setCompany(userService.jsonToString(profile.get("company")));
        userInfoEntity.setGithubUrl(userService.jsonToString(matchedUser.get("githubUrl")));
        userInfoEntity.setLinkedinUrl(userService.jsonToString(matchedUser.get("linkedinUrl")));
        userInfoEntity.setLcPhoto(userService.jsonToString(profile.get("userAvatar")));
        userInfoEntity.setSchool(userService.jsonToString(profile.get("school")));
        return userInfoEntity;
    }

    public UsersDataEntity setUsersData(JsonNode data,UsersDataEntity usersdata) throws NumberFormatException, JsonProcessingException {
        data=data.get("data");
        JsonNode matchedUser=data.get("matchedUser");
        JsonNode problems=matchedUser.get("submitStats").get("acSubmissionNum");
        usersdata.setLcCount(Integer.parseInt(userService.jsonToString(problems.get(0).get("count"))));
        usersdata.setEasyCount(Integer.parseInt(userService.jsonToString(problems.get(1).get("count"))));
        usersdata.setMediumCount(Integer.parseInt(userService.jsonToString(problems.get(2).get("count"))));
        usersdata.setHardCount(Integer.parseInt(userService.jsonToString(problems.get(3).get("count"))));
        usersdata.setLcRank(Integer.parseInt(userService.jsonToString(matchedUser.get("profile").get("ranking"))));
       usersdata.setLcRating((int)Float.parseFloat(userService.jsonToString(data.get("userContestRanking").get("rating"))));
       return usersdata;
    }

    public void getLcCalenderdata(String userId) throws Exception{
        WebClient.Builder builder = WebClient.builder();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        List<Integer> years=new ArrayList<>();
        String  query= "\n query userProfileCalendar($username: String!, $year: Int) {\n matchedUser(username: $username) {\n userCalendar(year: $year) {\n activeYears\n streak\n totalActiveDays\n dccBadges {\n timestamp\n badge {\n name\n icon\n }\n }\n submissionCalendar\n }\n }\n}\n ";
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> varibles = new HashMap<>();
        List<LcSubmissionsEntity> entityList=new ArrayList<>();
        varibles.put("username",userId);
        varibles.put("year",currentYear);
        jsonMap.put("query",query);jsonMap.put("variables",varibles);
        jsonMap.put("operationName", "userProfileCalendar");
        String url = "https://leetcode.com/graphql";
        String graphql=mapToString(jsonMap);
        while(true){
        try {
            // Create HttpHeaders with the desired headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Make a POST request with body and headers
            String response = builder.build()
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(graphql))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode submissions = objectMapper.readTree(response).get("data").get("matchedUser").get("userCalendar");
            if(currentYear==(int)varibles.get("year")){
               for(JsonNode data:submissions.get("activeYears")){
                    Integer year=Integer.parseInt(userService.jsonToString(data));
                    if(year!=currentYear)years.add(year);
               }
            }
            Map<Long, Long> mapofCurYear = objectMapper.readValue(submissions.get("submissionCalendar").asText(), new TypeReference<Map<Long, Long>>() {});
            for(Long key:mapofCurYear.keySet()){
                LcSubmissionsEntity lcSubmissionsEntity=new LcSubmissionsEntity();
                lcSubmissionsEntity.setUserId((JwtRequestFilter.userId.longValue()));
                lcSubmissionsEntity.setTimestamp(key);lcSubmissionsEntity.setCount(mapofCurYear.get(key));
                entityList.add(lcSubmissionsEntity);
            }
            if(years.isEmpty())break;
            varibles.put("year", years.remove(years.size()-1));
            jsonMap.put("variables",varibles);
            graphql=mapToString(jsonMap);
        }
        catch(Exception e){
            throw e;
        }
    }
    System.out.println("Before..calendardata");
     lcSubmissionsDao.saveAll(entityList);
    }

     public Map<String,Pair<Integer,Long>> extractUserSubmissions(String userId,long lastCheckedTime) throws JsonProcessingException{
        WebClient.Builder builder = WebClient.builder();
        String  query= "query recentAcSubmissions($username: String!, $limit: Int!) { recentAcSubmissionList(username: $username, limit: $limit) { id title titleSlug timestamp } }";
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> varibles = new HashMap<>();
        varibles.put("username",userId);
        varibles.put("limit",20);
        jsonMap.put("query",query);jsonMap.put("variables",varibles);
        jsonMap.put("operationName", "recentAcSubmissions");
        String url = "https://leetcode.com/graphql";
        String graphql=mapToString(jsonMap);
        try {
            // Create HttpHeaders with the desired headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Make a POST request with body and headers
            String response = builder.build()
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(graphql))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode submissions = objectMapper.readTree(response).get("data").get("recentAcSubmissionList");
            Map<String,Pair<Integer,Long>> questions=new HashMap<>();
            for(int i=0;i<submissions.size();i++)
            {
                if(lastCheckedTime>=(Long.parseLong(userService.jsonToString(submissions.get(i).get("timestamp")))))break;
                String question=(userService.jsonToString(submissions.get(i).get("titleSlug")));
                Integer id=Integer.parseInt(userService.jsonToString(submissions.get(i).get("id")));
                Long timeStamp=Long.parseLong(userService.jsonToString(submissions.get(i).get("timestamp")));
                Pair<Integer,Long> pair=Pair.of(id, timeStamp);
                questions.put(question,pair);
            }
            return questions;
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<LcProblemsEntity> findAll()
    {
        return lcProblemsDao.findAll();
    }

    public  Map<String,Pair<String,Integer>> mapQuestions(List<LcProblemsEntity> problems){
        Map<String,Pair<String,Integer>> map=new HashMap<>();
        for(LcProblemsEntity lcProblemsEntity:problems){
            map.put(lcProblemsEntity.getPath(), Pair.of(lcProblemsEntity.getTitle(),lcProblemsEntity.getId()));
        }
        return map;
    }


    public List<UsersSubmissionsEntity> saveUserSubmissions(String userId, List<UsersSubmissionsEntity> usersSubmissionsEntities,long lastCheckedTime) throws JsonProcessingException {
        Map<String,Pair<Integer,Long>> list=extractUserSubmissions(userId,lastCheckedTime);
        if(list.isEmpty())return usersSubmissionsEntities;
        Map<String,Pair<String,Integer>> map=mapQuestionsfromRedis();
        for(String uniqueid:list.keySet()){
            if(!map.containsKey(uniqueid))continue;
            UsersSubmissionsEntity usersSubmissionsEntity=new UsersSubmissionsEntity();
            usersSubmissionsEntity.setSubmissionId(list.get(uniqueid).getFirst());
            usersSubmissionsEntity.setTimeStamp(list.get(uniqueid).getSecond());
            usersSubmissionsEntity.setTitle(map.get(uniqueid).getFirst());
            usersSubmissionsEntity.setUniqueId(uniqueid);
            UserSubmissionId userSubmissionId=new UserSubmissionId();
            userSubmissionId.setUser_id(JwtRequestFilter.userId);
            userSubmissionId.setType("lc");
            userSubmissionId.setProblem_id(map.get(uniqueid).getSecond());
            usersSubmissionsEntity.setId(userSubmissionId);
            usersSubmissionsEntities.add(usersSubmissionsEntity);
        }
       return usersSubmissionsEntities;
    }
    
    private Map<String, Pair<String, Integer>> mapQuestionsfromRedis() throws JsonMappingException, JsonProcessingException
    {
        if(redisService.exists("lcmap")){
            return redisService.getMap("lcmap");
        }
        Map<String, Pair<String, Integer>> map=mapQuestions(findAll());
       redisService.storeMap("lcmap", map);
        return map;
    }

    public static String mapToString(Map<String,Object> map)
    {
        String res="";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert the map to a JSON string
            res = objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
