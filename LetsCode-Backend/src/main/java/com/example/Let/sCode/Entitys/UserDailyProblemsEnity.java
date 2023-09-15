package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_daily_problems")
@Data
public class UserDailyProblemsEnity {
    
      
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "platform", length = 20)
    private String platform;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "contest_id")
    private String contestId;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "rating")
    private String rating;
    
    @Column(name = "indicator")
    private String indicator;
    
    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name="session_id")
    private String sessionId;

}

