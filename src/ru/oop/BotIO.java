package ru.oop;

import java.util.ArrayList;
import java.util.List;

public class BotIO {
    public void PrintResponse(Response botResponse) {
        System.out.println(botResponse.responseString);

    }

    public Response Request(String request) {
        BotLogic botLogic = new BotLogic();
        if (request.equals(""))
            return new Response("Ошибка: запрос пустой");
        else if (request.equals("\\help"))
            return botLogic.helpMessage();
        else if (request.length() > 10 && request.startsWith("\\findsong")) {
            List<Song> songs = botLogic.findSongs(request.substring(10)).responseList;
            if (songs.isEmpty())
                return new Response("По введённому запросу не было найдено песен");
            else {
                Response resp = new Response(songs);
                resp.SongListToStr();
                return resp;
            }
        }
        else {
            return new Response("Ошибка: неизвестный запрос");
        }
    }
}
