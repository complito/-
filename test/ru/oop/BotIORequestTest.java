package ru.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotIORequestTest {
    BotIO botIO = new BotIO();

    @Test
    void testEmptyRequest() {
        assertEquals(botIO.Request("")[0], "Ошибка: запрос пустой");
    }

    @Test
    void testHelpMessage() {
        assertEquals(botIO.Request("\\help")[0], "\\findsong - Поиск песни по отрывку текста");
    }

    @Test
    void testSongsNotFound() {
        assertEquals(botIO.Request("\\findsong dasddasdwq")[0], "По введённому запросу не было найдено песен");
    }

    @Test
    void testSongsFound() {
        assertEquals(botIO.Request("\\findsong Humble")[0], "Список найденных песен:");
    }

    @Test
    void testUnknownRequest() {
        assertEquals(botIO.Request("sda")[0], "Ошибка: неизвестный запрос");
    }
}