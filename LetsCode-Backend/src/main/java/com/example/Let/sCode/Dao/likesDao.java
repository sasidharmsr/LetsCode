package com.example.Let.sCode.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Let.sCode.Entitys.LikesEntity;

import java.util.List;

public interface likesDao extends JpaRepository<LikesEntity, Long> {
    List<LikesEntity> findByUserId(Long userId);
    List<LikesEntity> findByPostId(Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
