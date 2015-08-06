package wolfgang.bergbauer.de.kletterguide.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.activities.ClimbingAreaDetailsActivity;
import wolfgang.bergbauer.de.kletterguide.activities.FullScreenImageDialog;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingImage;

/**
 * Created by berg21 on 06.08.2015.
 */
public class HeaderImageFragment extends Fragment {

    public static final String ARG_IMAGE = "image";

    private static final String TAG = ClimbingAreaPagerFragment.class.getSimpleName();

    public static HeaderImageFragment newInstance(ClimbingImage image) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE, image);
        HeaderImageFragment fragment = new HeaderImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    boolean isImageFitToScreen;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_image, container, false);

        final ClimbingImage climbingImage = getArguments().getParcelable(ARG_IMAGE);
        if (climbingImage != null) {

            TextView txt = (TextView) view.findViewById(R.id.textView_details_desc);
            txt.setText(climbingImage.getDescription());

            final ImageView image = (ImageView) view.findViewById(R.id.imageView_details_header);

            if (climbingImage.getImageUrl() != null) {
                try {
                    Drawable d = Drawable.createFromStream(getActivity().getAssets().open(climbingImage.getImageUrl()), null);
                    image.setImageDrawable(d);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), FullScreenImageDialog.class);
                    i.putExtra(ARG_IMAGE, climbingImage);

                    String transitionName = getString(R.string.transition_climbing_area);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                    v,   // The view which starts the transition
                                    transitionName    // The transitionName of the view we’re transitioning to
                            );
                    ActivityCompat.startActivity(getActivity(), i, options.toBundle());
                }
            });

        } else {
            Log.e(TAG, "No ImageId specified");
        }

        return view;
    }


}
