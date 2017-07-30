import java.util.*;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
public class Tester implements Observer {
    public static int count = 1;
    public static XMLReader reader;
    private PriorityQueue<DocObject> search_results;

    DocObjectComparator docObjectComparator;
    HashMap<Integer,Integer> predictionMap;
    //DocObjectRelevanceComparator docObjectRelevanceComparator;

    public Tester(){
        docObjectComparator = new DocObjectComparator();
        search_results = new PriorityQueue<>(1,docObjectComparator);
        predictionMap = new HashMap<>();
        //docObjectRelevanceComparator = new DocObjectRelevanceComparator();
        //search_results = new PriorityQueue<>(1,docObjectRelevanceComparator);
    }
    /*
    public static void main(String args[]){
        Tester test = new Tester();
        System.setProperty("jdk.xml.entityExpansionLimit", "0");
        reader = XMLReader.getReader();
        reader.parseXml();
        reader.addObserver(test);
        reader.relevance = false;
        reader.searchParams("Ajith Abraham");
        reader.parseXml();

        test.display();
        test.predict();
    }
    */
    public void update(Observable ob, Object obj){

        DocObject docObject = reader.getDocObject();
        for(DocElement d : docObject.docElements){
            if(d.tag.equalsIgnoreCase("year") && !d.data.equalsIgnoreCase("2016")){
                if(predictionMap.containsKey(Integer.valueOf(d.data))){
                    predictionMap.put(Integer.valueOf(d.data),(predictionMap.get(Integer.valueOf(d.data)))+1);
                    break;
                }else{
                    predictionMap.put(Integer.valueOf(d.data),1);
                    break;
                }

            }
        }
        search_results.add(DocObject.cloneDocObject(docObject));
        /*
        for(DocElement d : docObject.docElements){
            if(d.tag.equalsIgnoreCase("title")){
                double sim = StringMatch.tag_match(d.data, "Convergence properties of recursive rank-order filter and neural network");
                docObject.relevance = sim;
            }
        }

        if(docObject.relevance>0.4){
            search_results.add(DocObject.cloneDocObject(docObject));
            System.out.println(docObject.relevance);
        }
        */
    }


    public void display(){
        int i=1;
        while(!search_results.isEmpty()){
            for(DocElement d : search_results.peek().docElements){
                if(d.tag.equalsIgnoreCase("title")){
                    System.out.println(i + " " + d.data + " " + search_results.peek().relevance);
                    i++;
                    break;
                }

            }
            search_results.remove();
        }
    }

    public void predict(){
        double x=0.0,y=0.0,a=0.0,b=0.0,d = 0.0;
        int maxx=0,minx=2000;
        for(Map.Entry m : predictionMap.entrySet()){
            if(maxx<(Integer)m.getKey())
                maxx = (Integer)m.getKey();
            if(minx>(Integer)m.getKey())
                minx = (Integer)m.getKey();
            System.out.println(m.getKey() + " : " + m.getValue());
            x += (double)(Integer)m.getKey();
            y += (double)(Integer)m.getValue();


        }
        x = x/(double)predictionMap.size();
        y = y/(double)predictionMap.size();

        for(Map.Entry m : predictionMap.entrySet()){
            b += ((double)(Integer)m.getKey() - x)*((double)(Integer)m.getValue() - y);
            d += ((double)(Integer)m.getKey() - x)*((double)(Integer)m.getKey() - x);
        }
        b = b/d;

        a = y - (b*x);

        System.out.println((a + (b*(maxx + 1))));
    }
}
