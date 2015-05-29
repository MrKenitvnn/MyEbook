package ebook.ken.objects;

public class Bookmark {

    private int bookId;
    private String componentId;
    private String percent;


    ////////////////////////////////////////////////////////////////////////////////
    // TODO constructor

    public Bookmark() {
    }

    public Bookmark(int epubBook_id, String componentId, String percent) {
        super();
        this.bookId = epubBook_id;
        this.componentId = componentId;
        this.percent = percent;
    }// end-constructor


    ////////////////////////////////////////////////////////////////////////////////
    // TODO getter & setter

    public int getEpubBook_id() {
        return bookId;
    }

    public Bookmark setEpubBook_id(int epubBook_id) {
        this.bookId = epubBook_id;
        return this;
    }

    public String getComponentId() {
        return componentId;
    }

    public Bookmark setComponentId(String componentId) {
        this.componentId = componentId;
        return this;
    }

    public String getPercent() {
        return percent;
    }

    public Bookmark setPercent(String percent) {
        this.percent = percent;
        return this;
    }

}
