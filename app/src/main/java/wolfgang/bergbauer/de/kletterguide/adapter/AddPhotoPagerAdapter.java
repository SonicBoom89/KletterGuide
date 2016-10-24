package wolfgang.bergbauer.de.kletterguide.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import wolfgang.bergbauer.de.kletterguide.fragments.ChoosePhotoFragment;
import wolfgang.bergbauer.de.kletterguide.fragments.ChooseRouteFragment;
import wolfgang.bergbauer.de.kletterguide.fragments.DrawRouteFragment;

/**
 * Created by Wolfgang on 17.04.2016.
 */
public class AddPhotoPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 3;

    public AddPhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return ChoosePhotoFragment.newInstance();
            case 1: return ChooseRouteFragment.newInstance();
            case 2: return DrawRouteFragment.newInstance();
            default:
                return ChoosePhotoFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
