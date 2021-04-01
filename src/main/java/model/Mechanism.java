package model;

public class Mechanism implements Movable {
    private Position position;
    private int power;
    private String label;

    public Mechanism(Position position, String label, int power) {
        this.position = position;
        this.label = label;
        this.power = power;
    }

    public void charge(int time) {
        System.out.println(this.toString() + " charged for " + time + "s");
        this.power += time;
    }

    @Override
    public boolean doMove(Position p) {
        if (this.canMove(p)) {
            this.position = p;
            System.out.println(this.toString() + " moves to " + p.toString());
            return true;
        } else {
            System.out.println(this.toString() + " cant moves to " + p.toString());
            return false;
        }
    }

    @Override
    public boolean canMove(Position p) {
        return this.power > this.position.getDistance(p);
    }

    @Override
    public String toString() {
        return "Mechanism{" +
                "label='" + label + '\'' +
                '}';
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
