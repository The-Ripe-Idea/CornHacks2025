import bananalang.BananaInterpreter;
import bananalang.BananaParser;
import bananalang.BananaPreprocessor;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GUI wrapper for the Banana language interpreter.
 * Features a neobrutalist pastel yellow design matching the reference image.
 */
public class BananaGUI extends JFrame {
    // Color palette matching the image
    private static final Color PASTEL_YELLOW = new Color(255, 250, 205); // Creamy pastel yellow
    private static final Color YELLOW_ORANGE = new Color(255, 200, 100); // Yellow-orange for Run button
    private static final Color BLACK = new Color(0, 0, 0);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color DARK_BROWN = new Color(50, 30, 20); // Dark brown for text
    
    private JTextArea codeEditor;
    private JTextArea consoleOutput;
    private JTextField inputField;
    private JButton runButton;
    private JButton resetButton;
    private JButton submitButton;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private String currentFileName = "";
    
    // Queue for input handling
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private boolean waitingForInput = false;
    
    // Fonts that render properly
    private Font titleFont;
    private Font codeFont;
    private Font uiFont;
    
    public BananaGUI() {
        initializeFonts();
        initializeGUI();
        setupOutputRedirection();
    }
    
    private void initializeFonts() {
        // Use UIManager to get system default fonts which are guaranteed to work
        // These fonts are what the system uses for UI elements and definitely render all characters
        
        // Get the default button font from the system
        Font defaultButtonFont = UIManager.getFont("Button.font");
        if (defaultButtonFont == null) {
            defaultButtonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        }
        
        // For titles - use system default with bold
        titleFont = defaultButtonFont.deriveFont(Font.BOLD, 18f);
        
        // For code - use Monospaced which is designed for code
        codeFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        
        // For UI elements - use system default font
        uiFont = defaultButtonFont.deriveFont(13f);
        
        // Enable antialiasing for better text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
    
    private Border createThickBorder() {
        return new LineBorder(DARK_BROWN, 3, false);
    }
    
    private Border createThickBorder(int thickness) {
        return new LineBorder(DARK_BROWN, thickness, false);
    }
    
    public void showFileSelectionOnStart() {
        showFileSelectionDialog();
    }
    
    private void showFileSelectionDialog() {
        JDialog fileDialog = new JDialog(this, "Select File", true);
        fileDialog.setLayout(new BorderLayout());
        fileDialog.getContentPane().setBackground(PASTEL_YELLOW);
        fileDialog.setUndecorated(false);
        
        // Title label - use system default label font
        JLabel titleLabel = new JLabel("Choose a Banana File");
        Font labelFont = UIManager.getFont("Label.font");
        if (labelFont == null) {
            labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        } else {
            labelFont = labelFont.deriveFont(Font.BOLD, 20f);
        }
        titleLabel.setFont(labelFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(30, 30, 20, 30));
        titleLabel.setForeground(DARK_BROWN);
        titleLabel.putClientProperty("JComponent.aaTextInfoKey", true);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBackground(PASTEL_YELLOW);
        buttonPanel.setBorder(new EmptyBorder(20, 50, 30, 50));
        
        // Create buttons for each file
        String[] files = {"fitness.nana", "hello.nana", "calc.nana"};
        String[] labels = {"Fitness Calculator", "Hello World", "Calculator"};
        
        for (int i = 0; i < files.length; i++) {
            JButton fileButton = createFileSelectionButton(labels[i]);
            final String fileName = files[i];
            fileButton.addActionListener(e -> {
                loadFile(fileName);
                fileDialog.dispose();
            });
            buttonPanel.add(fileButton);
        }
        
        fileDialog.add(titleLabel, BorderLayout.NORTH);
        fileDialog.add(buttonPanel, BorderLayout.CENTER);
        
        fileDialog.setSize(350, 350);
        fileDialog.setLocationRelativeTo(null);
        fileDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        fileDialog.setVisible(true);
    }
    
    private JButton createFileSelectionButton(String text) {
        JButton button = new JButton(text);
        // Use system default button font which definitely renders all characters
        Font buttonFont = UIManager.getFont("Button.font");
        if (buttonFont == null) {
            buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        } else {
            buttonFont = buttonFont.deriveFont(Font.BOLD, 14f);
        }
        button.setFont(buttonFont);
        button.setPreferredSize(new Dimension(250, 60));
        button.setBackground(WHITE);
        button.setForeground(DARK_BROWN);
        button.setBorder(createThickBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.putClientProperty("JComponent.aaTextInfoKey", true);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(PASTEL_YELLOW.getRGB()));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
            }
        });
        
        return button;
    }
    
    private void loadFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                currentFileName = fileName;
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                codeEditor.setText(content.toString());
                codeEditor.setCaretPosition(0);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "File not found: " + fileName, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading file: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeGUI() {
        setTitle("Banana Language IDE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(PASTEL_YELLOW);
        setLayout(new BorderLayout());
        
        // Enable better text rendering globally
        enableTextAntialiasing();
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area - two columns
        JPanel mainContent = createMainContentPanel();
        add(mainContent, BorderLayout.CENTER);
        
        // Bottom action bar
        JPanel actionBar = createActionBar();
        add(actionBar, BorderLayout.SOUTH);
        
        // Set window size and center
        setSize(1000, 750);
        setLocationRelativeTo(null);
        
        // Set frame border
        ((JComponent) getContentPane()).setBorder(createThickBorder(4));
    }
    
    private void enableTextAntialiasing() {
        // Enable antialiasing for all text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Set rendering hints for better text quality
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        java.util.Map<?, ?> desktopHints = (java.util.Map<?, ?>) toolkit.getDesktopProperty("awt.font.desktophints");
        if (desktopHints != null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            // Apply hints if available
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PASTEL_YELLOW);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Title "Banana Programs"
        JLabel titleLabel = new JLabel("Banana Programs");
        titleLabel.setFont(titleFont.deriveFont(20f));
        titleLabel.setForeground(DARK_BROWN);
        titleLabel.putClientProperty("JComponent.aaTextInfoKey", true);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        header.add(titleLabel, BorderLayout.CENTER);
        return header;
    }
    
    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(PASTEL_YELLOW);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Left column - Code Editor (wider)
        JPanel codePanel = createCodePanel();
        
        // Right column - Output and Input (narrower)
        JPanel rightColumn = createRightColumn();
        
        // Split pane with custom divider
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, codePanel, rightColumn);
        splitPane.setResizeWeight(0.65); // Code gets 65%, right column gets 35%
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setBackground(PASTEL_YELLOW);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createCodePanel() {
        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.setBackground(WHITE);
        codePanel.setBorder(createThickBorder());
        
        // Title
        JLabel codeTitle = new JLabel("Code (" + currentFileName + ")");
        codeTitle.setFont(titleFont);
        codeTitle.setForeground(DARK_BROWN);
        codeTitle.setBorder(new CompoundBorder(
            new EmptyBorder(10, 15, 10, 15),
            new LineBorder(DARK_BROWN, 0, false)
        ));
        codeTitle.putClientProperty("JComponent.aaTextInfoKey", true);
        codePanel.add(codeTitle, BorderLayout.NORTH);
        
        // Code editor
        codeEditor = new JTextArea();
        codeEditor.setFont(codeFont);
        codeEditor.setBackground(WHITE);
        codeEditor.setForeground(DARK_BROWN);
        codeEditor.setTabSize(4);
        codeEditor.setLineWrap(false);
        codeEditor.setWrapStyleWord(false);
        // Enable better text rendering
        codeEditor.putClientProperty("JComponent.aaTextInfoKey", true);
        
        JScrollPane editorScroll = new JScrollPane(codeEditor);
        editorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        editorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        editorScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        editorScroll.getViewport().setBackground(WHITE);
        editorScroll.setViewportBorder(null);
        
        codePanel.add(editorScroll, BorderLayout.CENTER);
        return codePanel;
    }
    
    private JPanel createRightColumn() {
        JPanel rightColumn = new JPanel(new BorderLayout(0, 15));
        rightColumn.setBackground(PASTEL_YELLOW);
        
        // Output panel
        JPanel outputPanel = createOutputPanel();
        
        // Input panel
        JPanel inputPanel = createInputPanel();
        
        rightColumn.add(outputPanel, BorderLayout.CENTER);
        rightColumn.add(inputPanel, BorderLayout.SOUTH);
        
        return rightColumn;
    }
    
    private JPanel createOutputPanel() {
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(WHITE);
        outputPanel.setBorder(createThickBorder());
        
        // Title
        JLabel outputTitle = new JLabel("Output");
        outputTitle.setFont(titleFont);
        outputTitle.setForeground(DARK_BROWN);
        outputTitle.setBorder(new CompoundBorder(
            new EmptyBorder(10, 15, 10, 15),
            new LineBorder(DARK_BROWN, 0, false)
        ));
        outputTitle.putClientProperty("JComponent.aaTextInfoKey", true);
        outputPanel.add(outputTitle, BorderLayout.NORTH);
        
        // Output text area
        consoleOutput = new JTextArea();
        consoleOutput.setFont(uiFont);
        consoleOutput.setEditable(false);
        consoleOutput.setBackground(WHITE);
        consoleOutput.setForeground(DARK_BROWN);
        consoleOutput.setText("Run the program to see output...");
        consoleOutput.setLineWrap(true);
        consoleOutput.setWrapStyleWord(true);
        // Enable better text rendering
        consoleOutput.putClientProperty("JComponent.aaTextInfoKey", true);
        
        JScrollPane outputScroll = new JScrollPane(consoleOutput);
        outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outputScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        outputScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        outputScroll.getViewport().setBackground(WHITE);
        outputScroll.setViewportBorder(null);
        
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        return outputPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(WHITE);
        inputPanel.setBorder(createThickBorder());
        
        // Title
        JLabel inputTitle = new JLabel("Input");
        inputTitle.setFont(titleFont);
        inputTitle.setForeground(DARK_BROWN);
        inputTitle.setBorder(new CompoundBorder(
            new EmptyBorder(10, 15, 10, 15),
            new LineBorder(DARK_BROWN, 0, false)
        ));
        inputTitle.putClientProperty("JComponent.aaTextInfoKey", true);
        inputPanel.add(inputTitle, BorderLayout.NORTH);
        
        // Input field and button container
        JPanel inputContainer = new JPanel(new BorderLayout(10, 10));
        inputContainer.setBackground(WHITE);
        inputContainer.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Input field
        inputField = new JTextField();
        inputField.setFont(uiFont);
        inputField.setBackground(WHITE);
        inputField.setForeground(DARK_BROWN);
        inputField.setBorder(createThickBorder());
        inputField.setText("Waiting for program to request input..");
        inputField.setEditable(false);
        inputField.setFocusable(false);
        // Enable better text rendering
        inputField.putClientProperty("JComponent.aaTextInfoKey", true);
        
        // Submit button - use system default button font
        submitButton = new JButton("Submit");
        Font submitButtonFont = UIManager.getFont("Button.font");
        if (submitButtonFont == null) {
            submitButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        } else {
            submitButtonFont = submitButtonFont.deriveFont(Font.BOLD, 12f);
        }
        submitButton.setFont(submitButtonFont);
        submitButton.setPreferredSize(new Dimension(80, 35));
        submitButton.setBackground(WHITE);
        submitButton.setForeground(DARK_BROWN);
        submitButton.setBorder(createThickBorder());
        submitButton.setFocusPainted(false);
        submitButton.putClientProperty("JComponent.aaTextInfoKey", true);
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> submitInput());
        
        inputField.addActionListener(e -> submitInput());
        
        inputContainer.add(inputField, BorderLayout.CENTER);
        inputContainer.add(submitButton, BorderLayout.EAST);
        
        // Instruction text
        JLabel instructionLabel = new JLabel("Input will be requested when the program needs it");
        instructionLabel.setFont(uiFont.deriveFont(11f));
        instructionLabel.setForeground(new Color(DARK_BROWN.getRed(), DARK_BROWN.getGreen(), DARK_BROWN.getBlue(), 150));
        instructionLabel.setBorder(new EmptyBorder(5, 15, 10, 15));
        instructionLabel.putClientProperty("JComponent.aaTextInfoKey", true);
        
        JPanel inputContent = new JPanel(new BorderLayout());
        inputContent.setBackground(WHITE);
        inputContent.add(inputContainer, BorderLayout.CENTER);
        inputContent.add(instructionLabel, BorderLayout.SOUTH);
        
        inputPanel.add(inputContent, BorderLayout.CENTER);
        return inputPanel;
    }
    
    private JPanel createActionBar() {
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBackground(PASTEL_YELLOW);
        actionBar.setBorder(new CompoundBorder(
            createThickBorder(3),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side buttons
        JPanel leftButtonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtonContainer.setBackground(PASTEL_YELLOW);
        
        // Open File button - use system default button font
        JButton openFileButton = new JButton("Open File");
        Font openFileFont = UIManager.getFont("Button.font");
        if (openFileFont == null) {
            openFileFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        } else {
            openFileFont = openFileFont.deriveFont(Font.BOLD, 14f);
        }
        openFileButton.setFont(openFileFont);
        openFileButton.setPreferredSize(new Dimension(140, 45));
        openFileButton.setBackground(WHITE);
        openFileButton.setForeground(DARK_BROWN);
        openFileButton.setBorder(createThickBorder());
        openFileButton.setFocusPainted(false);
        openFileButton.putClientProperty("JComponent.aaTextInfoKey", true);
        openFileButton.setHorizontalAlignment(SwingConstants.CENTER);
        openFileButton.addActionListener(e -> showFileSelectionDialog());
        
        // Run Program button (yellow-orange) - use system default button font
        runButton = new JButton("▶ Run Program");
        Font runButtonFont = UIManager.getFont("Button.font");
        if (runButtonFont == null) {
            runButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        } else {
            runButtonFont = runButtonFont.deriveFont(Font.BOLD, 14f);
        }
        runButton.setFont(runButtonFont);
        runButton.setPreferredSize(new Dimension(150, 45));
        runButton.setBackground(YELLOW_ORANGE);
        runButton.setForeground(DARK_BROWN);
        runButton.setBorder(createThickBorder());
        runButton.setFocusPainted(false);
        runButton.putClientProperty("JComponent.aaTextInfoKey", true);
        runButton.addActionListener(new RunButtonListener());
        
        leftButtonContainer.add(openFileButton);
        leftButtonContainer.add(runButton);
        
        // Right side buttons
        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightContainer.setBackground(PASTEL_YELLOW);
        
        // Reset button (white) - use system default button font
        resetButton = new JButton("↻ Reset");
        Font resetButtonFont = UIManager.getFont("Button.font");
        if (resetButtonFont == null) {
            resetButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        } else {
            resetButtonFont = resetButtonFont.deriveFont(Font.BOLD, 14f);
        }
        resetButton.setFont(resetButtonFont);
        resetButton.setPreferredSize(new Dimension(120, 45));
        resetButton.setBackground(WHITE);
        resetButton.setForeground(DARK_BROWN);
        resetButton.setBorder(createThickBorder());
        resetButton.setFocusPainted(false);
        resetButton.putClientProperty("JComponent.aaTextInfoKey", true);
        resetButton.addActionListener(e -> {
            consoleOutput.setText("Run the program to see output...");
            outputStream.reset();
            inputField.setText("Waiting for program to request input..");
            inputField.setEditable(false);
            inputField.setFocusable(false);
            submitButton.setEnabled(false);
            waitingForInput = false;
            inputQueue.clear(); // Clear any pending input
            BananaInterpreter.list.clear();
        });
        
        rightContainer.add(resetButton);
        
        actionBar.add(leftButtonContainer, BorderLayout.WEST);
        actionBar.add(rightContainer, BorderLayout.EAST);
        
        return actionBar;
    }
    
    private void submitInput() {
        // Check if we're actually waiting for input
        boolean shouldSubmit = false;
        synchronized (this) {
            shouldSubmit = waitingForInput && inputField.isEditable() && submitButton.isEnabled();
        }
        
        if (shouldSubmit) {
            String input = inputField.getText();
            // Allow empty input (will be processed as 0 emojis)
            
            // Add input to queue - this will unblock the waiting thread
            inputQueue.offer(input != null ? input : "");
            
            // Output newline to console
            SwingUtilities.invokeLater(() -> {
                consoleOutput.append("\n");
            });
            
            // Reset UI immediately to prevent double submission
            inputField.setEditable(false);
            inputField.setFocusable(false);
            submitButton.setEnabled(false);
            inputField.setText("Waiting for program to request input..");
        }
    }
    
    private void setupOutputRedirection() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream, true);
    }
    
    private class RunButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Run the code currently in the editor
            runCode();
        }
    }
    
    private void runCode() {
        runButton.setEnabled(false);
        consoleOutput.setText("");
        consoleOutput.append(">>> Running code...\n");
        
        // Update code title with current file
        updateCodeTitle();
        
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
    
    private void updateCodeTitle() {
        // Find the code panel and update title
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateCodeTitleRecursive((JPanel) comp);
            }
        }
    }
    
    private void updateCodeTitleRecursive(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().startsWith("Code (")) {
                    label.setText("Code (" + currentFileName + ")");
                    return;
                }
            } else if (comp instanceof JPanel) {
                updateCodeTitleRecursive((JPanel) comp);
            }
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
                    consoleOutput.setText(output);
                });
                outputStream.reset();
            }
            
            SwingUtilities.invokeLater(() -> {
                consoleOutput.append("\n>>> Execution completed.\n");
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
     * GUI-friendly interpreter that handles input via the input field.
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
                        } catch (NumberFormatException ex) {
                            error("Invalid number after PUSH_ONE: " + numberStr);
                        }
                    } else {
                        error("PUSH_ONE requires a number but none was provided");
                    }
                    continue;
                }
                
                if (cmd.equals("PUSH_INPUT")) {
                    // Handle input via GUI input field
                    synchronized (BananaGUI.this) {
                        waitingForInput = true;
                    }
                    
                    // Clear any previous input from queue
                    inputQueue.clear();
                    
                    // Update UI to request input
                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            inputField.setText("");
                            inputField.setEditable(true);
                            inputField.setFocusable(true);
                            inputField.requestFocus();
                            submitButton.setEnabled(true);
                            consoleOutput.append(">>> Waiting for input...\n");
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    
                    // Wait for input
                    String input = null;
                    try {
                        input = inputQueue.take();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        synchronized (BananaGUI.this) {
                            waitingForInput = false;
                        }
                        SwingUtilities.invokeLater(() -> {
                            inputField.setText("Waiting for program to request input..");
                            inputField.setEditable(false);
                            inputField.setFocusable(false);
                            submitButton.setEnabled(false);
                        });
                        return;
                    }
                    
                    synchronized (BananaGUI.this) {
                        waitingForInput = false;
                    }
                    
                    // Reset UI
                    SwingUtilities.invokeLater(() -> {
                        inputField.setText("Waiting for program to request input..");
                        inputField.setEditable(false);
                        inputField.setFocusable(false);
                        submitButton.setEnabled(false);
                    });
                    
                    // Process input (allow empty input - it will just count as 0)
                    String processed = BananaPreprocessor.processInputString(input != null ? input : "");
                    boolean validInput = !processed.contains(" ");
                    
                    if (validInput) {
                        double emojiCount = 0;
                        for (int i = 0; i < processed.length();) {
                            emojiCount++;
                            i += Character.charCount(processed.codePointAt(i));
                        }
                        BananaInterpreter.list.add(emojiCount);
                    } else {
                        error("Invalid input! Only 🍌 emojis are allowed.");
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
                        } catch (NumberFormatException ex) {
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
                        
                        double value = BananaInterpreter.list.get(BananaInterpreter.list.size() - 1);
                        if ((value - Math.floor(value)) < 0.000000001) {
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
            BananaGUI gui = new BananaGUI();
            gui.setVisible(true);
            gui.showFileSelectionOnStart();
        });
    }
}
