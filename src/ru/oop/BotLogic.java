package ru.oop;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BotLogic {
    String GENIUSTOKEN = getGeniusToken();
    private String getGeniusToken() {
        FileReader reader;
        Properties properties = new Properties();
        try {
            reader = new FileReader("resources/config.properties");
            properties.load(reader);
            String geniusToken = properties.getProperty("geniusToken");
            reader.close();
            return geniusToken;
        }
        catch (IOException e) {
            System.out.println("Ошибка: файл свойств отсутствует");
            return "";
        }
    }
    public Response requestHandler(String request) {
        if (request.equals("")) {
            return new Response("Ошибка: запрос пустой");
        } else if (request.equals("/help")) {
            return helpMessage();
        } else if (request.equals("/start")) {
            return startMessage();
        } else if (request.length() > 10 && request.startsWith("/findsong")) {
            List<Song> songs = findSongs(request.substring(10)).getResponseList();
            if (songs.isEmpty()) {
                return new Response("По введённому запросу не было найдено песен");
            } else {
                Response resp = new Response(songs);
                resp.songListToStr();
                return resp;
            }
        } else {
            return new Response("Ошибка: неизвестный запрос");
        }
    }

    public Response startMessage() {
        return new Response("""
                Привет, я бот который умеет находить песню по отрывку ее текста. Если нужна помощь, напиши /help
                Введите запрос:\s
                """);
    }

    public Response helpMessage() {
        return new Response("/findsong - Поиск песни по отрывку текста");
    }

    public Response findSongs(String songLyrics) {
        List<Song> foundSongs = new ArrayList<>();
        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.genius.com/search?q=" + songLyrics)
                .header("Authorization","Bearer " + GENIUSTOKEN)
                .asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            JSONArray searchResults = parsedHttpResponse.getJSONObject("response").getJSONArray("hits");
            for (int i = 0; i < searchResults.length(); i++) {
                String apiPath = searchResults.getJSONObject(i).getJSONObject("result").getString("api_path");
                String fullTitle = searchResults.getJSONObject(i).getJSONObject("result").getString("full_title")
                        .replace("\u00a0", " ");
                String primaryArtistApiPath = searchResults.getJSONObject(i).getJSONObject("result")
                        .getJSONObject("primary_artist").getString("api_path");
                Song foundSong = new Song(apiPath, fullTitle, primaryArtistApiPath);
                foundSongs.add(foundSong);
            }
        }
        return new Response(foundSongs);
    }
}
