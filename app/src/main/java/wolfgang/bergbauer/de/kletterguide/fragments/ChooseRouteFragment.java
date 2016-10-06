package wolfgang.bergbauer.de.kletterguide.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wolfgang.bergbauer.de.kletterguide.R;

/**
 * Created by Wolfgang on 17.04.2016.
 */
public class ChooseRouteFragment extends Fragment {

    public static Fragment newInstance() {
        return new ChooseRouteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_choose_route, container, false);

        return rootView;
    }

}
