package ru.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotIORequestTest {
    BotIO botIO = new BotIO();

    @Test
    void testEmptyRequest() {
        assertEquals(botIO.Request("").responseString, "Ошибка: запрос пустой");
    }

    @Test
    void testHelpMessage() {
        assertEquals(botIO.Request("\\help").responseString, "\\findsong - Поиск песни по отрывку текста");
    }

    @Test
    void testSongsNotFound() {
        assertEquals(botIO.Request("\\findsong dasddasdwq").responseString, "По введённому запросу не было найдено песен");
    }

    @Test
    void testSongsFound() {
        Response botResponse = botIO.Request("\\findsong Humble");
        assertTrue(botResponse.responseString.startsWith("Список найденных песен:"));
    }

    @Test
    void testUnknownRequest() {
        assertEquals(botIO.Request("sda").responseString, "Ошибка: неизвестный запрос");
    }
}