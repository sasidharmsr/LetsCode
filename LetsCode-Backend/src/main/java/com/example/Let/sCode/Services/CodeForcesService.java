package com.example.Let.sCode.Services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.Let.sCode.Dao.cfProblemsDao;
import com.example.Let.sCode.Entitys.CfProblemsEntity;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeForcesService {

    private final cfProblemsDao cfProblemsDao;
    private final RedisService redisService;
     
    public JsonNode userIdCheck(userEntity user) throws JsonMappingException, JsonProcessingException,UsernameNotFoundException{
        WebClient.Builder builder = WebClient.builder();
        String userId=user.getCfUserId();
        String url = "https://codeforces.com/api/user.info?handles="+userId;
        String response=builder.build()
                        .get().uri(url).retrieve()
                        .bodyToMono(String.class)
                        .block();
        log.warn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        if(userService.jsonToString(jsonNode.get("status")).equals("FAILED"))
        {
            throw new UserIdNotFoundExeption(InternalErrorCodes.cfUserIdNotFound.getCode());
        }
        return jsonNode;
    }

    public UserInfoEntity setcfProfile(JsonNode data,UserInfoEntity userInfoEntity) throws JsonProcessingException {
        JsonNode profile=data.get("result").get(0);
        userInfoEntity.setCity(userService.jsonToString(profile.get("city")));
        userInfoEntity.setCountry(userService.jsonToString(profile.get("country")));
        userInfoEntity.setCfPhoto(userService.jsonToString(profile.get("titlePhoto")));
        userInfoEntity.setOrganization(userService.jsonToString(profile.get("organization")));
        return userInfoEntity;
    }

    public UsersDataEntity setUsersData(JsonNode data, UsersDataEntity usersdata) throws NumberFormatException, JsonProcessingException {
        data=data.get("result").get(0);
        usersdata.setCfCount(0);
        usersdata.setCfFriends(Integer.parseInt(userService.jsonToString(data.get("friendOfCount"))));
        usersdata.setCfRank(userService.jsonToString(data.get("rank")));
        usersdata.setCfRating(Integer.parseInt(userService.jsonToString(data.get("rating"))));
        usersdata.setMaxRank(userService.jsonToString(data.get("maxRank")));
        usersdata.setMaxRating(Integer.parseInt(userService.jsonToString(data.get("maxRating"))));
        return usersdata;
    }

    
    public Map<String,Pair<Integer,Long>> extractUserSubmissions(String userId,long lastCheckedTime) throws JsonMappingException, JsonProcessingException  {
        
        Map<String,Pair<Integer,Long>> questions=new HashMap<>();
        int From=1;
        JsonNode jsonNode=extractUserSubmissionsfrom(userId,From,200).get("result");
        while(jsonNode.isArray() && jsonNode.size()!=0){
            for(int i=0;i<jsonNode.size();i++)
            {
                if(lastCheckedTime>=Long.parseLong(userService.jsonToString(jsonNode.get(i).get("creationTimeSeconds"))))return questions;
                var problem=jsonNode.get(i).get("problem");
                if(userService.jsonToString(jsonNode.get(i).get("verdict")).equals("OK")){
                    var uniqueid=(userService.jsonToString(problem.get("contestId")))+"/"+(userService.jsonToString(problem.get("index")));
                    Integer id=Integer.parseInt(userService.jsonToString(jsonNode.get(i).get("id")));
                    Long timeStamp=Long.parseLong(userService.jsonToString(jsonNode.get(i).get("creationTimeSeconds")));
                    Pair<Integer,Long> pair=Pair.of(id, timeStamp);
                    questions.put(uniqueid,pair);
                }
            }
            From+=200;
            final int from=From;
            CompletableFuture<JsonNode> jsonnode = CompletableFuture.supplyAsync(() -> {
            try {
                JsonNode response = extractUserSubmissionsfrom(userId,from,200).get("result");
                return response;
            } catch (JsonProcessingException | UsernameNotFoundException e) {
                throw new UserIdNotFoundExeption(e.getMessage());
            }
        }).exceptionally(ex -> null);

            jsonnode.join();
            if(jsonnode.join()==null)break;
            jsonNode=jsonnode.join();
        }
        return questions;
    }


    public JsonNode extractUserSubmissionsfrom(String userId,int From,int Count) throws JsonMappingException, JsonProcessingException {
        WebClient.Builder builder = WebClient.builder();
        String url = "https://codeforces.com/api/user.status?handle="+userId+"&from="+From+"&count="+Count;
        String response=builder.build()
                        .get().uri(url).retrieve()
                        .bodyToMono(String.class)
                        .block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode;
    }

     public List<CfProblemsEntity> findAll()
    {
        return cfProblemsDao.findAll();
    }
    
    public  Map<String,Pair<String,Integer>> mapQuestions(List<CfProblemsEntity> problems){

            Map<String,Pair<String,Integer>> map=new HashMap<>();
            for(CfProblemsEntity CfProblemsEntity:problems){
                map.put(CfProblemsEntity.getContestId()+"/"+CfProblemsEntity.getIndicator(), Pair.of(CfProblemsEntity.getTitle(),CfProblemsEntity.getId()));
            }
            return map;
        }

    private Map<String, Pair<String, Integer>> mapQuestionsfromRedis() throws JsonMappingException, JsonProcessingException
    {
        if(redisService.exists("cfmap")){
            return redisService.getMap("cfmap");
        }
        Map<String, Pair<String, Integer>> map=mapQuestions(findAll());
        redisService.storeMap("cfmap", map);
        return map;
    }

    
     public Pair<List<UsersSubmissionsEntity>,Integer> saveUserSubmissions(String userId, List<UsersSubmissionsEntity> usersSubmissionsEntities,long lastCheckedTime) throws JsonProcessingException {
        Map<String,Pair<Integer,Long>> list=extractUserSubmissions(userId,lastCheckedTime);
        if(list.isEmpty())return Pair.of(usersSubmissionsEntities,0);
        
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
            userSubmissionId.setType("cf");
            userSubmissionId.setProblem_id(map.get(uniqueid).getSecond());
            usersSubmissionsEntity.setId(userSubmissionId);
            usersSubmissionsEntities.add(usersSubmissionsEntity);
        }
        return Pair.of(usersSubmissionsEntities,list.size());
    }

    public List<Object[]> getCombinedtotalQuestions() {
        return cfProblemsDao.getCombinedtotalQuestions();
    }

}
