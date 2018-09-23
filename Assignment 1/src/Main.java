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
            "    private static final String program = %s;\n" +
            "\n" +
            "    private static final char tab = 9;\n" +
            "    private static final char newLine = 10;\n" +
            "    private static final char quote = 34;\n" +
            "    private static final char backSlash = 92;\n" +
            "\n" +
            "    public static void main(String[] args) {\n" +
            "        try {\n" +
            "            writeStringToFile(String.format(program, escapeProgramToStringFormat(program)));\n" +
            "        } catch (IOException ex) {\n" +
            "            System.out.println(ex.getMessage());\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private static String escapeProgramToStringFormat(String programStr) {\n" +
            "        String[] lines = programStr.split(\"\" + backSlash + \"n\");\n" +
            "        String output = \"\";\n" +
            "        for(String line : lines) {\n" +
            "            String newLine = \"\";\n" +
            "\n" +
            "            if(output != \"\")\n" +
            "                newLine += \"\" + tab + tab + tab;\n" +
            "\n" +
            "            newLine += \"\" + quote + escapeLine(line) + backSlash + \"n\" + quote + \" +\" + newLine;\n" +
            "            output += newLine;\n" +
            "        }\n" +
            "        output = trimEnd(output, newLine);\n" +
            "        return trimEnd(output, '+');\n" +
            "    }\n" +
            "\n" +
            "    private static String escapeLine(String line) {\n" +
            "        line = line.replace(\"\" + quote, \"\" + backSlash + quote);\n" +
            "        line = line.replace(\"\" + tab, \"\" + backSlash + tab);\n" +
            "        line = line.replace(\"\" + backSlash, \"\" + backSlash + backSlash);\n" +
            "        line = line.replace(\"\" + newLine, \"\" + backSlash + newLine);\n" +
            "        return  line;\n" +
            "    }\n" +
            "\n" +
            "    private static String trimEnd(String stringToTrim, char trimCharacter) {\n" +
            "        return stringToTrim.replaceAll(\"[\" + trimCharacter + \"]+$\", \"\");\n" +
            "    }\n" +
            "\n" +
            "    private static void writeStringToFile(String input) throws IOException {\n" +
            "        File dir = new File(\".\" + File.separator + \"Iteration-\" + iteration);\n" +
            "        dir.mkdir();\n" +
            "        BufferedWriter writer = new BufferedWriter(new FileWriter(dir.getAbsolutePath() + File.separator + \"Main.java\"));\n" +
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
            writeStringToFile(String.format(program, escapeProgramToStringFormat(program)));
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

            newLineStr += "" + quote + escapeLine(line) + backSlash + "n" + quote + " +" + newLine;
            output += newLineStr;
        }
        output = trimEnd(output, newLine);
        return trimEnd(output, '+');
    }

    private static String escapeLine(String line) {
        line = line.replace("" + quote, Character.toString(backSlash) + Character.toString(quote));
        line = line.replace("" + tab, "" + backSlash + tab);
        line = line.replace("" + backSlash, "" + backSlash + backSlash);
        line = line.replace("" + newLine, "" + backSlash + newLine);
        return  line;
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
