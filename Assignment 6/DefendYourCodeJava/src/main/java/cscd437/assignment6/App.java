package cscd437.assignment6;

import java.io.File;
import java.io.NotActiveException;
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String FIRST_NAME_PROMPT = "Please input your first name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters): ";
    private static final String LAST_NAME_PROMPT = "Please input your last name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters): ";
    private static final String VAL1_PROMPT = "Please input the first number. It must be from " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE;
    private static final String VAL2_PROMPT = "Please input the second number. It must be from " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE;
    private static final String READ_FILE_PROMPT = "Please input the name of an existing .txt file in the current directory.";
    private static final String WRITE_FILE_PROMPT = "Please input a name of a non-existing .txt file to write to in the current directory.";
    private static final String FIRST_PASSWORD_PROMPT = "Please input a password of the following format, 1-255 characters, A-Z upper or lower case, 0-9, and special characters(-_+=!@#$%^&*()): ";
    private static final String SECOND_PASSWORD_PROMPT = "Please input the password again: ";


    private static final String NAME_REGEX = "^[A-Za-z]{1,50}$";
    private static final String FILE_REGEX = "^[\\w\\- ]+\\.[Tt][Xx][Tt]$";
    private static final String PASS_REGEX = "^[\\w\\d\\-_\\+=!@#$%^&\\*\\(\\)]{1,255}$";
    
    private static final boolean READ_FILE_MUST_EXIST = true;
    private static final boolean WRITE_FILE_MUST_EXIST = false;

    public static void main(final String[] args )
    {
        Scanner kb = new Scanner(System.in);

        String firstName = promptForValidText(kb, FIRST_NAME_PROMPT, NAME_REGEX);
        String lastName = promptForValidText(kb, LAST_NAME_PROMPT, NAME_REGEX);

        BigInteger val1 = new BigInteger(promptForValidInt(kb, VAL1_PROMPT) + "");
        BigInteger val2 = new BigInteger(promptForValidInt(kb, VAL2_PROMPT) + "");

        String fileToReadFromName = promptForValidFileName(kb, READ_FILE_PROMPT, FILE_REGEX, READ_FILE_MUST_EXIST);
        String fileToWriteTo = promptForValidFileName(kb, WRITE_FILE_PROMPT, FILE_REGEX, WRITE_FILE_MUST_EXIST);


    }

    private static void passwordInputValidation(final Scanner kb) {
        String password = promptForValidText(kb, FIRST_PASSWORD_PROMPT, PASS_REGEX);

    }

    private static byte[] hashPassword(final char[] password) {
        throw new RuntimeException();
    }

    private static String promptForValidText(final Scanner kb, final String prompt, final String regex) {
        boolean inputIsValid = false;
        String input = "";
        do {
            System.out.println(prompt);
            try {
                input = kb.nextLine();
                if(Pattern.matches(regex, input)) {
                    inputIsValid = true;
                } else 
                    throw new InputMismatchException();

            } catch(Exception ex) {
                System.out.println("There was an error processing input, please try again.\n");
            }
        } while(!inputIsValid);
        return input;
    }

    private static String promptForValidFileName(final Scanner kb, final String prompt, final String regex, final boolean fileMustAlreadyExist) {
        boolean inputIsValid = false;
        String input = "";
        do {
            System.out.println(prompt);
            try {
                input = kb.nextLine();

                if(!Pattern.matches(regex, input))
                    throw new InputMismatchException();

                File file = new File(input);
                if((fileMustAlreadyExist && !file.exists()) ||
                   (!fileMustAlreadyExist && file.exists()))
                    throw new InputMismatchException();

                inputIsValid = true;
            } catch(Exception ex) {
                System.out.println("That is not a valid file name.\n");
            }
        } while(!inputIsValid);
        return input;
    }

    private static int promptForValidInt(final Scanner kb, final String prompt) {
        boolean inputIsValid = false;
        int input = 0;
        do {
            System.out.println(prompt);
            try {
                if(kb.hasNextInt()) {
                    input = kb.nextInt();
                    inputIsValid = true;
                } else 
                    throw new InputMismatchException();
            } catch(Exception ex) {
                System.out.println("There was an error processing the input. Please try again.\n");
            } finally {
                kb.nextLine();
            }
        } while(!inputIsValid);
        return input;
    }
}
