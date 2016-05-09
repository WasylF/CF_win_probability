package wsl_f.cf_win_probability;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author Wsl_F
 */
public class CalcWinProbability {

    private final int division;
    private ArrayList<Integer> contestIds;

    private final int minRating;
    private final int maxRating;
    private final int shift;
    private final long[] total;
    private final long[] overcome;

    public CalcWinProbability(int division, int minRating, int maxRating, int minContestId, int maxContestId) {
        this.division = division;
        this.maxRating = maxRating;
        this.minRating = minRating;
        this.shift = maxRating - minRating;
        this.total = new long[maxRating + shift];
        this.overcome = new long[maxRating + shift];

        contestIds = CodeForcesAPI.getContestsList(division, minContestId, maxContestId);
    }

    public CalcWinProbability(int division, int minRating, int maxRating) {
        this(division, maxRating, minRating, 0, 1_000_000);
    }

    public void calulateProbability() {
        for (int contestId : contestIds) {
            addToStatistic(CodeForcesAPI.getContestResults(contestId));
        }
    }

    private void addToStatistic(ArrayList<Pair<Integer, Integer>> results) {
        int n = results.size();
        for (int i = 0; i < n; i++) {
            int rank_I = results.get(i).getKey();
            int rating_I = results.get(i).getValue();
            for (int j = i + 1; j < n; j++) {
                int rank_J = results.get(j).getKey();
                int rating_J = results.get(j).getValue();

                // probability J win I
                int ratingDiff = rating_I - rating_J;
                total[ratingDiff + shift]++;
                if (rank_J < rank_I) {
                    overcome[ratingDiff + shift]++;
                }

                // probability I win J
                ratingDiff = rating_J - rating_I;
                total[ratingDiff + shift]++;
                if (rank_I < rank_J) {
                    overcome[ratingDiff + shift]++;
                }
            }
        }
    }

    public void printStatistic() {
        System.out.println("Statistic for div. " + division + "contests:\nBEGIN\n");
        for (int i = shift; i < total.length; i++) {
            if (total[i] > 10) {
                double probability = (double) overcome[i] / (double) total[i];
                System.out.println((i - shift) + "\t" + probability);
            }
        }
        System.out.println("\n\nEND\nStatistic for div. " + division + "contests.");
    }

}
