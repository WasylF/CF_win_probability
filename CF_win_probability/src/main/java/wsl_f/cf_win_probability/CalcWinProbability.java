package wsl_f.cf_win_probability;

import java.util.ArrayList;

/**
 *
 * @author Wsl_F
 */
public class CalcWinProbability {

    private final int division;
    private ArrayList<Integer> contestIds;

    private final int maxRating;
    private final int minRating;
    private final int shift;
    private final long[] total;
    private final long[] overcome;

    public CalcWinProbability(int division, int maxRating, int minRating, int minContestId, int maxContestId) {
        this.division = division;
        this.maxRating = maxRating;
        this.minRating = minRating;
        this.shift = maxRating - minRating;
        this.total = new long[maxRating + shift];
        this.overcome = new long[maxRating + shift];

        contestIds = CodeForcesAPI.getContestsList(division, minContestId, maxContestId);
    }

    public CalcWinProbability(int division, int maxRating, int minRating) {
        this(division, maxRating, minRating, 0, 1_000_000);
    }

}
