import javax.jws.Oneway;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */

public class Query2 extends JPanel implements ActionListener, Observer {

    private JTextField numPublications;
    private JButton Search;
    private JButton Reset;
    private XMLReader reader;
    private ArrayList<Object> data;
    private ResultTable resultTable;

    Query2() {
        setLayout(null);

        JLabel num = new JLabel("Number of Publications");
        num.setBounds(20, 40, 150, 50);
        add(num);

        numPublications = new JTextField();
        numPublications.setBounds(175, 50, 80, 25);
        add(numPublications);

        Search = new JButton("Search");
        Search.setBounds(25, 100, 100, 40);
        Search.addActionListener(this);
        add(Search);

        Reset = new JButton("Reset");
        Reset.setBounds(145, 100, 100, 40);
        Reset.addActionListener(this);
        add(Reset);

        data = new ArrayList<>();

        resultTable = ResultTable.getInstance();
        resultTable.SetQueryMode("QUERY2");
        resultTable.SetupTable();
        resultTable.setVisible(true);
    }

    public void setXMLReader(XMLReader reader) {
        this.reader = reader;
        this.reader.deleteObservers();
        reader.addObserver(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer number_of_publications = Integer.parseInt(numPublications.getText());
        if(e.getActionCommand().equals("Search")) {
            reader.ProcessQuery2(number_of_publications);
            reader.ProcessQuery2Handler();
            resultTable.setQuery2Data(data);
        }
        else if(e.getActionCommand().equals("Reset")) {
            data = new ArrayList<>();
            numPublications.setText("");
            resultTable.ResetQuery();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        data.add(arg);
    }
}
