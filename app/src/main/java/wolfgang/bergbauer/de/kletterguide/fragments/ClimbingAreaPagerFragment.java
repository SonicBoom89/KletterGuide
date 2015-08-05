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

import java.util.ArrayList;
import java.util.List;

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
public class ClimbingAreaPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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
            getLoaderManager().restartLoader(URL_CLIMBINGAREAS_LOADER_ALL, null, this);
        } else {
            Log.e(TAG, "No ClimbingType specified");
        }
        climbingAreas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView gridView = (GridView) view.findViewById(R.id.gridView_climbing);

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

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        // Construct the new query in the form of a Cursor Loader. Use the id
        // parameter to contruct and return different loaders.

        String[] projection = null;
        String where = null;
        String[] whereArgs = null;
        String sortOrder = null;
        // Query URI
        Uri queryUri = null;
        switch (loaderID) {
            case URL_CLIMBINGAREAS_LOADER_ALL:
                queryUri = ClimbingContentProvider.CLIMBINGAREAS_URI;
                where = ClimbingDBHelper.COLUMN_CLIMBINGAREAS_TYPE + "= ?";
                whereArgs = new String[]{climbingAreaType.name()};
                break;
            default:
        }

        // Create the new Cursor loader.
        return new CursorLoader(getActivity(), queryUri,
                projection, where, whereArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Replace the result Cursor displayed by the Cursor Adapter with
        // the new result set.

        // This handler is not synchonrized with the UI thread, so you
        // will need to synchronize it before modiyfing any UI elements
        // directly.

        switch (loader.getId()) {
            case URL_CLIMBINGAREAS_LOADER_ALL:
                while(data.moveToNext()) {
                    int climbingAreaIdColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_ID);
                    int climbingAreaNameColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_NAME);
                    int climbingAreaLatitudeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_LATITUDE);
                    int climbingAreaLongitudeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_LONGITUDE);
                    int climbingAreaRankingColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_RANKING);
                    int climbingAreaTypeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_TYPE);
                    int climbingAreaImageColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_IMAGE_URL);
                    int climbingAreaDescriptionColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_DESCRIPTION);

                    int id = data.getInt(climbingAreaIdColumnIndex);
                    String name = data.getString(climbingAreaNameColumnIndex);
                    float latitude = data.getFloat(climbingAreaLatitudeColumnIndex);
                    float longitude = data.getFloat(climbingAreaLongitudeColumnIndex);
                    float ranking = data.getFloat(climbingAreaRankingColumnIndex);
                    String type = data.getString(climbingAreaTypeColumnIndex);
                    String imageUrl = data.getString(climbingAreaImageColumnIndex);
                    String description = data.getString(climbingAreaDescriptionColumnIndex);

                    ClimbingArea climbingArea = new ClimbingArea(ClimbingAreaType.valueOf(type));
                    climbingArea.setId(id);
                    climbingArea.setName(name);
                    climbingArea.setLatitude(latitude);
                    climbingArea.setLongitude(longitude);
                    climbingArea.setRanking(ranking);
                    climbingArea.setDrawableUrl(imageUrl);
                    climbingArea.setDescription(description);

                    Log.d(TAG, "Climbing Area loaded: " + climbingArea);
                    climbingAreas.add(climbingArea);
                    gridViewAdapter.notifyDataSetChanged();
                }
                break;
            default:
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
