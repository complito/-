package ru.oop;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class Song {
    String apiPath;
    String fullTitle;
    String primaryArtistApiPath;
    Song(String apiPath, String fullTitle, String primaryArtistApiPath) {
        Song.this.apiPath = apiPath;
        Song.this.fullTitle = fullTitle;
        Song.this.primaryArtistApiPath = primaryArtistApiPath;
    }
}

public class BotLogic {

    public String startMessage() {
        return "Привет, я бот который умеет находить песню по отрывку ее текста\n";
    }

    public String helpMessage() {
        return "\\findsong - Поиск песни по отрывку текста";
    }

    public List<Song> findSongs(String songLyrics) { // Возвращает список найденых песен
        List<Song> foundSongs = new ArrayList<>();
        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.genius.com/search?q=" + songLyrics)
                .header("Authorization","Bearer JWHWXT3wkETpLyNmqq9YXl6V3Ftbgk1D1cRCJz60edII4BNQHLEmhRhs8KKkNqxf")
                .asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            JSONArray searchResults = parsedHttpResponse.getJSONObject("response").getJSONArray("hits");
            for (int i = 0; i < searchResults.length(); i++) {
                String apiPath = searchResults.getJSONObject(i).getJSONObject("result").getString("api_path");
                String fullTitle = searchResults.getJSONObject(i).getJSONObject("result").getString("full_title");
                String primaryArtistApiPath = searchResults.getJSONObject(i).getJSONObject("result")
                        .getJSONObject("primary_artist").getString("api_path");
                Song foundSong = new Song(apiPath, fullTitle, primaryArtistApiPath);
                foundSongs.add(foundSong);
            }
        }
        return foundSongs;
    }
}
