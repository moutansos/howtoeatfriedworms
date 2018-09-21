import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private const int iteration = 0;
    private static String program = "import java.io.BufferedWriter;\n" +
            "import java.io.FileWriter;\n" +
            "import java.io.IOException;\n" +
            "\n" +
            "public class Main {\n" +
            "    private const int iteration = " + (iteration + 1) + ";\n" +
            "    private static String program = \"" + program + "\";\n" +
            "\n" +
            "    public static void main(String[] args) {\n" +
            "        writeStringToFile(program);\n" +
            "    }\n" +
            "\n" +
            "    public static void writeStringToFile(String input) throws IOException {\n" +
            "        BufferedWriter writer = new BufferedWriter(new FileWriter(\"Iteration-\" + iteration + \".java\"));\n" +
            "        writer.write(input);\n" +
            "        writer.close();();\n" +
            "    }\n" +
            "}\n";

    public static void main(String[] args) {
        try {
            writeStringToFile(program);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void writeStringToFile(String input) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("Iteration-" + iteration + ".java"));
        writer.write(input);
        writer.close();();
    }
}
