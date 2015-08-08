package wolfgang.bergbauer.de.kletterguide.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.activities.FullScreenImageDialog;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingImage;

/**
 * Created by Wolfgang on 08.08.2015.
 */
public class ClimbingAreaInfoFragment extends Fragment {

    private static final String TAG = ClimbingAreaPagerFragment.class.getSimpleName();
    private static final String ARG_CLIMBING_AREA = ClimbingAreaInfoFragment.class.getSimpleName();

    public static ClimbingAreaInfoFragment newInstance(ClimbingArea area) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CLIMBING_AREA, area);
        ClimbingAreaInfoFragment fragment = new ClimbingAreaInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_climbing_area_info, container, false);

        final ClimbingArea climbingArea = getArguments().getParcelable(ARG_CLIMBING_AREA);
        if (climbingArea != null) {
            TextView title = (TextView) view.findViewById(R.id.txt_info_fragment_title);
            TextView routes = (TextView) view.findViewById(R.id.txt_info_fragment_routes);
            TextView desc = (TextView) view.findViewById(R.id.txt_info_fragment_description);

            title.setText(climbingArea.getName());
            routes.setText(climbingArea.getRoutes().size() + "");
            desc.setText(climbingArea.getDescription());
        } else {
            Log.e(TAG, "No ClimbingArea specified");
        }

        return view;
    }


}
