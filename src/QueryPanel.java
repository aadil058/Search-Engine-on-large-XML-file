import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */

public class QueryPanel extends JPanel implements ActionListener {

    private JComboBox list;
    private String[] options = { "Select Query Type", "Query 1", "Query 2", "Query 3" };
    private String SelectedQuery;
    private JPanel query;
    private XMLReader reader;

    QueryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 400));
        setBackground(new Color(255, 248, 238));

        this.query = null;

        list = new JComboBox(options);
        list.setSelectedIndex(0);
        list.addActionListener(this);
        list.setBorder(new EmptyBorder(0, 50, 0, 50));
        add(list, BorderLayout.NORTH);

        reader = XMLReader.getReader();
        reader.parseXml();

        add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.LINE_END);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("comboBoxChanged")) {
            if(list.getSelectedItem().equals(options[1]))
                SelectedQuery = options[1];
            else if(list.getSelectedItem().equals(options[2]))
                SelectedQuery = options[2];
            else if(list.getSelectedItem().equals(options[3]))
                SelectedQuery = options[3];
        }

        AddQuery();
    }

    public void AddQuery() {
        if(query != null) remove(query);
        setVisible(false);
        query = QueryFactory.getInstance(this.SelectedQuery);
        assert query != null;
        query.setPreferredSize(new Dimension(300, 400));
        query.setBackground(new Color(255, 248, 238));
        query.setBorder(new EmptyBorder(50, 0, 0, 0));
        if(SelectedQuery.equals(options[1]))   ((Query1) query).setXMLReader(reader);
        if(SelectedQuery.equals(options[2]))   ((Query2) query).setXMLReader(reader);
        if(SelectedQuery.equals(options[3]))   ((Query3) query).setXMLReader(reader);
        add(query);
        repaint();
        setVisible(true);
    }
}
