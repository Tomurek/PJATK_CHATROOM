package org.example.pjatk_chatroom.service;

import org.example.pjatk_chatroom.domain.Message;
import org.example.pjatk_chatroom.domain.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MessageService")
class MessageServiceTest {

    private MessageService service;

    @BeforeEach
    void setUp() {
        service = new MessageService();
    }

    @Test
    @DisplayName("addMessageWithNormalization ignoruje null")
    void add_ignoresNull() {
        assertDoesNotThrow(() -> service.addMessageWithNormalization(null));
        assertTrue(service.lastForActiveUser(10, "any").isEmpty());
    }

    @Test
    @DisplayName("author == null → Anon")
    void nullAuthor_becomesAnon() {
        service.addMessageWithNormalization(new MessageDto(null, "x"));
        assertEquals("Anon", service.lastForActiveUser(1, "any").get(0).author());
    }

    @Test
    @DisplayName("author jest pusty/whitespace → Anon")
    void blankAuthor_becomesAnon() {
        service.addMessageWithNormalization(new MessageDto("   ", "x"));
        assertEquals("Anon", service.lastForActiveUser(1, "any").get(0).author());
    }

    @Test
    @DisplayName("author jest trimowany")
    void authorIsTrimmed() {
        service.addMessageWithNormalization(new MessageDto("  Alice  ", "x"));
        assertEquals("Alice", service.lastForActiveUser(1, "Alice").get(0).author());
    }

    @Test
    @DisplayName("content == null → pusty string")
    void nullContent_becomesEmptyString() {
        service.addMessageWithNormalization(new MessageDto("A", null));
        assertEquals("", service.lastForActiveUser(1, "A").get(0).content());
    }

    @Test
    @DisplayName("content jest trimowany")
    void contentIsTrimmed() {
        service.addMessageWithNormalization(new MessageDto("A", "  hello  "));
        assertEquals("hello", service.lastForActiveUser(1, "A").get(0).content());
    }

    @ParameterizedTest(name = "limit={0}")
    @ValueSource(ints = {0, 1, 3, 5, 10})
    @DisplayName("lastForActiveUser zwraca prawidłowy ogon dla różnych limitów")
    void returnsTailForVariousLimits(int limit) {
        IntStream.range(0, 10)
                .forEach(i -> service.addMessageWithNormalization(new MessageDto("A", "m" + i)));

        var out = service.lastForActiveUser(limit, "A");

        var expected = IntStream.range(10 - Math.min(limit, 10), 10)
                .mapToObj(i -> "m" + i)
                .toList();

        assertEquals(expected, out.stream().map(Message::content).toList());
        assertTrue(out.stream().allMatch(Message::mine));
    }

    @ParameterizedTest(name = "excess={0} (ile ponad MAX)")
    @ValueSource(ints = {1, 5, 42})
    @DisplayName("po przekroczeniu MAX najstarsze wpisy są usuwane")
    void historyEviction(int excess) {
        final int max = 200;
        final int total = max + excess;

        IntStream.range(0, total)
                .forEach(i -> service.addMessageWithNormalization(new MessageDto("u" + i, "msg-" + i)));

        var out = service.lastForActiveUser(9999, "nobody");

        assertEquals(max, out.size());
        assertEquals("msg-" + excess, out.get(0).content());                      // pierwsza zachowana
        assertEquals("msg-" + (total - 1), out.get(out.size() - 1).content());    // ostatnia
    }


    @Test
    @DisplayName("mine jest wyznaczane case-insensitive dla autora 'Alice'")
    void mineCaseInsensitiveForAlice() {
        service.addMessageWithNormalization(new MessageDto("Alice", "hello"));
        service.addMessageWithNormalization(new MessageDto("bob", "hi"));

        var out = service.lastForActiveUser(10, "ALICE");

        assertTrue(out.get(0).mine());
        assertFalse(out.get(1).mine());
    }

    @Test
    @DisplayName("mine jest wyznaczane case-insensitive dla autora 'bob'")
    void mineCaseInsensitiveForBob() {
        service.addMessageWithNormalization(new MessageDto("Alice", "hello"));
        service.addMessageWithNormalization(new MessageDto("bob", "hi"));

        var out = service.lastForActiveUser(10, "BoB");

        assertFalse(out.get(0).mine());
        assertTrue(out.get(1).mine());
    }


    @Test
    @DisplayName("limit ujemny → pusty wynik")
    void negativeLimit_emptyList() {
        service.addMessageWithNormalization(new MessageDto("A", "x"));
        assertTrue(service.lastForActiveUser(-5, "A").isEmpty());
    }
}
