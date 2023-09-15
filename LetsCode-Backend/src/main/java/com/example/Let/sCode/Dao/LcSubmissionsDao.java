package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Let.sCode.Entitys.LcSubmissionsEntity;

@Repository
public interface LcSubmissionsDao extends JpaRepository<LcSubmissionsEntity, Long> {
    
    
    @Query(value = "SELECT DATE(FROM_UNIXTIME(timestamp)) AS date, count FROM lc_user_submissions where user_id= ?1", nativeQuery = true)
    List<Object[]> getSubmissions(Long userId);
}
