package ru.oop;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

// t.me/GeniusSearchBot

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BotLogic botLogic = new BotLogic();
        BotIO botIO = new BotIO();
        botIO.printResponse(botLogic.startMessage());
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotIO());
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
        while (true) {
            String query = input.nextLine();
            botIO.printResponse(botIO.sendRequest(query));
        }
    }
}