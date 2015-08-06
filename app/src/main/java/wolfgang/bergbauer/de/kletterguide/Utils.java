package wolfgang.bergbauer.de.kletterguide;

/**
 * Created by Wolfgang on 06.08.2015.
 */
public class Utils {

    public static int trimUIAARank(String difficulty) {
        String trimmedDifficulty = difficulty.replaceAll("[\\+\\-abc]", "");
        int routeDifficulty = Integer.parseInt(trimmedDifficulty);
        return routeDifficulty;
    }

    public static String getUniqueImageFilename() {
        return "img_" + System.currentTimeMillis();
    }
}
