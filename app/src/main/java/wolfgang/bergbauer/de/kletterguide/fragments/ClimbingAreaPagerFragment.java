package wolfgang.bergbauer.de.kletterguide.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wolfgang.bergbauer.de.kletterguide.AppConstants;
import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.activities.ClimbingAreaDetailsActivity;
import wolfgang.bergbauer.de.kletterguide.adapter.ClimbingGridViewAdapter;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingContentProvider;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingDBHelper;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;

/**
 * Created by berg21 on 05.08.2015.
 */
public class ClimbingAreaPagerFragment extends Fragment {

    private static final String ARG_CLIMBING_AREA_TYPE = "climbing_area_type";

    /* Init constants to identify loaders */
    private static final int URL_CLIMBINGAREAS_LOADER_ALL = 0;
    private static final String TAG = ClimbingAreaPagerFragment.class.getSimpleName();

    private ClimbingAreaType climbingAreaType;
    private List<ClimbingArea> climbingAreas;
    private ClimbingGridViewAdapter gridViewAdapter;

    public static ClimbingAreaPagerFragment newInstance(ClimbingAreaType type) {
        Bundle args = new Bundle();
        args.putString(ARG_CLIMBING_AREA_TYPE, type.name());
        ClimbingAreaPagerFragment fragment = new ClimbingAreaPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String arg = getArguments().getString(ARG_CLIMBING_AREA_TYPE);
        if (arg != null) {

            climbingAreaType = ClimbingAreaType.valueOf(arg);
            Bundle args = new Bundle();
            args.putString(ARG_CLIMBING_AREA_TYPE, climbingAreaType.name());

            //getLoaderManager().restartLoader(URL_CLIMBINGAREAS_LOADER_ALL, args, this);
        } else {
            Log.e(TAG, "No ClimbingType specified");
        }
    }

    private List<ClimbingArea> loadClimbingAreas(ClimbingAreaType climbingAreaType) {
        final List<ClimbingArea> result = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String selectedDatabase;
        switch (climbingAreaType) {
            case OUTDOOR:
                selectedDatabase = "climbinareas";
                break;
            case INDOOR:
                selectedDatabase = "climbinghalls";
                break;
            default:
                selectedDatabase = "climbingareas";
        }
        DatabaseReference ref = database.getReference(selectedDatabase);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClimbingArea area = postSnapshot.getValue(ClimbingArea.class);
                    climbingAreas.add(area);
                }
                gridViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error", databaseError.toException());
            }
        });
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView gridView = (GridView) view.findViewById(R.id.gridView_climbing);

        climbingAreas = new ArrayList<>();
        gridViewAdapter = new ClimbingGridViewAdapter(getActivity(), climbingAreas);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ClimbingAreaDetailsActivity.class);
                ClimbingArea climbingarea = (ClimbingArea) gridView.getAdapter().getItem(position);
                intent.putExtra(AppConstants.EXTRA_CLIMBING_AREA, climbingarea);

                String transitionName = getString(R.string.transition_climbing_area);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                view,   // The view which starts the transition
                                transitionName    // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        });

        loadClimbingAreas(climbingAreaType);
        return view;
    }
}
