package com.example.Let.sCode.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Let.sCode.Dao.topicDao;
import com.example.Let.sCode.Entitys.TopicEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicServices {
    
    private final topicDao topicDaoo;

    public List<TopicEntity> getAllTopics(){
        List<TopicEntity> topics= topicDaoo.findAll();
        return topics;
    }
}
