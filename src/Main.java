import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The entry point of the application.
 */
public class Main {
    private static final String SPLIT_REGEX = "\\s+\\("; // splits on white space followed by a "("
    static final String EMPTY_FILE_ERROR = "The file at the following given path is empty: ";
    private static final String PROGRAM_USAGE = "The program requires exactly 4 arguments:\n" +
            "1st argument: the number of states of the MDP\n" +
            "2nd argument: the number of possible actions\n" +
            "3rd argument: the input file path\n" +
            "4th argument: the discount factor\n\n" +
            "Example usage:\n" +
            "java Main 4 4 ../resources/dataSet1/test.in 0.5";

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println(PROGRAM_USAGE);
            System.exit(1);
        }
        try {
            int numOfStates = Integer.parseInt(args[0]);
            int numOfActions = Integer.parseInt(args[1]);
            String inputFilePath = args[2];
            double discountFactor = Double.parseDouble(args[3]);

            start(numOfStates, numOfActions, inputFilePath, discountFactor);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(PROGRAM_USAGE);
        }
    }

    static void start(int numOfStates, int numOfActions, String inputFilePath, double discountFactor) throws IOException {
        List<String> lines = getLines(inputFilePath);
        final int[] stateRewards = getRewards(numOfStates, lines);
        final double[][] probabilityMatrix = getProbabilityMatrix(numOfActions, numOfStates, lines);
    }

    private static int[] getRewards(int numOfStates, List<String> lines) {
        if (lines.size() != numOfStates) {
            final String error = "The number of states parsed from the file does not match the give number of states.";
            throw new IllegalArgumentException(error);
        }

        int[] stateRewards = new int[numOfStates];

        int rewardIndex = 0;

        for (String line : lines) {
            String[] elements = line.split(SPLIT_REGEX);
            int reward = Integer.parseInt(elements[0].split("\\s+")[1]);
            stateRewards[rewardIndex++] = reward;
        }
        return stateRewards;
    }

    private static double[][] getProbabilityMatrix(int numOfActions, int numOfStates, List<String> lines) {
        double[][] probabilityMatrix = new double[numOfActions][numOfStates];

        for (String line : lines) {
            final String regex = "\\(([^)]+)\\)"; // matches everything between ( and ).
            Matcher m = Pattern.compile(regex).matcher(line);

            while (m.find()) {
                String[] tokens = m.group(1).trim().split("\\s+");
                // get the index of the action and state and decrement it to make it zero based.
                int actionIndex = Integer.parseInt(tokens[0].substring(1, 2)) - 1;
                int stateIndex = Integer.parseInt(tokens[1].substring(1, 2)) - 1;
                double probability = Double.parseDouble(tokens[2]);

                probabilityMatrix[actionIndex][stateIndex] = probability;
            }
        }
        return probabilityMatrix;
    }

    private static List<String> getLines(String path) throws IOException {
        File f = new File(path);
        List<String> lines = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null)
                if (!line.isEmpty())
                    lines.add(line.trim());
        }
        if (lines.size() == 0)
            throw new EmptyFileException(EMPTY_FILE_ERROR + path);

        return lines;
    }

    static class EmptyFileException extends IOException {

        EmptyFileException(String msg) {
            super(msg);
        }
    }
}
