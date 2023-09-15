package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Let.sCode.Entitys.TopicEntity;

@Repository
public interface topicDao extends JpaRepository<TopicEntity,Integer> {
    List<TopicEntity> findAll();
}
