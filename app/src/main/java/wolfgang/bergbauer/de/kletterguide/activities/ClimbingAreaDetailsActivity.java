package wolfgang.bergbauer.de.kletterguide.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import wolfgang.bergbauer.de.kletterguide.AppConstants;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.adapter.ClimbingFragmentPagerAdapter;
import wolfgang.bergbauer.de.kletterguide.adapter.HeaderImagePagerAdapter;
import wolfgang.bergbauer.de.kletterguide.adapter.RouteCardViewAdapter;
import wolfgang.bergbauer.de.kletterguide.adapter.TransitionAdapter;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingContentProvider;
import wolfgang.bergbauer.de.kletterguide.dataaccess.ClimbingDBHelper;
import wolfgang.bergbauer.de.kletterguide.formatter.MyValueFormatter;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingArea;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingImage;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingRoute;

/**
 * Created by berg21 on 05.08.2015.
 */
public class ClimbingAreaDetailsActivity extends AppCompatActivity implements OnChartValueSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {


    /* Init constants to identify loaders */
    private static final int URL_ROUTES_LOADER_ALL = 0;
    private static final String TAG = ClimbingAreaDetailsActivity.class.getSimpleName();
    
    private static final int MIN_UIAA_LEVEL_CHART = 2;
    private static final int MAX_UIAA_LEVEL_CHART = 12;
    protected BarChart mChart;

    private List<ClimbingRoute> selectedRoutes = new ArrayList<>();
    private ClimbingArea selectedArea;

    private RouteCardViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.climbing_area_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        selectedArea = getIntent().getExtras().getParcelable(AppConstants.EXTRA_CLIMBING_AREA);
        selectedArea.setRoutes(new ArrayList<ClimbingRoute>());//TODO Load with loader


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_climbing_details);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(selectedArea.getName());

        initViewPagerHeader();
        initChart(selectedArea);
        initRouteList(selectedArea);

        /*
        ImageView imageView = (ImageView) findViewById(R.id.imageView_climbing_area_details);
        if (selectedArea.getDrawableUrl() != null) {
            try {
                Drawable d = Drawable.createFromStream(getAssets().open(selectedArea.getDrawableUrl()), null);
                imageView.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
       }

          */
        animateView();
        getSupportLoaderManager().restartLoader(URL_ROUTES_LOADER_ALL, null, this);
    }

    private void initViewPagerHeader() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_details_header);
        List<ClimbingImage> images = new ArrayList<>();
        for (int i = 0; i < 5; ++i)
        {
            ClimbingImage image = new ClimbingImage();
            image.setClimbingId(i);
            image.setImageUrl(i % 2 == 0 ? "riederin.jpg" : "riederin2.jpg");
            image.setDescription(i % 2 == 0 ? "Beschreibung 1" : "Beschreibung 2");
            images.add(image);
        }
        viewPager.setAdapter(new HeaderImagePagerAdapter(getSupportFragmentManager(), ClimbingAreaDetailsActivity.this, images));

    }

    private void initRouteList(ClimbingArea selectedArea) {
        RecyclerView recList = (RecyclerView)
                findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recyclerViewAdapter = new RouteCardViewAdapter(selectedRoutes);
        recList.setAdapter(recyclerViewAdapter);
        recList.setItemAnimator(new SlideInUpAnimator());

        recyclerViewAdapter.notifyItemRangeChanged(0, selectedRoutes.size());
    }

    private void initChart(ClimbingArea climbingArea) {
        // Init first chart
        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.getLegend().setEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                return false;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().getEnterTransition().addListener(new TransitionAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {

                    findViewById(R.id.fab).animate().alpha(1.0f);
                    //findViewById(R.id.linearlayout_imageDesc).animate().alpha(1.0f);
                    //getWindow().getEnterTransition().removeListener(this);
                }
            });
        } else {
            findViewById(R.id.fab).animate().alpha(1.0f).start();
           // findViewById(R.id.linearlayout_imageDesc).animate().alpha(1.0f).start();
        }
    }

    private void setChartData(ClimbingArea ClimbingArea) {

        HashMap<Integer, Integer> difficultyMap =new HashMap<>();
        for (int i = MIN_UIAA_LEVEL_CHART; i < MAX_UIAA_LEVEL_CHART; ++i)
        {
            difficultyMap.put(i, 0);
        }

        for (ClimbingRoute route : ClimbingArea.getRoutes())
        {
            String difficulty = route.getUIAARank();
            int routeDifficulty = trimUIAARank(difficulty);
            if (difficultyMap.containsKey(routeDifficulty))
            {
                difficultyMap.put(routeDifficulty, difficultyMap.get(routeDifficulty) + 1);
            }
        }

        ArrayList<BarEntry> routeDifficulties = new ArrayList<BarEntry>();
        int indexCounter = 0;
        String[] xVals = new String[difficultyMap.size()];
        List<Integer> orderedList = new ArrayList<>(difficultyMap.keySet());
        Collections.sort(orderedList);
        for (Integer difficulty : orderedList)
        {
            xVals[indexCounter] = difficulty + "";
            BarEntry entry = new BarEntry(difficultyMap.get(difficulty), indexCounter++ , difficulty +"");
            routeDifficulties.add(entry);
        }

        BarDataSet set1 = new BarDataSet(routeDifficulties, "Schwierigkeiten");
        set1.setColor(getResources().getColor(R.color.primary_700));
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);

        mChart.setData(data);
        mChart.animateX(1000);
        mChart.animateY(1000);
    }

    private int trimUIAARank(String difficulty) {
        String trimmedDifficulty = difficulty.replaceAll("[\\+\\-abc]", "");
        int routeDifficulty = Integer.parseInt(trimmedDifficulty);
        return routeDifficulty;
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        int selectedDifficulty = Integer.parseInt((String) e.getData());

        clearSelectedRoutes();

       for (ClimbingRoute route : selectedArea.getRoutes())
       {
           if (trimUIAARank(route.getUIAARank()) == selectedDifficulty)
               selectedRoutes.add(route);
       }
    }

    private void clearSelectedRoutes() {
        int size = selectedRoutes.size();
        for (int i = 0; i < size; ++i)
        {
            selectedRoutes.remove(0);
            recyclerViewAdapter.notifyItemRemoved(i);
        }
    }

    public void onNothingSelected() {
        clearSelectedRoutes();
        selectedRoutes.addAll(selectedArea.getRoutes());
    };

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
            case URL_ROUTES_LOADER_ALL:
                queryUri = ClimbingContentProvider.ROUTES_URI;
                where = ClimbingDBHelper.COLUMN_ROUTES_CLIMBINGAREA_ID + "= ?";
                whereArgs = new String[]{String.valueOf(selectedArea.getId())};
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
            case URL_ROUTES_LOADER_ALL:
                while(data.moveToNext()) {
                    
                    int climbingRouteIdColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_ID);
                    int climbingRouteNameColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_NAME);
                    int climbingRouteLatitudeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_LATITUDE);
                    int climbingRouteLongitudeColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_LONGITUDE);
                    int climbingRouteRankingColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_RANKING);
                    int climbingRouteImageColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_IMAGE_URL);
                    int climbingRouteDifficultyColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_UIAA);
                    int climbingRouteFollowClimbColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_FOLLOW_CLIMB_ACCOMPLISHED);
                    int climbingRouteLeadingClimbColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_LEADING_CLIMB_ACCOMPLISHED);
                    int climbingRouteRouteAccomplishedColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_ROUTE_ACCOMPLISHED);
                    int climbingRouteClimbingAreaColumnIndex = data.getColumnIndex(ClimbingDBHelper.COLUMN_ROUTES_CLIMBINGAREA_ID);

                    int id = data.getInt(climbingRouteIdColumnIndex);
                    String name = data.getString(climbingRouteNameColumnIndex);
                    float latitude = data.getFloat(climbingRouteLatitudeColumnIndex);
                    float longitude = data.getFloat(climbingRouteLongitudeColumnIndex);
                    float ranking = data.getFloat(climbingRouteRankingColumnIndex);
                    String difficulty = data.getString(climbingRouteDifficultyColumnIndex);
                    String imageUrl = data.getString(climbingRouteImageColumnIndex);
                    boolean followClimbAccomplished = data.getInt(climbingRouteFollowClimbColumnIndex)>0;
                    boolean leadClimbAccomplished = data.getInt(climbingRouteLeadingClimbColumnIndex)>0;
                    boolean routeAccomplished = data.getInt(climbingRouteRouteAccomplishedColumnIndex)>0;
                    int climbingArea = data.getInt(climbingRouteClimbingAreaColumnIndex);

                    ClimbingRoute climbingRoute = new ClimbingRoute();
                    climbingRoute.setId(id);
                    climbingRoute.setName(name);
                    climbingRoute.setLatitude(latitude);
                    climbingRoute.setLongitude(longitude);
                    climbingRoute.setRanking(ranking);
                    climbingRoute.setDrawableUrl(imageUrl);
                    climbingRoute.setUIAARank(difficulty);
                    climbingRoute.setFollowClimbAccomplished(followClimbAccomplished);
                    climbingRoute.setLeadClimbAccomplished(leadClimbAccomplished);
                    climbingRoute.setRouteAccomplished(routeAccomplished);

                    Log.d(TAG, "Climbing Area loaded: " + climbingArea);
                    selectedArea.getRoutes().add(climbingRoute);
                }
                break;
            default:
        }

        int size = selectedRoutes.size();
        clearSelectedRoutes();
        selectedRoutes.addAll(selectedArea.getRoutes());
        recyclerViewAdapter.notifyItemRangeRemoved(0, size);
        setChartData(selectedArea);

        // TextView sumRoutes = (TextView) findViewById(R.id.textView_sum_routes);
        // sumRoutes.setText(selectedArea.getRoutes().size() + "");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
