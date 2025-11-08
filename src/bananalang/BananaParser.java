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
    public List<String> parse(String code) {
        List<String> commands = new ArrayList<String>();
        String[] tokens = code.split("\\s+"); // split by whitespace

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.trim().isEmpty()) {
                continue;
            }
            switch (token) {
                case "🍌":
                    // Check if next token is a number
                    if (i + 1 < tokens.length) {
                        String nextToken = tokens[i + 1];
                        try {
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
                int number = Integer.parseInt(binaryString.toString(), 2));
                            
                            commands.add("PUSH_ONE");
                            commands.add(String.valueOf(number));
                            i++; // Skip the number token
                            break;
                        } catch (NumberFormatException e) {
                            // Not a number, push 1 as default
                            commands.add("PUSH_ONE");
                            commands.add("1");
                            break;
                        }
                    } else {
                        // No next token, push 1 as default
                        commands.add("PUSH_ONE");
                        commands.add("1");
                    }
                    break;
                case "🍌🍌":
                    commands.add("ADD");
                    break;
                case "🍌🍌🍌🍌":
                    commands.add("MULTIPLY");
                    break;
                case "🍌🍌🍌":
                    commands.add("PRINT");
                    break;
                case "🍌🍌🍌🍌🍌":
                    commands.add("CLEAR");
                    break;
                default:
                    System.out.println("⚠️ Unknown token: " + token);
            }
        }

        return commands;
    }
}
