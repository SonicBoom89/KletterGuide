package wolfgang.bergbauer.de.kletterguide.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import wolfgang.bergbauer.de.kletterguide.fragments.ClimbingAreaInfoFragment;
import wolfgang.bergbauer.de.kletterguide.fragments.HeaderImageFragment;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;

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