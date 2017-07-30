import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 *
 * STORES DATA OF A PUBLICATION RECORD e.g <ARTICLE>.....</ARTICLE>
 */
public class DocObject extends Observable {
    public String firstTag;
    public double relevance;
    public ArrayList<DocElement> docElements;

    public DocObject(){
        docElements = new ArrayList<>();
    }

    public void informChange(){
        setChanged();
    }

    public static DocObject cloneDocObject(DocObject docObject){
        DocObject clone = new DocObject();
        clone.firstTag = new String(docObject.firstTag);
        clone.relevance = docObject.relevance;
        for(DocElement d : docObject.docElements){
            clone.docElements.add(new DocElement(new String(d.tag),new String(d.data)));
        }
        return clone;
    }
}
