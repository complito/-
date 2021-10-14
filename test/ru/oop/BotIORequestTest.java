package ru.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotIORequestTest {
    BotIO botIO = new BotIO();

    @Test
    void testEmptyRequest() {
        assertEquals(botIO.sendRequest("").getResponseString(), "Ошибка: запрос пустой");
    }

    @Test
    void testHelpMessage() {
        assertEquals(botIO.sendRequest("\\help").getResponseString(),
                "\\findsong - Поиск песни по отрывку текста");
    }

    @Test
    void testSongsNotFound() {
        assertEquals(botIO.sendRequest("\\findsong dasddasdwq").getResponseString(),
                "По введённому запросу не было найдено песен");
    }

    @Test
    void testSongsFound() {
        Response botResponse = botIO.sendRequest("\\findsong Humble");
        assertTrue(botResponse.getResponseString().startsWith("Список найденных песен:"));
    }

    @Test
    void testUnknownRequest() {
        assertEquals(botIO.sendRequest("sda").getResponseString(), "Ошибка: неизвестный запрос");
    }
}