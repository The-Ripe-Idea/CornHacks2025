import bananalang.BananaInterpreter;
import bananalang.BananaParser;
import bananalang.BananaPreprocessor;
import java.io.File;
import java.io.FileNotFoundException;
//import java.util.ArrayList;
import java.util.List;

/**
 * Main class for testing the Banana language interpreter.
 */
public class Main {
    /**
     * Main method to run the Banana language interpreter.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) throws FileNotFoundException{
        //String code = "🍌 🌙🍌🌙🌙🍌🌙🌙🌙 🍌🍌🍌🍌🍌🍌 🍌 🌙🍌🍌🌙🌙🍌🌙🍌 🍌🍌🍌🍌🍌🍌";
        String code = BananaPreprocessor.process(new File("./add.txt"));
        //System.out.println(code);
        BananaParser parser = new BananaParser();
        List<String> commands = parser.parse(code);
        BananaInterpreter interpreter = new BananaInterpreter();
        interpreter.run(commands);
    }
}
