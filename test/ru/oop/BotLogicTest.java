package ru.oop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTest {
    BotLogic botLogic = new BotLogic();

    @Test
    public void testCorrectFindSongs() {
        Song foundSong = botLogic.findSongs("Humble").getSongList().get(0);
        Song testSong = new Song("/songs/3039923",
                "HUMBLE. by Kendrick Lamar",
                "/artists/1421", "/Kendrick-lamar-humble-lyrics");
        assertEquals(testSong, foundSong);
    }
    @Test
    public void testEmptyRequest() {
        assertEquals(botLogic.requestHandler("").getResponseString(), "Ошибка: неизвестный запрос");
    }

    @Test
    public void testHelpMessage() {
        assertTrue(botLogic.requestHandler("/help").getResponseString()
                .startsWith("/findsong ТЕКСТ - Поиск песни по отрывку текста"));
    }

    @Test
    public void testSongsNotFound() {
        assertEquals(botLogic.requestHandler("/findsong dasddasdwq").getResponseString(),
                "По введённому запросу не было найдено песен");
    }

    @Test
    public void testSongsFound() {
        assertTrue(botLogic.requestHandler("/findsong Humble").getResponseString()
                .startsWith("Список найденных песен:"));
    }

    @Test
    public void testArtistSongsFound() {
        assertTrue(botLogic.requestHandler("/findartistsongs 1421").getResponseString()
                .startsWith("Список найденных песен:"));
    }

    @Test
    public void testArtistsFound() {
        assertTrue(botLogic.requestHandler("/findartists Kendrick Lamar").getResponseString()
                .startsWith("Список найденных артистов:"));
    }

    @Test
    public void testUnknownRequest() {
        assertEquals(botLogic.requestHandler("sda").getResponseString(), "Ошибка: неизвестный запрос");
    }

    @Test
    public void testFindSongLyrics() {
        assertTrue(botLogic.requestHandler("/findsonglyrics 3039923").getResponseString().
                replaceAll("\\s","").startsWith("[Intro]Nobodyprayforme"));
    }

    @Test
    public void testFindSongInfo() {
        assertTrue(botLogic.requestHandler("/findsonginfo 3039923").getResponseString()
                .startsWith("Полное название: HUMBLE. by Kendrick Lamar"));
    }
}