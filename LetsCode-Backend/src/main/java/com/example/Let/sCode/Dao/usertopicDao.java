package com.example.Let.sCode.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Let.sCode.Entitys.UserTopicEntity;

@Repository
public interface usertopicDao extends JpaRepository<UserTopicEntity, Long> {
    // Add custom query methods if needed
}
