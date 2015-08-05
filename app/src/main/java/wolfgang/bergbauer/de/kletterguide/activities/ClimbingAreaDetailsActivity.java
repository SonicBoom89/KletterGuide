package wolfgang.bergbauer.de.kletterguide.activities;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;

import java.io.IOException;

import wolfgang.bergbauer.de.kletterguide.AppConstants;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.adapter.TransitionAdapter;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingBase;

/**
 * Created by berg21 on 05.08.2015.
 */
public class ClimbingAreaDetailsActivity extends AppCompatActivity {


    private BarChartView mChartOne;
    private ImageButton mPlayOne;
    private boolean mUpdateOne;
    private final String[] mLabelsOne= {"2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private final float[] mValuesOne = {0, 1, 2, 1, 3, 4, 3, 2, 0, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.climbing_area_details);

        ClimbingBase climbingBase = getIntent().getExtras().getParcelable(AppConstants.EXTRA_CLIMBING_AREA);

        initChart(climbingBase);
        TextView header = (TextView) findViewById(R.id.textHeader);
        header.setText(climbingBase.getName());

        ImageView imageView = (ImageView) findViewById(R.id.imageView_climbing_area_details);
        if (climbingBase.getDrawableUrl() != null) {
            try {
                Drawable d = Drawable.createFromStream(getAssets().open(climbingBase.getDrawableUrl()), null);
                imageView.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
       }
        animateView();
    }

    private void initChart(ClimbingBase climbingBase) {
        // Init first chart
        mUpdateOne = true;
        mChartOne = (BarChartView) findViewById(R.id.barchart1);
        showChart(0, mChartOne);
    }

    /**
     * Show a CardView chart
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart   Chart view
     */
    private void showChart(final int tag, final ChartView chart){

        switch(tag) {
            case 0:
                produceOne(chart); break;
        }
    }

    public void produceOne(ChartView chart){
        final BarChartView barChart = (BarChartView) chart;

        barChart.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex, Rect rect) {
                Snackbar.make(barChart, "Lade Alle Routen " + entryIndex, Snackbar.LENGTH_SHORT).show();
            }
        });

        BarSet barSet = new BarSet(mLabelsOne, mValuesOne);
        barSet.setColor(getResources().getColor(R.color.primary_700));
        barChart.addData(barSet);

        barChart.setSetSpacing(Tools.fromDpToPx(-15));
        barChart.setBarSpacing(Tools.fromDpToPx(35));
        barChart.setRoundCorners(Tools.fromDpToPx(2));

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#8986705C"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1.75f));

        barChart.setBorderSpacing(5)
                .setAxisBorderValues(0, 10, 2)
                .setGrid(BarChartView.GridType.NONE, gridPaint)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE)
                .setLabelsColor(getResources().getColor(R.color.blue_grey))
                .setAxisColor(getResources().getColor(R.color.primary_500));

        int[] order = {0, 1, 2, 3 , 4 , 5, 6,7 ,8,9};
        barChart.show(new Animation().setOverlap(.5f, order));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().getEnterTransition().addListener(new TransitionAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {

                    findViewById(R.id.fab).animate().alpha(1.0f);
                    findViewById(R.id.linearlayout_imageDesc).animate().alpha(1.0f);
                    //getWindow().getEnterTransition().removeListener(this);
                }
            });
        } else {
            findViewById(R.id.fab).animate().alpha(1.0f).start();
            findViewById(R.id.linearlayout_imageDesc).animate().alpha(1.0f).start();
        }
    }
}
