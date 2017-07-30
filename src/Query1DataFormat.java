
/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
/*
 * PROVIDES THE DATA IN THE FORM OF A ROW OF RESULT TABLE
 */

public class Query1DataFormat {
    private String Authors;
    private String Title;
    private String Pages;
    private String Year;
    private String Volume;
    private String URL;

    Query1DataFormat(String Authors, String Title, String Pages, String Year, String Volume, String URL) {
        this.Authors = Authors;
        this.Title = Title;
        this.Pages = Pages;
        this.Year = Year;
        this.Volume = Volume;
        this.URL = URL;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthors() {
        return Authors;
    }

    public String getPages() {
        return Pages;
    }

    public String getVolume() {
        return Volume;
    }

    public String getURL() {
        return URL;
    }

    public String getYear() {
        return Year;
    }
}