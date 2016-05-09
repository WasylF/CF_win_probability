package wsl_f.cf_win_probability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wsl_F
 */
public class CodeForcesAPI {

    /**
     * delay before send request to codeforces. Value in milliseconds.
     */
    final static int API_DELAY_MS = 250;

    /**
     *
     * @param contestId codeforces contest id (!!! not equals cf round number)
     * @return rating changes
     */
    public static JSONObject getRatingChanges(int contestId) {
        try {
            TimeUnit.MILLISECONDS.sleep(API_DELAY_MS);
            JSONObject obj = JsonReader.read("http://codeforces.com/api/contest.ratingChanges?contestId=" + contestId);
            if (obj != null) {
                if (obj.getString("status").equals("OK")) {
                    return obj;
                }
            }
        } catch (InterruptedException | IOException | JSONException exception) {
            System.err.println("Failed contestId: " + contestId);
            System.err.println(exception.getMessage());
        }

        return null;
    }

    /**
     *
     * @param division number of division (1 or 2)
     * @param minId minimum number of contest id
     * @param maxId maximum number of contest id
     * @return list of finished contests, id in range
     * [{@code minId},{@code  maxId}]
     */
    public static ArrayList<Integer> getContestsList(int division, int minId, int maxId) {
        ArrayList<Integer> contestsIds = new ArrayList<>();
        String goodEnd = "(Div. " + division + ")";
        try {
            TimeUnit.MILLISECONDS.sleep(API_DELAY_MS);
            JSONObject obj = JsonReader.read("http://codeforces.com/api/contest.list?gym=false");

            if (obj != null && obj.getString("status").equals("OK")) {
                JSONArray array = obj.getJSONArray("result");
                for (int i = array.length() - 1; i >= 0; i--) {
                    JSONObject contest = (JSONObject) array.get(i);
                    if (contest.getString("phase").equals("FINISHED")
                            && contest.getString("type").equals("CF")
                            && contest.getString("name").endsWith(goodEnd)) {
                        int contestId = contest.getInt("id");
                        if (contestId >= minId && contestId <= maxId) {
                            contestsIds.add(contestId);
                        }
                    }
                }
            }

        } catch (InterruptedException | IOException | JSONException ex) {
            System.err.println("Failed get contests list");
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }

        return contestsIds;
    }

    /**
     *
     * @param userHandle user's handle on codeforces
     * @return number of contests that {@code userHandle} have particapated
     */
    public static int getContestNumber(String userHandle) {
        try {
            TimeUnit.MILLISECONDS.sleep(API_DELAY_MS);
            JSONObject obj = JsonReader.read("http://codeforces.com/api/user.rating?handle=" + userHandle);

            if (obj != null && obj.getString("status").equals("OK")) {
                JSONArray array = obj.getJSONArray("result");
                int contestNumber = array.length();
                return contestNumber;
            }
        } catch (InterruptedException | IOException | JSONException ex) {
            System.err.println("Failed get contests number for user: " + userHandle);
            System.err.println(ex.getMessage());
            return 0;
        }

        return 0;
    }

    /**
     * get users rank and rating by contestId.
     *
     * If get any Exception, return empty list.
     *
     * @param contestId contestId (!!!not number of cf round)
     * @return ArrayList<Pair<user rank in contest, user rating before contest>>
     */
    public static ArrayList<Pair<Integer, Integer>> getContestResults(int contestId) {
        return getContestResults(contestId, 0);
    }

    /**
     * get users rank and rating by contestId.
     *
     * If get any Exception, return empty list.
     *
     * @param contestId contestId (!!!not number of cf round)
     * @param minParticipationNumb minimum number of contests that user should
     * particapated
     * @return ArrayList<Pair<user rank in contest, user rating before contest>>
     */
    public static ArrayList<Pair<Integer, Integer>> getContestResults(int contestId, int minParticipationNumb) {
        ArrayList<Pair<Integer, Integer>> results = new ArrayList<>();

        try {
            JSONObject obj = getRatingChanges(contestId);
            if (obj == null) {
                return results;
            }

            JSONArray array = obj.getJSONArray("result");

            int l = array.length();
            for (int i = 0; i < l; i++) {
                JSONObject user = (JSONObject) array.get(i);
                if (getContestNumber(user.getString("handle")) > minParticipationNumb) {
                    Pair<Integer, Integer> pair = new Pair<>(user.getInt("rank"), user.getInt("oldRating"));
                    results.add(pair);
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed get contest results! contestId: " + contestId);
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }

        return results;
    }

}
