package wolfgang.bergbauer.de.kletterguide;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andraskindler.parallaxviewpager.ParallaxViewPager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.adapter.SampleFragmentPagerAdapter;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingContentProvider;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingDBHelper;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingAreaType;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingBase;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /* Tag for logging */
    public static final String TAG = MainActivity.class.getSimpleName();

    /* Init constants to identify loaders */
    private static final int URL_CLIMBINGAREAS_LOADER_ALL = 0;
    private static final int URL_ROUTES_LOADER_ALL = 1;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_toolbar;
    private List<ClimbingBase> climbingAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                /*
                 * Get database to create the structure. This must be done manually, as the database
                 * is created with sql files, which needs a context
                */
        ClimbingDBHelper dbHelper = new ClimbingDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.close();
        setContentView(R.layout.activity_main);

        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        getLoaderManager().restartLoader(URL_CLIMBINGAREAS_LOADER_ALL, null, this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ParallaxViewPager viewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
        viewPager.setOverlapPercentage(0.25f);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        colorize();
    }

    private void colorize() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        Palette palette = Palette.generate(bm);
        int vibrant = palette.getVibrantColor(0x000000);
        int vibrantLight = palette.getLightVibrantColor(0x000000);
        int vibrantDark = palette.getDarkVibrantColor(0x000000);
        int muted = palette.getMutedColor(0x000000);
        int mutedLight = palette.getLightMutedColor(0x000000);
        int mutedDark = palette.getDarkMutedColor(0x000000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
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
                    int climbingAreaTypeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_TYPE);

                    int id = data.getInt(climbingAreaIdColumnIndex);
                    String name = data.getString(climbingAreaNameColumnIndex);
                    float latitude = data.getFloat(climbingAreaLatitudeColumnIndex);
                    float longitude = data.getFloat(climbingAreaLongitudeColumnIndex);
                    float ranking = data.getFloat(climbingAreaRankingColumnIndex);
                    String type = data.getString(climbingAreaTypeColumnIndex);

                    ClimbingBase climbingArea = new ClimbingBase(ClimbingAreaType.valueOf(type));
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
