package wolfgang.bergbauer.de.kletterguide;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Wolfgang on 07.08.2015.
 */
public class RatingHelper {

    public static final int MAX_RATING_VALUE = 5;

    public static final int RATING_EMPTY_ICON = R.drawable.star_empty;
    public static final int RATING_HALF_ICON = R.drawable.star_half;
    public static final int RATING_FULL_ICON = R.drawable.star;
    private static final double DISPLAY_HALF_RATING_SYMBOL_LIMIT = 0.5;
    private static final int DEFAULT_RATING_ICON_SIZE_DP = 10;

    public static LinearLayout getRatingIconView(Context context, float ratingValue) {
        return getRatingIconView(context, ratingValue, DEFAULT_RATING_ICON_SIZE_DP);
    }
    
    public static LinearLayout getRatingIconView(Context context, float ratingValue, int iconSizeinDp)
    {
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (ratingValue > MAX_RATING_VALUE)
        {
            for (int i = 0; i < MAX_RATING_VALUE; ++i)
            {
                ImageView icon = createRatingIcon(context, RATING_FULL_ICON, iconSizeinDp);
                container.addView(icon);
            }
        } else if (ratingValue < 0.5) {
            for (int i = 0; i < MAX_RATING_VALUE; ++i)
            {
                ImageView icon = createRatingIcon(context, RATING_EMPTY_ICON, iconSizeinDp);
                container.addView(icon);
            }
        } else {
            //Set full stars first
            for (int i = 0; i < (int) ratingValue; ++i)
            {
                ImageView icon = createRatingIcon(context, RATING_FULL_ICON, iconSizeinDp);
                container.addView(icon);
            }
            //Set half star
            double floatingPointValue = Math.abs(ratingValue - Math.floor(ratingValue));
            if (floatingPointValue >= DISPLAY_HALF_RATING_SYMBOL_LIMIT)
            {
                ImageView icon = createRatingIcon(context, RATING_HALF_ICON, iconSizeinDp);
                container.addView(icon);
            } else {
                ImageView icon = createRatingIcon(context, RATING_EMPTY_ICON, iconSizeinDp);
                container.addView(icon);
            }
            //Fill rest with empty icons
            for (int i = container.getChildCount(); i < MAX_RATING_VALUE; ++i)
            {
                ImageView icon = createRatingIcon(context, RATING_EMPTY_ICON, iconSizeinDp);
                container.addView(icon);
            }
        }

        return container;
    }

    private static ImageView createRatingIcon(Context context, int iconResource, int iconSizeinDp) {
        ImageView ratingIcon = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = (int) ScreenUtil.convertDpToPixel(iconSizeinDp, context);
        ratingIcon.setLayoutParams(params);
        ratingIcon.setImageDrawable(context.getResources().getDrawable(iconResource));
        return ratingIcon;
    }

}
