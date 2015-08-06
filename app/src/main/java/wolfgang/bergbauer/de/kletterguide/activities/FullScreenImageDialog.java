package wolfgang.bergbauer.de.kletterguide.activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.io.IOException;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.fragments.HeaderImageFragment;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingImage;

/**
 * Created by berg21 on 06.08.2015.
 */
public class FullScreenImageDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        ClimbingImage climbingImage = extras.getParcelable(HeaderImageFragment.ARG_IMAGE);

        ImageView fullscreenImage = new ImageView(this);

        if (climbingImage.getImageUrl() != null) {
            try {
                Drawable d = Drawable.createFromStream(getAssets().open(climbingImage.getImageUrl()), null);
                fullscreenImage.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullscreenImage.setTransitionName(getResources().getString(R.string.transition_climbing_area));
        }
        fullscreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(FullScreenImageDialog.this);
            }
        });
        setContentView(fullscreenImage);
    }
}
