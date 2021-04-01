package model;

public class Book {
    private int pagesCount;
    private String title;

    public Book(int pagesCount, String title) {
        this.pagesCount = pagesCount;
        this.title = title;
    }

    public boolean hasPage(int pageNumber) {
        return 0 < pageNumber && pageNumber <= pagesCount;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "pagesCount=" + pagesCount +
                ", title='" + title + '\'' +
                '}';
    }
}
