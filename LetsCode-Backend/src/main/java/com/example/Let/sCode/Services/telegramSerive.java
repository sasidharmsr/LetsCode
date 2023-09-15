package com.example.Let.sCode.Services;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.example.Let.sCode.Dao.atProblemsDao;
import com.example.Let.sCode.Dao.dailyProblemsdao;
import com.example.Let.sCode.Dao.userDao;
import com.example.Let.sCode.Dao.userStatisticsDao;
import com.example.Let.sCode.Entitys.UserDailyProblemsEnity;
import com.example.Let.sCode.Entitys.userEntity;
import com.example.Let.sCode.Entitys.userStatisticsEntity;
import com.example.Let.sCode.json.Telegrammessages;
import com.example.Let.sCode.json.statsResp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class telegramSerive {

    private final atProblemsDao atProblemsdao;
    private final userDao userdao;
    private final twilloService twilloService;
    private final EmailService emailService;
    private final RedisService redisService;
    private final userStatisticsDao userStatisticsDao;
    private final dailyProblemsdao dailyProblemsdao;
    
    public String genrateRanmomQuestion() {
         int randomNumber=gerateRandomNumber(1,3);
        if(randomNumber==1){
            List<Object[]> res=atProblemsdao.findRandomLCProblem();
            return genarateLcProb(res,0);
        }
        if(randomNumber==2){
            List<Object[]> res=atProblemsdao.findRandomCfProblem();
            return genarateCfProb(res);
        }
        List<Object[]> res=atProblemsdao.findRandomAtProblem();
        return genarateAtProb(res);
    }

    public static int gerateRandomNumber(int min,int max){
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public String genarateLcProbByDif(String diffculty){
        List<Object[]> res=atProblemsdao.findRandomLCProblemByDifculty(diffculty);
        return genarateLcProb(res,0);
    }

    public String genarateCfProbByRating(String rating){
        List<Object[]> res=atProblemsdao.findRandomCfProblemByRating(Integer.parseInt(rating));
        return genarateCfProb(res);
    }

    public String genarateAtProbByType(String type){
        List<Object[]> res=atProblemsdao.findRandomAtProblemByIndex(type);
        return genarateAtProb(res);
    }
    
    
    public String genarateLcProb(List<Object[]> res,int type){
        Object[] problem=res.get(0);String path=(String)problem[2];Double acRate=(double)0;
        if(type==1){
            BigDecimal decimalValue = (BigDecimal) problem[3];
            acRate=decimalValue.doubleValue();
        }else if(type==3){
            BigDecimal decimalValue = new BigDecimal((String)problem[3]);
            acRate=decimalValue.doubleValue();
        }
        else {
            acRate=(Double) problem[3];
        }
        String message="Platform: LeetCode\n";DecimalFormat df = new DecimalFormat("#.##");
        message+=String.format("Title: [%s](https://leetcode.com/problems/%s/)", (String)problem[1],path)+"\n";
        message+="Difficulty "+(String)problem[0]+"\n";
        message+="Success Rate "+df.format(acRate)+"\n";
        return message;
    }
    public String genarateCfProb(List<Object[]> res){
        Object[] problem=res.get(0);String path=(Integer)problem[0]+"/"+(String)problem[1];
        String message="Platform: CodeForces\n";
        message+=String.format("Title: [%s](https://codeforces.com/problemset/problem/%s)", (String)problem[2],path)+"\n";
        message+="Rating "+(Integer)problem[3]+"\n";
        return message;
    }
    public String genarateAtProb(List<Object[]> res){
        Object[] problem=res.get(0);String path=(String)problem[1]+"/tasks/"+(String)problem[0];
        String message="Platform: Atcoder\n";
        message+=String.format("Title: [%s](https://atcoder.jp/contests/%s)", (String)problem[2],path)+"\n";
        message+="Problem "+(String)problem[3]+"\n";
        return message;
    }

    public Map<String,String> isValidUsername(String userName) {
        userEntity user=userdao.findbyuserName(userName);
        if(user==null)return null;
        Map<String,String> map=new HashMap<>();map.put("name",user.getName());
        map.put("userName",user.getUserName());map.put("email",user.getEmail());
        map.put("role", user.getRole()); map.put("phoneNumber", user.getPhoneNumber());
        return map;
    }

    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);
        String allowedChars = "0123456789";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            otp.append(randomChar);
        }
        return otp.toString();
    }

    public Boolean sendOtp(String name,String email,String userName,String phoneNumber) {
        String otp=generateOTP(6);
        if(phoneNumber!=""){
            String resp=twilloService.sendMessage("+91"+phoneNumber,Telegrammessages.GenarateOtp.getMessage().replace("123456",otp));
            if(resp==null)return false;
            redisService.setOtp(otp+"",userName);
            return true;
        }
        String message=Telegrammessages.MailOtpMessage.getMessage();
        message=message.replace("[xx]", name);
        message=message.replace("[otp]", otp);
        Boolean resp=emailService.sendEmail(email,"Your One-Time Password (OTP) for connect with LetsCode Bot",message);
        if(!resp)return false;
        redisService.setOtp(otp+"",userName);
        return true;
    }

    public String extractUserName(String input) {
        return input.substring(1);
    }

    public boolean isItOtp(String input) {
            if (input.length() != 6) {
                return false;
            }
            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
    }
    
    public String validateOtp(String otp){
        return redisService.getOtp(otp);
    }

    public String saveChatId(Long chatId,String userName) {
        userEntity user=userdao.findbyuserName(userName);
        user.setChatId(chatId+"");
        userdao.save(user);

            Map<String,String> map=new HashMap<>();
            map.put("userName",user.getUserName());map.put("name",user.getName());
            map.put("userId",""+user.getUserId());
            redisService.storechatId(chatId+"",map);

        return user.getName();
    }

    public void logout(Long userId,String chatId) {
        userEntity user=userdao.findbyId(userId.intValue());
        user.setChatId("");
        userdao.save(user);
        Map<String,String>map=new HashMap<>();
        map.put("userName","NONE");map.put("name","NONE");
        map.put("userId","NONE");
     redisService.storechatId(chatId,map);
  
    }

    public Map<String,String> getuserNameBydbChatId(String chatId) {
        userEntity user=userdao.findbychatId(chatId);
        if(user==null)return null;
        Map<String,String> map=new HashMap<>();
        map.put("userName",user.getUserName());map.put("name",user.getName());
        map.put("userId",""+user.getUserId());
        return map;
    }

    public Map<String,String> getuserNameByChatId(String chatId) {
        Map<String,String> data=redisService.getUserByChatId(chatId);
        if(data!=null){
            if(data.get("name").equals("NONE"))return null;
            return data;
        }
        Map<String,String> dbdata=getuserNameBydbChatId(chatId);
        Map<String,String>map=new HashMap<>();
        map.put("userName","NONE");map.put("name","NONE");
        map.put("userId","NONE");
        if(dbdata==null)dbdata=map;
 
            redisService.storechatId(chatId,dbdata);
 
        if(dbdata.get("name").equals("NONE"))return null;
        return dbdata;
    }

    public String getrandomSolved(Long userId,String type) {
        int randomNumber=gerateRandomNumber(1,3);
        if(type=="cf")randomNumber=2;
        else if(type=="lc")randomNumber=1;
        else if(type=="at")randomNumber=3;
        if(randomNumber==1){
            List<Object[]> res=atProblemsdao.findSolvedProblem(userId,"lc");
            return genarateSolvedLcProb(res);
        }
        if(randomNumber==2){
            List<Object[]> res=atProblemsdao.findSolvedProblem(userId,"cf");
            return genarateSolvedCfProb(res);
        }
        List<Object[]> res=atProblemsdao.findSolvedProblem(userId,"at");
        return genarateSolvedAtProb(res);
    }

    private String genarateSolvedAtProb(List<Object[]> res) {
         Object[] problem=res.get(0);String path=(String)problem[2];
        String message="Platform: AtCoder\n";String contestId=path.split("_")[0];
        message+=String.format("Title: [%s](https://atcoder.jp/contests/%s/tasks/%s)", (String)problem[1],contestId,path)+"\n";
        message+="Solved on "+(Date)problem[0]+"\n";
        message+=String.format("Past [submission](https://atcoder.jp/contests/%s/submissions/%d)", contestId,(Integer)problem[4]);
        return message;
    }

    private String genarateSolvedCfProb(List<Object[]> res) {
        Object[] problem=res.get(0);String path=(String)problem[2];
        int contestId = Integer.parseInt(path.split("/")[0]);
        String message="Platform: CodeForces\n";
        message+=String.format("Title: [%s](https://codeforces.com/problemset/problem/%s)", (String)problem[1],path)+"\n";
        message+="Solved on "+(Date)problem[0]+"\n";
        message+=String.format("Past [submission](https://codeforces.com/contest/%d/submission/%d)", contestId,(Integer)problem[4]);
        return message;
    }

    private String genarateSolvedLcProb(List<Object[]> res) {
        Object[] problem=res.get(0);String path=(String)problem[2];
        String message="Platform: LeetCode\n";
        message+=String.format("Title: [%s](https://leetcode.com/problems/%s/)", (String)problem[1],path)+"\n";
        message+="Solved on "+(Date)problem[0]+"\n";
        message+=String.format("Past [submission](https://leetcode.com/submissions/detail/%s/)",(Integer)problem[4]);
        return message;
    }

    public Pair<statsResp, Boolean> genarateDailyProblems(Long userId,String chatId) {
        statsResp redisReponse=redisService.getStats(chatId+"stats!");
        if(redisReponse!=null)return Pair.of(redisReponse,false);
        Optional<userStatisticsEntity> stats=userStatisticsDao.findById(userId.intValue());
        statsResp response=new statsResp();
        if(stats.isPresent()){
            userStatisticsEntity stat=stats.get();
            if(stat.getEnabled()==0)return null;
            List<Object[]> cfList=atProblemsdao.getFilteredCfProblems(userId, stat.getCfCount(), stat.getRating());
            List<Object[]> atList=atProblemsdao.getFilteredAtProblems(userId, stat.getAcCount(), stat.getAtType());
            List<Object[]> lcList=atProblemsdao.getFilteredLcProblems(userId, stat.getLcCount(), stat.getDifficulty());
            response.setAtMap(atList);response.setLcMap(lcList);
            response.setCfMap(cfList);
            redisService.setStats(chatId+"stats!",response);

        }
        else{
            return null;
        }
        return Pair.of(response,true);
    }


    public void saveQuestionstoDb(Map<String,List<Object[]>> questions,Long userId){
        Long curTime = System.currentTimeMillis() / 1000;
        List<UserDailyProblemsEnity> problems=new ArrayList<>();
        String seesionId=("Msr"+generateOTP(6));
        for(String key:questions.keySet())
        {
            UserDailyProblemsEnity problem=new UserDailyProblemsEnity();
            problem.setSessionId(seesionId);
            problem.setUserId(userId);problem.setTimestamp(curTime);
            problem.setName(key);
            Object[] question=questions.get(key).get(0);
            problem.setTitle(question[1].toString());problem.setContestId(question[0].toString());
            problem.setIndicator(question[2].toString());problem.setRating(question[3].toString());
            problem.setPlatform(question[4].toString());
            problems.add(problem);
        }
        dailyProblemsdao.saveAll(problems);
    }   

    public Pair<String,String> isQuestionExist(String title){
        UserDailyProblemsEnity question=dailyProblemsdao.getDailyChallengeByname(title);
        if(question==null)return null;
        String message=Telegrammessages.DailyChallenge.getMessage();
        List<Object[]> res=new ArrayList<>();Object[] problem = new Object[5];
        if(question.getPlatform().equals("cf")){
            problem[0]=Integer.parseInt(question.getContestId()); problem[1]=question.getTitle();
            problem[2]=question.getIndicator(); problem[3]=Integer.parseInt(question.getRating());problem[4]="cf";
            res.add(problem);message+=genarateCfProb(res);
        }
        else if(question.getPlatform().equals("lc")){
            problem[0]=question.getContestId(); problem[1]=question.getTitle();
            problem[2]=question.getIndicator(); problem[3]=question.getRating();problem[4]="lc";
            res.add(problem);message+=genarateLcProb(res,3);
        }
        else if(question.getPlatform().equals("at")){
            problem[0]=question.getContestId(); problem[1]=question.getTitle();
            problem[2]=question.getIndicator(); problem[3]=question.getRating();problem[4]="at";
            res.add(problem);message+=genarateAtProb(res);
        }
        return Pair.of(message, question.getSessionId());
    }

    public List<String> getChallengesByid(String sessionId){
        List<UserDailyProblemsEnity> questions=dailyProblemsdao.getDailyChallengesById(sessionId);
        if(questions==null)return null;
        List<String> buttons=new ArrayList<>();
        for(UserDailyProblemsEnity question:questions){
            buttons.add(question.getName());
        }
        return buttons;
    }
    public Pair<List<String>,Map<String,List<Object[]>>> getdatlyQuestions(Long userId, String chatId) {
        Pair<statsResp,Boolean> randomChallenges = genarateDailyProblems(userId, chatId);
        if (randomChallenges == null)
            return null;
        statsResp response=randomChallenges.getFirst();
        List<String> buttons = new ArrayList<>();
        Map<String,List<Object[]>> questions=new HashMap<>();
        for (Map<String, Object> map : response.getCfProblems()) {
            String title=(String)map.get("title");
            if(title.length()>33)title=title.substring(0, 32)+"...";
            List<Object[]> res=new ArrayList<>();Object[] problem = new Object[5];
            buttons.add(map.get("indicator") + ". " +title);
            problem[0]=map.get("contest_id"); problem[1]=map.get("indicator");
            problem[2]=map.get("title"); problem[3]=map.get("rating");problem[4]="cf";
            res.add(problem);questions.put(map.get("indicator") + ". " +title, res);
        }
        
        for (Map<String, Object> map : response.getLcProblems()) {
            List<Object[]> res=new ArrayList<>();Object[] problem = new Object[5];
            String title=(String)map.get("title");
            if(title.length()>33)title=title.substring(0, 32)+"...";
            buttons.add(map.get("id") + ". " +title);
            problem[0]=map.get("difficulty"); problem[1]=map.get("title");
            problem[2]=map.get("path"); problem[3]=map.get("ac_rate");problem[4]="lc";
            res.add(problem);questions.put(map.get("id") + ". " +title, res);
        }
        
        for (Map<String, Object> map : response.getAtProblems()) {
            List<Object[]> res=new ArrayList<>();Object[] problem = new Object[5];
            String title=(String)map.get("title");
            if(title.length()>33)title=title.substring(0, 32)+"...";
            buttons.add(title);
            problem[0]=map.get("unique_id"); problem[1]=map.get("contest_id");
            problem[2]=map.get("title"); problem[3]=map.get("problem_index");
            problem[4]="at";
            res.add(problem);questions.put(title, res);
        }
        if(randomChallenges.getSecond())saveQuestionstoDb(questions,userId);

        return Pair.of(buttons, questions);
    }

    public String lockInstance() {
        String lockName = "dailyChallengesLock";
        String lockValue = "locked";
        String lock = redisService.getLock(lockName);
    
        if (lock != null) {
            return null;
        }
    
        redisService.setLock(lockName, lockValue);
        return "MSR";
    }
    
    
    public  List<Pair<List<String>, Map<String,String>>> genarateDailyProblemstoAllUsers(){
        List<userEntity> users=userdao.findallEnabledUsers();
        if(users==null)return null;
        List<Pair<List<String>, Map<String,String>>> allUserChallenges=new ArrayList<>();
        for(userEntity user:users){
            redisService.deleteKey(user.getChatId()+"stats!");
            Pair<List<String>, Map<String, List<Object[]>>> userChallenges=getdatlyQuestions(user.getUserId(), user.getChatId());
            if(userChallenges==null)continue;
            Map<String,String> userData=new HashMap<>();userData.put("name",user.getName());
            userData.put("chatId",user.getChatId());
            allUserChallenges.add(Pair.of(userChallenges.getFirst(),userData));
        }
        return allUserChallenges;
    }

    
}
