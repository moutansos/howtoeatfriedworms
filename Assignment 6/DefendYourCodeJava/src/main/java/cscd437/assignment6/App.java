package cscd437.assignment6;

//Contributors: Travis Currier, Ben Brougher, Daylyn Hoxie
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

    private static final String PASS_STORAGE_FILE = "pass_storage.dat";
    private static final int PASS_ITERATIONS = 12;
    private static final int PASS_KEY_LENGTH = 256;
    
    private static final boolean READ_FILE_MUST_EXIST = true;
    private static final boolean WRITE_FILE_MUST_EXIST = false;

    public static void main(final String[] args ) throws IOException
    {
        Scanner kb = new Scanner(System.in);

        String firstName = promptForValidText(kb, FIRST_NAME_PROMPT, NAME_REGEX);
        String lastName = promptForValidText(kb, LAST_NAME_PROMPT, NAME_REGEX);

        BigInteger val1 = new BigInteger(promptForValidInt(kb, VAL1_PROMPT) + "");
        BigInteger val2 = new BigInteger(promptForValidInt(kb, VAL2_PROMPT) + "");
        
        String fileToReadFromName = promptForValidFileName(kb, READ_FILE_PROMPT, FILE_REGEX, READ_FILE_MUST_EXIST);
        String fileToWriteTo = promptForValidFileName(kb, WRITE_FILE_PROMPT, FILE_REGEX, WRITE_FILE_MUST_EXIST);

        try {
            String firstPassword = promptForValidText(kb, FIRST_PASSWORD_PROMPT, PASS_REGEX);
            storePassword(firstPassword);
            String secondPassword = promptForValidText(kb, SECOND_PASSWORD_PROMPT, PASS_REGEX);
            if(storedPasswordMatches(secondPassword)) {
                System.out.println("The password matches");
            } else {
                System.out.println("The password does not match!");
            }

            outputInfoToFile(firstName, lastName, val1, val2, fileToReadFromName, fileToWriteTo);
        } catch(IOException ex) {
            System.out.println("Error with password storage.");
        }
        
    }

    private static void storePassword(String password) throws IOException {
        byte[] salt = getSalt();
        byte[] encodedSalt = Base64.getEncoder().encode(salt);
        byte[] hashedPassword = hashPassword(password.toCharArray(), salt, PASS_ITERATIONS, PASS_KEY_LENGTH);
        byte[] encodedPassword = Base64.getEncoder().encode(hashedPassword);
        
        boolean append = false;
        BufferedWriter writer = new BufferedWriter(new FileWriter(PASS_STORAGE_FILE, append));

        writer.append(new String(encodedSalt));
        writer.append('\n');
        writer.append(new String(encodedPassword));

        writer.close();
    }

    private static boolean storedPasswordMatches(String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PASS_STORAGE_FILE));
        Scanner file = new Scanner(reader);
        String saltText = file.nextLine();
        String hashText = file.nextLine();
        reader.close();

        byte[] saltFromFile = Base64.getDecoder().decode(saltText.getBytes());
        byte[] hashFromFile = Base64.getDecoder().decode(hashText.getBytes());

        byte[] hashedPassword = hashPassword(password.toCharArray(), saltFromFile, PASS_ITERATIONS, PASS_KEY_LENGTH);

        return Arrays.equals(hashFromFile, hashedPassword);
    }

    //https://www.owasp.org/index.php/Hashing_Java
    private static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;
  
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch(InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
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

    public static byte[] getSalt() {
        Random random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
      }

   private static void outputInfoToFile(String firstname, String lastname, BigInteger val1, BigInteger val2, String readFileName, String writeFileName) throws IOException{
       File writeFile = new File(writeFileName);
       writeFile.createNewFile();
       File readFile = new File(readFileName);
       
       PrintWriter writer = new PrintWriter(writeFile);
       BufferedReader reader = new BufferedReader(new FileReader(readFile));
       String line;
       
       BigInteger addVals = val1.add(val2);
       BigInteger multVals = val1.multiply(val2);
       
       writer.println(firstname + " " + lastname);
       writer.println(val1 + " + " + val2 + " = " + addVals);
       writer.println(val1 + " * " + val2 + " = " + multVals);
       
       while((line = reader.readLine()) != null){
           writer.println(line);
       }

       reader.close();
       writer.close();

   }

}
