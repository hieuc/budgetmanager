package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fucntions.F;

public class ViewGUI extends JFrame{
    /** Generated serial ID. */
    private static final long serialVersionUID = -8446679627136328719L;
    
    /** Writing mode. 0 = not initialized, 1 = deposit, 2 = deduction. */
    private int mode;
    
    /** Edit mode check. */
    private boolean editMode;
    
    /** Warning label. */
    private JLabel warningLabel;
    
    /** Text area displays log. */
    private JTextArea outputLog;
    
    /** Input text for price. */
    private JTextField inputPrice;
    
    /** Input text for comment, */
    private JTextField inputComment;

    /**
     * Init fields.
     */
    public ViewGUI() {
        super();
        initFields();
        initGUI();
        setProperties();
    }
    
    private void setProperties() {
        this.setTitle("money manager i guess");
        this.setSize(650, 350);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void initGUI() {
        final JPanel contentP = new JPanel(new BorderLayout());
        contentP.add(makeTopPane(), BorderLayout.NORTH);
        contentP.add(makeLeftPane(), BorderLayout.WEST);
        contentP.add(makeBotPane(), BorderLayout.SOUTH);
        contentP.add(makeMidPane(), BorderLayout.CENTER);
        this.setContentPane(contentP);
    }
    
    private JPanel makeTopPane() {
        final JPanel top = new JPanel();
        final JRadioButton depos = new JRadioButton("Deposit");
        depos.addActionListener(e -> {
            mode = 1;
        });
        final JRadioButton deduct = new JRadioButton("Deduction");
        deduct.addActionListener(e -> {
            mode = 2;
        });
        final ButtonGroup bg = new ButtonGroup();
        bg.add(depos);
        bg.add(deduct);
        
        top.add(depos);
        top.add(deduct);
        // add space
        top.add(Box.createRigidArea(new Dimension(70, 0)));
        
        top.add(new JLabel("Price:"));
        inputPrice = new JTextField("");
        inputPrice.setColumns(5);
        top.add(inputPrice);
        
        top.add(new JLabel("Comment:"));
        inputComment = new JTextField("");
        inputComment.setColumns(20);
        top.add(inputComment);
        
        return top;
    }
    
    private JPanel makeLeftPane() {
        final JPanel left = new JPanel();
        left.setBorder(new EmptyBorder(5, 5, 5, 5));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(Box.createRigidArea(new Dimension(0, 20)));
        final JButton monthSum = new JButton("Monthly Summary");
        left.add(monthSum);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton totalSum = new JButton("Total Summary");
        left.add(totalSum);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton editDepos = new JButton("Edit Deposit");
        left.add(editDepos);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton editDeduct = new JButton("Edit Deduction");
        left.add(editDeduct);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton delLine = new JButton("Delete Line");
        left.add(delLine);
        return left;
    }
    
    private JPanel makeMidPane() {
        final JPanel mid = new JPanel(new BorderLayout());
        mid.setBorder(BorderFactory.createEtchedBorder());
        outputLog = new JTextArea();
        outputLog.setLineWrap(true);
        mid.add(outputLog, BorderLayout.CENTER);
        final JScrollPane scroll = new JScrollPane(outputLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mid.add(scroll);
        return mid;
    }
    
    private JPanel makeBotPane() {
        final JPanel bot = new JPanel(new BorderLayout());        
        bot.setBorder(new EmptyBorder(5, 5, 5, 5));
        bot.add(Box.createRigidArea(new Dimension(50, 0)), BorderLayout.WEST);
        warningLabel = new JLabel("this is a test warning label and hopefully it is very long so it can account for the whole program length");
        warningLabel.setForeground(Color.RED);
        bot.add(warningLabel, BorderLayout.CENTER);
        final JButton save = new JButton("Save");
        save.addActionListener(e -> {
            // INTERACTION WITH THE WHOLE PANE
        });
        
        bot.add(save, BorderLayout.EAST);
        return bot;
    }
    
    private void initFields() {
        mode = 0;
        editMode = false;
    }
}
