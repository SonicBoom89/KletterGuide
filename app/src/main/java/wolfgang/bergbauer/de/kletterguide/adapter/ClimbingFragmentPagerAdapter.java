package wolfgang.bergbauer.de.kletterguide.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.fragments.ClimbingAreaPagerFragment;

/**
 * Created by Wolfgang on 04.08.2015.
 */
public class ClimbingFragmentPagerAdapter extends FragmentPagerAdapter {

    private int[] imageResId = {
            R.drawable.ic_terrain_white_24dp,
            R.drawable.ic_business_white_24dp,
            R.drawable.ic_history_white_24dp
    };

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Klettergebiete", "Kletterhallen", "Favoriten" };
    private Context context;

    public ClimbingFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ClimbingAreaPagerFragment.newInstance(ClimbingAreaType.OUTDOOR);
            case 1:
                return ClimbingAreaPagerFragment.newInstance(ClimbingAreaType.INDOOR);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}