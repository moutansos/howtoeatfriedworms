package cscd437.assignment6;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String FIRST_NAME_PROMPT = "Please input your first name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters) : ";
    private static final String LAST_NAME_PROMPT = "Please input your last name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters): ";

    private static final String NAME_REGEX = "[\\w\\-]{1-50}";

    public static void main( String[] args )
    {
        Scanner kb = new Scanner(System.in);

        String firstName = promptForValidText(kb, FIRST_NAME_PROMPT, NAME_REGEX);
        String lastName = promptForValidText(kb, LAST_NAME_PROMPT, NAME_REGEX);
    }

    private static String promptForValidText(Scanner kb, String prompt, String regex) {
        boolean inputIsValid = false;
        String input = "";
        do {
            System.out.println(prompt);
            try {
                input = kb.nextLine();
                inputIsValid = Pattern.matches(regex, input);

                if(!inputIsValid)
                    System.out.println("The format of the input was not correct!");

            } catch(Exception ex) {
                System.out.println("There was an error processing input, please try again.\n");
            }
        } while(!inputIsValid);
        return input;
    }
}
