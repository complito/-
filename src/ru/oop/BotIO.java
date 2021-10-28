package ru.oop;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotIO extends TelegramLongPollingBot {
    public void printResponse(Response botResponse) {
        System.out.println(botResponse.getResponseString());
    }

    public Response sendRequest(String query) {
        BotLogic botLogic = new BotLogic();
        return botLogic.requestHandler(query);
    }

    @Override
    public String getBotToken() {
        return "2081242476:AAHWlLdicsAyw_04JNXQOoAcpqBi0OnPC2w";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            BotLogic botLogic = new BotLogic();
            Response botResponse = botLogic.requestHandler(update.getMessage().getText());
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(botResponse.getResponseString());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Genius Search";
    }
}
