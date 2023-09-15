package com.example.Let.sCode.Controllers;


import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Let.sCode.Entitys.CommentsEntity;
import com.example.Let.sCode.Entitys.LikesEntity;
import com.example.Let.sCode.Entitys.PostsEntity;
import com.example.Let.sCode.Entitys.TopicEntity;
import com.example.Let.sCode.Entitys.UserInfoEntity;
import com.example.Let.sCode.Entitys.userEntity;
import com.example.Let.sCode.Entitys.userStatisticsEntity;
import com.example.Let.sCode.Exceptions.InternalErrorCodes;
import com.example.Let.sCode.Exceptions.UserIdNotFoundExeption;
import com.example.Let.sCode.Services.AtCoderService;
import com.example.Let.sCode.Services.CodeForcesService;
import com.example.Let.sCode.Services.LeetCodeService;
import com.example.Let.sCode.Services.PostsService;
import com.example.Let.sCode.Services.TopicServices;
import com.example.Let.sCode.Services.userService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/msr/")
@CrossOrigin(origins = "*",allowedHeaders = "*")
@Slf4j
public class homeController {
    
    private final LeetCodeService leetCodeService; 
    private final CodeForcesService codeForcesService;
    private final AtCoderService atCoderService;
    private final userService userService ;
    private final TopicServices topicServices;
    private final PostsService postsService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> msrLogin(@RequestBody userEntity user) throws Throwable {

         CompletableFuture<List<TopicEntity>> topics=CompletableFuture.supplyAsync(()->{
            return topicServices.getAllTopics();
        });

        CompletableFuture<JsonNode> check1 = CompletableFuture.supplyAsync(() -> {
            try {
                 return leetCodeService.userIdCheck(user);
            } catch (JsonProcessingException | UsernameNotFoundException e) {
                 throw new UserIdNotFoundExeption(e.getMessage());
            }
        }).exceptionally(ex -> null);

        CompletableFuture<JsonNode> check2 = CompletableFuture.supplyAsync(() -> {
            try {
                return codeForcesService.userIdCheck(user);
            } catch (JsonProcessingException | UsernameNotFoundException e) {
                throw new UserIdNotFoundExeption(e.getMessage());
            }
        }).exceptionally(ex -> null);

        CompletableFuture<JsonNode> check3 = CompletableFuture.supplyAsync(() -> {
            try {
                JsonNode response = atCoderService.userIdCheck(user);
                return response;
            } catch (JsonProcessingException | UsernameNotFoundException e) {
                throw new UserIdNotFoundExeption(e.getMessage());
            }
        }).exceptionally(ex -> null);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(check1, check2, check3);

       try{
        allOf.join(); // Wait cheddam poyidemundi
       }catch(Exception e){
            throw e;
       }

       System.out.println(check1.join());
       System.out.println(check2.join());
       System.out.println(check3.join());

        if (check1.join()==null || check2.join()==null || check3.join()==null) {
            String failedChecks = "";
            if (check1.join()==null ) {
                failedChecks+=(InternalErrorCodes.LcUserIdNotFound.getCode());
            }
            if (check2.join()==null ) {
                failedChecks+=(InternalErrorCodes.cfUserIdNotFound.getCode());
            }
            if (check3.join()==null ) {
                failedChecks+=(InternalErrorCodes.AtUserIdNotFound.getCode());
            }
            log.warn(failedChecks);
            throw new UserIdNotFoundExeption(failedChecks.toString());
        }

        // CompletableFuture.runAsync(()->{
        //     try {
        //         userService.saveIds(user,check2.join());
        //         userService.saveUsersInfos(check1,check2,check3);
        //         userService.saveUsersData(check1,check2,check3);
        //         userService.saveUsersSubmissions(user);
        //         leetCodeService.getLcCalenderdata(user.getLcUserId());
        //     } catch (Throwable e) {
        //         e.printStackTrace();
        //     }
        // });
        
        userService.saveIds(user,check2.join());
        userService.saveUsersInfos(check1,check2,check3);
        userService.saveUsersData(check1,check2,check3);
        userService.saveUsersSubmissions(user);
        leetCodeService.getLcCalenderdata(user.getLcUserId());

        try{
        topics.join(); 
       }catch(Exception e){
            throw e;
       }

        return ResponseEntity.ok(topics.join());
    }

      @PostMapping("/topics")
      public ResponseEntity<?> getTopics(@RequestBody List<TopicEntity> topics){
        return ResponseEntity.ok("");
      }
      
      @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test() throws Exception {
     //   cfproblems.generateSqlScript();
        return ResponseEntity.ok("Its Working Partner....");
    }

    @PutMapping("/userinfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoEntity userinfo){
        return ResponseEntity.ok(userService.saveUsersInfo(userinfo));
    }

      
  @PutMapping("/users")
    public ResponseEntity<?> updateuser(@RequestBody userEntity user){
        return ResponseEntity.ok(userService.updateuser(user));
    }

    @PostMapping("/profilepic")
    public ResponseEntity<?> updateprofile(@RequestBody userEntity user){
        return ResponseEntity.ok(userService.updateprofile(user));
    }

    @PostMapping("/usersstats")
    public ResponseEntity<?> saveUsersstats(@RequestBody userStatisticsEntity userstats){
        return ResponseEntity.ok(userService.saveUserStats(userstats));
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody CommentsEntity comment){
        return ResponseEntity.ok(postsService.saveComment(comment));
    }
      
    @PutMapping("/comments")
    public ResponseEntity<?> updtecomment(@RequestBody CommentsEntity comment){
        return ResponseEntity.ok(postsService.updateComment(comment));
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@RequestParam("post_id") Long postId,@RequestParam("offset")Long offset){
        return ResponseEntity.ok(postsService.getPostComments(postId,offset));
    }

    @PutMapping("/posts")
    public ResponseEntity<?> updtepost(@RequestBody PostsEntity post){
        return ResponseEntity.ok(postsService.updatePost(post));
    }

    @PostMapping("/likes")
    public ResponseEntity<?> addLike(@RequestBody LikesEntity like){
        return ResponseEntity.ok(postsService.saveLike(like));
    }

    @GetMapping("/likes")
    public ResponseEntity<?> getLikes(@RequestParam("post_id") Long postId){
        return ResponseEntity.ok(postsService.getpostLikes(postId));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<?> deleteLike(@RequestBody LikesEntity like){
        postsService.deleteLike(like);
        return ResponseEntity.ok("");
    }
    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestBody PostsEntity post){
        postsService.deletePost(post);
        return ResponseEntity.ok("");
    }
    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentsEntity comment){
        postsService.deleteComment(comment);
        return ResponseEntity.ok("");
    }

}
