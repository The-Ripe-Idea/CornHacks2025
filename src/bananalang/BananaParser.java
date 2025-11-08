package bananalang;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for parsing the Banana language.
 */
public class BananaParser {

    /**
     * Parses the given Banana language code and returns a list of commands.
     * 
     * @param code the Banana language code to parse
     * @return a list of BananaCommands parsed from the code
     */
    public List<BananaCommands> parse(String code) {
        List<BananaCommands> commands = new ArrayList<>();
        String[] tokens = code.split("\\s+"); // split by whitespace

        for (String token : tokens) {
            if (token.trim().isEmpty()) {
                continue;
            }
            switch (token) {
                case "🍌":
                    commands.add(BananaCommands.PUSH_ONE);
                    break;
                case "🍌together":
                    commands.add(BananaCommands.ADD);
                    break;
                case "🍌tree":
                    commands.add(BananaCommands.MULTIPLY);
                    break;
                case "🍌splat":
                    commands.add(BananaCommands.PRINT);
                    break;
                case "🍌armagedon":
                    commands.add(BananaCommands.CLEAR);
                    break;
                default:
                    System.out.println("⚠️ Unknown token: " + token);
            }
        }

        return commands;
    }
}
