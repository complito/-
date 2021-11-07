package ru.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotLogicTest {
    BotLogic botLogic = new BotLogic();

    @Test
    void testCorrectFindSongs() {
        Song foundSong = botLogic.findSongs("Humble").getResponseList().get(0);
        Song testSong = new Song("/songs/3039923",
                "HUMBLE. by Kendrick Lamar",
                "/artists/1421");
        assertEquals(testSong, foundSong);
    }
    @Test
    void testEmptyRequest() {
        assertEquals(botLogic.requestHandler("").getResponseString(), "Ошибка: запрос пустой");
    }

    @Test
    void testHelpMessage() {
        assertEquals(botLogic.requestHandler("/help").getResponseString(),
                "/findsong - Поиск песни по отрывку текста");
    }

    @Test
    void testSongsNotFound() {
        assertEquals(botLogic.requestHandler("/findsong dasddasdwq").getResponseString(),
                "По введённому запросу не было найдено песен");
    }

    @Test
    void testSongsFound() {
        Response botResponse = botLogic.requestHandler("/findsong Humble");
        assertTrue(botResponse.getResponseString().startsWith("Список найденных песен:"));
    }

    @Test
    void testUnknownRequest() {
        assertEquals(botLogic.requestHandler("sda").getResponseString(), "Ошибка: неизвестный запрос");
    }
}