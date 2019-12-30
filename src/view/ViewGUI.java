package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
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
    
    /** Input text for selecting month summary. */
    private JTextField inputMonth;
    
    /** Input text for selecting year summary. */
    private JTextField inputYear;

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
        top.add(Box.createRigidArea(new Dimension(55, 0)));
        
        top.add(new JLabel("Price:"));
        inputPrice = new JTextField("");
        inputPrice.setColumns(5);
        top.add(inputPrice);
        
        top.add(new JLabel("Comment:"));
        inputComment = new JTextField("");
        inputComment.setColumns(30);
        top.add(inputComment);
        
        final JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            
        });
        top.add(ok);
        
        return top;
    }
    
    private JPanel makeLeftPane() {
        final JPanel left = new JPanel();
        left.setBorder(new EmptyBorder(5, 5, 5, 5));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        addMonthFunctions(left);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        final JButton totalSum = new JButton("Total Summary");
        totalSum.addActionListener(e -> { 
            double net = 0;
            appendText("Displaying total summary...\n", Color.GREEN);
            appendText("TOTAL DEDUCTION:\n", Color.RED);
            net -= displayDeduct(true);
            appendText("----------------------------------\n");
            appendText("TOTAL DEPOSIT:\n", Color.RED);
            net += displayDepos(true);
            appendText(String.format(" " + LINE_FORMAT.replace("-", ""),
                    "NET:", net, ""), Color.RED);
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
        final JButton clear = new JButton("Clear console");
        clear.addActionListener(e -> {
            outputLog.setText("");
            outputLog.setEditable(true);
        });
        left.add(clear);
        return left;
    }
    
    private JPanel makeMidPane() {
        final JPanel mid = new JPanel(new BorderLayout());
        mid.setBorder(BorderFactory.createEtchedBorder());
        outputLog = new JTextPane();
        outputLog.setBackground(TEXTBOX_BACKGROUND_COLOR);
        outputLog.setForeground(DEFAULT_TEXT_COLOR);
        outputLog.setCaretColor(DEFAULT_TEXT_COLOR);
        outputLog.setFont(new Font("monospaced", Font.PLAIN, 12));
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
     * Append text with default color to the log panel.
     * 
     * @param text string to append
     */
    private void appendText(final String text) {
        appendText(text, DEFAULT_TEXT_COLOR);
    }
    
    /**
     * Append text with custom color to the log panel. 
     * 
     * @param text string to append
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
    private double displayDeduct(final boolean displaySum) {
        return displayTextChunk(F.readDeduct(), displaySum);
    }
    
    /**
     * Append deposit text to the log panel.
     * 
     * @param displaySum true to display sum, false otherwise
     */
    private double displayDepos(final boolean displaySum) {
        return displayTextChunk(F.readDepos(), displaySum);
    }
    
    /**
     * Display a lot of text. Helper method for displayDeduct and displayDepos.
     * 
     * @param lines of text.
     * @param displaySum true to display sum, false otherwise
     */
    private double displayTextChunk(final List<String> lines, final boolean displaySum) {
        double price = 0;
        try {
            lines.forEach(s -> {
                final String[] data = F.parseLine(s);
                final String text = String.format(LINE_FORMAT, data[0].trim(), Double.valueOf(data[1].trim()), data[2].trim());
                appendText(text);
                appendText("\n");
            });
            
            if (displaySum) {
                price = F.calcTotal(lines);
                appendText(String.format(" " + LINE_FORMAT.replace("-", ""),
                        "TOTAL:", price, ""), Color.RED);
            }
        } catch (IndexOutOfBoundsException e) {
            appendText("Data not found!");
        }
        appendText("\n");
        return price;
    }
    
    /**
     * Append monthly sum to the text area.
     * 
     * @param month selected
     */
    private void displayMonthSum(final int month, final int year) {
        final boolean displaySum = true;
        double net = 0;
        appendText("TOTAL DEDUCTION:\n", Color.RED);
        net -= displayTextChunk(F.readDeductMonth(month, year), displaySum);
        appendText("----------------------------------\n");
        appendText("TOTAL DEPOSIT:\n", Color.RED);
        net += displayTextChunk(F.readDeposMonth(month, year), displaySum);
        appendText(String.format(" " + LINE_FORMAT.replace("-", ""),
                "NET:", net, ""), Color.RED);
        outputLog.setEditable(false);
    }
    
    /**
     * Add the month summary components to the panel.
     * 
     * @param panel
     */
    private void addMonthFunctions(final JPanel panel) {
        // add default month sum button
        final JButton monthSum = new JButton("Monthly Summary");
        monthSum.addActionListener(new DisplayMonth(true));
        panel.add(monthSum);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        // label
        final JLabel monthSumLabel = new JLabel("Enter month:");
        panel.add(monthSumLabel);
        // input month panel
        final JPanel monthPanel = new JPanel();
        // month input
        inputMonth = new JTextField("");
        inputMonth.setColumns(2);
        inputMonth.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startSum");
        inputMonth.getActionMap().put("startSum", new DisplayMonth(false));
        monthPanel.add(inputMonth);
        // separating label
        monthPanel.add(new JLabel("/"));
        // inputYear
        inputYear = new JTextField(String.valueOf(LocalDateTime.now().getYear()));
        inputYear.setColumns(3);
        inputYear.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startSum");
        inputYear.getActionMap().put("startSum", new DisplayMonth(false));
        monthPanel.add(inputYear);
        // confirm button
        final JButton monthConfirm = new JButton("OK");
        monthConfirm.addActionListener(new DisplayMonth(false));
        monthPanel.add(monthConfirm);
        // alignment setup
        monthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, monthConfirm.getMinimumSize().height + 5));
        monthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createHorizontalGlue());
        panel.add(monthPanel);
    }
    /**
     * Display month key action.
     * 
     * @author Victor Chau
     * @version December 2019
     */
    @SuppressWarnings("serial")
    private class DisplayMonth extends AbstractAction {
        /** Display mode: true = default, false = custom. */
        private boolean defaultMode;
        
        /** 
         * Initialize mode.
         * 
         * @param defaultMode true = default, false = custom.
         */
        DisplayMonth(final boolean defaultMode) {
            this.defaultMode = defaultMode;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                appendText("Displaying monthly summary...\n", Color.GREEN);
                int month;
                int year;
                if (defaultMode) {
                    month = LocalDateTime.now().getMonthValue();
                    year = LocalDateTime.now().getYear();
                } else {
                    month = Integer.valueOf(inputMonth.getText().trim());
                    year = Integer.valueOf(inputYear.getText().trim());
                }
                appendText(Month.values()[month - 1].toString() + " " + year + "\n", Color.GREEN);
                displayMonthSum(month, year);
            } catch (NumberFormatException e1) {
                appendText("Wrong format of input date.\n", Color.RED);
            }
        }
    }
}
