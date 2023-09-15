package com.example.Let.sCode.Entitys;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_topics")
public class UserTopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_id")
    private Long topicId;
}
