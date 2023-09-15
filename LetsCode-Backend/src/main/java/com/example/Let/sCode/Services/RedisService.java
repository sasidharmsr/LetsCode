package com.example.Let.sCode.Services;

import redis.clients.jedis.Jedis;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.util.Pair;
import com.example.Let.sCode.json.statsResp;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final ObjectMapper objectMapper;

    @Value("${spring.redis.url}")
    String redisurl;

    private Jedis initializeJedis() {
        return new Jedis(redisurl);
    }

    public void storeMap(String key, Map<String, Pair<String, Integer>> map) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = objectMapper.writeValueAsString(map);
            jedis.set(key, jsonValue);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    public Map<String, Pair<String, Integer>> getMap(String key) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = jedis.get(key);
            try {
                return objectMapper.readValue(jsonValue, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Pair.class));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            closeJedis(jedis);
        }
    }

    public long getLatestTimeStamp(String key) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            if (!exists(key)) return -1;
            String value = jedis.get(key);
            return Long.parseLong(value);
        } finally {
            closeJedis(jedis);
        }
    }

    public void setLatestTimeStamp(String key, long value) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            jedis.set(key, String.valueOf(value));
        } finally {
            closeJedis(jedis);
        }
    }

    public void setOtp(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            jedis.setex(key, 150, value);
        } finally {
            closeJedis(jedis);
        }
    }

    public String getOtp(String key) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            if (!exists(key)) return null;
            return jedis.get(key);
        } finally {
            closeJedis(jedis);
        }
    }

    public void setToken(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            jedis.setex(key, 30 * 60, value);
        } finally {
            closeJedis(jedis);
        }
    }

    public String getToken(String key) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            if (!exists(key)) return null;
            return jedis.get(key);
        } finally {
            closeJedis(jedis);
        }
    }

    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            return jedis.exists(key);
        } finally {
            closeJedis(jedis);
        }
    }

    public void storechatId(String chatId, Map<String, String> data) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = objectMapper.writeValueAsString(data);
            jedis.set(chatId, jsonValue);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    public Map<String, String> getUserByChatId(String chatId) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = jedis.get(chatId);
            Map<String, String> res = new HashMap<>();
            if (jsonValue != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonValue);
                    res.put("userId", jsonObject.getString("userId"));
                    res.put("userName", jsonObject.getString("userName"));
                    res.put("name", jsonObject.getString("name"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
            return res;
        } finally {
            closeJedis(jedis);
        }
    }

    public void deleteKey(String chatId) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            jedis.del(chatId);
        } finally {
            closeJedis(jedis);
        }
    }

    public void setStats(String chatId, statsResp response) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = objectMapper.writeValueAsString(response);
            jedis.setex(chatId, 2 * 24 * 60 * 60, jsonValue); // 2 days expiration
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    public statsResp getStats(String chatId) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = jedis.get(chatId);
            statsResp statsResp = new statsResp();
            if (jsonValue != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonValue);
                    statsResp.setCfProblems(getstatsMap(jsonObject.optString("cfProblems", null)));
                    statsResp.setLcProblems(getstatsMap(jsonObject.optString("lcProblems", null)));
                    statsResp.setAtProblems(getstatsMap(jsonObject.optString("atProblems", null)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
            return statsResp;
        } finally {
            closeJedis(jedis);
        }
    }

    public void setLock(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            jedis.setex(key, 30, value);
        } finally {
            closeJedis(jedis);
        }
    }

    public String getLock(String key) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            return jedis.get(key);
        } finally {
            closeJedis(jedis);
        }
    }

    public void setTimeStamp(String key, long timestamp) {
        Jedis jedis = null;
        try {
            jedis = initializeJedis();
            String jsonValue = objectMapper.writeValueAsString(key);
            jedis.set(key, jsonValue);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    private List<Map<String, Object>> getstatsMap(String jsonstring) {
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

    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
