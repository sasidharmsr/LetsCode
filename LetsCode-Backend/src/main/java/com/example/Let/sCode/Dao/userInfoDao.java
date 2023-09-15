package com.example.Let.sCode.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Let.sCode.Entitys.UserInfoEntity;

@Repository
public interface userInfoDao extends JpaRepository<UserInfoEntity,Integer>{
    
    UserInfoEntity findByUserId(Integer id);
}


// @Repository
// public interface UserInfoDao extends JpaRepository<UserInfoEntity, Integer> {
    
//     @Query(value = "SELECT * FROM user_info WHERE city = ?1", nativeQuery = true)
//     List<UserInfoEntity> findByCity(String city);
// }


// @Repository
// public interface UserInfoDao extends JpaRepository<UserInfoEntity, Integer> {
    
//     @Query("SELECT u FROM UserInfoEntity u WHERE u.city = :city")
//     List<UserInfoEntity> findByCity(@Param("city") String city);
// }
