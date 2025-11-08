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
            if (token.startsWith("🌙")) {
                StringBuilder binaryString = new StringBuilder();
                for (int i = 0; i < token.length(); ) {
                    int codePoint = token.codePointAt(i); // get the full emoji code point
                    String emoji = new String(Character.toChars(codePoint));
                    
                    if (emoji.equals("🍌")) {
                        binaryString.append('1');
                    } else if (emoji.equals("🌙")) {
                        binaryString.append('0');
                    }

                    i += Character.charCount(codePoint); // move to the next emoji
                }
                commands.add(Integer.parseInt(binaryString.toString(), 2));
            } else {
                switch (token) {
                    case "🍌":
                        commands.add(BananaCommands.PUSH_ONE);
                        break;
                    case "🍌🍌":
                        commands.add(BananaCommands.ADD);
                        break;
                    case "🍌🍌🍌":
                        commands.add(BananaCommands.MULTIPLY);
                        break;
                    case "🍌🍌🍌🍌":
                        commands.add(BananaCommands.MULTIPLY);
                        break;
                    case "🍌🍌🍌🍌🍌":
                        commands.add(BananaCommands.PRINT);
                        break;
                    case "🍌🍌🍌🍌🍌🍌":
                        commands.add(BananaCommands.CLEAR);
                        break;
                    default:
                        System.out.println("⚠️ Unknown token: " + token);
                }
            }
        }

        return commands;
    }
}
