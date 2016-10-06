package wolfgang.bergbauer.de.kletterguide.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.activities.DrawOnBitmapActivity;

/**
 * Created by Wolfgang on 17.04.2016.
 */
public class DrawRouteFragment extends Fragment {

    private Button paintRoute;
    public static int REQUEST_PAINT_ROUTE = 3;

    public static Fragment newInstance() {
        return new DrawRouteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_draw_route, container, false);
        //paintRoute = (Button) rootView.findViewById(R.id.button_topo);

        //paintRoute.setOnClickListener(new View.OnClickListener() {
          //  @Override
          // public void onClick(View v) {
                //Intent intent = new Intent(ClimbingPhotoActivity.this, DrawOnBitmapActivity.class);
                //intent.putExtra(ARG_PHOTO_URI, croppedOutputFileUri);
                //startActivityForResult(intent, REQUEST_PAINT_ROUTE);
            //}
       // });
        return rootView;
    }
}
