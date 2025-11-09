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
                    if (i + 1 < tokens.length) {
                        String nextToken = tokens[i + 1];
                        
                        // Check if next token is 🍌🍌🍌 (input token)
                        if (nextToken.equals("🍌🍌🍌")) {
                            commands.add("PUSH_INPUT");
                            i++; // Skip the input token
                            break;
                        }

                        if (nextToken.startsWith("🙉")) {
                            StringBuilder binaryString = new StringBuilder();
                            for (int j = 1; j < nextToken.length(); ) {
                                int codePoint = nextToken.codePointAt(j); // get the full emoji code point
                                String emoji = new String(Character.toChars(codePoint));

                                if (emoji.equals("🍌")) {
                                    binaryString.append('1');
                                } else if (emoji.equals("🌙")) {
                                    binaryString.append('0');
                                }

                                j += Character.charCount(codePoint); // move to the next emoji
                            }
                            int index = Integer.parseInt(binaryString.toString(), 2);
                            i++; // Skip the number token

                            // Generate command to push element from index at runtime
                            commands.add("PUSH_FROM_INDEX");
                            commands.add(String.valueOf(index));
                            
                            break;
                        }
                        
                        double number = 1;
                        int j = 6;
                        boolean negative = false;
                        try {
                            if (nextToken.startsWith("🌙🌙")) {
                                
                                if (nextToken.startsWith("🌙🌙🍌")) {
                                    negative = true;
                                }
                                int x = 0;
                                StringBuilder binaryString = new StringBuilder();
                                String[] numberHalves = new String[2];
                                numberHalves[0] = "";
                                numberHalves[1] = "";
                                while (j < nextToken.length()) {
                                    int codePoint = nextToken.codePointAt(j); // get the full emoji code point
                                    String emoji = new String(Character.toChars(codePoint));

                                    if (emoji.equals("🍌")) {
                                        binaryString.append('1');
                                    } else if (emoji.equals("🌙")) {
                                        binaryString.append('0');
                                    } else if (emoji.equals("🐒")) {
                                        numberHalves[x] += binaryString.toString();
                                        binaryString = new StringBuilder();
                                        x++;
                                    }
                                    j += Character.charCount(codePoint); // move to the next emoji
                                }
                                numberHalves[x] += binaryString.toString();
                                StringBuilder doubleString = new StringBuilder(Integer.parseInt(numberHalves[0], 2) + ".");
                                while (numberHalves[1].startsWith("0")) {
                                    doubleString.append("0");
                                    numberHalves[1] = numberHalves[1].substring(1);
                                }
                                doubleString.append(Integer.parseInt(numberHalves[1], 2));
                                number = (Double.parseDouble(doubleString.toString()));
                                if (negative) {
                                    number *= -1;
                                }
                                i++; // Skip the number token
                            } else if (nextToken.startsWith("🌙")) {
                                if (nextToken.startsWith("🌙🍌")) {
                                    negative = true;
                                }
                                StringBuilder binaryString = new StringBuilder();
                                for (j = 2; j < nextToken.length(); ) {
                                    int codePoint = nextToken.codePointAt(j); // get the full emoji code point
                                    String emoji = new String(Character.toChars(codePoint));

                                    if (emoji.equals("🍌")) {
                                        binaryString.append('1');
                                    } else if (emoji.equals("🌙")) {
                                        binaryString.append('0');
                                    }

                                    j += Character.charCount(codePoint); // move to the next emoji
                                }
                                number = Integer.parseInt(binaryString.toString(), 2);
                                if (negative) {
                                    number *= -1;
                                }
                                i++; // Skip the number token
                            }   
                            
                            commands.add("PUSH_ONE");
                            commands.add(String.valueOf((double) number));
                            
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
                case "🍌🍂":
                    commands.add("SUBTRACT");
                    break;
                case "🍌🌴":
                    commands.add("MULTIPLY");
                    break;
                case "🍌🪾":
                    commands.add("DIVIDE");
                    break;
                case "🍌❄️":
                    commands.add("MODULUS");
                    break;
                case "🍌🙈":
                    commands.add("PRINT");
                    break;
                case "🍌🙉":
                    commands.add("PRINTC");
                    break;
                case "🍌🍌🍌🍌🍌":
                    commands.add("CLEAR");
                    break;
                case "🍌❓":
                    commands.add("EQUALS");
                    break;
                case "︶":
                    commands.add("︶");
                    break;
                default:
                    System.out.println("⚠️ Unknown token: " + token);
            }
        }

        return commands;
    }
}
