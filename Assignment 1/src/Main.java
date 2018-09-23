import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final int iteration = 0;
    private static final String program = "import java.io.BufferedWriter;\n" +
            "import java.io.File;\n" +
            "import java.io.FileWriter;\n" +
            "import java.io.IOException;\n" +
            "\n" +
            "public class Main {\n" +
            "    private static final int iteration = 0;\n" +
            "    private static final String program = %1$s;\n" +
            "\n" +
            "    private static final char tab = 9;\n" +
            "    private static final char newLine = 10;\n" +
            "    private static final char quote = 34;\n" +
            "    private static final char backSlash = 92;\n" +
            "\n" +
            "    public static void main(String[] args) {\n" +
            "        try {\n" +
            "            writeStringToFile(String.format(program, escapeProgramToStringFormat(program), quote));\n" +
            "        } catch (IOException ex) {\n" +
            "            System.out.println(ex.getMessage());\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private static String escapeProgramToStringFormat(String programStr) {\n" +
            "        String[] lines = programStr.split(%2$c%2$c + backSlash + %2$cn%2$c);\n" +
            "        String output = %2$c%2$c;\n" +
            "        for(String line : lines) {\n" +
            "            String newLineStr = %2$c%2$c;\n" +
            "\n" +
            "            if(output != %2$c%2$c)\n" +
            "                newLineStr += %2$c%2$c + tab + tab + tab;\n" +
            "\n" +
            "            newLineStr += %2$c%2$c + quote + line + backSlash + %2$cn%2$c + quote + %2$c +%2$c + newLine;\n" +
            "            output += newLineStr;\n" +
            "        }\n" +
            "        output = trimEnd(output, newLine);\n" +
            "        return trimEnd(output, '+');\n" +
            "    }\n" +
            "\n" +
            "    private static String trimEnd(String stringToTrim, char trimCharacter) {\n" +
            "        return stringToTrim.replaceAll(%2$c[%2$c + trimCharacter + %2$c]+$%2$c, %2$c%2$c);\n" +
            "    }\n" +
            "\n" +
            "    private static void writeStringToFile(String input) throws IOException {\n" +
            "        File dir = new File(%2$c.%2$c + File.separator + %2$cIteration-%2$c + iteration);\n" +
            "        dir.mkdir();\n" +
            "        BufferedWriter writer = new BufferedWriter(new FileWriter(dir.getAbsolutePath() + File.separator + %2$cMain.java%2$c));\n" +
            "        writer.write(input);\n" +
            "        writer.close();\n" +
            "    }\n" +
            "}\n";

    private static final char tab = 9;
    private static final char newLine = 10;
    private static final char quote = 34;
    private static final char backSlash = 92;

    public static void main(String[] args) {
        try {
            writeStringToFile(String.format(program, escapeProgramToStringFormat(program), quote));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static String escapeProgramToStringFormat(String programStr) {
        String[] lines = programStr.split("" + backSlash + "n");
        String output = "";
        for(String line : lines) {
            String newLineStr = "";

            if(output != "")
                newLineStr += "" + tab + tab + tab;

            newLineStr += "" + quote + line + backSlash + "n" + quote + " +" + newLine;
            output += newLineStr;
        }
        output = trimEnd(output, newLine);
        return trimEnd(output, '+');
    }

    private static String trimEnd(String stringToTrim, char trimCharacter) {
        return stringToTrim.replaceAll("[" + trimCharacter + "]+$", "");
    }

    private static void writeStringToFile(String input) throws IOException {
        File dir = new File("." + File.separator + "Iteration-" + iteration);
        dir.mkdir();
        BufferedWriter writer = new BufferedWriter(new FileWriter(dir.getAbsolutePath() + File.separator + "Main.java"));
        writer.write(input);
        writer.close();
    }
}
