package ru.oop;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

public class BotLogic {
    private static final String GENIUSTOKEN = getGeniusToken();
    private final Map<String, Function<String, Response>> handlers = new HashMap<>();

    public BotLogic() {
        handlers.put("/help", this::helpMessage);
        handlers.put("/start", this::startMessage);
        handlers.put("/findsonglyrics", this::findSongLyrics);
        handlers.put("/findsonginfo", this::findSongInfo);
        handlers.put("/findartists", this::findArtists);
        handlers.put("/findartistsongs", this::findArtistSongs);
        handlers.put("/findsong", this::findSongs);
    }

    private static String getGeniusToken() throws ConfigPropertiesException {
        //use this for config.properties
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
            throw new ConfigPropertiesException("Ошибка: файл config.properties не найден");
        }
        // or this for environment vars
        //return System.getenv("geniusToken");
    }

    public Response requestHandler(String request) {
        int indexOfSpace = request.indexOf(" ");
        String command;
        if (indexOfSpace == -1) {
            command = request;
            request = "";
        } else {
            command = request.substring(0, indexOfSpace);
            request = request.substring(indexOfSpace + 1);
        }
        if (handlers.containsKey(command))
            return handlers.get(command).apply(request);
        else return new Response("Ошибка: неизвестный запрос");
    }

    public Response startMessage(String str) {
        return new Response("""
                Привет, я бот который умеет находить песню по отрывку ее текста. Если нужна помощь, напиши /help
                Введите запрос:\s
                """);
    }

    public Response helpMessage(String str) {
        return new Response("""
                /findsong ТЕКСТ - Поиск песни по отрывку текста
                /findsonglyrics НОМЕР - Поиск текста песни по её номеру
                /findsonginfo НОМЕР - Поиск информации песни по её номеру
                /findartists ИМЯ - Поиск артистов по введёному имени
                /findartistsongs НОМЕР - Поиск песен артиста по его номеру""");
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
        if (foundSongs.isEmpty())
            return new Response("По введённому запросу не было найдено песен");
        else {
            Response response = new Response(foundSongs);
            response.songListToStr();
            return response;
        }
    }

    public Response findArtistSongs(String artistId) {
        List<Song> foundArtistSongs = new ArrayList<>();
        HttpResponse<JsonNode> httpResponse = Unirest.get(
                "https://api.genius.com/artists/" + artistId + "/songs?sort=popularity&per_page=10")
                .header("Authorization","Bearer " + GENIUSTOKEN)
                .asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            JSONArray searchResults = parsedHttpResponse.getJSONObject("response").getJSONArray("songs");
            for (int i = 0; i < searchResults.length(); ++i) {
                String apiPath = searchResults.getJSONObject(i).getString("api_path");
                String fullTitle = searchResults.getJSONObject(i).getString("full_title");
                String primaryArtistApiPath = searchResults.getJSONObject(i)
                        .getJSONObject("primary_artist")
                        .getString("api_path");
                String lyricsPath = searchResults.getJSONObject(i).getString("path");
                Song foundSong = new Song(apiPath, fullTitle, primaryArtistApiPath, lyricsPath);
                foundArtistSongs.add(foundSong);
            }
        }
        if (foundArtistSongs.isEmpty())
            return new Response("По введённому запросу не было найдено песен");
        else {
            Response response = new Response(foundArtistSongs);
            response.songListToStr();
            return response;
        }
    }

    public Response findArtists(String artistName) {
        List<Artist> foundArtists = new ArrayList<>();
        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.genius.com/search?q=" + artistName)
                .header("Authorization","Bearer " + GENIUSTOKEN)
                .asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            JSONArray searchResults = parsedHttpResponse.getJSONObject("response").getJSONArray("hits");
            for (int i = 0; i < searchResults.length(); ++i) {
                artistName = searchResults.getJSONObject(i)
                        .getJSONObject("result")
                        .getJSONObject("primary_artist")
                        .getString("name");
                String artistId = searchResults.getJSONObject(i)
                        .getJSONObject("result")
                        .getJSONObject("primary_artist")
                        .getString("id");
                Artist foundArtist = new Artist(artistName, artistId);
                if (!foundArtists.contains(foundArtist))
                    foundArtists.add(foundArtist);
            }
        }
        if (foundArtists.isEmpty())
            return new Response("По введённому запросу не было найдено артистов");
        else {
            Response response = new Response();
            response.setArtistList(foundArtists);
            response.artistListToStr();
            return response;
        }
    }

    public Response findSongLyrics(String songId) {
        HttpResponse<JsonNode> httpResponse = Unirest.get("https://api.genius.com/songs/" + songId)
                .header("Authorization","Bearer " + GENIUSTOKEN).asJson();
        JSONObject parsedHttpResponse = httpResponse.getBody().getObject();
        if (parsedHttpResponse.getJSONObject("meta").getString("status").equals("200")) {
            String searchResult = parsedHttpResponse.getJSONObject("response").getJSONObject("song")
                    .getString("path");
            Document doc = null;
            try {
                doc = Jsoup.connect("https://genius.com" + searchResult).get();
            } catch (IOException ignore) { }
            Elements lyrics = doc.select("div[data-lyrics-container]");
            Safelist mySafelist = new Safelist();
            mySafelist.addTags("br");
            Cleaner cleaner = new Cleaner(mySafelist);
            Document dirty = Jsoup.parse(lyrics.toString());
            Document clean = cleaner.clean(dirty);
            return new Response(clean.toString().replace("<html>", "")
                    .replace("</html>", "").replace("<head>", "")
                    .replace("</head>", "").replace("<body>", "")
                    .replace("</body>", "").replace("<br>", "").trim());
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
