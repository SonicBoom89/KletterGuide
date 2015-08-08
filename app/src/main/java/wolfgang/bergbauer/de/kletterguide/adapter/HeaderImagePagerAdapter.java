package wolfgang.bergbauer.de.kletterguide.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.List;

import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.activities.ClimbingAreaDetailsActivity;
import wolfgang.bergbauer.de.kletterguide.fragments.ClimbingAreaInfoFragment;
import wolfgang.bergbauer.de.kletterguide.fragments.ClimbingAreaPagerFragment;
import wolfgang.bergbauer.de.kletterguide.fragments.GoogleMapsFragment;
import wolfgang.bergbauer.de.kletterguide.fragments.HeaderImageFragment;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingImage;

/**
 * Created by berg21 on 06.08.2015.
 */
public class HeaderImagePagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ClimbingArea climbingArea;

    public HeaderImagePagerAdapter(FragmentManager fm, Context context, ClimbingArea climbingArea) {
        super(fm);
        this.context = context;
        this.climbingArea = climbingArea;
    }

    @Override
    public int getCount() {
        return climbingArea.getImages().size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            return ClimbingAreaInfoFragment.newInstance(climbingArea);
        } else
            return HeaderImageFragment.newInstance(climbingArea.getImages().get(position));
        // return GoogleMapsFragment.newInstance(climbingArea.getName(), climbingArea.getLatitude(), climbingArea.getLongitude());
    }

}