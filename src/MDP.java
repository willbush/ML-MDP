import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

class MDP {
    // This project has a predefined number of iterations to perform.
    private static final int NUM_OF_ITERATIONS = 20;
    private final List<State> states;
    private final double discountFactor;

    MDP(List<State> states, double discountFactor) {
        this.states = states;
        this.discountFactor = discountFactor;
    }

    void performValueIteration() {
        double[] jValues = new double[states.size()];
        int[] bestActions = new int[jValues.length];

        for (int i = 1; i <= NUM_OF_ITERATIONS; ++i) {

            double[] tempJValues = new double[jValues.length];
            for (int state_i = 0; state_i < states.size(); ++state_i) {
                State s = states.get(state_i);
                int r = s.getRewardAmount();
                Pair<Integer, Double> p = s.argMax(jValues, states.size(), i);

                bestActions[state_i] = p.getLeft();
                tempJValues[state_i] = r + (discountFactor * p.getRight());
            }
            jValues = Arrays.copyOf(tempJValues, tempJValues.length);

            System.out.println(String.format("After iteration %s:", i));
            for (int state_i = 0; state_i < states.size(); ++state_i) {
                DecimalFormat df = new DecimalFormat("0.0000");
                df.setRoundingMode(RoundingMode.HALF_EVEN);
                System.out.print(String.format("(s%s a%s %s) ", (state_i + 1), bestActions[state_i], df.format(jValues[state_i])));
            }
            System.out.println();
        }
    }
}
