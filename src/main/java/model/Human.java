package model;

public class Human implements Movable {
    private Position position;
    private String name;
    private int endurance;

    public Human(Position position, String name, int endurance) {
        this.position = position;
        this.name = name;
        this.endurance = endurance;
    }

    public boolean readBookPages(Book book, int pageStart, int pageEnd) {
        if (pageStart < 0 || pageEnd < 0) {
            System.out.println("Page numbers need to be positive");
            return false;
        }
        if (pageEnd < pageStart) {
            System.out.println("End page can't be less that start page");
            return false;
        }
        if (book.hasPage(pageStart) && book.hasPage(pageEnd)) {
            System.out.println(this.toString() + " read book " + book.toString() + " from page " + pageStart + " to page " + pageEnd);
            return true;
        } else {
            System.out.println("Incorrect pages range. Book has only " + book.getPagesCount() + " pages");
            return false;
        }
    }

    @Override
    public boolean doMove(Position p) {
        if (this.canMove(p)) {
            this.position = p;
            System.out.println(this.toString() + " change position to " + p.toString());
            return true;
        } else {
            System.out.println(this.toString() + " change position to " + p.toString());
            return false;
        }
    }

    @Override
    public boolean canMove(Position p) {
        return this.endurance > this.position.getDistance(p);
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                '}';
    }
}
