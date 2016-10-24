package wolfgang.bergbauer.de.kletterguide.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.GraphicHelper;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.Utils;
import wolfgang.bergbauer.de.kletterguide.adapter.AddPhotoPagerAdapter;

/**
 * Created by Wolfgang on 06.08.2015.
 */
public class ClimbingPhotoActivity extends ToolbarActivity {

    private ViewPager mPager;

    public static final String ARG_PHOTO_URI = "photo_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        enableToolbar();

        mPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new AddPhotoPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
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
           // case R.id.menu_item_done:
                //TODO Upload Image + Information to Server
                //TODO show Progressbar
                //Snackbar.make(croppedImage, "Vielen Dank für dein Foto! Es wird schon bald online für andere zur Verfügung stehen.", Snackbar.LENGTH_LONG).show();
              //  finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICTURE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    croppedOutputFileUri = selectedImageUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    croppedOutputFileUri =  getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }

                // When the user is done picking a picture, let's start the CropImage Activity,
                // setting the output image file and size to 200x200 pixels square.
                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(0,0, 1920, 1080, croppedOutputFileUri);
                cropImage.setOutlineColor(getResources().getColor(R.color.primary_500));
                cropImage.setSourceImage(selectedImageUri);
                startActivityForResult(cropImage.getIntent(getActivity()), REQUEST_CROP_PICTURE);

            } else if ((requestCode == REQUEST_CROP_PICTURE) ) {

                croppedImage.setImageURI(croppedOutputFileUri);
            } else if (requestCode == REQUEST_PAINT_ROUTE) {
                croppedImage.setImageDrawable(getResources().getDrawable(R.drawable.routeicon));
                croppedImage.setImageURI(croppedOutputFileUri);
            }
        }
*/
    }
}
