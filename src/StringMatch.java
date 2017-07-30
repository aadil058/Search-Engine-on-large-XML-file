import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
public class StringMatch {

    public static double tag_match(String tag,String title){

        String tagArray[] = tag.split(" ");
        String titleArray[] = title.split(" ");
        int match = 0;

        ArrayList<Integer> used = new ArrayList<>();

        for(int i=0;i<tagArray.length;++i){
            for(int j=0;j<titleArray.length;++j){
                if(tagArray[i].equalsIgnoreCase(titleArray[j]) && !used.contains(j)){
                    match++;
                    used.add(j);
                    break;
                }
            }
        }

        return (double)match/(double)tagArray.length;

    }
}
