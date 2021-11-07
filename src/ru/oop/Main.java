package ru.oop;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

// t.me/GeniusSearchBot

public class Main {
    public static void main(String[] args) {
        System.out.println("Бот запущен \n");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotIO());
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}