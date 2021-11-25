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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

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
            e.printStackTrace();
            return null;
        }
    }
    public Response requestHandler(String request) {
        if (request.equals("")) {
            return new Response("Ошибка: запрос пустой");
        } else if (request.equals("/help")) {
            return helpMessage();
        } else if (request.equals("/start")) {
            return startMessage();
        } else if (request.length() > 16 && request.startsWith("/findsonglyrics")) {
            return findSongLyrics(request.substring(16));
        } else if (request.length() > 14 && request.startsWith("/findsonginfo")) {
                return findSongInfo(request.substring(14));
        } else if (request.length() > 10 && request.startsWith("/findsong ")) {
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
        return new Response("""
                /findsong - Поиск песни по отрывку текста
                /findsonglyrics - Поиск текста песни по её номеру
                /findsonginfo - Поиск информации песни по её номеру""");
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
                String lyricsPath = searchResults.getJSONObject(i).getJSONObject("result").getString("path");
                Song foundSong = new Song(apiPath, fullTitle, primaryArtistApiPath, lyricsPath);
                foundSongs.add(foundSong);
            }
        }
        return new Response(foundSongs);
    }

    public Response findSongLyrics(String songId) {
        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.genius.com/songs/" + songId)
                .header("Authorization","Bearer " + GENIUSTOKEN).asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            String searchResult = parsedHttpResponse.getJSONObject("response").getJSONObject("song")
                    .getString("path");
            try {
                while (true){
                    Document doc = Jsoup.connect("https://genius.com" + searchResult).get();
                    Elements listLyrics = doc.select("div.song_body-lyrics").select("div.lyrics").select("p");
                    for (Element element: listLyrics) {
                        Safelist mySafelist = new Safelist();
                        mySafelist.addTags("br");
                        Cleaner cleaner = new Cleaner(mySafelist);
                        Document dirty = Jsoup.parse(element.toString());
                        Document clean = cleaner.clean(dirty);
                        return new Response(clean.toString().replace("<html>", "")
                            .replace("</html>", "").replace("<head>", "")
                            .replace("</head>", "").replace("<body>", "")
                            .replace("</body>", "").replace("<br>", "").trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Response findSongInfo(String songId) {
        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.genius.com/songs/" + songId)
                .header("Authorization","Bearer " + GENIUSTOKEN).asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            String fullTitle = parsedHttpResponse.getJSONObject("response").getJSONObject("song")
                    .getString("full_title").replace("\u00a0", " ");
            String releaseDate = parsedHttpResponse.getJSONObject("response").getJSONObject("song")
                    .getString("release_date_for_display").replace("\u00a0", " ");
            String pageViews = parsedHttpResponse.getJSONObject("response").getJSONObject("song").getJSONObject("stats")
                    .getString("pageviews");
            String albumName = parsedHttpResponse.getJSONObject("response").getJSONObject("song").getJSONObject("album")
                    .getString("name");
            return new Response("Полное название: " + fullTitle + "\n" + "Дата выхода: " + releaseDate
                    + "\n" + "Количество просмотров песни: " + pageViews + "\n" + "Альбом: " + albumName);
        }
        return null;
    }
}
