package model;

public class Book {
    private int pagesCount;
    private String title;

    public Book(int pagesCount, String title) {
        this.pagesCount = pagesCount;
        this.title = title;
    }

    public boolean hasPage(int pageNumber) {
        return pageNumber < pagesCount;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public String getTitle() {
        return title;
    }
}
