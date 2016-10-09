import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class State {
    private final int rewardAmount;
    private final Map<String, Double> transitionProbMap;

    private State(int rewardAmount, Map<String, Double> transitionProbMap) {
        this.rewardAmount = rewardAmount;
        this.transitionProbMap = transitionProbMap;
    }

    static Builder Builder(int rewardAmount) {
        return new Builder(rewardAmount);
    }

    int getRewardAmount() {
        return rewardAmount;
    }

    Pair<Integer, Double> argMax(double[] jValues, int numOfActions, int i) {
        double maxExpected = 0.0;
        int bestAction = 1;

        if (i == 1)
            return new Pair<>(bestAction, maxExpected);


        for (int a = 1; a <= numOfActions; ++a) {
            double expected = 0.0;
            for (int s = 1; s <= jValues.length; ++s) {
                String key = "a" + a + "s" + s;
                if (transitionProbMap.containsKey(key)) {
                    double prob = transitionProbMap.get(key);
                    expected += prob * jValues[s - 1];
                }
            }
            if (expected > maxExpected) {
                bestAction = a;
                maxExpected = expected;
            }
        }
        return new Pair<>(bestAction, maxExpected);
    }

    static class Builder {
        private final int rewardAmount;
        private final Map<String, Double> transitionProbMap;

        Builder(int rewardAmount) {
            this.rewardAmount = rewardAmount;
            transitionProbMap = new HashMap<>();
        }

        void put(String actionToStateKey, Double transitionProbability) {
            transitionProbMap.put(actionToStateKey, transitionProbability);
        }

        State build() {
            return new State(rewardAmount, Collections.unmodifiableMap(transitionProbMap));
        }
    }
}
