import java.util.Comparator;

/**
 *
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 *
 * COMPARES TWO DOCBJECTS ON THE BASIS OF YEAR OF PUBLICATION TO PROVIDE AN EFFICIENT WAY TO
 * GET SORTED DATA
 */
public class DocObjectComparator implements Comparator<DocObject> {
    @Override
    public int compare(DocObject d1,DocObject d2){
        Integer y1=0,y2=0;
        for(DocElement d : d1.docElements){
            if(d.tag.equalsIgnoreCase("year"))
                y1 = Integer.valueOf(d.data);
        }
        for(DocElement d : d2.docElements){
            if(d.tag.equalsIgnoreCase("year"))
                y2 = Integer.valueOf(d.data);
        }

        if(y1>y2)
            return 1;
        else
            return -1;
    }
}
