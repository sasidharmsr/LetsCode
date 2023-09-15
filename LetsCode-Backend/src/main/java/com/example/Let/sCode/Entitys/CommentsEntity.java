package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_comments")
@Data
public class CommentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "user_id")
    private Long    userId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "time_stamp")
    private Long timeStamp;
}
