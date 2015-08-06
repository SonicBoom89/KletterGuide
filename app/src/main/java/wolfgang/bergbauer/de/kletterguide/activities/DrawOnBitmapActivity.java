package wolfgang.bergbauer.de.kletterguide.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.views.DrawableImageView;

/**
 * Created by Wolfgang on 06.08.2015.
 */
public class DrawOnBitmapActivity extends ToolbarActivity
{

    DrawableImageView choosenImageView;

    Bitmap bmp;
    Bitmap alteredBitmap;
    Uri drawableSourceUri;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawimage_layout);
        enableToolbar();

        drawableSourceUri = (Uri) getIntent().getExtras().get(ClimbingPhotoActivity.ARG_PHOTO_URI);

        choosenImageView = (DrawableImageView) this.findViewById(R.id.drawble_view);
        choosenImageView.setImageURI(drawableSourceUri);

        try {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            bmp = BitmapFactory
                    .decodeStream(
                            getContentResolver().openInputStream(
                                    drawableSourceUri), null, bmpFactoryOptions);

            bmpFactoryOptions.inJustDecodeBounds = false;
            bmp = BitmapFactory
                    .decodeStream(
                            getContentResolver().openInputStream(
                                    drawableSourceUri), null, bmpFactoryOptions);

            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(),
                    bmp.getHeight(), bmp.getConfig());

            choosenImageView.setNewImage(alteredBitmap, bmp);
        }
        catch (Exception e) {
            Log.v("ERROR", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_done:
                if (alteredBitmap != null)
                {
                    ContentValues contentValues = new ContentValues(3);
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

                    Uri imageFileUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    try {
                        OutputStream imageFileOS = getContentResolver()
                                .openOutputStream(drawableSourceUri);
                        alteredBitmap.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS);

                    } catch (Exception e) {
                        Log.v("EXCEPTION", e.getMessage());
                    }
                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK);
                    } else {
                        getParent().setResult(Activity.RESULT_OK);
                    }
                    finishActivity(ClimbingPhotoActivity.REQUEST_PAINT_ROUTE);
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
