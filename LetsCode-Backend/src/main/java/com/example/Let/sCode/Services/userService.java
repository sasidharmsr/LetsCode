package com.example.Let.sCode.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.Let.sCode.Dao.followDao;
import com.example.Let.sCode.Dao.userDao;
import com.example.Let.sCode.Dao.userInfoDao;
import com.example.Let.sCode.Dao.userStatisticsDao;
import com.example.Let.sCode.Dao.userSubmissionsDao;
import com.example.Let.sCode.Dao.usersDataDao;
import com.example.Let.sCode.Dao.usertopicDao;
import com.example.Let.sCode.Entitys.FollowEntity;
import com.example.Let.sCode.Entitys.FollowEntityId;
import com.example.Let.sCode.Entitys.TopicEntity;
import com.example.Let.sCode.Entitys.UserInfoEntity;
import com.example.Let.sCode.Entitys.UserTopicEntity;
import com.example.Let.sCode.Entitys.UsersDataEntity;
import com.example.Let.sCode.Entitys.UsersSubmissionsEntity;
import com.example.Let.sCode.Entitys.userEntity;
import com.example.Let.sCode.Entitys.userStatisticsEntity;
import com.example.Let.sCode.Exceptions.UserIdNotFoundExeption;
import com.example.Let.sCode.Security.JwtRequestFilter;
import com.example.Let.sCode.Security.JwtTokenService;
import com.example.Let.sCode.json.JwtResponse;
import com.example.Let.sCode.json.Telegrammessages;
import com.example.Let.sCode.json.followers;
import com.example.Let.sCode.json.loginResponse;
import com.example.Let.sCode.json.profileResponse;
import com.example.Let.sCode.json.registerRequest;
import com.example.Let.sCode.json.userResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class userService {

    
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenUtil;
    private final userDetailsService userDetailsService;
    private final userDao userdao;
    private final userInfoDao userInfodao;
    private final usersDataDao usersDataDao;
    private final followDao followDao;
    private final userSubmissionsDao userSubmissionsDao;
    private final PasswordEncoder bcryptEncoder;
    private final LeetCodeService leetCodeService; 
    private final CodeForcesService codeForcesService;
    private final AtCoderService atCoderService;
    private final RedisService redisService;
    private final usertopicDao usertopicDao;
    private final userStatisticsDao userStatisticsDao;
    private final EmailService emailService;

    @Value("${spring.app.url}")
    String url;

    // For Registering a user
    public JwtResponse save(registerRequest request) {
		userEntity newUser = new userEntity();
        String message=Telegrammessages.WelcomePage.getMessage().replace("[XXX]", request.getName());
        userEntity OtherUser=userdao.findbyuserName(request.getUser_name());
        if(OtherUser!=null){
            throw new UsernameNotFoundException("UserName is taken");
        }
        if(emailService.sendEmail(request.getEmail(), "Getting Started with Let's Code Platform", message)==false){
            throw new UsernameNotFoundException("Email Not Exist");
        }
		newUser.setUserName(request.getUser_name());
		newUser.setEmail(request.getEmail());newUser.setRole("USER");
		newUser.setPassword(bcryptEncoder.encode(request.getPassword()));
        newUser.setName(request.getName());
        userdao.save(newUser).getUserId();
        var user=userDetailsService.loadUserByUsername(request.getUser_name());
        JwtResponse jwtResponse =new JwtResponse(jwtTokenUtil.generateToken(user),jwtTokenUtil.generateRefreshToken(user));
        return jwtResponse;
    }

    // For finding user from db by user_name
    public userEntity findbyuserName(String userName) {
        return userdao.findbyuserName(userName);
    }

    public List<Long> findLatestSubmission()
    {
           return userSubmissionsDao.getLatestTimeStamp(JwtRequestFilter.userId) ;
    }

    // For Login Service
    public Object   auth(registerRequest request) throws Exception {
        authenticate(request.getUser_name(), request.getPassword());
        var user=userDetailsService.loadUserByUsername(request.getUser_name());
        JwtResponse jwtResponse =new JwtResponse(jwtTokenUtil.generateToken(user),jwtTokenUtil.generateRefreshToken(user));
        return jwtResponse;
    }

    public Object passwordChange(registerRequest request) throws Exception {
        userEntity newUser = userdao.findbyuserName(request.getUser_name());
		newUser.setPassword(bcryptEncoder.encode(request.getPassword()));
        userdao.save(newUser).getUserId();
        var user=userDetailsService.loadUserByUsername(request.getUser_name());
        JwtResponse jwtResponse =new JwtResponse(jwtTokenUtil.generateToken(user),jwtTokenUtil.generateRefreshToken(user));
        return jwtResponse;
    }

    // For Refresh Token 
    public Object refreshToken(String AUTHORIZATION) {
        Optional<String> jwt=getJwtFromRequest(AUTHORIZATION);
        if(jwt.isPresent()){
            String userName = jwtTokenUtil.extractUsername(jwt.get());
            if(userName!=null){
                UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
                if (jwtTokenUtil.isTokenValid(jwt.get(), userDetails))
                {
                    var accessToken = jwtTokenUtil.generateToken(userDetails);
                    JwtResponse jwtResponse =new JwtResponse(accessToken,jwt.get());
                    return ResponseEntity.ok(jwtResponse);
                }
            }
        }else{
           return ResponseEntity.ok("Token is Required");
        }
        return ResponseEntity.ok("Some thing went wrong");
    }

    private static Optional<String> getJwtFromRequest(String bearerToken) {
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
        return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public static String jsonToString(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        if (prettyJson.startsWith("\"") && prettyJson.endsWith("\"")) {
            return prettyJson.substring(1, prettyJson.length() - 1);
        }
        return prettyJson;
    }

    public  List<Map<String,Object>> getallUsers() {
        List<Map<String,Object>> list=new ArrayList<>();
        List<userEntity> users=userdao.findallUsers();
        for(userEntity user:users){
            Map<String,Object> map=new HashMap<>();
            map.put("userId",user.getUserId()); map.put("userName",user.getUserName());
            map.put("pic",user.getPic());map.put("name",user.getName());
            list.add(map);
        }
        return list;
    }

    public loginResponse saveIds(userEntity user,JsonNode data) throws Throwable {
        JsonNode profile=data.get("result").get(0);
        user.setPic(userService.jsonToString(profile.get("titlePhoto")));
        System.out.println(userService.jsonToString(profile.get("titlePhoto")));
        int result= userdao.updateWithUserName(user,JwtRequestFilter.userName);
        if(result==0)throw new Throwable("Updating user with given userid's was failed by db");
        loginResponse response=new loginResponse();response.setAtcoder_user_id(user.getAtUserId());
        response.setUser_name(JwtRequestFilter.userName);response.setCodeforces_user_id(user.getCfUserId());
        response.setLeetcode_user_id(user.getLcUserId());
        return response;
    }

    public void saveUsersInfos(CompletableFuture<JsonNode> check1, CompletableFuture<JsonNode> check2,CompletableFuture<JsonNode> check3) throws JsonProcessingException {
            
        UserInfoEntity userInfoEntity= userInfodao.findByUserId(JwtRequestFilter.userId);
        if(userInfoEntity==null){
            userInfoEntity=new UserInfoEntity();
            userInfoEntity.setUserId(JwtRequestFilter.userId);
        }
        userInfoEntity=leetCodeService.setlcProfile(check1.join(), userInfoEntity);
        userInfoEntity=codeForcesService.setcfProfile(check2.join(),userInfoEntity);
        userInfodao.save(userInfoEntity);
    }

    public void saveUsersData(CompletableFuture<JsonNode> check1, CompletableFuture<JsonNode> check2,CompletableFuture<JsonNode> check3) throws NumberFormatException, JsonProcessingException {
        UsersDataEntity usersdata= usersDataDao.findByUserId(JwtRequestFilter.userId);
        if(usersdata==null){
            usersdata=new UsersDataEntity();
            usersdata.setUserId(JwtRequestFilter.userId);
        }
        usersdata=leetCodeService.setUsersData(check1.join(), usersdata);
        usersdata=codeForcesService.setUsersData(check2.join(), usersdata);
        usersdata=atCoderService.setUsersData(check3.join(), usersdata);
        usersDataDao.save(usersdata);
    }

    public String getUserPic(){
        userEntity user= userdao.findbyId(JwtRequestFilter.userId);
        return user.getPic();
    }

     public UserInfoEntity saveUsersInfo(UserInfoEntity reqinfo)  {
        UserInfoEntity userInfoEntity= userInfodao.findByUserId(JwtRequestFilter.userId);
        userInfoEntity.setCity(reqinfo.getCity());userInfoEntity.setCountry(reqinfo.getCountry());
        userInfoEntity.setCompany(reqinfo.getCompany());userInfoEntity.setGithubUrl(reqinfo.getGithubUrl());
        userInfoEntity.setLinkedinUrl(reqinfo.getLinkedinUrl());userInfoEntity.setSchool(reqinfo.getSchool());
        userInfodao.save(userInfoEntity);
        return userInfoEntity;
    }

    public userResponse updateuser(userEntity reqinfo)  {
        userEntity user= userdao.findbyId(JwtRequestFilter.userId);
        user.setName(reqinfo.getName());
        user.setEmail(reqinfo.getEmail());user.setPhoneNumber(reqinfo.getPhoneNumber());
        userdao.save(user);
        userResponse userResp=new userResponse();userResp.setName(user.getName());
        userResp.setEmail(user.getEmail());userResp.setPhoneNumber(user.getPhoneNumber());
        return userResp;
    }

    public userStatisticsEntity saveUserStats(userStatisticsEntity reqinfo)  {
        userStatisticsDao.save(reqinfo);
        return reqinfo;
    }


    public long getuserLastTimestamp(){
       String userName=JwtRequestFilter.userName;
       long value=redisService.getLatestTimeStamp(userName+"$MSR$"+JwtRequestFilter.userId);
        if(value==-1){
            List<Long> list=findLatestSubmission();if(list.isEmpty())return -1;
            return Collections.max(list);
        }
        return value;
    }
    public void saveUsersSubmissions(userEntity userEntity) throws NumberFormatException, JsonProcessingException {
        long lastCheckedTime=getuserLastTimestamp();
        System.out.println("step1");
        long currentUnixTimestampInSeconds = System.currentTimeMillis() / 1000;
        if(lastCheckedTime!=-1){
            int assurancesTime=10;// They are in minutes
            System.out.println(currentUnixTimestampInSeconds-lastCheckedTime+" Seconds");
            if((currentUnixTimestampInSeconds-lastCheckedTime)<=(assurancesTime*60)){
            redisService.setLatestTimeStamp(JwtRequestFilter.userName+"$MSR$"+JwtRequestFilter.userId,currentUnixTimestampInSeconds );
            return;}
        }else{
            lastCheckedTime=946684800;//2000 year
        }
        System.out.println("step2");
        List<UsersSubmissionsEntity> usersSubmissionsEntities=new ArrayList<>();
        usersSubmissionsEntities=leetCodeService.saveUserSubmissions(userEntity.getLcUserId(),usersSubmissionsEntities,lastCheckedTime);
        Pair<List<UsersSubmissionsEntity>,Integer> cfResult=codeForcesService.saveUserSubmissions(userEntity.getCfUserId(),usersSubmissionsEntities,lastCheckedTime);
        usersSubmissionsEntities=cfResult.getFirst();
        usersSubmissionsEntities=atCoderService.saveUserSubmissions(userEntity.getAtUserId(),usersSubmissionsEntities,lastCheckedTime);
        redisService.setLatestTimeStamp(JwtRequestFilter.userName+"$MSR$"+JwtRequestFilter.userId,currentUnixTimestampInSeconds );
        if(usersSubmissionsEntities.isEmpty())return;
        if(cfResult.getSecond()!=0)updateCfCount(cfResult.getSecond());
        userSubmissionsDao.saveAll(usersSubmissionsEntities);
        log.warn("Hey.........I Saves All Submissions");
    }

    public void updateCfCount(Integer count){
        usersDataDao.updateCfCount(count,JwtRequestFilter.userId);
    }

    public FollowEntity saveFollower(FollowEntityId followEntityId){
        FollowEntity followEntity=new FollowEntity();
        followEntity.setFollowEntityId(followEntityId);
        return followDao.save(followEntity);
    }

    public List<followers> findfollowing(Long user_id){
        List<followers> followers=new ArrayList<>();
        Long curUser=JwtRequestFilter.userId.longValue();
        List<Object[]> result;
        if(curUser==user_id)result=followDao.findfollowing(user_id);
        else result=followDao.getFollowingWithTagAndUserDetails(curUser,user_id);
        for(Object[] i:result){
            followers follower=new followers();
            follower.setUser_id((int)i[0]);
            follower.setUser_name((String)i[1]);
            follower.setName((String)i[2]);
            follower.setPic((String)i[3]);
            if(curUser!=user_id)follower.setTag((String)i[4]);
            followers.add(follower);
        }
        return followers;
    }

     public List<followers> findfollowers(boolean[] check,Long user_id){
        List<followers> followers=new ArrayList<>();
        Long curUser=JwtRequestFilter.userId.longValue();
        List<Object[]> result;
        if(curUser==user_id)result=followDao.getFollowersAndTags(user_id);
        else result=followDao.getFollowersWithTagAndUserDetails(user_id,curUser);
        for(Object[] i:result){
            followers follower=new followers();
            follower.setUser_id((int)i[0]);
            follower.setUser_name((String)i[1]);
            follower.setName((String)i[2]);
            follower.setPic((String)i[3]);
            follower.setTag((String)i[4]);
            if(follower.getUser_id()==curUser)check[0]=true;
            followers.add(follower);
        }
        return followers;
    }

    public void saveUserTopics(List<TopicEntity> topics){
        List<UserTopicEntity> list=new ArrayList<>();
        for(TopicEntity topic:topics){
            UserTopicEntity userTopicEntity =new UserTopicEntity();
            userTopicEntity.setTopicId(topic.getId());
            userTopicEntity.setUserId(Long.valueOf(JwtRequestFilter.userId));
            list.add(userTopicEntity);
        }
        usertopicDao.saveAll(list);
    }

    public void saveAlltimeSubmissions() throws NumberFormatException, JsonProcessingException {
        userEntity user=userdao.findbyId(JwtRequestFilter.userId);
        saveUsersSubmissions(user);
    }


    public profileResponse getProfile(Long userId) {
        List<Object[]> dbresponse= usersDataDao.getUserDetails(userId);
        List<Object[]> totalQuestions=codeForcesService.getCombinedtotalQuestions();
        Optional<userStatisticsEntity> userStats=userStatisticsDao.findById(userId.intValue());
         List<Object[]> solvedCf=userSubmissionsDao.getSubmissionCountsForUser(userId);
        profileResponse response=new profileResponse(dbresponse.get(0));
        response.addTotalQuestion(totalQuestions);
        response.cfsolvedQuestion(solvedCf);
        boolean[] check={false};
        response.setFollowers(findfollowers(check,userId));
        response.setFollowerCheck(check[0]);
        response.setFollowing(findfollowing(userId));
        if(userStats.isPresent())response.setUserstats(userStats.get());
        return response;
    }

    public Long getuserIdFromUserName(String userName) {
        userEntity user=userdao.findbyuserName(userName);
        return user.getUserId();
    }

    public Object findusercount() {
        UsersDataEntity usersdata= usersDataDao.findByUserId(JwtRequestFilter.userId);
        Map<String,Integer> map=new HashMap<>(); map.put("At", usersdata.getAtCount());
        map.put("Cf", usersdata.getCfCount()); map.put("Lc", usersdata.getLcCount());
        return map;
    }

    public void deleteFollower(FollowEntityId follower) {
        FollowEntity followEntity=new FollowEntity();
        followEntity.setFollowEntityId(follower);
        followDao.delete(followEntity);
    }

    public String updateprofile(userEntity reqinfo) {
        userEntity user= userdao.findbyId(JwtRequestFilter.userId);
        user.setPic(reqinfo.getPic());
        userdao.save(user);
        return "Success";
    }

    public static String generateRandomToken() {
        UUID uuid = UUID.randomUUID();
        String randomToken = uuid.toString().replaceAll("-", "");
        return randomToken;
    }

    public Map<String,String> forgotPassword(registerRequest request) {
        userEntity user=null;
        if(request.getEmail()!=null){
             user=userdao.findbyEmail(request.getEmail());
             if(user==null)throw new UserIdNotFoundExeption("Email id not found with email : " + request.getEmail());
        }else if(request.getUser_name()!=null){
            user=userdao.findbyuserName(request.getUser_name());
             if(user==null)throw new UserIdNotFoundExeption("User name not found with username : " + request.getUser_name());
        }else if(request.getPhone_number()!=null){
             user=userdao.findbyphoneNumber(request.getPhone_number());
             if(user==null)throw new UserIdNotFoundExeption("Phone Number not found: " + request.getPhone_number());
        }
        String token=generateRandomToken();
        redisService.setToken(token,user.getUserName());
        String Url=url+"/recovery?token="+token;
        String message=String.format(Telegrammessages.ForgotPassword.getMessage(), user.getName(),Url);
        emailService.sendEmail(user.getEmail(), "Password Reset", message);
        Map<String,String> result=new HashMap<>();
        result.put("email", user.getEmail());
        return result;
    }

    public Object validateToken(String token) throws Throwable {
        String exist=redisService.getToken(token);
        if(exist==null){
            throw new Throwable("Token not Exist");
        }
        Map<String,String> result=new HashMap<>();
        result.put("userName", exist);
        return result;
    }

    public void checkUserHasUserIds() {
        userEntity user=userdao.findbyId(JwtRequestFilter.userId);
        if(user.getAtUserId()==null)
        {
            throw new UserIdNotFoundExeption("UsersIds Not Set");
        }
    }

   
}
