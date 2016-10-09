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

    Pair<Integer, Double> argMax(double[] jValues) {
        int bestAction = 1;

        Map<Integer, Double> expectedFutureRewards = new HashMap<>();

        for (Map.Entry<String, Double> entry : transitionProbMap.entrySet()) {
            final String key = entry.getKey();
            final int sIndex = key.indexOf('s');
            final int actionNum = Integer.parseInt(key.substring(key.indexOf('a') + 1, sIndex));
            final int stateNum = Integer.parseInt(key.substring(sIndex + 1, key.length()));

            double transitionProbability = entry.getValue();
            if (expectedFutureRewards.containsKey(actionNum)) {
                double currentRewards = expectedFutureRewards.get(actionNum);
                expectedFutureRewards.put(actionNum, currentRewards + transitionProbability * jValues[stateNum - 1]);
            } else {
                expectedFutureRewards.put(actionNum, transitionProbability * jValues[stateNum - 1]);
            }
        }

        Double maxExpected = null;
        for (Map.Entry<Integer, Double> entry : expectedFutureRewards.entrySet()) {
            if (maxExpected == null) {
                bestAction = entry.getKey();
                maxExpected = entry.getValue();
            } else if (entry.getValue() > maxExpected) {
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
