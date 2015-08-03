package wolfgang.bergbauer.de.kletterguide;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingContentProvider;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingDBHelper;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoaderManager.LoaderCallbacks<Cursor> {

    /* Tag for logging */
    public static final String TAG = MainActivity.class.getSimpleName();

    /* Init constants to identify loaders */
    private static final int URL_CLIMBINGAREAS_LOADER_ALL = 0;
    private static final int URL_ROUTES_LOADER_ALL = 1;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private List<ClimbingArea> climbingAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClimbingDBHelper dbHelper = new ClimbingDBHelper(this);
                /*
                 * Get database to create the structure. This must be done manually, as the database
                 * is created with sql files, which needs a context
                */
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.close();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getLoaderManager().restartLoader(URL_CLIMBINGAREAS_LOADER_ALL, null, this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                //where = PaTrackDBHelper.COLUMN_BUSSTOPS_ID + "= ?";
                //whereArgs = new String[]{"007"};
                break;
            case URL_ROUTES_LOADER_ALL:
                queryUri = ClimbingContentProvider.ROUTES_URI;
                break;
            default:
        }

        // Create the new Cursor loader.
        return new CursorLoader(this, queryUri,
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
                climbingAreas = new ArrayList<>();
                while(data.moveToNext()) {
                    int climbingAreaIdColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_ID);
                    int climbingAreaNameColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_NAME);
                    int climbingAreaLatitudeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_LATITUDE);
                    int climbingAreaLongitudeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_LONGITUDE);
                    int climbingAreaRankingColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_RANKING);

                    int id = data.getInt(climbingAreaIdColumnIndex);
                    String name = data.getString(climbingAreaNameColumnIndex);
                    float latitude = data.getFloat(climbingAreaLatitudeColumnIndex);
                    float longitude = data.getFloat(climbingAreaLongitudeColumnIndex);
                    float ranking = data.getFloat(climbingAreaRankingColumnIndex);

                    ClimbingArea climbingArea = new ClimbingArea();
                    climbingArea.setId(id);
                    climbingArea.setName(name);
                    climbingArea.setLatitude(latitude);
                    climbingArea.setLongitude(longitude);
                    climbingArea.setRanking(ranking);

                    Log.d(TAG, "Climbing Area loaded: " + climbingArea);
                    climbingAreas.add(climbingArea);
                }
                break;
            case URL_ROUTES_LOADER_ALL:
                break;
            default:
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
