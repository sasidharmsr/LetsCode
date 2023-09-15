package com.example.Let.sCode.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.Let.sCode.Dao.atProblemsDao;
import com.example.Let.sCode.Entitys.AtProblemsEntity;
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
@Slf4j
@RequiredArgsConstructor
public class AtCoderService {

    private final atProblemsDao atProblemsDao;
    private final RedisService redisService;

    public JsonNode userIdCheck(userEntity user) throws JsonMappingException, JsonProcessingException,UsernameNotFoundException{
        WebClient.Builder builder = WebClient.builder();
        String userId=user.getAtUserId();
        String url = "https://kenkoooo.com/atcoder/atcoder-api/v2/user_info?user="+userId;
        String response=builder.build()
                        .get().uri(url).retrieve()
                        .bodyToMono(String.class)
                        .block();
        log.warn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        if(userService.jsonToString(jsonNode.get("accepted_count")).equals("0"))
        {
            throw new UserIdNotFoundExeption(InternalErrorCodes.AtUserIdNotFound.getCode());
        }
        return jsonNode;
    }

    public UsersDataEntity setUsersData(JsonNode data, UsersDataEntity usersdata) throws NumberFormatException, JsonProcessingException {
        usersdata.setAtCount(Integer.parseInt(userService.jsonToString(data.get("accepted_count"))));
        return usersdata;
    }
    
     public List<List<String>> extractUserSubmissions(String userId,long lastCheckedTime) throws JsonMappingException, JsonProcessingException  {
        
        List<List<String>> questions=new ArrayList<>();
        JsonNode jsonNode=extractUserSubmissionsfrom(userId,lastCheckedTime);
        if(jsonNode.isEmpty())return questions;
        while(jsonNode.isArray() && jsonNode.size()!=0){
              String lastSubmissionTime="";
            for(int i=0;i<jsonNode.size();i++)
            {
                var problem=jsonNode.get(i);
                if(userService.jsonToString(jsonNode.get(i).get("result")).equals("AC")){
                    List<String> question=new ArrayList<>();
                    question.add(userService.jsonToString(problem.get("problem_id")));
                    question.add(userService.jsonToString(problem.get("id")));
                    question.add(userService.jsonToString(problem.get("epoch_second")));
                    questions.add(question);
                }
                lastSubmissionTime=userService.jsonToString(problem.get("epoch_second"));
            }
            String LastSubmissionTime=lastSubmissionTime;
            CompletableFuture<JsonNode> jsonnode = CompletableFuture.supplyAsync(() -> {
            try {
                JsonNode response = extractUserSubmissionsfrom(userId,Long.parseLong(LastSubmissionTime)+1);
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



    public JsonNode extractUserSubmissionsfrom(String userId,long fromSeconds) throws JsonMappingException, JsonProcessingException {
        WebClient.Builder builder = WebClient.builder();
        String url = "https://kenkoooo.com/atcoder/atcoder-api/v3/user/submissions?user="+userId+"&from_second="+fromSeconds;
        String response=builder.build()
                        .get().uri(url).retrieve()
                        .bodyToMono(String.class)
                        .block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode;
    }

    public List<AtProblemsEntity> findAll()
    {
        return atProblemsDao.findAll();
    }

    public  Map<String,Pair<String,Integer>> mapQuestions(List<AtProblemsEntity> problems){
        Map<String,Pair<String,Integer>> map=new HashMap<>();
        for(AtProblemsEntity AtProblemsEntity:problems){
            map.put(AtProblemsEntity.getUniqueId(), Pair.of(AtProblemsEntity.getTitle(),AtProblemsEntity.getId()));
        }
        return map;
    }

    private Map<String, Pair<String, Integer>> mapQuestionsfromRedis() throws JsonMappingException, JsonProcessingException
    {
        if(redisService.exists("atmap")){
            return redisService.getMap("atmap");
        }
        Map<String, Pair<String, Integer>> map=mapQuestions(findAll());
        redisService.storeMap("atmap", map);
        return map;
    }

    public List<UsersSubmissionsEntity> saveUserSubmissions(String userId, List<UsersSubmissionsEntity> usersSubmissionsEntities,long lastCheckedTime) throws JsonProcessingException {
        List<List<String>> list=extractUserSubmissions(userId,lastCheckedTime);
        if(list.isEmpty())return usersSubmissionsEntities;
        Map<String,Pair<String,Integer>> map=mapQuestionsfromRedis();
        for(List<String> submission:list){
            if(!map.containsKey(submission.get(0)))continue;
            UsersSubmissionsEntity usersSubmissionsEntity=new UsersSubmissionsEntity();
            usersSubmissionsEntity.setSubmissionId(Integer.parseInt(submission.get(1)));
            usersSubmissionsEntity.setTimeStamp(Long.parseLong((submission.get(2))));
            usersSubmissionsEntity.setTitle(map.get(submission.get(0)).getFirst());
            usersSubmissionsEntity.setUniqueId(submission.get(0));
            UserSubmissionId userSubmissionId=new UserSubmissionId();
            userSubmissionId.setUser_id(JwtRequestFilter.userId);
            userSubmissionId.setType("at");
            userSubmissionId.setProblem_id(map.get(submission.get(0)).getSecond());
            usersSubmissionsEntity.setId(userSubmissionId);
            usersSubmissionsEntities.add(usersSubmissionsEntity);
        }
       return usersSubmissionsEntities;
    }
}
