package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Let.sCode.Entitys.UserDailyProblemsEnity;


public interface dailyProblemsdao extends JpaRepository<UserDailyProblemsEnity, Long> {
   
    @Query("From UserDailyProblemsEnity where name=:title order by timestamp limit 1")
    UserDailyProblemsEnity getDailyChallengeByname(@Param("title") String title);

    @Query("From UserDailyProblemsEnity where sessionId=:sessionId")
    List<UserDailyProblemsEnity> getDailyChallengesById(@Param("sessionId") String sessionId);
}
