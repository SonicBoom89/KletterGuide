package wolfgang.bergbauer.de.kletterguide.activities;

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.adapter.ClimbingFragmentPagerAdapter;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingDBHelper;


public class MainActivity extends ToolbarActivity {

    /* Tag for logging */
    public static final String TAG = MainActivity.class.getSimpleName();

    private CollapsingToolbarLayout collapsing_toolbar;

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
        enableToolbar();

        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ClimbingFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

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


}
