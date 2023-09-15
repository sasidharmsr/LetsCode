package com.example.Let.sCode.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Let.sCode.Entitys.CommentsEntity;

import java.util.List;

public interface commentsDao extends JpaRepository<CommentsEntity, Long> {
    List<CommentsEntity> findByUserId(Long userId);
    List<CommentsEntity> findByPostId(Long postId);
}
