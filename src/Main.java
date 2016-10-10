import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            "java Main 4 2 ../resources/DataSet1/test.in 0.9";

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println(PROGRAM_USAGE);
            System.exit(1);
        }

        try {
            int numOfStates = Integer.parseInt(args[0]);
            // args[1] is number of actions and I don't need it.
            String inputFilePath = args[2];
            double discountFactor = Double.parseDouble(args[3]);

            start(numOfStates, inputFilePath, discountFactor);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(PROGRAM_USAGE);
        }
    }

    static void start(int numOfStates, String inputFilePath, double discountFactor) throws IOException {
        final List<State> states = getStates(numOfStates, getLines(inputFilePath));

        MDP mdp = new MDP(states, discountFactor);
        mdp.performValueIteration();
    }

    private static List<State> getStates(int numOfStates, List<String> lines) {
        if (lines.size() != numOfStates) {
            final String error = "The number of states parsed from the file does not match the give number of states.";
            throw new IllegalArgumentException(error);
        }
        return lines.stream().map(Main::getState).collect(Collectors.toList());
    }

    private static State getState(String line) {
        State.Builder stateBuilder = State.Builder(getReward(line));

        final String regex = "\\(([^)]+)\\)"; // matches everything between ( and ).
        Matcher m = Pattern.compile(regex).matcher(line);

        while (m.find()) {
            String[] tokens = m.group(1).trim().split("\\s+");
            // get the index of the action and state and decrement it to make it zero based.

            String actionNum = tokens[0];
            String stateNum = tokens[1];
            double transitionProb = Double.parseDouble(tokens[2]);
            stateBuilder.put(actionNum + stateNum, transitionProb);
        }
        return stateBuilder.build();
    }

    private static int getReward(String line) {
        String[] elements = line.split(SPLIT_REGEX);
        return Integer.parseInt(elements[0].split("\\s+")[1]);
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
