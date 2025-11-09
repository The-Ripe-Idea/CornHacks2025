import bananalang.BananaInterpreter;
import bananalang.BananaParser;
import bananalang.BananaPreprocessor;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * GUI wrapper for the Banana language interpreter.
 * Provides a code editor, console output window, and run button.
 */
public class BananaGUI extends JFrame {
    private JTextArea codeEditor;
    private JTextArea consoleOutput;
    private JButton runButton;
    private JButton clearButton;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    
    public BananaGUI() {
        initializeGUI();
        setupOutputRedirection();
    }
    
    private void initializeGUI() {
        setTitle("Banana Language IDE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create code editor panel
        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBorder(new TitledBorder("Code Editor"));
        codeEditor = new JTextArea(20, 50);
        codeEditor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        codeEditor.setTabSize(4);
        JScrollPane editorScroll = new JScrollPane(codeEditor);
        editorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        editorPanel.add(editorScroll, BorderLayout.CENTER);
        
        // Create console output panel
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.setBorder(new TitledBorder("Console Output"));
        consoleOutput = new JTextArea(10, 50);
        consoleOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        consoleOutput.setEditable(false);
        consoleOutput.setBackground(Color.BLACK);
        consoleOutput.setForeground(Color.GREEN);
        JScrollPane consoleScroll = new JScrollPane(consoleOutput);
        consoleScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        consoleScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        consolePanel.add(consoleScroll, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        runButton = new JButton("Run");
        runButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        runButton.setPreferredSize(new Dimension(100, 35));
        runButton.addActionListener(new RunButtonListener());
        
        clearButton = new JButton("Clear Console");
        clearButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        clearButton.setPreferredSize(new Dimension(120, 35));
        clearButton.addActionListener(e -> {
            consoleOutput.setText("");
            outputStream.reset();
        });
        
        buttonPanel.add(runButton);
        buttonPanel.add(clearButton);
        
        // Create split pane to divide editor and console
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, consolePanel);
        splitPane.setResizeWeight(0.6); // Give 60% to editor, 40% to console
        splitPane.setDividerLocation(400);
        
        // Add components to frame
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set window size and center
        setSize(800, 700);
        setLocationRelativeTo(null);
    }
    
    private void setupOutputRedirection() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream, true);
    }
    
    private class RunButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            runButton.setEnabled(false);
            consoleOutput.append(">>> Running code...\n");
            
            // Run in a separate thread to prevent UI freezing
            new Thread(() -> {
                try {
                    executeCode();
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        consoleOutput.append("🚫 Error: " + ex.getMessage() + "\n");
                    });
                    ex.printStackTrace();
                } finally {
                    SwingUtilities.invokeLater(() -> {
                        runButton.setEnabled(true);
                    });
                }
            }).start();
        }
    }
    
    private void executeCode() {
        // Redirect System.out to capture output
        System.setOut(printStream);
        
        try {
            // Get code from editor
            String code = codeEditor.getText();
            
            // Process the code through preprocessor
            String processedCode = BananaPreprocessor.processString(code);
            
            // Parse the code
            BananaParser parser = new BananaParser();
            List<String> commands = parser.parse(processedCode);
            
            // Create a GUI-friendly interpreter
            GUIInterpreter interpreter = new GUIInterpreter();
            interpreter.run(commands);
            
            // Flush any remaining output
            printStream.flush();
            
            // Get output and display it
            final String output = outputStream.toString();
            if (!output.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    consoleOutput.append(output);
                });
                outputStream.reset();
            }
            
            SwingUtilities.invokeLater(() -> {
                consoleOutput.append(">>> Execution completed.\n");
            });
            
        } catch (Exception e) {
            final String errorMsg = e.getMessage();
            SwingUtilities.invokeLater(() -> {
                consoleOutput.append("🚫 Error: " + errorMsg + "\n");
            });
            e.printStackTrace();
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }
    
    /**
     * GUI-friendly interpreter that handles input via dialogs.
     */
    private class GUIInterpreter {
        private static boolean lookingForU = false;
        private static int uCounter = 0;
        
        public void run(List<String> commands) {
            Iterator<String> iterator = commands.iterator();
            while (iterator.hasNext()) {
                String cmd = iterator.next();
                
                while (lookingForU) {
                    cmd = iterator.next();
                    if (cmd.equals("EQUALS")) {
                        uCounter++;
                    } else if (cmd.equals("︶")) {
                        uCounter--;
                        if (uCounter == 0) {
                            lookingForU = false;
                            cmd = iterator.next();
                        }
                    }
                }
                
                if (cmd.equals("PUSH_ONE")) {
                    if (iterator.hasNext()) {
                        String numberStr = iterator.next();
                        try {
                            double number = Double.parseDouble(numberStr);
                            BananaInterpreter.list.add(number);
                        } catch (NumberFormatException e) {
                            error("Invalid number after PUSH_ONE: " + numberStr);
                        }
                    } else {
                        error("PUSH_ONE requires a number but none was provided");
                    }
                    continue;
                }
                
                if (cmd.equals("PUSH_INPUT")) {
                    // Handle input via GUI dialog
                    String input = null;
                    boolean validInput = false;
                    
                    while (!validInput) {
                        final String[] inputHolder = new String[1];
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                inputHolder[0] = JOptionPane.showInputDialog(
                                    BananaGUI.this,
                                    "Enter input (only 🍌 emojis allowed):",
                                    "Banana Input",
                                    JOptionPane.PLAIN_MESSAGE
                                );
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        input = inputHolder[0];
                        
                        if (input == null) {
                            // User cancelled, treat as empty input
                            input = "";
                            validInput = true;
                        } else {
                            // Process input: convert non-whitelisted characters to spaces
                            String processed = BananaPreprocessor.processInputString(input);
                            
                            // Check if processed input contains only whitelisted emojis (no spaces)
                            validInput = !processed.contains(" ");
                            
                            if (validInput) {
                                // Count the number of whitelisted emojis
                                double emojiCount = 0;
                                for (int i = 0; i < processed.length();) {
                                    emojiCount++;
                                    i += Character.charCount(processed.codePointAt(i));
                                }
                                BananaInterpreter.list.add(emojiCount);
                            } else {
                                // Invalid input, show error
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(
                                        BananaGUI.this,
                                        "Invalid input! Only 🍌 emojis are allowed.",
                                        "Input Error",
                                        JOptionPane.ERROR_MESSAGE
                                    );
                                });
                            }
                        }
                    }
                    continue;
                }
                
                if (cmd.equals("PUSH_FROM_INDEX")) {
                    if (iterator.hasNext()) {
                        String indexStr = iterator.next();
                        try {
                            int index = Integer.parseInt(indexStr);
                            
                            if (index < 0 || index >= BananaInterpreter.list.size()) {
                                error("PUSH_FROM_INDEX: Index " + index + 
                                    " is out of bounds (list size: " + BananaInterpreter.list.size() + ")");
                            } else {
                                double value = BananaInterpreter.list.get(index);
                                BananaInterpreter.list.add(value);
                            }
                        } catch (NumberFormatException e) {
                            error("Invalid index after PUSH_FROM_INDEX: " + indexStr);
                        }
                    } else {
                        error("PUSH_FROM_INDEX requires an index but none was provided");
                    }
                    continue;
                }
                
                switch (cmd) {
                    case "ADD": {
                        if (BananaInterpreter.list.size() < 2) {
                            error("ADD needs 2 values!");
                            break;
                        }
                        double b = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        double a = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        BananaInterpreter.list.add(a + b);
                        break;
                    }
                    
                    case "SUBTRACT": {
                        if (BananaInterpreter.list.size() < 2) {
                            error("SUBTRACT needs 2 values!");
                            break;
                        }
                        double b = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        double a = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        BananaInterpreter.list.add(a - b);
                        break;
                    }
                    
                    case "MULTIPLY": {
                        if (BananaInterpreter.list.size() < 2) {
                            error("MULTIPLY needs 2 values!");
                            break;
                        }
                        double b = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        double a = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        BananaInterpreter.list.add(a * b);
                        break;
                    }
                    
                    case "DIVIDE": {
                        if (BananaInterpreter.list.size() < 2) {
                            error("DIVIDE needs 2 values!");
                            break;
                        }
                        double b = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        double a = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        BananaInterpreter.list.add(a / b);
                        break;
                    }
                    
                    case "MODULUS": {
                        if (BananaInterpreter.list.size() < 2) {
                            error("MODULUS needs 2 values!");
                            break;
                        }
                        double b = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        double a = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        BananaInterpreter.list.add(a % b);
                        break;
                    }
                    
                    case "PRINT": {
                        if (BananaInterpreter.list.isEmpty()) {
                            error("PRINT needs 1 value!");
                            break;
                        }
                        
                        if ((BananaInterpreter.list.get(BananaInterpreter.list.size() - 1) - 
                             Math.floor(BananaInterpreter.list.get(BananaInterpreter.list.size() - 1))) < 0.000000001) {
                            System.out.print(BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1).intValue());
                        } else {
                            System.out.print(BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1));
                        }
                        break;
                    }
                    
                    case "PRINTC":
                        if (BananaInterpreter.list.isEmpty()) {
                            error("PRINT needs 1 value!");
                            break;
                        }
                        System.out.print((char) BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1).intValue());
                        break;
                    
                    case "CLEAR": {
                        BananaInterpreter.list.clear();
                        break;
                    }
                    
                    case "EQUALS": {
                        double b = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        double a = BananaInterpreter.list.remove(BananaInterpreter.list.size() - 1);
                        if (a == b) {
                            BananaInterpreter.list.add(1.0);
                        } else {
                            BananaInterpreter.list.add(b);
                            lookingForU = true;
                            uCounter = 1;
                        }
                        break;
                    }
                    
                    case "︶":
                        break;
                    
                    default:
                        error("Unknown command: " + cmd);
                        break;
                }
            }
        }
        
        private void error(String msg) {
            System.out.println("🚫 Error: " + msg);
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new BananaGUI().setVisible(true);
        });
    }
}
