package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

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
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import functions.F;

public class ViewGUI extends JFrame{
    /** Generated serial ID. */
    private static final long serialVersionUID = -8446679627136328719L;
    
    /** General format of a data file in format : comment-price-date. */
    private static final String LINE_FORMAT = "- %-60s $%10.2f %20s ";
    
    /** Default text color of log text. */
    private static final Color DEFAULT_TEXT_COLOR = Color.LIGHT_GRAY;
    
    /** Background color for log text area. */
    private static final Color TEXTBOX_BACKGROUND_COLOR = new Color(13, 4, 26);  // Black purple-ish
    
    /** Writing mode. 0 = not initialized, 1 = deposit, 2 = deduction. */
    private int mode;
    
    /** Edit mode check. */
    private boolean editMode;
    
    /** Warning label. */
    private JLabel warningLabel;
    
    /** Text area displays log. */
    private JTextPane outputLog;
    
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
        this.setSize(800, 500);
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
        inputComment.setColumns(30);
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
        totalSum.addActionListener(e -> { 
            appendText("Displaying total summary...\n", Color.GREEN);
            appendText("TOTAL DEDUCTION:\n", Color.RED);
            displayDeduct(true);
            appendText("----------------------------------\n", DEFAULT_TEXT_COLOR);
            appendText("TOTAL DEPOSIT:\n", Color.RED);
            displayDepos(true);
            outputLog.setEditable(false);
        });
        left.add(totalSum);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton editDepos = new JButton("Edit Deposit");
        editDepos.addActionListener(e -> {
            outputLog.setText("");
            displayDepos(false);
            outputLog.setEditable(true);
        });
        left.add(editDepos);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton editDeduct = new JButton("Edit Deduction");
        editDeduct.addActionListener(e -> {
            outputLog.setText("");
            displayDeduct(false);
            outputLog.setEditable(true);
        });
        left.add(editDeduct);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton delLine = new JButton("Delete Line");
        left.add(delLine);
        return left;
    }
    
    private JPanel makeMidPane() {
        final JPanel mid = new JPanel(new BorderLayout());
        mid.setBorder(BorderFactory.createEtchedBorder());
        outputLog = new JTextPane();
        outputLog.setBackground(TEXTBOX_BACKGROUND_COLOR);
        outputLog.setForeground(DEFAULT_TEXT_COLOR);
        outputLog.setFont(new Font("monospaced", Font.PLAIN, 12));
        
        //outputLog.setLineWrap(true);
        //outputLog.setEditable(false);
        mid.add(outputLog, BorderLayout.CENTER);
        final JScrollPane scroll = new JScrollPane(outputLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
    
    /**
     * Append text with custom color to the log panel. 
     * 
     * @param text string t append
     * @param color color of text
     */
    private void appendText(final String text, final Color color) {
        final Document doc = outputLog.getStyledDocument();
        final Style style = outputLog.addStyle(null, null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }   
    }
    
    /**
     * Append deduction text to the log panel.
     * 
     * @param displaySum true to display sum, false otherwise
     */
    private void displayDeduct(final boolean displaySum) {
        final List<String> lines = F.readDeduct();
        displayTextChunk(lines);
        if (displaySum) {
            appendText(String.format(" " + LINE_FORMAT.replace("-", ""),
                    "TOTAL:", F.calcTotal(lines), ""), Color.RED);
        }
    }
    
    /**
     * Append deposit text to the log panel.
     * 
     * @param displaySum true to display sum, false otherwise
     */
    private void displayDepos(final boolean displaySum) {
        final List<String> lines = F.readDepos();
        displayTextChunk(lines);
        if (displaySum) {
            appendText(String.format(" " + LINE_FORMAT.replace("-", ""),
                    "TOTAL:", F.calcTotal(lines), ""), Color.RED);
        }
    }
    
    /**
     * Display a lot of text. Helper method for displayDeduct and displayDepos.
     * 
     * @param lines of text.
     */
    private void displayTextChunk(final List<String> lines) {
        lines.forEach(s -> {
            final String[] data = F.parseLine(s);
            final String text = String.format(LINE_FORMAT, data[0].trim(), Double.valueOf(data[1].trim()), data[2].trim());
            appendText(text, DEFAULT_TEXT_COLOR);
            appendText("\n", DEFAULT_TEXT_COLOR);
        });
    }
}
