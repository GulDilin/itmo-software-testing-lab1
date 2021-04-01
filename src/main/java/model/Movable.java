package model;

public interface Movable {
    boolean doMove(Position p);
    boolean canMove(Position p);
}
