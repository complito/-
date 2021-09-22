package ru.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessingQueryTest {
    @Test
    void testEmptyQuery() {
        assertEquals(ProcessingQuery.executeQuery(""), ExecuteQueryStatus.QueryIsEmpty);
    }

    @Test
    void testHelpMessage() {
        assertEquals(ProcessingQuery.executeQuery("\\help"), ExecuteQueryStatus.HelpMessage);
    }

    @Test
    void testSongsNotFound() {
        assertEquals(ProcessingQuery.executeQuery("\\findsong dasddasdwq"), ExecuteQueryStatus.SongsNotFound);
    }

    @Test
    void testSongsFound() {
        assertEquals(ProcessingQuery.executeQuery("\\findsong Humble"), ExecuteQueryStatus.SongsFound);
    }

    @Test
    void testUnknownQuery() {
        assertEquals(ProcessingQuery.executeQuery("sda"), ExecuteQueryStatus.UnknownQuery);
    }
}