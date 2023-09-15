package com.example.Let.sCode.Controllers;


import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Let.sCode.Entitys.FollowEntityId;
import com.example.Let.sCode.Entitys.PostsEntity;
import com.example.Let.sCode.Security.JwtRequestFilter;
import com.example.Let.sCode.Services.PostsService;
import com.example.Let.sCode.Services.ProblemService;
import com.example.Let.sCode.Services.TopicServices;
import com.example.Let.sCode.Services.userService;
import com.example.Let.sCode.json.profileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/msr/")
public class userController {
    
    private final PostsService postsService;
    private final ProblemService problemService;
    private final userService userService;
    private final TopicServices topicServices;

    @PostMapping("/posts")
    public ResponseEntity<?> savePosts(@RequestBody PostsEntity post)
    {
        return ResponseEntity.ok(postsService.save(post));
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@RequestParam("offset") int offSet)
    {
      return ResponseEntity.ok(postsService.getallPosts(offSet));
    }

    @GetMapping("/userposts")
    public ResponseEntity<?> getuserPosts(@RequestParam("offset") Long offSet,@RequestParam("user_id") Long userId)
    {
        return ResponseEntity.ok(postsService.getRecentPostsofUser(userId,offSet));
    }

    @PostMapping("/follow")
    public ResponseEntity<?> saveFollower(@RequestBody FollowEntityId follower)
    {
        return ResponseEntity.ok(userService.saveFollower(follower));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> deleteFollower(@RequestBody FollowEntityId follower)
    {
      userService.deleteFollower(follower);
      return ResponseEntity.ok("");
    }
    @GetMapping("/home")
      public ResponseEntity<?> getTopics(){
        userService.checkUserHasUserIds();
        Map<String,Object> map=new HashMap<>();
        map.put("users", userService.getallUsers());
        map.put("calendar",problemService.getLatestmonthSubmissions());
        map.put("count",userService.findusercount());
        map.put("topics",topicServices.getAllTopics());
        map.put("pic",userService.getUserPic());
        map.put("totalPosts",postsService.getTotalPosts());
        CheckalltimeSubmissions();
        return ResponseEntity.ok(map);
      }

      @GetMapping("/profile/{user_name}")
      public ResponseEntity<?> getProfile(@PathVariable("user_name") String userName) throws ParseException {
         userService.checkUserHasUserIds();
        Long userId=userService.getuserIdFromUserName(userName);
        profileResponse response=userService.getProfile(userId);long offset=0;
        response=problemService.getLatestyearSubmissions(response,userId);
        response.setUserPosts(postsService.getRecentPostsofUser(userId,offset));
        response.setPostsCount(postsService.getPostsCount(userId));
        CheckalltimeSubmissions();
        return ResponseEntity.ok(response);
      }

      @GetMapping("/contests/cf")
      public ResponseEntity<?> getcfContestsCf( @RequestParam("contest_id") Long contestId,
        @RequestParam("type") int type) {
        return ResponseEntity.ok(problemService.getcfContestsdata(type, JwtRequestFilter.userId.longValue(), contestId));
      }

      @GetMapping("/contests/at")
      public ResponseEntity<?> getcfContestsAt( @RequestParam("contest_id") String contestId,
        @RequestParam("type") int type) {
            String contestType="";
            if(type==1)contestType="abc%";
            else if(type==2)contestType="arc%";
        return ResponseEntity.ok(problemService.getatContestsdata(contestType, JwtRequestFilter.userId.longValue(), contestId));
      }

        @GetMapping("/submissions/{type}")
      public ResponseEntity<?> getUserSubmissions(@PathVariable("type") String type,@RequestParam("date") String date,@RequestParam("offset") Long offset) {
        Long userId=JwtRequestFilter.userId.longValue();
        String typePattern=type;String datePattern=date.replaceAll("\\$","_");
        if(type.equals("all"))typePattern="__";
        return ResponseEntity.ok(problemService.getUserSubmissions(userId,typePattern,datePattern,offset));
      }

        @GetMapping("/problems/{type}")
      public ResponseEntity<?> getcfProblems(@PathVariable("type") String type,@RequestParam("status") int status,@RequestParam("offset") Long offset,@RequestParam("topics") String topics,
                                              @RequestParam("or") Boolean doOr,@RequestParam("ratingL")  int ratingL,@RequestParam("ratingR") int ratingR,@RequestParam("difficulty") int difficulty,@RequestParam("title") String title) {
        Long userId=JwtRequestFilter.userId.longValue();
        String[] alltopics=topics.split(",");
        title=title.toLowerCase()+'%';
        if(type.equals("cf") && topics=="")return ResponseEntity.ok(problemService.getCfProblems(userId,status,offset,ratingL,ratingR,title));
        else if(type.equals("cf") && doOr==true)return ResponseEntity.ok(problemService.getCfProblemsWithOrtopics(userId,status,offset,topics,ratingL,ratingR,title));
        else if(type.equals("cf") && doOr==false)return ResponseEntity.ok(problemService.getCfProblemsWithAndtopics(userId,status,offset,topics,alltopics.length,ratingL,ratingR,title));
        if(type.equals("lc") && topics=="")return ResponseEntity.ok(problemService.getLcProblems(userId,status,offset,ratingL,ratingR,difficulty,title));
        else if(type.equals("lc") && doOr==true)return ResponseEntity.ok(problemService.getLcProblemsWithOrtopics(userId,status,offset,topics,ratingL,ratingR,difficulty,title));
        else if(type.equals("lc") && doOr==false)return ResponseEntity.ok(problemService.getLcProblemsWithAndtopics(userId,status,offset,topics,alltopics.length,ratingL,ratingR,difficulty,title));
        return ResponseEntity.ok("Type InValid");
      }

      private void CheckalltimeSubmissions(){
        CompletableFuture.runAsync(()->{
            try {
                userService.saveAlltimeSubmissions();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
      }
}
