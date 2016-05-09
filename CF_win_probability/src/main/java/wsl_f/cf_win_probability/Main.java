package wsl_f.cf_win_probability;

/**
 *
 * @author Wsl_F
 */
public class Main {

    public static void main(String[] args) {
        // MinContestId 343 - CF round 200
        CalcWinProbability calcWinProbability_div1 = new CalcWinProbability(1, -500, 4000, 343, 1000);
        CalcWinProbability calcWinProbability_div2 = new CalcWinProbability(2, -500, 4000, 343, 1000);

        calcWinProbability_div1.calulateProbability();
        calcWinProbability_div1.printStatistic();

        calcWinProbability_div2.calulateProbability();
        calcWinProbability_div2.printStatistic();
    }

}
