import java.text.DecimalFormat;
import java.util.List;

/**
 * This class simply performs value iteration on a markov decision process and
 * prints the results after its done.
 */
class MDP {
    // This project has a predefined number of iterations to perform.
    private static final int NUM_OF_ITERATIONS = 20;
    private final List<State> states;
    private final double discountFactor;
    // best action for a given state index.
    private final int[] bestActions;
    // The table of J values. It has exactly two rows to alternate between for dynamic programming
    // purposes. It has 2 rows because I cannot modify the j values in the table while looping over
    // the states in value iteration, but I can at the end of an iteration. So instead of copying in
    // a temporary array, I just alternate between using two rows.
    private final double[][] jTable;

    MDP(List<State> states, double discountFactor) {
        this.states = states;
        this.discountFactor = discountFactor;
        final int jTableRowCount = 2; // two rows to alternate between for dynamic programming.
        jTable = new double[jTableRowCount][states.size()];
        bestActions = new int[states.size()];
    }

    void performValueIteration() {
        for (int i = 1; i <= NUM_OF_ITERATIONS; ++i) {
            // instead of copying a temp array into the j table at the end of an iteration,
            // I just alternate between two rows in a 2 x n state J value table.
            int j = i % 2;

            for (int state_i = 0; state_i < states.size(); ++state_i) {
                final State s = states.get(state_i);
                final int reward = s.getReward();

                if (i == 1) {
                    bestActions[state_i] = 1; // for the first iteration any action is fine.
                    jTable[j][state_i] = reward; // nothing to do on first iteration but collection rewards.
                } else {
                    Pair<Integer, Double> p = s.argMax(jTable[(i + 1) % 2]);
                    bestActions[state_i] = p.getLeft();
                    jTable[j][state_i] = reward + discountFactor * p.getRight();
                }
            }
            printIterationResult(i, j);
        }
    }

    private void printIterationResult(int i, int j) {
        System.out.println(String.format("After iteration %s:", i));
        DecimalFormat df = new DecimalFormat("0.0000");

        for (int state_i = 0; state_i < states.size(); ++state_i)
            System.out.print(String.format("(s%s a%s %s) ", (state_i + 1), bestActions[state_i], df.format(jTable[j][state_i])));

        System.out.println();
    }
}
