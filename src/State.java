import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a State in a markov decision process.
 */
class State {
    private final int reward;
    // A map where the key is an action a, state s.
    // For example, key "a1s2", value 0.5 means action 1 transitions to state 2 with probability 0.5.
    // Using a map here helps reduce the space time complexity since a 2d array of a * n.
    // usually has a lot less than a * n transitions.
    private final Map<String, Double> transitionProbMap;

    private State(int reward, Map<String, Double> transitionProbMap) {
        this.reward = reward;
        this.transitionProbMap = transitionProbMap;
    }

    /**
     * I'm using the builder pattern here to build immutable states.
     *
     * @param rewardAmount State reward
     * @return State builder.
     */
    static Builder Builder(int rewardAmount) {
        return new Builder(rewardAmount);
    }

    int getReward() {
        return reward;
    }

    Pair<Integer, Double> argMax(double[] jValues) {
        return getMaxRewardActionAndJvalue(getExpectedFutureRewards(jValues));
    }

    private Map<Integer, Double> getExpectedFutureRewards(double[] jValues) {
        Map<Integer, Double> expectedFutureRewards = new HashMap<>();

        for (Map.Entry<String, Double> entry : transitionProbMap.entrySet()) {
            final String key = entry.getKey();
            final int sIndex = key.indexOf('s');
            final int actionNum = Integer.parseInt(key.substring(key.indexOf('a') + 1, sIndex));
            final int stateNum = Integer.parseInt(key.substring(sIndex + 1, key.length()));

            double transitionProbability = entry.getValue();
            if (expectedFutureRewards.containsKey(actionNum)) {
                double currentExpectation = expectedFutureRewards.get(actionNum);
                expectedFutureRewards.put(actionNum, currentExpectation + transitionProbability * jValues[stateNum - 1]);
            } else {
                expectedFutureRewards.put(actionNum, transitionProbability * jValues[stateNum - 1]);
            }
        }
        return expectedFutureRewards;
    }

    /**
     * @param expectedFutureRewards map of actions to
     * @return action number and J value pair.
     */
    private Pair<Integer, Double> getMaxRewardActionAndJvalue(Map<Integer, Double> expectedFutureRewards) {
        int bestAction = 1;
        Double maxExpected = null;
        for (Map.Entry<Integer, Double> entry : expectedFutureRewards.entrySet()) {
            if (maxExpected == null || entry.getValue() > maxExpected) {
                bestAction = entry.getKey();
                maxExpected = entry.getValue();
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
