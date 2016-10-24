package wolfgang.bergbauer.de.kletterguide.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import wolfgang.bergbauer.de.kletterguide.AppConstants;
import wolfgang.bergbauer.de.kletterguide.GraphicHelper;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.Utils;
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
public class ClimbingAreaDetailsActivity extends ToolbarActivity implements OnChartValueSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    /* Init constants to identify loaders */
    private static final int URL_ROUTES_LOADER_ALL = 0;
    private static final String TAG = ClimbingAreaDetailsActivity.class.getSimpleName();

    protected BarChart mChart;

    private List<ClimbingRoute> selectedRoutes = new ArrayList<>();
    private ClimbingArea selectedArea;

    private RecyclerView recList;
    private RouteCardViewAdapter recyclerViewAdapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.climbing_area_details);
        enableToolbar();

        selectedArea = getIntent().getExtras().getParcelable(AppConstants.EXTRA_CLIMBING_AREA);
        selectedArea.setRoutes(new ArrayList<ClimbingRoute>());

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_climbing_details);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(selectedArea.getName());

        initViewPagerHeader();
        initChart(selectedArea);
        initRouteList(selectedArea);
        initFab();

        animateView();
        getSupportLoaderManager().restartLoader(URL_ROUTES_LOADER_ALL, null, this);
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClimbingAreaDetailsActivity.this, ClimbingPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViewPagerHeader() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_details_header);
        //TODO Get Images from Server and cache them locally
        List<ClimbingImage> images = new ArrayList<>();
        for (int i = 0; i < 5; ++i)
        {
            ClimbingImage image = new ClimbingImage();
            image.setClimbingId(i);
            image.setImageUrl(i % 2 == 0 ? "riederin.jpg" : "riederin2.jpg");
            image.setDescription(i % 2 == 0 ? "Beschreibung 1" : "Beschreibung 2");
            images.add(image);
        }
        selectedArea.setImages(images);
        viewPager.setAdapter(new HeaderImagePagerAdapter(getSupportFragmentManager(), ClimbingAreaDetailsActivity.this, selectedArea));
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable() {
            public void run() {
               viewPager.setCurrentItem(1, true);
            }}, 500);
    }

    private void initRouteList(ClimbingArea selectedArea) {
        recList = (RecyclerView)
                findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recyclerViewAdapter = new RouteCardViewAdapter(this, selectedRoutes);
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
        mChart.getDescription().setEnabled(false);

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
        xAxis.setLabelCount(2);

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
            case R.id.menu_item_details_info:
                Snackbar.make(mChart,"Info", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.menu_item_details_navigate:
                startNavigationIntent(selectedArea);
                break;
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
                }
            });
        } else {
            findViewById(R.id.fab).animate().alpha(1.0f).start();
        }

    }

    private void setChartData(ClimbingArea ClimbingArea) {

        HashMap<Integer, Integer> difficultyMap = new HashMap<>();
        for (int i = AppConstants.MIN_UIAA_LEVEL; i <= AppConstants.MAX_UIAA_LEVEL; ++i)
        {
            difficultyMap.put(i, 0);
        }

        for (ClimbingRoute route : ClimbingArea.getRoutes())
        {
            String difficulty = route.getUIAARank();
            int routeDifficulty = Utils.trimUIAARank(difficulty);
            if (difficultyMap.containsKey(routeDifficulty))
            {
                difficultyMap.put(routeDifficulty, difficultyMap.get(routeDifficulty) + 1);
            }
        }

        ArrayList<BarEntry> routeDifficulties = new ArrayList<BarEntry>();

        String[] xVals = new String[difficultyMap.size()];
        List<Integer> orderedList = new ArrayList<>(difficultyMap.keySet());
        Collections.sort(orderedList);
        int indexCounter = 0;
        for (Integer difficulty : orderedList)
        {
            xVals[indexCounter] = difficulty + "";
            //BarEntry entry = new BarEntry(difficultyMap.get(difficulty), indexCounter++ , difficulty +"");
            BarEntry entry = new BarEntry(difficulty, difficultyMap.get(difficulty), indexCounter++);
            routeDifficulties.add(entry);
        }
        mChart.getXAxis().setLabelCount(xVals.length);

        BarDataSet set1 = new BarDataSet(routeDifficulties, "Schwierigkeiten");
        //set1.setColor(getResources().getColor(R.color.primary_700));
        int[] uiaaChartColors = new int[AppConstants.UIAA_CHART_COLORS.length];
        for (int i = 0; i < uiaaChartColors.length; ++i)
        {
            uiaaChartColors[i] = getResources().getColor(AppConstants.UIAA_CHART_COLORS[i]);
        }
        set1.setColors(uiaaChartColors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);

        mChart.setData(data);

        mChart.animateX(1000);
        mChart.animateY(1000);
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int selectedDifficulty = (int) e.getX();

        clearSelectedRoutes();

       for (ClimbingRoute route : selectedArea.getRoutes())
       {
           if (Utils.trimUIAARank(route.getUIAARank()) == selectedDifficulty)
               selectedRoutes.add(route);
       }
        //recyclerViewAdapter.notifyItemRangeChanged(0, selectedRoutes.size());
        recyclerViewAdapter.notifyItemRangeRemoved(0, selectedArea.getRoutes().size());
        recList.invalidate();

        if (selectedRoutes.isEmpty())
            onNothingSelected();
    }

    private void clearSelectedRoutes() {
        int size = selectedRoutes.size();
        selectedRoutes.clear();
        recyclerViewAdapter.notifyItemRangeRemoved(0, selectedArea.getRoutes().size());
    }

    public void onNothingSelected() {
        clearSelectedRoutes();
        selectedRoutes.addAll(selectedArea.getRoutes());
        recyclerViewAdapter.notifyItemRangeRemoved(0, selectedArea.getRoutes().size());
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
        setShareIntent(selectedArea);
        // TextView sumRoutes = (TextView) findViewById(R.id.textView_sum_routes);
        // sumRoutes.setText(selectedArea.getRoutes().size() + "");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void setShareIntent(ClimbingArea area) {
        if (menu != null) {
            MenuItem shareItem = menu.findItem(R.id.menu_item_details_share);
            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(AppConstants.MIME_TEXT_PLAIN);
            String shareBody = "";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Klettergebiet Empfehlung");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            mShareActionProvider.setShareIntent(sharingIntent);
        }
    }

    public void startNavigationIntent(final ClimbingArea area) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Navigation starten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Navigation zum Klettergebiet starten?")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + area.getLatitude() + "," +
                                        area.getLongitude() + "&daddr=" + area.getLatitude() + "," + area.getLongitude()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Abbrechen",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
