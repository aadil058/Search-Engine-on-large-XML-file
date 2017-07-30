import java.util.Comparator;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
public class DocObjectRelevanceComparator implements Comparator<DocObject> {
    private String title;

    @Override
    public int compare(DocObject d1, DocObject d2){

        if(d1.relevance>d2.relevance)
            return -1;
        else
            return 1;
    }
}
