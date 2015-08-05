package wolfgang.bergbauer.de.kletterguide.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import wolfgang.bergbauer.de.kletterguide.PageFragment;
import wolfgang.bergbauer.de.kletterguide.R;

/**
 * Created by Wolfgang on 04.08.2015.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private int[] imageResId = {
            R.drawable.ic_outdoor,
            R.drawable.ic_outdoor,
            android.R.drawable.ic_menu_info_details,
    };

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Klettergebiete", "Kletterhallen", "Profil" };
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
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