package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "posts")
@Data
public class PostsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="post_description",columnDefinition = "TEXT")
    private String postDescription;

    @Column(name="pic")
    private String pic;

    @Column(name="post_type",length = 20)
    private String postType;

    @Column(name = "likes_count")
    private int likesCount = 0;

    @Column(name = "comments_count")
    private int commentsCount = 0;

    @Column(name = "created_at", updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;



}
