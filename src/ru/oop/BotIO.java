package ru.oop;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class BotIO extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        FileReader reader;
        Properties properties = new Properties();
        try {
            reader = new FileReader("resources/config.properties");
            properties.load(reader);
            String botToken = properties.getProperty("botToken");
            reader.close();
            return botToken;
        }
        catch (IOException e) {
            System.out.println("Ошибка: файл свойств отсутствует");
        }
        return null;
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
