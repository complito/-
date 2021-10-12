package ru.oop;

import java.util.ArrayList;
import java.util.List;

public class BotIO {
    public void Response(String BotResponse) {
        System.out.println(BotResponse);
    }

    public String[] Request(String request) {
        BotLogic botLogic = new BotLogic();
        if (request.equals(""))
            return new String[] {"Ошибка: запрос пустой"};
        else if (request.equals("\\help"))
            return new String[] {botLogic.helpMessage()};
        else if (request.length() > 10 && request.startsWith("\\findsong")) {
            List<Song> songs = botLogic.findSongs(request.substring(10));
            if (songs.isEmpty())
                return new String[] {"По введённому запросу не было найдено песен"};
            else {
                List<String> botRequest = new ArrayList<>();
                botRequest.add("Список найденных песен:");
                for (Song song : songs) botRequest.add(song.fullTitle);
                String[] botRequestInArray = new String[botRequest.size()];
                botRequestInArray = botRequest.toArray(botRequestInArray);
                return botRequestInArray;
            }
        }
        else return new String[] {"Ошибка: неизвестный запрос"};
    }
}
