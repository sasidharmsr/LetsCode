package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Let.sCode.Entitys.FollowEntity;
import com.example.Let.sCode.Entitys.FollowEntityId;


public interface followDao extends JpaRepository<FollowEntity,FollowEntityId>{
    
    @Query("SELECT followEntityId.following_id, u.userName , u.name,u.pic FROM FollowEntity f INNER JOIN userEntity u ON u.userId = f.followEntityId.following_id WHERE f.followEntityId.follower_id= :userId")
    List<Object[]> findfollowing(@Param("userId") Long userId);

   
   @Query("SELECT followEntityId.follower_id, u.userName , u.name,u.pic FROM FollowEntity f INNER JOIN userEntity u ON u.userId = f.followEntityId.follower_id WHERE f.followEntityId.following_id= :userId")
    List<Object[]> findfollowers(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "SELECT f1.follower_id, u.user_name, u.name,u.pic, " +
        "CASE WHEN f2.following_id IS NOT NULL THEN 'Follower' ELSE 'NONE' END AS tag " +
        "FROM (followers AS f1 " +
        "LEFT JOIN followers AS f2 ON f1.follower_id = f2.following_id AND f2.follower_id = :userId) " +
        "INNER JOIN users u ON u.user_id = f1.follower_id " +
        "WHERE f1.following_id = :userId")
List<Object[]> getFollowersAndTags(@Param("userId") Long userId);

    
    @Query(value = "SELECT f1.following_id, users.user_name, users.name,users.pic, " +
    "CASE WHEN f2.follower_id IS NOT NULL THEN 'Follower' ELSE 'NONE' END AS tag " +
    "FROM (followers AS f1 " +
    "LEFT JOIN followers AS f2 ON f1.following_id = f2.following_id AND f2.follower_id = :followerId) " +
    "INNER JOIN users ON users.user_id = f1.following_id " +
    "WHERE f1.follower_id = :followingId", nativeQuery = true)
List<Object[]> getFollowingWithTagAndUserDetails(@Param("followerId")Long followerId,@Param("followingId") Long followingId);

@Query(value = "SELECT f1.follower_id,  users.user_name, users.name,users.pic, " +
                   "CASE WHEN f2.following_id IS NOT NULL THEN 'Follower' ELSE 'NONE' END AS tag " +
                   "FROM (followers AS f1 " +
                   "LEFT JOIN followers AS f2 ON f1.follower_id = f2.following_id AND f2.follower_id = :followingId) " +
                   "INNER JOIN users ON users.user_id = f1.follower_id " +
                   "WHERE f1.following_id = :followerId", nativeQuery = true)
    List<Object[]> getFollowersWithTagAndUserDetails(@Param("followerId")Long followerId,@Param("followingId") Long followingId);

}
