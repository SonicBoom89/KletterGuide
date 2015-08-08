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

    /**
     * This method is used to convert the integer coord used by PTE to the float coord
     * used by google
     * @param lat the int coord
     * @return the float coord
     */
    public static float createGoogleCoords(int lat) {
        return (((float) lat) / 1000000);
    }

}
