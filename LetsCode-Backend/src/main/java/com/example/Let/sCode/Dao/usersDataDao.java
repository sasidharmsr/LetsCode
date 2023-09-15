package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.Let.sCode.Entitys.UsersDataEntity;

public interface usersDataDao extends JpaRepository<UsersDataEntity,Integer> {
    
    UsersDataEntity findByUserId(int id);
    
    @Modifying
    @Transactional
    @Query("UPDATE UsersDataEntity SET cfCount=cfCount+:count where userId=:userId")
    void updateCfCount(@Param("count") Integer count,@Param("userId")Integer userId);


 

    @Query(value = "SELECT cf_count,lc_count,at_count,cf_rating,lc_rank,lc_rating,"+
                    "easy_count,medium_count,hard_count,cf_rank,school,company,linkedin_url,"+
                    "github_url,city,country,cf_photo,lc_photo,cf_school,name,email_id,lc_id,at_id,cf_id,user_name,users.user_id,phone_number,pic,role FROM users_data " +
                   "INNER JOIN users_info ON users_data.user_id = users_info.user_id " +
                   "INNER JOIN users ON users_data.user_id = users.user_id " +
                   "WHERE users.user_id = :userId", nativeQuery = true)
    List<Object[]> getUserDetails(@Param("userId") Long userId);


}
