package com.example.Let.sCode.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.example.Let.sCode.json.statsResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServices {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void storeMap(String key, Map<String, Pair<String, Integer>> map) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(map);
        redisTemplate.opsForValue().set(key, jsonValue);
    }
    public Map<String, Pair<String, Integer>> getMap(String key) throws JsonMappingException, JsonProcessingException  {
        System.out.println("Getting From Redis");
        String jsonValue = redisTemplate.opsForValue().get(key);
        return objectMapper.readValue(jsonValue,objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Pair.class));
    }
    public long getLatestTimeStamp(String key){
        if(!exists(key))return -1;
        return Long.parseLong(redisTemplate.opsForValue().get(key));
    }

    public void setLatestTimeStamp(String key,long value){
        redisTemplate.opsForValue().set(key,""+value);
    }

    public void setOtp(String key, String value){
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key, 150, TimeUnit.SECONDS);
    }

    public String getOtp(String key){
        if(!exists(key))return null;
        return redisTemplate.opsForValue().get(key);
    }

     public void setToken(String key, String value){
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
    }

     public String getToken(String key){
        if(!exists(key))return null;
        return redisTemplate.opsForValue().get(key);
    }

    public boolean exists(String key)
    {
        return redisTemplate.hasKey(key);
    }
    public void storechatId(String chatId, Map<String,String> data) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(chatId, jsonValue);
    }
    public Map<String,String> getUserByChatId(String chatId) {
        String jsonValue = redisTemplate.opsForValue().get(chatId);
        Map<String,String> res=new HashMap<>();
        if (jsonValue != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonValue);
                res.put("userId",jsonObject.getString("userId"));
                res.put("userName",jsonObject.getString("userName"));
                res.put("name",jsonObject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return res;
    }

    public void deleteKey(String chatId) {
        redisTemplate.delete(chatId);
    }

    public void setStats(String chatId, statsResp response) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(response);
        redisTemplate.opsForValue().set(chatId, jsonValue);
        redisTemplate.expire(chatId, 2, TimeUnit.DAYS);
    }

      public statsResp getStats(String chatId) {
        String jsonValue = redisTemplate.opsForValue().get(chatId);
        statsResp statsResp=new statsResp();
        if (jsonValue != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonValue);
                statsResp.setCfProblems(getstatsMap(jsonObject.optString("cfProblems", null)));
                statsResp.setLcProblems(getstatsMap(jsonObject.optString("lcProblems", null)));
                statsResp.setAtProblems(getstatsMap(jsonObject.optString("atProblems", null)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return statsResp;
    }

    public void setLock(String key,String value) {
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key, 30, TimeUnit.SECONDS);
    }

    public String getLock(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public void setTimeStamp(String key, long timestamp) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(key);
        redisTemplate.opsForValue().set(key, jsonValue);
    }

    private List<Map<String, Object>>  getstatsMap(String jsonstring){
        JSONArray cfArray = new JSONArray(jsonstring);
        List<Map<String, Object>> cfProblemsList = new ArrayList<>();
        for (int i = 0; i < cfArray.length(); i++) {
            JSONObject cfObj = cfArray.getJSONObject(i);
            Map<String, Object> cfMap = new HashMap<>();
            Iterator<String> keys = cfObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = cfObj.get(key);
                cfMap.put(key, value);
            }
            cfProblemsList.add(cfMap);
        }
        return cfProblemsList;
    }
}