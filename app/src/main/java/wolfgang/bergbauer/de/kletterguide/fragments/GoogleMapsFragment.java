package wolfgang.bergbauer.de.kletterguide.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Wolfgang on 08.08.2015.
 */
public class GoogleMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    /* Tag for logging */
    public static final String TAG = GoogleMapsFragment.class.getSimpleName();
    private static final String ARG_NAME = "name";
    private static final String ARG_LAT = "latitude";
    private static final String ARG_LNG = "longitude";

    private String name;
    private float latitude;
    private float longitude;

    private GoogleMap map;

    public static GoogleMapsFragment newInstance(String name, float latitude, float longitude) {
        GoogleMapsFragment mapFragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putFloat(ARG_LAT, latitude);
        args.putFloat(ARG_LNG, longitude);
        return mapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null && containsMapInfo(savedInstanceState)) {
            name = savedInstanceState.getString(ARG_NAME);
            latitude = savedInstanceState.getFloat(ARG_LAT);
            longitude = savedInstanceState.getFloat(ARG_LNG);
            getMapAsync(this);
        } else {
            //TODO Show Error if no Location is available
        }

        return view;
    }

    private boolean containsMapInfo(Bundle args) {
        return args.containsKey(ARG_NAME) && args.containsKey(ARG_LAT) && args.containsKey(ARG_LNG);
    }

    private void initMap(String name, float latitude, float longitude) {
        if (map == null) {
            Toast.makeText(getActivity(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
            getActivity().finish();
        } else {
            map.clear();

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            android.location.Location myLoc = map.getMyLocation();
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude))      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(10)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            LocationManager service = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initMap(name, latitude, longitude);
    }
}
