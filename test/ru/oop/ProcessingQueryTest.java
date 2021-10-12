package ru.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotIORequestTest {
    BotIO botIO = new BotIO();

    @Test
    void testEmptyRequest() {
        assertEquals(botIO.Request(""), new String[] {"Ошибка: запрос пустой"});
    }

    @Test
    void testHelpMessage() {
        assertEquals(botIO.Request("\\help"), new String[] {"Привет, я бот который умеет находить песню по отрывку ее текста. Если нужна помощь, напиши \\help"});
    }

    @Test
    void testSongsNotFound() {
        assertEquals(botIO.Request("\\findsong dasddasdwq"), new String[] {"По введённому запросу не было найдено песен"});
    }

    @Test
    void testSongsFound() {
        String[] botRequest = botIO.Request("\\findsong Humble");
        assertEquals(botRequest[0], "Список найденных песен:");
    }

    @Test
    void testUnknownRequest() {
        assertEquals(botIO.Request("sda"), new String[] {"Ошибка: неизвестный запрос"});
    }
}