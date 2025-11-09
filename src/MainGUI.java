import bananalang.BananaInterpreter;
import bananalang.BananaParser;
import bananalang.BananaPreprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * Main class for testing the Banana language interpreter.
 */
public class MainGUI {
    /**
     * Main method to run the Banana language interpreter.
     * 
     * @param args command line arguments - if empty, launches GUI; otherwise runs file-based interpreter
     */
    public static void main(String[] args) throws FileNotFoundException {
        // If no arguments, launch GUI
        if (args.length == 0) {
            SwingUtilities.invokeLater(() -> {
                try {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new BananaGUI().setVisible(true);
            });
        } else {
            // Run file-based interpreter (original behavior)
            String code = BananaPreprocessor.process(new File("./fitness.nana"));
            
            BananaParser parser = new BananaParser();
            List<String> commands = parser.parse(code);
            BananaInterpreter interpreter = new BananaInterpreter();
            interpreter.run(commands);
        }
    }
}
