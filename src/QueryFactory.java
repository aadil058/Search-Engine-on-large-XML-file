import javax.swing.*;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */

public class QueryFactory {

    public static JPanel getInstance(String identifier) {
        if(identifier.equals("Query 1")) {
            return new Query1();
        }

        else if(identifier.equals("Query 2")) {
            return new Query2();
        }

        else if(identifier.equals("Query 3")) {
            return new Query3();
        }

        return null;
    }
}