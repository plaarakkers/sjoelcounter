package nl.gellygwin.imageprocessing.sjoelengui.models;

import java.util.Map;

/**
 *
 * RoundResult
 */
public class RoundResult {

    private final int round;

    private final int poort1Count;

    private final int poort2Count;

    private final int poort3Count;

    private final int poort4Count;

    private final int score;

    public RoundResult(int round, Map<Integer, Integer> amountInPoorten) {
        this.round = round;
        poort1Count = amountInPoorten.getOrDefault(1, 0);
        poort2Count = amountInPoorten.getOrDefault(2, 0);
        poort3Count = amountInPoorten.getOrDefault(3, 0);
        poort4Count = amountInPoorten.getOrDefault(4, 0);

        final int baseAmount = amountInPoorten.size() == 4 ? amountInPoorten.values().stream().min(Integer::compare).orElse(0) : 0;

        score = baseAmount * 20 + amountInPoorten.entrySet().stream().mapToInt(e -> (e.getValue() - baseAmount) * e.getKey()).sum();
    }

    /**
     * @return the round
     */
    public int getRound() {
        return round;
    }

    /**
     * @return the poort1Count
     */
    public int getPoort1Count() {
        return poort1Count;
    }

    /**
     * @return the poort2Count
     */
    public int getPoort2Count() {
        return poort2Count;
    }

    /**
     * @return the poort3Count
     */
    public int getPoort3Count() {
        return poort3Count;
    }

    /**
     * @return the poort4Count
     */
    public int getPoort4Count() {
        return poort4Count;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }
}
