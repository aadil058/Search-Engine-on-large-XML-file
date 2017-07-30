import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */

public class XMLReader extends Observable implements Observer {
    private DocObject docObject;
    private XMLHandler handler;
    private ArrayList<ArrayList<String>> AuthorList;
    private ArrayList<String> search_author_list;
    private String search_author;
    private static XMLReader reader = null;
    public int parsing_count = 0;

    private boolean isQuery2DataAvailable;
    private HashMap<String, Integer> map;
    private static String Mode;
    private Integer k;

    public boolean relevance = false;

    private XMLReader() {
        docObject = new DocObject();
        docObject.addObserver(this);
        AuthorList = new ArrayList<>();
        isQuery2DataAvailable = false;
        Mode = null;
        map = new HashMap<>();
    }

    public static XMLReader getReader() {
        if(reader == null) {
            reader = new XMLReader();
            return reader;
        }
        return reader;
    }

    public void searchParams(String author_name){
        Mode = "QUERY1";
        search_author = author_name;
        int authorlistsize = AuthorList.size();

        for(int i=0;i<authorlistsize;++i){

            int authornames = AuthorList.get(i).size();

            for(int j=0;j<authornames;++j){
                if(search_author.equalsIgnoreCase(AuthorList.get(i).get(j))){
                    search_author_list = AuthorList.get(i);
                    return;
                }
            }
        }
    }

    public boolean search_in_list(String name){
        for(String st : search_author_list)
            if(st.equalsIgnoreCase(name))
                return true;
        return false;
    }

    public void parseXml(){
        try {
            parsing_count++;
            handler = new XMLHandler(docObject);

            File inputFile = new File("dblp.xml");
            InputStream inputStream= new FileInputStream(inputFile);
            Reader reader = new InputStreamReader(inputStream, "ISO-8859-1");
            org.xml.sax.InputSource is = new org.xml.sax.InputSource(reader);
            is.setEncoding("ISO-8859-1");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProcessQuery2(Integer number_of_Publications) {
        Mode = "QUERY2";
        k = number_of_Publications;

        if(!isQuery2DataAvailable)
            parseXml();

        isQuery2DataAvailable = true;
    }

    public void ProcessQuery2Handler() {
        int size = AuthorList.size();
        for (int i = 0; i < size; ++i) {
            int names = AuthorList.get(i).size();
            int sum = 0;

            for (int j = 0; j < names; ++j)
                sum += map.get(AuthorList.get(i).get(j));

            if(sum >= k) {
                setChanged();
                notifyObservers(AuthorList.get(i).get(0));
                System.out.println(AuthorList.get(i).get(0) + " - " + sum);
            }
        }
    }

    public void update(Observable obs,Object obj) {
        if(Mode != null && Mode.equals("QUERY2")) {
            if(!docObject.firstTag.equalsIgnoreCase("www")) {
                ArrayList<DocElement> docElements = docObject.docElements;
                for(int i = 0; i < docElements.size(); ++i) {
                    if(docElements.get(i).tag.equalsIgnoreCase("author")) {
                        map.putIfAbsent(docElements.get(i).data, 0);
                        map.put(docElements.get(i).data, map.get(docElements.get(i).data) + 1);
                    }
                }
            }
        }
        else {
            if (parsing_count == 1) {
                if (docObject.firstTag.equalsIgnoreCase("www")) {
                    ArrayList<String> author_alias = new ArrayList<>();
                    ArrayList<DocElement> docElements = docObject.docElements;

                    for (int i = 0; i < docElements.size(); ++i) {
                        if (docElements.get(i).tag.equalsIgnoreCase("author")) {
                            author_alias.add(docElements.get(i).data);
                            map.put(docElements.get(i).data, 0);
                        }
                    }
                    AuthorList.add(author_alias);
                }
            }
            else if (parsing_count > 1 && !relevance) {
                ArrayList<DocElement> docElements = docObject.docElements;
                for(int i = 0; i < docElements.size(); ++i) {
                    if (!docObject.firstTag.equalsIgnoreCase("www") && docElements.get(i).tag.equalsIgnoreCase("author") && search_in_list(docElements.get(i).data)) {
                        setChanged();
                        notifyObservers(docObject);
                        break;
                    }
                }
            }else if(parsing_count > 1 && relevance){
                setChanged();
                notifyObservers(docObject);
            }
        }
    }

    public DocObject getDocObject(){
        return docObject;
    }
    /*

    public DocObject docObject;
    public XMLHandler handler;
    public ArrayList<ArrayList<String>> AuthorList;
    public ArrayList<String> search_author_list;
    public String search_author;
    public static boolean created = false;
    public int parsing_count = 0;
    public boolean relevance = false;

    private XMLReader(){
        docObject = new DocObject();
        docObject.addObserver(this);
        AuthorList = new ArrayList<>();

    }

    public static XMLReader getReader(){
        if(!created){
            created = true;
            return new XMLReader();
        }else
            return null;
    }

    public void searchParams(String author_name){
        search_author = author_name;
        int authorlistsize = AuthorList.size();

        for(int i=0;i<authorlistsize;++i){

            int authornames = AuthorList.get(i).size();

            for(int j=0;j<authornames;++j){
                if(search_author.equalsIgnoreCase(AuthorList.get(i).get(j))){
                    search_author_list = AuthorList.get(i);
                    return;
                }
            }
        }
    }

    public boolean search_in_list(String name){
        for(String st : search_author_list){
            if(st.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public void parseXml(){
        try {
            parsing_count++;
            handler = new XMLHandler(docObject);
            File inputFile = new File("dblp.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(inputFile, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DocObject getDocObject(){
        return docObject;
    }

    public void update(Observable obs,Object obj){
        if(!(obj instanceof Integer)){
            if(parsing_count==1){
                if(docObject.firstTag.equalsIgnoreCase("www")){
                    ArrayList<String> author_alias = new ArrayList<>();
                    ArrayList<DocElement> docElements = docObject.docElements;

                    for(int i=0;i<docElements.size();++i){
                        if(docElements.get(i).tag.equalsIgnoreCase("author")){
                            //System.out.print(docElements.get(i).data + ", ");
                            author_alias.add(docElements.get(i).data);
                        }
                    }
                    //System.out.println();
                    AuthorList.add(author_alias);
                }
            }else if(parsing_count>1 && !relevance){
                ArrayList<DocElement> docElements = docObject.docElements;

                for(int i=0;i<docElements.size();++i){
                    if(docElements.get(i).tag.equalsIgnoreCase("author") && search_in_list(docElements.get(i).data)){
                        setChanged();
                        notifyObservers(docObject);
                        break;
                    }
                }

            }else if(parsing_count>1 && relevance){
                setChanged();
                notifyObservers(docObject);
            }
        }else{
            setChanged();
            notifyObservers(new Integer(10));
        }



    }
    */
}
