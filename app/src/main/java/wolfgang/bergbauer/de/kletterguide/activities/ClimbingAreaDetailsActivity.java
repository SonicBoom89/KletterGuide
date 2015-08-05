package wolfgang.bergbauer.de.kletterguide.activities;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import wolfgang.bergbauer.de.kletterguide.AppConstants;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingBase;

/**
 * Created by berg21 on 05.08.2015.
 */
public class ClimbingAreaDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.climbing_area_details);

        ClimbingBase climbingBase = getIntent().getExtras().getParcelable(AppConstants.EXTRA_CLIMBING_AREA);

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
    }
}
