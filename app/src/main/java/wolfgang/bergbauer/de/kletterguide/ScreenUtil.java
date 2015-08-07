package wolfgang.bergbauer.de.kletterguide;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Wolfgang on 24.02.2015.
 */
public class ScreenUtil {

    public static float getDisplayWidthInPx(Context c)
    {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static float getDisplayWidthInDp(Context c)
    {

        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return dpWidth;
    }

    public static float getDisplayHeightInDp(Context c)
    {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        return dpHeight;
    }

    public static boolean checkScreenWidth(Context c, LinearLayout ll) {
        return checkScreenWidth(c, ll, 0);
    }

    public static boolean checkScreenWidth(Context c, LinearLayout ll, int margins) {
        int measureSpec = View.MeasureSpec.UNSPECIFIED;
        ll.measure(measureSpec,measureSpec);
        int viewWidthPx = ll.getMeasuredWidth() + margins;
        float screenWidth =  ScreenUtil.getDisplayWidthInPx(c);
        boolean viewFitsInLayout = viewWidthPx < screenWidth;
        return viewFitsInLayout;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
