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
    private static final String GENIUSTOKEN = getGeniusToken();

    private static String getGeniusToken() {
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
        } else if (request.length() > 13 && request.startsWith("/findartists ")) {
            List<Artist> artists = findArtists(request.substring(13)).getArtistList();
            if (artists.isEmpty()) {
                return new Response("По введённому запросу не было найдено артистов");
            } else {
                Response resp = new Response();
                resp.setArtistList(artists);
                resp.artistListToStr();
                return resp;
            }
        } else if ((request.length() > 10 && request.startsWith("/findsong ")) ||
                (request.length() > 17 && request.startsWith("/findartistsongs"))) {
            List<Song> songs;
            if (request.startsWith("/findsong "))
                songs = findSongs(request.substring(10)).getSongList();
            else songs = findArtistSongs(request.substring(17)).getSongList();
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
        return new Response(foundSongs);
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
        return new Response(foundArtistSongs);
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
        Response response = new Response();
        response.setArtistList(foundArtists);
        return response;
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
