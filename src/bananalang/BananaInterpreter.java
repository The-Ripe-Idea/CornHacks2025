package bananalang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for interpreting the Banana language.
 */
public class BananaInterpreter {

    public static final ArrayList<Double> list = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private static boolean lookingForU = false;
    private static int uCounter = 0;

    /**
     * Executes the given list of Banana language commands.
     * 
     * @param commands the list of commands to execute
     */


    public void run(List<String> commands) {
        Iterator<String> iterator = commands.iterator();
        while (iterator.hasNext()) {
            String cmd = iterator.next();
            while (lookingForU) { // while (uCounter > 0)
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
                // Get the next command which should be a number
                if (iterator.hasNext()) {
                    String numberStr = iterator.next();
                    try {
                        double number = Double.parseDouble(numberStr);
                        list.add(number);
                    } catch (NumberFormatException e) {
                        this.error("Invalid number after PUSH_ONE: " + numberStr);
                    }
                } else {
                    this.error("PUSH_ONE requires a number but none was provided");
                }
                continue;
            }

            if (cmd.equals("PUSH_INPUT")) {
                // Read input from console and process it through the preprocessor
                String input;
                boolean validInput = false;

                while (!validInput) {
                    input = this.scanner.nextLine();
                    
                    // Process input: convert non-whitelisted characters to spaces
                    // Uses input-specific whitelist (only 🍌)
                    String processed = BananaPreprocessor.processInputString(input);
                    
                    // Check if processed input contains only whitelisted emojis (no spaces)
                    // If it contains spaces, it means there were non-whitelisted characters
                    validInput = !processed.contains(" ");
                    
                    if (validInput) {
                        // Count the number of whitelisted emojis
                        double emojiCount = 0;
                        for (int i = 0; i < processed.length();) {
                            emojiCount++;
                            i += Character.charCount(processed.codePointAt(i));
                        }

                        list.add(emojiCount);
                    }
                }
                continue;
            }
            
            if (cmd.equals("PUSH_FROM_INDEX")) {
                // Get the index from the next command
                if (iterator.hasNext()) {
                    String indexStr = iterator.next();
                    try {
                        int index = Integer.parseInt(indexStr);
                        
                        // Check if index is valid
                        if (index < 0 || index >= list.size()) {
                            this.error("PUSH_FROM_INDEX: Index " + index + 
                                " is out of bounds (list size: " + list.size() + ")");
                        } else {
                            // Get the element at the specified index and push it to the end (duplicate it)
                            double value = list.get(index);
                            list.add(value);
                        }
                    } catch (NumberFormatException e) {
                        this.error("Invalid index after PUSH_FROM_INDEX: " + indexStr);
                    }
                } else {
                    this.error("PUSH_FROM_INDEX requires an index but none was provided");
                }
                continue;
            }

            switch (cmd) {

                case "ADD": {
                    if (list.size() < 2) {
                        this.error("ADD needs 2 values!");
                        break;
                    }
                    double b = list.remove(list.size() - 1);
                    double a = list.remove(list.size() - 1);
                    list.add(a + b);
                    break;
                }

                case "SUBTRACT": {
                    if (list.size() < 2) {
                        this.error("SUBTRACT needs 2 values!");
                        break;
                    }
                    double b = list.remove(list.size() - 1);
                    double a = list.remove(list.size() - 1);
                    list.add(a - b);
                    break;
                }

                case "MULTIPLY": {
                    if (list.size() < 2) {
                        this.error("MULTIPLY needs 2 values!");
                        break;
                    }
                    double b = list.remove(list.size() - 1);
                    double a = list.remove(list.size() - 1);
                    list.add(a * b);
                    break;
                }

                case "DUP":
                    if (list.isEmpty()) {
                        this.error("DUP needs 1 value!");
                        break;
                    }
                    double value = list.get(list.size() - 1); // Look at top without removing
                    list.add(value); // Push a copy
                    break;
                case "DIVIDE": {
                    if (list.size() < 2) {
                        this.error("DIVIDE needs 2 values!");
                        break;
                    }
                    double b = list.remove(list.size() - 1);
                    double a = list.remove(list.size() - 1);
                    list.add(a / b);
                    break;
                }

                case "MODULUS": {
                    if (list.size() < 2) {
                        this.error("MODULUS needs 2 values!");
                        break;
                    }
                    double b = list.remove(list.size() - 1);
                    double a = list.remove(list.size() - 1);
                    list.add(a % b);
                    break;
                }

                case "PRINT": {
                    if (list.isEmpty()) {
                        this.error("PRINT needs 1 value!");
                        break;
                    }
                    

                    if ((list.get(list.size() - 1) -  Math.floor(list.get(list.size() - 1))) < 0.000000001) {
                        System.out.print(list.remove(list.size() - 1).intValue());
                    } else {
                        System.out.print(list.remove(list.size() - 1));
                    }

                    break;
                }

                case "PRINTC":
                    if (list.isEmpty()) {
                        this.error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.print((char) list.remove(list.size() - 1).intValue());

                    break;

                case "CLEAR": {
                    list.clear();
                    //System.out.println("💥 List cleared!");
                    break;
                }

                case "EQUALS": {
                    // if (list.size() < 2) { // no equality found
                    //     list.add(0.0);
                    //     lookingForU = true;
                    //     uCounter = 1;
                    // }
                    //System.out.println(list.toString() + "a");
                    double b = list.remove(list.size() - 1);
                    double a = list.remove(list.size() - 1);
                    if (a == b) {
                        list.add(1.0);
                    } else {
                        list.add(b);
                        lookingForU = true;
                        uCounter = 1;
                    }
                    break;
                }

                case "︶":
                    break;

                default:
                    this.error("Unknown command: " + cmd);
                    break;
            }
        }
    }

    /**
     * Prints an error message.
     * 
     * @param msg the error message to print
     */
    private void error(String msg) {
        System.out.println("🚫 Error: " + msg);
    }

}
