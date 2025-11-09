package bananalang;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * the PreProcessor for the banana language.
 */

public class BananaPreprocessor {
    /**
     * function for processing the input string.
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
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            int codePoint = s.codePointAt(i);
            String emoji = new String(Character.toChars(codePoint));
            if (emoji.equals("🍌") || emoji.equals("🌙") || emoji.equals("🙉") || emoji.equals("🙈") || emoji.equals("🌴")) {
                result.append(emoji);
            } else {
                result.append(" ");
            }
            i += Character.charCount(codePoint);
        }
        s = result.toString();
        fileScanner.close();// ︵︶
        return s;

    }
}
