import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */

public class ResultTable extends JPanel implements ActionListener {

    private JLabel resultCount;
    private JTable table;
    private static ResultTable resultTable;
    private String[] Query1Columns = {"Sno", "Authors", "Title", "Pages", "Year", "Volume", "URL"};
    private String[] Query2Columns = {"Sno", "Authors"};
    private String[] Query3Columns = {"Sno", "Author", "Predicted Value"};
    private ArrayList<Query1DataFormat> Query1Data;
    private ArrayList<Object> Query2Data;
    private ArrayList<String> Query3DataAuthors;
    private ArrayList<Integer> Query3DataPredicted;
    private int Query1Start;
    private int Query2Start;
    private int Query3Start;
    private JButton next;
    private String QueryMode;

    private ResultTable() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 248, 238));

        QueryMode = "QUERY1";

        resultCount = new JLabel();
        resultCount.setText("Number of results: " + 0);

        table = new JTable();
        table.setBackground(new Color(255, 255, 255));
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setGridColor(new Color(0, 0, 0));
        table.setRowHeight(table.getRowHeight() + 8);
        SetupTable();

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBorder(new EmptyBorder(10, 0, 10, 5));
        tablePane.setBackground(new Color(255, 248, 238));

        add(resultCount, BorderLayout.NORTH);
        add(tablePane, BorderLayout.CENTER);

        JPanel button = new JPanel(new BorderLayout());
        button.setBackground(new Color(255, 248, 238));
        next = new JButton("Next");
        button.add(next, BorderLayout.EAST);
        next.addActionListener(this);
        add(button, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void SetupTable() {
        DefaultTableModel model = null;
        if(QueryMode.equals("QUERY1"))      model = new DefaultTableModel(Query1Columns, 20);
        else if(QueryMode.equals("QUERY2")) model = new DefaultTableModel(Query2Columns, 20);
        else if(QueryMode.equals("QUERY3")) model = new DefaultTableModel(Query3Columns, 20);
        table.setModel(model);
    }

    public JTable getTable() { return this.table; }

    public static ResultTable getInstance() {
        if(resultTable == null) {
            resultTable = new ResultTable();
            return resultTable;
        }
        return resultTable;
    }

    public void SetQueryMode(String Mode) {
        this.QueryMode = Mode;
    }

    public void setQuery1Data(ArrayList<Query1DataFormat> query1Data) {
        this.Query1Data = query1Data;
        resultCount.setVisible(false);
        resultCount.setText("Number of results: " + query1Data.size());
        resultCount.setVisible(true);
        Query1Start = 0;
        ProcessQuery1();
    }

    public void ProcessQuery1() {
        Object[][] row = new Object[20][7];
        for(int i = 0; i < 20; ++i) {
            if(Query1Start < Query1Data.size()) {
                row[i][1] = Query1Data.get(Query1Start).getAuthors();
                row[i][2] = Query1Data.get(Query1Start).getTitle();
                row[i][3] = Query1Data.get(Query1Start).getPages();
                row[i][4] = Query1Data.get(Query1Start).getYear();
                row[i][5] = Query1Data.get(Query1Start).getVolume();
                row[i][6] = Query1Data.get(Query1Start).getURL();
                row[i][0] = (Query1Start + 1);
            }
            else
                row[i][1] = row[i][2] = row[i][3] = row[i][4] = row[i][5] = row[i][6] = row[i][0] = "";
            ++Query1Start;
            if(Query1Start > Query1Data.size()) next.setEnabled(false);
        }
        DefaultTableModel tableModel = new DefaultTableModel(row, Query1Columns);
        tableModel.setColumnCount(7);
        tableModel.setRowCount(20);
        resultTable.getTable().setModel(tableModel);
    }

    public void setQuery2Data(ArrayList<Object> data) {
        this.Query2Data = data;
        resultCount.setVisible(false);
        resultCount.setText("Number of results: " + data.size());
        resultCount.setVisible(true);
        Query2Start = 0;
        ProcessQuery2();
    }

    public void ProcessQuery2() {
        Object[][] row = new Object[20][2];
        for(int i = 0; i < 20; ++i) {
            if(Query2Start < Query2Data.size()) {
                row[i][1] = Query2Data.get(Query2Start);
                row[i][0] = (Query2Start + 1);
            } else
                row[i][1] = row[i][0] = "";
            ++Query2Start;
            if(Query2Start > Query2Data.size())
                next.setEnabled(false);
        }

        DefaultTableModel tableModel = new DefaultTableModel(row, Query2Columns);
        tableModel.setColumnCount(2);tableModel.setRowCount(20);
        resultTable.getTable().setModel(tableModel);
    }

    public void setQuery3Data(HashMap<String, Integer> data) {
        this.Query3DataAuthors = new ArrayList<>();
        this.Query3DataPredicted = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            this.Query3DataAuthors.add(entry.getKey());
            this.Query3DataPredicted.add(entry.getValue());
        }
        resultCount.setVisible(false);
        resultCount.setText("Number of results: " + data.size());
        resultCount.setVisible(true);
        Query3Start = 0;
        ProcessQuery3();
    }

    public void ProcessQuery3() {
        Object[][] row = new Object[20][3];
        for(int i = 0; i < 20; ++i) {
            if(Query3Start < Query3DataAuthors.size()) {
                row[i][0] = (Query3Start + 1);
                row[i][1] = Query3DataAuthors.get(Query3Start);
                row[i][2] = Query3DataPredicted.get(Query3Start);
            } else
                row[i][2] = row[i][1] = row[i][0] = "";
            ++Query3Start;
            if(Query3Start > Query3DataAuthors.size()) next.setEnabled(false);
        }
        DefaultTableModel tableModel = new DefaultTableModel(row, Query3Columns);
        tableModel.setColumnCount(3);
        tableModel.setRowCount(20);
        resultTable.getTable().setModel(tableModel);
    }

    public void ResetQuery() {
        if(QueryMode.equals("QUERY1"))  this.Query1Data = null;
        if(QueryMode.equals("QUERY2"))  this.Query2Data = null;
        if(QueryMode.equals("QUERY3"))  { this.Query3DataAuthors = null; this.Query3DataPredicted = null; }
        SetupTable();
        next.setEnabled(true);
        resultCount.setVisible(false);
        resultCount.setText("Number of results: " + 0);
        resultCount.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Next")) {
            if(QueryMode.equals("QUERY2"))      ProcessQuery2();
            else if(QueryMode.equals("QUERY1")) ProcessQuery1();
            else if(QueryMode.equals("QUERY3")) ProcessQuery3();
        }
    }
}