package ru.oop;

import java.util.List;

enum ExecuteQueryStatus {
    QueryIsEmpty,
    HelpMessage,
    SongsNotFound,
    SongsFound,
    UnknownQuery
}

public class ProcessingQuery {
    public static ExecuteQueryStatus executeQuery(String query) {
        if (query.equals("")) {
            System.out.println("Ошибка: запрос пустой");
            return ExecuteQueryStatus.QueryIsEmpty;
        }
        else if (query.equals("\\help")) {
            System.out.println(BotLogic.helpMessage());
            return ExecuteQueryStatus.HelpMessage;
        }
        else if (query.length() > 10 && query.substring(0, 9).equals("\\findsong")) {
            List<Song> songs = BotLogic.findSongs(query.substring(10));
            if (songs.isEmpty()) {
                System.out.println("По введённому запросу не было найдено песен");
                return ExecuteQueryStatus.SongsNotFound;
            }
            else {
                System.out.println("Список найденных песен:");
                for (Song song : songs) System.out.println(song.fullTitle);
                return ExecuteQueryStatus.SongsFound;
            }
        }
        else {
            System.out.println("Ошибка: неизвестный запрос");
            return ExecuteQueryStatus.UnknownQuery;
        }
    }
}
