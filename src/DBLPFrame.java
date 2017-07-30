import javax.swing.*;
import java.awt.*;
/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
public class DBLPFrame extends JFrame {

    private JLabel header;
    private QueryPanel queryPanel;   // This panel deals with queries
    private ResultTable resultTable;  //Table to show the results of query

    /*
     * To draw the main window of the query engine
     */
    public void initialize() {

        System.setProperty("jdk.xml.entityExpansionLimit", "0");

        setSize(1300, 700);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 248, 238));

        JPanel HeaderPanel = new JPanel(new BorderLayout(20, 20));
        header = new JLabel("DBLP Query Engine", JLabel.CENTER);
        header.setFont(new Font("Courier", Font.PLAIN, 50));
        HeaderPanel.add(header);
        HeaderPanel.add(new JSeparator(), BorderLayout.PAGE_END);
        HeaderPanel.setBackground(new Color(255, 248, 238));
        add(HeaderPanel, BorderLayout.NORTH);

        resultTable = ResultTable.getInstance();
        add(resultTable, BorderLayout.CENTER);

        queryPanel = new QueryPanel();
        add(queryPanel, BorderLayout.WEST);

        display();
    }

    public void display() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            System.out.println("No Nimbus");
        }

        DBLPFrame dblp = new DBLPFrame();
        dblp.initialize();
    }
}