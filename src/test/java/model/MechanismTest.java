package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MechanismTest {

    private Mechanism mechanism;
    private Position start;

    @BeforeEach
    public void setup() {
        start = new Position(5, 10);
        mechanism = new Mechanism(start, "robot", 10);
    }

    @DisplayName("Can Move test")
    @Test
    public void testCanMove() {
        assertTrue(mechanism.canMove(start), "Move to start");
        Position p = new Position(5, 1);
        assertTrue(mechanism.canMove(p) , "Move to " + p.toString());
        p = new Position(4, 5);
        assertTrue(mechanism.canMove(p), "Move to " + p.toString());
    }

    @DisplayName("Cannot Move test")
    @Test
    public void testCannotMove() {
        assertFalse(mechanism.canMove(new Position(start.getX(), start.getY() - 11)));
        assertFalse(mechanism.canMove(new Position(start.getX() + 40, start.getY())));
    }

    @DisplayName("Do Move test")
    @Test
    public void testDoMove() {
        Position position = new Position(start.getX() + 2, start.getY() + 2);
        int powerStart = mechanism.getPower();
        mechanism.doMove(position);
        assertEquals(mechanism.getPosition(), position, "Position changed");
        assertTrue(mechanism.getPower() < powerStart, "Reduce power");
    }
}
