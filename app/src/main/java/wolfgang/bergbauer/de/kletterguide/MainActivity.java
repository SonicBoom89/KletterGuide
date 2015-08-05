package wolfgang.bergbauer.de.kletterguide;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.andraskindler.parallaxviewpager.ParallaxViewPager;

import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingContentProvider;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingDBHelper;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /* Tag for logging */
    public static final String TAG = MainActivity.class.getSimpleName();

    /* Init constants to identify loaders */
    private static final int URL_CLIMBINGAREAS_LOADER_ALL = 0;
    private static final int URL_ROUTES_LOADER_ALL = 1;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getLoaderManager().restartLoader(URL_CLIMBINGAREAS_LOADER_ALL, null, this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ParallaxViewPager viewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
        viewPager.setOverlapPercentage(0.25f);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
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

}
