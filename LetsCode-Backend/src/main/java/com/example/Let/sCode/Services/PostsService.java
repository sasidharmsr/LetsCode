package com.example.Let.sCode.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Let.sCode.Dao.commentsDao;
import com.example.Let.sCode.Dao.likesDao;
import com.example.Let.sCode.Dao.postsDao;
import com.example.Let.sCode.Entitys.CommentsEntity;
import com.example.Let.sCode.Entitys.LikesEntity;
import com.example.Let.sCode.Entitys.PostsEntity;
import com.example.Let.sCode.Security.JwtRequestFilter;
import com.example.Let.sCode.json.followers;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostsService {
    
    private final postsDao postsDao;
    private final commentsDao commentsDao;
    private final likesDao likesDao;

    public List<PostsEntity> getPosts(){
        return postsDao.findAll();
    }

    public List<PostsEntity> getRecentPosts(int limit){
        return postsDao.findAll();
    }

    public Long getPostsCount(Long userId){
       if(userId==JwtRequestFilter.userId.longValue())return postsDao.getuserpostsCounts(userId);
       return postsDao.getuserPostsCount(JwtRequestFilter.userId.longValue(),userId);
    }

    public List<Map<String,Object>> getRecentPostsofUser(Long userId,Long offset){
        List<Map<String,Object>> list=new ArrayList<>();
        List<Object[]> posts=new ArrayList<>();
        if(JwtRequestFilter.userId.longValue()==userId)posts= postsDao.getRecntPostsofUserWithLikes(userId,offset);
        else posts=postsDao.findPostsByUserIdWithOffset(JwtRequestFilter.userId.longValue(),userId,offset);
          for(Object[] post:posts){
            Map<String,Object> map=new HashMap<>();
           map.put("post_id",post[0]);map.put("post_type",post[1]);
           map.put("likes_count",post[2]);map.put("comments_count",post[3]);map.put("post_pic",post[4]);
           map.put("post_description",post[5]);
           map.put("is_liked", ((String)post[6]).equals("like"));
           list.add(map);
        }
        return list;
    }

    public List<Map<String,Object>> getallPosts(int offSet){
        List<Map<String,Object>> list=new ArrayList<>();
        List<Object[]> posts=postsDao.getAllPosts(JwtRequestFilter.userId.longValue(),offSet);
          for(Object[] post:posts){
            Map<String,Object> map=new HashMap<>();
           map.put("post_id",post[0]);map.put("user_id",post[1]);
           map.put("user_name",post[2]);map.put("pic",post[3]);map.put("likes_count",post[4]);
           map.put("comments_count",post[5]);map.put("post_pic",post[6]);map.put("post_description",post[7]);
           map.put("is_liked", ((String)post[8]).equals("like"));
           list.add(map);
        }
        return list;
    }
   

    public PostsEntity save(PostsEntity post){
        long currentUnixTimestampInSeconds = System.currentTimeMillis() / 1000;
        post.setUserId(JwtRequestFilter.userId);
        post.setCreatedAt(currentUnixTimestampInSeconds);
        post.setUpdatedAt(currentUnixTimestampInSeconds);
        return postsDao.save(post);
    }
    
    @Transactional
    public CommentsEntity saveComment(CommentsEntity comment){
        comment.setUserId(JwtRequestFilter.userId.longValue());
        long currentUnixTimestampInSeconds = System.currentTimeMillis() / 1000;
        comment.setTimeStamp(currentUnixTimestampInSeconds);
        commentsDao.save(comment);
        postsDao.incrementCommentsCount(comment.getPostId());
        return comment;
    }
    public CommentsEntity updateComment(CommentsEntity comment){
        Optional<CommentsEntity> dbComment=commentsDao.findById(comment.getCommentId());
        if(dbComment.isPresent()){
            dbComment.get().setCommentText(comment.getCommentText());
            commentsDao.save(dbComment.get());
            return dbComment.get();
        }
        return null;
    }

    @Transactional
    public LikesEntity saveLike(LikesEntity like){
        like.setUserId(JwtRequestFilter.userId.longValue());
        long currentUnixTimestampInSeconds = System.currentTimeMillis() / 1000;
        like.setTimeStamp(currentUnixTimestampInSeconds);
        likesDao.save(like);
        postsDao.incrementLikesCount(like.getPostId());
        return like;
    }
    @Transactional
    public void deleteLike(LikesEntity like){
        likesDao.deleteByUserIdAndPostId(JwtRequestFilter.userId.longValue(),like.getPostId());
        postsDao.decrementLikesCount(like.getPostId());
    }
    @Transactional
    public void deletePost(PostsEntity post){
        postsDao.deleteById(post.getPost_id());
    }
    @Transactional
    public void deleteComment(CommentsEntity comment){
        commentsDao.deleteById(comment.getCommentId());
        postsDao.decrementCommentsCount(comment.getPostId());
    }


    public Long getTotalPosts(){
        Long totalCount=postsDao.countposts(JwtRequestFilter.userId.longValue());
        return totalCount;
    }


    @Transactional
    public PostsEntity updatePost(PostsEntity post) {
        Optional<PostsEntity> dbPost=postsDao.findById(post.getPost_id());
        long currentUnixTimestampInSeconds = System.currentTimeMillis() / 1000;
        if(dbPost.isPresent()){
            dbPost.get().setPostDescription(post.getPostDescription());
            dbPost.get().setPostType(post.getPostType());
            dbPost.get().setPic(post.getPic());dbPost.get().setUpdatedAt(currentUnixTimestampInSeconds);
            postsDao.save(dbPost.get());
            return dbPost.get();
        }
        return null;
    }

    public List<followers> getpostLikes(Long postId) {
        List<Object[]> result=postsDao.findLikesUsersForPostId(postId,JwtRequestFilter.userId.longValue());
        List<followers> followers=new ArrayList<>();
        for(Object[] i:result){
            followers follower=new followers();
            follower.setUser_id((int)i[0]);
            follower.setUser_name((String)i[1]);
            follower.setName((String)i[2]);
            follower.setPic((String)i[3]);
            follower.setTag((String)i[4]);
            followers.add(follower);
        }
        return followers;
    }

    public List<Map<String, Object>> getPostComments(Long postId, Long offset) {
        List<Object[]> result = postsDao.findCommentsUsersForPostId(postId, offset);
        List<Map<String, Object>> res = new ArrayList<>();
        for (Object[] obj : result) {
            Map<String, Object> map = new HashMap<>();
            map.put("userName", obj[0]);
            map.put("name", obj[1]);
            map.put("pic", obj[2]);
            map.put("commentText", obj[3]);
            map.put("timeStamp", obj[4]);
            map.put("commentId", obj[5]);
            res.add(map);
        }
        return res;
    }
}
