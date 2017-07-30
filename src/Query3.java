import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */

public class Query3 extends JPanel implements Observer, ActionListener {

    private JTextField year;
    private JTextField author1;
    private JTextField author2;
    private JTextField author3;
    private JTextField author4;
    private JTextField author5;
    private JButton Search;
    private JButton Reset;
    private XMLReader reader;

    private HashMap<Integer, Integer> predictionMap;
    private HashMap<String, Integer> data;
    private ResultTable resultTable;

    Query3() {
        setLayout(null);

        JLabel label1 = new JLabel("Year");
        label1.setBounds(20, 40, 100, 50);
        add(label1);

        year = new JTextField();
        year.setBounds(150, 50, 80, 25);
        add(year);

        JLabel label2 = new JLabel("Author 1 Name");
        label2.setBounds(20, 70, 100, 50);
        add(label2);

        author1 = new JTextField();
        author1.setBounds(150, 80, 140, 25);
        add(author1);

        JLabel label3 = new JLabel("Author 2 Name");
        label3.setBounds(20, 100, 100, 50);
        add(label3);

        author2 = new JTextField();
        author2.setBounds(150, 110, 140, 25);
        add(author2);

        JLabel label4 = new JLabel("Author 3 Name");
        label4.setBounds(20, 130, 100, 50);
        add(label4);

        author3 = new JTextField();
        author3.setBounds(150, 140, 140, 25);
        add(author3);

        JLabel label5 = new JLabel("Author 4 Name");
        label5.setBounds(20, 160, 100, 50);
        add(label5);

        author4 = new JTextField();
        author4.setBounds(150, 170, 140, 25);
        add(author4);

        JLabel label6 = new JLabel("Author 5 Name");
        label6.setBounds(20, 190, 100, 50);
        add(label6);

        author5 = new JTextField();
        author5.setBounds(150, 200, 140, 25);
        add(author5);

        Search = new JButton("Search");
        Search.setBounds(25, 250, 100, 40);
        Search.addActionListener(this);
        add(Search);

        Reset = new JButton("Reset");
        Reset.setBounds(145, 250, 100, 40);
        Reset.addActionListener(this);
        add(Reset);

        data = new HashMap<>();
        resultTable = ResultTable.getInstance();
        resultTable.SetQueryMode("QUERY3");
        resultTable.SetupTable();
        resultTable.setVisible(true);
    }

    public void setXMLReader(XMLReader reader) {
        this.reader = reader;
        this.reader.deleteObservers();
        this.reader.addObserver(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Search")) {
            predict();
        } else if(e.getActionCommand().equals("Reset")) {
            data = new HashMap<>();
            author1.setText("");author2.setText("");author3.setText("");author4.setText("");author5.setText("");year.setText("");
            resultTable.ResetQuery();
        }
    }

    @Override
    public void update(Observable o, Object arg){
        if(!(arg instanceof Integer)) {
            DocObject docObject = (DocObject)arg;
            for(DocElement d : docObject.docElements){
                if(d.tag.equalsIgnoreCase("year") && (Integer.valueOf(d.data)) <= Integer.valueOf(year.getText())) {
                    if(predictionMap.containsKey(Integer.valueOf(d.data))){
                        predictionMap.put(Integer.valueOf(d.data), (predictionMap.get(Integer.valueOf(d.data))) + 1);
                        break;
                    } else {
                        predictionMap.put(Integer.valueOf(d.data),1);
                        break;
                    }
                }
            }
        }
    }

    public void predict(){

        //author1
        predictionMap = new HashMap<>();
        reader.searchParams(author1.getText());
        reader.parseXml();
        data.put(author1.getText(), actual_predictor(predictionMap));

        //author2
        predictionMap = new HashMap<>();
        reader.searchParams(author2.getText());
        reader.parseXml();
        data.put(author2.getText(), actual_predictor(predictionMap));

        //author3
        predictionMap = new HashMap<>();
        reader.searchParams(author3.getText());
        reader.parseXml();
        data.put(author3.getText(), actual_predictor(predictionMap));

        //author4
        predictionMap = new HashMap<>();
        reader.searchParams(author4.getText());
        reader.parseXml();
        data.put(author4.getText(), actual_predictor(predictionMap));

        //author5
        predictionMap = new HashMap<>();
        reader.searchParams(author5.getText());
        reader.parseXml();
        data.put(author5.getText(), actual_predictor(predictionMap));


        resultTable.setQuery3Data(data);
    }

    public Integer actual_predictor(HashMap<Integer,Integer> predictionMap){
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

        return ((Double) (a + (b * (maxx + 1)))).intValue();
    }
}