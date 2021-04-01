package model;

import java.util.ArrayList;

public class Area {
    private ArrayList<Movable> movables;
    private int sizeX;
    private int sizeY;
    private int maxMovables;


    public Area(int sizeX, int sizeY, int maxMovables) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.maxMovables = maxMovables;
    }

    public boolean addMovable(Movable m) {
        if (movables.size() == maxMovables) {
            System.out.println("Movables cant be overflowed");
            return false;
        }
        if (this.movables.add(m)) {
            System.out.println(m.toString() + " was added to " + this.toString());
            return true;
        }
        return false;
    }
}
