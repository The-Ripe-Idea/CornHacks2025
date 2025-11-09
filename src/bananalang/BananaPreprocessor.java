package bananalang;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * the PreProcessor for the banana language.
 */

public class BananaPreprocessor {
    // Whitelist of allowed emojis for file processing
    private static final Set<String> ALLOWED_EMOJIS = createAllowedEmojisSet();
    
    // Whitelist of allowed emojis for input (more restrictive)
    private static final Set<String> ALLOWED_INPUT_EMOJIS = createAllowedInputEmojisSet();
    
    /**
     * Creates and initializes the set of allowed emojis for file processing.
     * * @return a Set containing the allowed emojis
     */
    private static Set<String> createAllowedEmojisSet() {
        Set<String> allowed = new HashSet<>();
        // Emojis used in the parser
        allowed.add("🍌");  // Banana - used in many tokens
        allowed.add("🌙");  // Moon - used in number literals
        allowed.add("🌴");  // Palm tree - DUP token
        allowed.add("🐒");  // Monkey - used in number literals
        allowed.add("❓");  // Question mark - EQUALS token
        allowed.add("🍂");  // Fallen leaf - SUBTRACT token
        allowed.add("🪾");  // Jar - DIVIDE token
        allowed.add("❄️");  // Snowflake - MODULUS token
        allowed.add("🙈");  // See-no-evil monkey - PRINT token
        allowed.add("🙉");  // Hear-no-evil monkey - PRINTC token
        allowed.add("︶");  // <-- ADDED THIS CHARACTER

        // Add more allowed emojis here if needed in the future
        return allowed;
    }
    
    /**
     * Creates and initializes the set of allowed emojis for input.
     * * @return a Set containing the allowed input emojis
     */
    private static Set<String> createAllowedInputEmojisSet() {
        Set<String> allowed = new HashSet<>();
        allowed.add("🍌");
        // Add more allowed input emojis here if needed in the future
        return allowed;
    }
    
    /**
     * Processes a string by converting non-whitelisted characters to spaces.
     * Uses the file processing whitelist.
     * * @param input the input string to process
     * @return the processed string with non-whitelisted characters replaced by spaces
     */
    public static String processString(String input) {
        return processString(input, ALLOWED_EMOJIS);
    }
    
    /**
     * Processes a string by converting non-whitelisted characters to spaces.
     * * @param input the input string to process
     * @param whitelist the set of allowed emojis
     * @return the processed string with non-whitelisted characters replaced by spaces
     */
    private static String processString(String input, Set<String> whitelist) {
        if (input == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); ) {
            int codePoint = input.codePointAt(i);
            String emoji = new String(Character.toChars(codePoint));
            
            // If emoji is in whitelist, keep it; otherwise convert to space
            if (whitelist.contains(emoji)) {
                result.append(emoji);
            } else {
                result.append(" ");
            }
            
            i += Character.charCount(codePoint);
        }
        return result.toString();
    }
    
    /**
     * Processes a string by converting non-whitelisted characters to spaces.
     * Uses the input whitelist (more restrictive).
     * * @param input the input string to process
     * @return the processed string with non-whitelisted characters replaced by spaces
     */
    public static String processInputString(String input) {
        return processString(input, ALLOWED_INPUT_EMOJIS);
    }
    
    /**
     * function for processing the input file.
     */
    public static String process(File file) throws FileNotFoundException {
        if (!file.exists()) {
            return "";
        }
        String s = "";
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            s += line + " ";
        }
        fileScanner.close();
        
        // Use the processString method to filter emojis
        return processString(s);
    }
}
