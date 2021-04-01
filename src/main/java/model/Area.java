package model;

import java.util.ArrayList;

public class Area {
    private ArrayList<Movable> movables;
    private int maxMovables;


    public Area(int maxMovables) {
        this.maxMovables = maxMovables;
        this.movables = new ArrayList<>();
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
