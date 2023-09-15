package com.example.Let.sCode.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.Let.sCode.Services.TelegramBotService;

@Configuration
public class TelegramConfiguration {
    
    public static String botToken;
    
    public TelegramConfiguration(@Value("${spring.app.token}") String botToken) {
        TelegramConfiguration.botToken = botToken;
    }
    @Bean
    public TelegramBotService configureTelegram() throws TelegramApiException{
        TelegramBotService bot = new TelegramBotService();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return bot;
    }
}
