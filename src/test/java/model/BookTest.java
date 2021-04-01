package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTest {
    private Book book;

    @BeforeEach
    public void setup() {
        book = new Book(300, "Interesting book");
    }

    @DisplayName("Negative pages")
    @Test
    public void testnegativePages() {
        assertFalse(book.hasPage(-1));
        assertFalse(book.hasPage(-8));
    }

    @DisplayName("OK pages")
    @Test
    public void testOKPages() {
        assertTrue(book.hasPage(1));
        assertTrue(book.hasPage(150));
        assertTrue(book.hasPage(300));
    }

    @DisplayName("Overflow pages")
    @Test
    public void testOverflowPages() {
        assertFalse(book.hasPage(301));
        assertFalse(book.hasPage(1000));
    }
}
