package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Let.sCode.Entitys.PostsEntity;

public interface postsDao extends JpaRepository<PostsEntity,Long>{
    
    @Query("From PostsEntity order by createdAt desc")
    List<PostsEntity> getRecntPosts();

    @Query("From PostsEntity where userId=:userId order by updatedAt desc limit 10 offset :offset")
    List<PostsEntity> getRecntPostsofUser(@Param("userId") Long userId,@Param("offset") Long offset);


    @Query(value = "SELECT " +
                   "p.post_id, " +
                   "p.post_type, " +
                   "p.likes_count, " +
                   "p.comments_count, " +
                   "p.pic AS post_pic, " +
                   "p.post_description, " +
                   "CASE " +
                       "WHEN ul.like_id IS NULL THEN 'unlike' " +
                       "ELSE 'like' " +
                   "END AS is_liked " +
               "FROM posts p " +
               "LEFT JOIN user_likes ul ON ul.post_id = p.post_id AND ul.user_id = ?1 " +
               "WHERE p.user_id = ?1 " +
               "ORDER BY p.created_at DESC " +
               "LIMIT 10 OFFSET ?2", nativeQuery = true)
    List<Object[]> getRecntPostsofUserWithLikes(Long userId, Long offset);

     @Query(value = "SELECT " +
                   "count(*) " +
               "FROM posts p " +
               "LEFT JOIN user_likes ul ON ul.post_id = p.post_id AND ul.user_id = ?1 " +
               "WHERE p.user_id = ?1 ", nativeQuery = true)
    Long getuserpostsCounts(Long userId);


    @Query(value = "SELECT " +
               "p.post_id, " +
               "p.post_type, " +
               "p.likes_count, " +
               "p.comments_count, " +
               "p.pic AS post_pic, " +
               "p.post_description, " +
               "CASE " +
                   "WHEN ul.like_id IS NULL THEN 'unlike' " +
                   "ELSE 'like' " +
               "END AS is_liked " +
           "FROM posts p " +
           "LEFT JOIN user_likes ul ON ul.post_id = p.post_id AND ul.user_id = ?1 " +
           "LEFT JOIN (SELECT DISTINCT follower_id FROM followers WHERE following_id = ?2) AS f ON f.follower_id = ?1 " +
           "WHERE p.user_id = ?2 AND (p.post_type = 'public' OR f.follower_id IS NOT NULL) " +
           "ORDER BY p.updated_at DESC " +
           "LIMIT 10 OFFSET ?3", nativeQuery = true)
List<Object[]> findPostsByUserIdWithOffset(Long loggedInUserId, Long userId,Long offset);



  @Query(value = "SELECT " +
               "count(*) " +
           "FROM posts p " +
           "LEFT JOIN user_likes ul ON ul.post_id = p.post_id AND ul.user_id = ?1 " +
           "LEFT JOIN (SELECT DISTINCT follower_id FROM followers WHERE following_id = ?2) AS f ON f.follower_id = ?1 " +
           "WHERE p.user_id = ?2 AND (p.post_type = 'public' OR f.follower_id IS NOT NULL) ", nativeQuery = true)
Long getuserPostsCount(Long loggedInUserId, Long userId);


    @Query(value = "SELECT p.post_id, u.user_id, u.user_name, u.pic, p.likes_count, p.comments_count, p.pic AS post_pic, p.post_description,p.post_type " +
                   "FROM posts p INNER JOIN users u ON u.user_id = p.user_id  order by created_at desc",
                   nativeQuery = true)
    List<Object[]> getallPosts();  // past version 

    @Query(
        value = "SELECT p.post_id, u.user_id, u.user_name, u.pic, p.likes_count, p.comments_count, " +
                "p.pic AS post_pic, p.post_description, " +
                "CASE WHEN ul.like_id IS NULL THEN 'unlike' ELSE 'like' END AS is_liked " +
                "FROM posts p " +
                "INNER JOIN users u ON u.user_id = p.user_id " +
                "LEFT JOIN user_likes ul ON ul.post_id = p.post_id AND ul.user_id = ?1 " +
                "LEFT JOIN (SELECT DISTINCT following_id FROM followers WHERE follower_id = ?1) AS f " +
                "ON u.user_id = f.following_id " +
                "WHERE p.post_type = 'public' OR u.user_id = ?1 OR f.following_id IS NOT NULL " +
                "ORDER BY p.created_at DESC LIMIT 10 OFFSET ?2",
        nativeQuery = true
    )
    List<Object[]> getAllPosts(Long userId,int offset);

    @Modifying
    @Query("update PostsEntity p set p.likesCount = p.likesCount + 1 where p.post_id = ?1")
    void incrementLikesCount(Long postId);

    @Modifying
    @Query("update PostsEntity p set p.likesCount = p.likesCount - 1 where p.post_id = ?1")
    void decrementLikesCount(Long postId);

     @Modifying
    @Query("update PostsEntity p set p.commentsCount = p.commentsCount + 1 where p.post_id = ?1")
    void incrementCommentsCount(Long postId);

    @Modifying
    @Query("update PostsEntity p set p.commentsCount = p.commentsCount - 1 where p.post_id = ?1")
    void decrementCommentsCount(Long postId);

    @Query(
        value = "SELECT count(*) as count  " +
                "FROM posts p " +
                "INNER JOIN users u ON u.user_id = p.user_id " +
                "LEFT JOIN (SELECT DISTINCT following_id FROM followers WHERE follower_id = ?1) AS f " +
                "ON u.user_id = f.following_id " +
                "WHERE p.post_type = 'public' OR u.user_id = ?1 OR f.following_id IS NOT NULL " ,
        nativeQuery = true
    )
    Long countposts(Long userId);

    @Query(value = "SELECT users.user_id, user_name, name,pic , " +
                   "CASE WHEN f.follower_id IS NOT NULL THEN 'Follower' ELSE 'NONE' END AS tag " +
                   "FROM user_likes " +
                   "INNER JOIN users ON users.user_id = user_likes.user_id " +
                   "LEFT JOIN followers f ON f.following_id = users.user_id AND f.follower_id = ?2 " +
                   "WHERE post_id = ?1", nativeQuery = true)
    List<Object[]> findLikesUsersForPostId(Long postId,Long userId);


     @Query(value = "SELECT user_name, name,pic ,comment_text, time_stamp, comment_id " +
                   "FROM user_comments " +
                   "INNER JOIN users ON users.user_id = user_comments.user_id " +
                   "WHERE post_id = ?1 order by time_stamp desc limit 3 offset ?2", nativeQuery = true)
    List<Object[]> findCommentsUsersForPostId(Long postId,Long offset);

}
