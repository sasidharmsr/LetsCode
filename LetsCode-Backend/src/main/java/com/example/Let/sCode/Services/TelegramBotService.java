package com.example.Let.sCode.Services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.Let.sCode.Security.TelegramConfiguration;
import com.example.Let.sCode.json.Telegrammessages;

import jakarta.annotation.PostConstruct;


@Service
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    public telegramSerive telegramSerive;


    @Value("${spring.app.url}")
    String url;




    @Override
    public String getBotUsername() {
        return "LetsCodersBot";
    }
    @Override
    public String getBotToken() {
        return  TelegramConfiguration.botToken;
    }
    @Override
    public void onUpdateReceived(Update update) {
         if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String chatId = callbackQuery.getMessage().getChatId().toString();
                int messageId = callbackQuery.getMessage().getMessageId();
                String messageText = callbackQuery.getMessage().getText();
                String callbackData = callbackQuery.getData();
                Map<String,String> result=telegramSerive.getuserNameByChatId(chatId);
                String knownUser="";long val=0;String userName="";
                Long userId=val;
                if(result!=null){
                    knownUser=result.get("name");userName=result.get("userName");
                    userId=Long.parseLong(result.get("userId"));
                }
                boolean solvedCheck=messageText.equals(Telegrammessages.SolvedResp.getMessage());
                Pair<List<String>,Map<String,List<Object[]>>> challenges=null;
                if(knownUser!="")challenges=telegramSerive.getdatlyQuestions(userId, chatId);
                if ("Login".equals(callbackData)) {
                    if(knownUser=="")SendMessage(getResponse(chatId,Telegrammessages.LOGIN_RESP.getMessage()));
                    else SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                } else if ("Random".equals(callbackData) || "Back to Platforms Menu".equals(callbackData)) {
                    InlineKeyboardMarkup inlineKeyboard =attachButtons(3,2, Arrays.asList("ALL", "CodeForces","LeetCode","AtCoder","Back to Menu"),"");
                    SendEditMessage(getEditResponse(chatId,messageId,Telegrammessages.RandomResp.getMessage(),inlineKeyboard));
                }
                else if ("Solved".equals(callbackData) ) {
                    InlineKeyboardMarkup inlineKeyboard =attachButtons(3,2, Arrays.asList("ALL", "CodeForces","LeetCode","AtCoder","Back to Menu"),"");
                    SendEditMessage(getEditResponse(chatId,messageId,Telegrammessages.SolvedResp.getMessage(),inlineKeyboard));
                } else if("HomeBack to Menu".equals(callbackData) || "Back to Menu".equals(callbackData) ){
                    if(knownUser==""){
                        InlineKeyboardMarkup inlineKeyboard =attachButtons(1,2, Arrays.asList("Login", "Random"),"");
                        SendEditMessage(getEditResponse(chatId,messageId,Telegrammessages.MAIN_RESP.getMessage(),inlineKeyboard));
                    }else
                        SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                }else if("Daily Challenges".equals(callbackData)||"Back to Challenges".equals(callbackData)){
                    if(challenges==null){
                     SendEditMessage(getEditResponse(chatId+"",messageId,String.format(Telegrammessages.NotEnabled.getMessage(),url,userName),attachButtons(1,1, Arrays.asList("Back to Menu"),"")));
                    }else
                    {
                        List<String> buttons=challenges.getFirst();
                        buttons.add("Back to Menu");
                        SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.DailyChallenges.getMessage(),attachButtons(buttons.size(),1,buttons,"Home")));
                    }
                }else if("<< Back to Challenges".equals(callbackData)){
                    List<String> buttons=challenges.getFirst();
                    SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.DailyChallenges.getMessage(),attachButtons(buttons.size(),1,buttons,"")));
                }
                else if("ALL".equals(callbackData)){
                    String message="";
                    if(solvedCheck)message=Telegrammessages.SolvedRandomResp.getMessage()+telegramSerive.getrandomSolved(userId,"All");
                    else message=Telegrammessages.AllRandomResp.getMessage()+telegramSerive.genrateRanmomQuestion();
                    SendMessage(getResponse(chatId, message));
                    if(knownUser=="")SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MAIN_RESP.getMessage(),attachButtons(1,2, Arrays.asList("Login", "Random"),"")));
                    else SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                    return;
                }
                else if("CodeForces".equals(callbackData)){
                    if(solvedCheck)
                    {
                        String message=Telegrammessages.SolvedRandomResp.getMessage()+telegramSerive.getrandomSolved(userId,"cf");
                        SendMessage(getResponse(chatId, message));
                    }
                    else {
                        InlineKeyboardMarkup inlineKeyboard =attachButtons(5,4,genarateRatingList(),"");
                        SendEditMessage(getEditResponse(chatId,messageId,Telegrammessages.CfprobRandomResp.getMessage(),inlineKeyboard));
                    }
                }
                else if("LeetCode".equals(callbackData)){
                    if(solvedCheck)
                    {
                        String message=Telegrammessages.SolvedRandomResp.getMessage()+telegramSerive.getrandomSolved(userId,"lc");
                        SendMessage(getResponse(chatId, message));
                    }
                    else {
                    InlineKeyboardMarkup inlineKeyboard =attachButtons(2,3, Arrays.asList("Easy","Medium","Hard","Back to Platforms Menu"),"");
                    SendEditMessage(getEditResponse(chatId,messageId,Telegrammessages.LcprobRandomResp.getMessage(),inlineKeyboard));
                    }
                }
                else if("AtCoder".equals(callbackData)){
                    if(solvedCheck)
                    {
                        String message=Telegrammessages.SolvedRandomResp.getMessage()+telegramSerive.getrandomSolved(userId,"at");
                        SendMessage(getResponse(chatId, message));
                    }
                    else {
                        InlineKeyboardMarkup inlineKeyboard =attachButtons(3,4, Arrays.asList("A", "B","C","D","E","F","G","H","Back to Platforms Menu"),"");
                        SendEditMessage(getEditResponse(chatId,messageId,Telegrammessages.AtprobRandomResp.getMessage(),inlineKeyboard));
                    }
                }else if("Logout".equals(callbackData)){
                    telegramSerive.logout(userId,chatId);
                    SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MAIN_RESP.getMessage(),attachButtons(1,2, Arrays.asList("Login", "Random"),"")));
                }else if(callbackData.endsWith("Phone Number")){
                    if(knownUser!=""){
                       SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                         return;
                    }
                    String[] data =callbackData.split("/");
                    Map<String,String> check=telegramSerive.isValidUsername(data[0]);
                    Boolean validition=telegramSerive.sendOtp(check.get("name"),check.get("email"),check.get("userName"),check.get("phoneNumber"));
                    if(validition==false)SendMessage(getResponse(chatId+"",Telegrammessages.IncorrectPhoneNumber.getMessage()));
                    else SendMessage(getResponse(chatId+"", String.format(Telegrammessages.OtpMobileResp.getMessage(),check.get("phoneNumber").substring(0, 5))));
                }else if(callbackData.endsWith("Email")){
                    if(knownUser!=""){
                       SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                         return;
                    }
                    String[] data =callbackData.split("/");Map<String,String> check=telegramSerive.isValidUsername(data[0]);
                    Boolean validition=telegramSerive.sendOtp(check.get("name"),check.get("email"),check.get("userName"),"");
                    if(validition==false)SendMessage(getResponse(chatId+"",Telegrammessages.Incorrectemail.getMessage()));
                    else SendMessage(getResponse(chatId+"", String.format(Telegrammessages.OtpResp.getMessage(),check.get("email"))));
                }
                else{
                    if(challenges!=null && (challenges.getSecond().containsKey(callbackData)|| callbackData.startsWith("Home"))){
                        String backButton="<< Back to Challenges";
                        if(callbackData.startsWith("Home")){backButton="Back to Challenges";
                        callbackData=callbackData.substring(4);
                        }
                        Object[] problem=challenges.getSecond().get(callbackData).get(0);
                        String message=Telegrammessages.DailyChallenge.getMessage();
                        if(problem[4]=="at")message+=telegramSerive.genarateAtProb(challenges.getSecond().get(callbackData));
                        else if(problem[4]=="lc")message+=telegramSerive.genarateLcProb(challenges.getSecond().get(callbackData),1);
                        else if(problem[4]=="cf")message+=telegramSerive.genarateCfProb(challenges.getSecond().get(callbackData));
                       
                        SendEditMessage(getEditResponse(chatId+"",messageId,message,attachButtons(messageId, messageId, Arrays.asList(backButton),"")));
                        return;
                    }
                    for(String rating:genarateRatingList()){
                        if(rating.equals(callbackData)){
                            String message=Telegrammessages.CfRandomResp.getMessage()+telegramSerive.genarateCfProbByRating(rating);
                            SendMessage(getResponse(chatId, message));
                            if(knownUser=="")SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MAIN_RESP.getMessage(),attachButtons(1,2, Arrays.asList("Login", "Random"),"")));
                            else SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                            return;
                        }
                    }
                    for(String diffculty:Telegrammessages.LcDifficultyTypes.getMessage().split(",")){
                        if(diffculty.equals(callbackData)){
                            String message=Telegrammessages.LcRandomResp.getMessage()+telegramSerive.genarateLcProbByDif(diffculty);
                            SendMessage(getResponse(chatId, message));
                            if(knownUser=="")SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MAIN_RESP.getMessage(),attachButtons(1,2, Arrays.asList("Login", "Random"),"")));
                            else SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                            return;
                        }
                    }
                    for(String type:Telegrammessages.AtProblemTypes.getMessage().split(",")){
                        if(type.equals(callbackData)){
                            String message=Telegrammessages.AtRandomResp.getMessage()+telegramSerive.genarateAtProbByType(type);
                            SendMessage(getResponse(chatId, message));
                            if(knownUser=="")SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MAIN_RESP.getMessage(),attachButtons(1,2, Arrays.asList("Login", "Random"),"")));
                            else SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                            return;
                        }
                    }
                    if(callbackData.startsWith("Msr")){
                        List<String> buttons=telegramSerive.getChallengesByid(callbackData);
                        if(buttons!=null){
                            SendEditMessage(getEditResponse(chatId+"",messageId,Telegrammessages.DailyAutoChallenges.getMessage().replace("XX", knownUser),attachButtons(buttons.size(),1,buttons,"")));
                        }
                    }
                    Pair<String,String> isPastQuestion=telegramSerive.isQuestionExist(callbackData);
                    if(isPastQuestion!=null){
                        SendEditMessage(getEditResponse(chatId+"",messageId,isPastQuestion.getFirst(),createBackButton(isPastQuestion.getSecond())));
                        return;
                    }
                    

                }
            }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText(); 
            System.out.println(messageText+"-"+chatId.toString());
            Map<String,String> result=telegramSerive.getuserNameByChatId(chatId+"");
            String knownUser="";
            if(result!=null)knownUser=result.get("name");
            if(knownUser=="" && messageText.startsWith("@"))
            {
                String userName=telegramSerive.extractUserName(messageText);
                Map<String,String> check=telegramSerive.isValidUsername(userName);
                if(check==null){
                    SendMessage(getResponse(chatId+"",String.format(Telegrammessages.UserNotExist.getMessage(),url)));
                }else{
                    if(check.get("role").equals("ADMIN")){
                        SendMessage(getResponsewithButtons(chatId+"",Telegrammessages.AdminResp.getMessage().replace("XX", check.get("name")),attachButtons(1,2, Arrays.asList("Phone Number", "Email"),check.get("userName")+"/")));
                        return;
                    }
                    Boolean validition=telegramSerive.sendOtp(check.get("name"),check.get("email"),check.get("userName"),"");
                    if(validition==false)SendMessage(getResponse(chatId+"",Telegrammessages.Incorrectemail.getMessage()));
                    else SendMessage(getResponse(chatId+"", String.format(Telegrammessages.OtpResp.getMessage(),check.get("email"))));
                }
            }
            else if(knownUser=="" && telegramSerive.isItOtp(messageText)){
                String userName=telegramSerive.validateOtp(messageText);
                if(userName==null)SendMessage(getResponse(chatId+"", Telegrammessages.OtpIncorrectResp.getMessage()));
                else {
                    String name=telegramSerive.saveChatId(chatId,userName);
                    SendMessage(getResponsewithButtons(chatId+"",Telegrammessages.MainNameResp.getMessage().replace("XX", name),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
                }
            }
            else if(knownUser!=""){
                  SendMessage(getResponsewithButtons(chatId+"",Telegrammessages.MainNameResp.getMessage().replace("XX", knownUser),attachButtons(2,2, Arrays.asList("Random","Solved","Daily Challenges","Logout"),"")));
            }
            else{
                 SendMessage(getResponsewithButtons(chatId+"",Telegrammessages.MAIN_RESP.getMessage(),attachButtons(1,2, Arrays.asList("Login", "Random"),"")));
            }
          }
    }

    private List<String> genarateRatingList() {
        List<String> list=new ArrayList<>();
        for(int rating=800;rating<=2300;rating+=100)list.add(rating+"");
        list.add("Back to Platforms Menu");
        return list;
    }

    public void SendMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendEditMessage(EditMessageText message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private SendMessage getResponse(String chatId,String message){
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(message);
        responseMessage.setParseMode("Markdown");
        return responseMessage;
    }

    private EditMessageText getEditResponse(String chatId,int messageId,String message,InlineKeyboardMarkup buttons){
        EditMessageText responseMessage = new EditMessageText();
        responseMessage.setChatId(chatId);
        responseMessage.setMessageId(messageId);
        responseMessage.setText(message);
        responseMessage.setReplyMarkup(buttons);
        responseMessage.setParseMode("Markdown");
        return responseMessage;
    }

    private SendMessage getResponsewithButtons(String chatId,String message,InlineKeyboardMarkup buttons){
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(message);
        responseMessage.setReplyMarkup(buttons);
        return responseMessage;
    }


    private InlineKeyboardMarkup attachButtons(int rowCount,int colCount,List<String> buttons,String header){
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        int index=0;int size=buttons.size();
        for(int i=0;i<rowCount;i++){
            List<InlineKeyboardButton> ButtonRow = new ArrayList<>();
            for(int j=0;j<colCount;j++){
                if(index<size){
                InlineKeyboardButton Button = new InlineKeyboardButton();
                Button.setText(buttons.get(index));
                Button.setCallbackData(header+buttons.get(index));
                ButtonRow.add(Button);
                }
                index++;
                if(index==size)break;
            }
            keyboard.add(ButtonRow);
        }
        inlineKeyboard.setKeyboard(keyboard);
        return inlineKeyboard;
    }

    private InlineKeyboardMarkup createBackButton(String data){
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> ButtonRow = new ArrayList<>();
        InlineKeyboardButton Button = new InlineKeyboardButton();
        Button.setText("<< Back to Challenges");
        Button.setCallbackData(data);
        ButtonRow.add(Button);
        keyboard.add(ButtonRow);
        inlineKeyboard.setKeyboard(keyboard);
        return inlineKeyboard;
    }


   // @Scheduled(fixedDelay = 2 * 60 * 60 * 1000) // 2 hours in milliseconds
   @Scheduled(cron = "${spring.app.time}")
   public void GenarateDailyChallengs() throws InterruptedException {
       String instance = telegramSerive.lockInstance();
       System.out.println(instance);
       if (instance == null) {
           return;
       }
       List<Pair<List<String>, Map<String, String>>> usersChallenges = telegramSerive.genarateDailyProblemstoAllUsers();
       System.out.println(usersChallenges);
       if(usersChallenges==null)return;
       for (Pair<List<String>, Map<String, String>> userChallenge : usersChallenges) {
           List<String> buttons = userChallenge.getFirst();
           String chatId = userChallenge.getSecond().get("chatId");
           String name = userChallenge.getSecond().get("name");
           SendMessage(getResponsewithButtons(chatId + "", Telegrammessages.DailyAutoChallenges.getMessage().replace("XX", name), attachButtons(buttons.size(), 1, buttons, "")));
       }
   }

 @PostConstruct
   public void configureTimeZone() {
       TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
   }

}