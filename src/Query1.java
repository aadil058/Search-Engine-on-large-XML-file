import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
public class Query1 extends JPanel implements ActionListener, Observer {
    private JComboBox searchBy;
    private JTextField name;
    private JTextField since;
    private JTextField start;
    private JTextField end;
    private JCheckBox sortByYear;
    private JCheckBox sortByRelevance;
    private JButton Search;
    private JButton Reset;
    private ButtonGroup btngrp;

    private ResultTable resultTable;
    private XMLReader reader;
    private PriorityQueue<DocObject> search_results;
    private DocObjectComparator docObjectComparator;
    private  DocObjectRelevanceComparator docObjectRelevanceComparator;

    private ArrayList<Query1DataFormat> data;

    Query1() {
        setLayout(null);

        String[] options = {"Search By", "Author Name", "Title Tags"};
        searchBy = new JComboBox(options);

        searchBy.setSelectedIndex(0);
        searchBy.setBounds(80, 20, 150, 25);
        searchBy.addActionListener(this);
        add(searchBy);

        JLabel nameLabel = new JLabel("Name/Title tags");
        nameLabel.setBounds(10, 60, 120, 50);
        add(nameLabel);

        name = new JTextField();
        name.setBounds(125, 75, 150, 25);
        add(name);

        JLabel sinceLabel = new JLabel("Since Year ");
        sinceLabel.setBounds(10, 100, 100, 50);
        add(sinceLabel);

        since = new JTextField();
        since.setBounds(125, 115, 75, 25);
        add(since);

        JLabel rangeLabel = new JLabel("Custom Range ");
        rangeLabel.setBounds(10, 140, 120, 50);
        add(rangeLabel);

        start = new JTextField();
        start.setBounds(125, 155, 65, 25);
        add(start);

        JLabel separator = new JLabel("-");
        separator.setBounds(200, 155, 65, 25);
        add(separator);

        end = new JTextField();
        end.setBounds(215, 155, 65, 25);
        add(end);

        btngrp = new ButtonGroup();

        sortByYear = new JCheckBox("Sort By Year");
        sortByYear.setBounds(20, 190, 200, 50);
        add(sortByYear);

        sortByRelevance = new JCheckBox("Sort By Relevance");
        sortByRelevance.setBounds(20, 240, 200, 50);
        add(sortByRelevance);

        btngrp.add(sortByYear);
        btngrp.add(sortByRelevance);

        Search = new JButton("Search");
        Search.setBounds(25, 300, 100, 40);
        Search.addActionListener(this);
        add(Search);

        Reset = new JButton("Reset");
        Reset.setBounds(145, 300, 100, 40);
        Reset.addActionListener(this);
        add(Reset);

        docObjectComparator = new DocObjectComparator();
        docObjectRelevanceComparator = new DocObjectRelevanceComparator();
        resultTable = ResultTable.getInstance();
        resultTable.SetQueryMode("QUERY1");
        resultTable.SetupTable();
        resultTable.setVisible(true);

        data = new ArrayList<>();
    }

    public void setXMLReader(XMLReader reader) {
        this.reader = reader;
        this.reader.deleteObservers();
        reader.addObserver(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Search")){
            if(sortByYear.isSelected()) {
                searchByYear();
            }else if(sortByRelevance.isSelected()){
                searchByRelevance();
            }
        }
        else if(e.getActionCommand().equalsIgnoreCase("Reset")) {
            data = new ArrayList<>();
            resultTable.ResetQuery();
            name.setText("");
            since.setText("");
            start.setText("");
            end.setText("");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!(arg instanceof Integer)) {
            DocObject resultObject = (DocObject) arg;
            if(!reader.relevance)
                search_results.add(DocObject.cloneDocObject(resultObject));
            else{
                if(searchBy.getSelectedItem().toString().equalsIgnoreCase("Title Tags")){
                    for(DocElement d : resultObject.docElements){
                        if(d.tag.equalsIgnoreCase("title")){
                            resultObject.relevance=StringMatch.tag_match(name.getText().toString(),d.data);
                            if(resultObject.relevance>0.4){
                                search_results.add(DocObject.cloneDocObject(resultObject));
                            }

                        }
                    }
                }else if(searchBy.getSelectedItem().toString().equalsIgnoreCase("Author name")){
                    for(DocElement d : resultObject.docElements){
                        if(d.tag.equalsIgnoreCase("author") && !resultObject.firstTag.equalsIgnoreCase("www")){
                            resultObject.relevance=StringMatch.tag_match(name.getText().toString(),d.data);
                            if(resultObject.relevance>0.4){
                                search_results.add(DocObject.cloneDocObject(resultObject));
                                break;
                            }

                        }
                    }
                }

            }
        }
    }

    public void searchByYear(){
        reader.searchParams(name.getText().toString());
        reader.relevance = false;
        search_results = new PriorityQueue<>(1, docObjectComparator);
        reader.parseXml();

        while(!search_results.isEmpty()) {

            Integer SINCE = 0;
            Integer START = 0;
            Integer END = 0;

            if(!since.getText().equals("") && since.getText() != null)
                SINCE = Integer.parseInt(since.getText());
            else {
                START = Integer.parseInt(start.getText());
                END = Integer.parseInt(end.getText());
            }

            int count = 0;
            String author = "";
            String title = "";
            String pages = "";
            String year = "";
            String volume = "";
            String url = "";

            for(DocElement d : search_results.element().docElements) {
                if(d.tag.equalsIgnoreCase("author")) {
                    if(count > 0) author += ", ";
                    author += d.data;
                    ++count;
                }
                else if(d.tag.equalsIgnoreCase("title")) title = d.data;
                else if(d.tag.equalsIgnoreCase("pages")) pages = d.data;
                else if(d.tag.equalsIgnoreCase("year")) year = d.data;
                else if(d.tag.equalsIgnoreCase("volume")) volume = d.data;
                else if(d.tag.equalsIgnoreCase("url")) url = d.data;
            }

            if(!year.equals("")) {
                if(SINCE != 0 && SINCE <= Integer.parseInt(year))
                    data.add(new Query1DataFormat(author, title, pages, year, volume, url));
                else if(SINCE == 0 && START <= Integer.parseInt(year) && END >= Integer.parseInt(year))
                    data.add(new Query1DataFormat(author, title, pages, year, volume, url));
            }
            else
                data.add(new Query1DataFormat(author, title, pages, year, volume, url));

            search_results.remove();
        }

        resultTable.setQuery1Data(data);
    }

    public void searchByRelevance(){

        reader.relevance = true;
        search_results = new PriorityQueue<>(1,docObjectRelevanceComparator);
        reader.parseXml();

        while(!search_results.isEmpty()) {

            Integer SINCE = 0;
            Integer START = 0;
            Integer END = 0;

            if(!since.getText().equals("") && since.getText() != null)
                SINCE = Integer.parseInt(since.getText());
            else {
                START = Integer.parseInt(start.getText());
                END = Integer.parseInt(end.getText());
            }

            int count = 0;
            String author = "";
            String title = "";
            String pages = "";
            String year = "";
            String volume = "";
            String url = "";

            for(DocElement d : search_results.element().docElements) {
                if(d.tag.equalsIgnoreCase("author")) {
                    if(count > 0) author += ", ";
                    author += d.data;
                    ++count;
                }
                else if(d.tag.equalsIgnoreCase("title")) title = d.data;
                else if(d.tag.equalsIgnoreCase("pages")) pages = d.data;
                else if(d.tag.equalsIgnoreCase("year")) year = d.data;
                else if(d.tag.equalsIgnoreCase("volume")) volume = d.data;
                else if(d.tag.equalsIgnoreCase("url")) url = d.data;
            }

            if(!year.equals("")) {
                if(SINCE != 0 && SINCE <= Integer.parseInt(year))
                    data.add(new Query1DataFormat(author, title, pages, year, volume, url));
                else if(SINCE == 0 && START <= Integer.parseInt(year) && END >= Integer.parseInt(year))
                    data.add(new Query1DataFormat(author, title, pages, year, volume, url));
            }
            else
                data.add(new Query1DataFormat(author, title, pages, year, volume, url));

            search_results.remove();
        }

        resultTable.setQuery1Data(data);
    }


}
