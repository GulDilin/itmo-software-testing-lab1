package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HumanTest {

    private Human human;
    private Position start;
    private Book book;

    @BeforeEach
    public void setup() {
        start = new Position(5, 10);
        human = new Human(start, "Volodya", 10);
        book = new Book(300, "Interesting book");
    }

    @DisplayName("Can Move test")
    @Test
    public void testCanMove() {
        assertTrue(human.canMove(start), "Move to start");
        Position p = new Position(5, 1);
        assertTrue(human.canMove(p) , "Move to " + p.toString());
        p = new Position(4, 5);
        assertTrue(human.canMove(p), "Move to " + p.toString());
    }

    @DisplayName("Cannot Move test")
    @Test
    public void testCannotMove() {
        assertFalse(human.canMove(new Position(start.getX(), start.getY() - 11)));
        assertFalse(human.canMove(new Position(start.getX() + 40, start.getY())));
    }

    @DisplayName("Do Move test")
    @Test
    public void testDoMove() {
        Position position = new Position(start.getX() + 2, start.getY() + 2);
        int enduranceStart = human.getEndurance();
        human.doMove(position);
        assertEquals(human.getPosition(), position, "Position changed");
        assertTrue(human.getEndurance() < enduranceStart, "Endurance reduced");
    }

    @DisplayName("Read book")
    @Test
    public void testReadBook() {
        assertTrue(human.readBookPages(book, 1, 200), "Read 1 - 200");
        assertFalse(human.readBookPages(book, -1, 20), "Read -1 - 20");
        assertFalse(human.readBookPages(book, 5, 1), "Read 5 - 1");
    }
}
