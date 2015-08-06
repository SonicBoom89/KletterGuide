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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.GraphicHelper;
import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.Utils;

/**
 * Created by Wolfgang on 06.08.2015.
 */
public class ClimbingPhotoActivity extends ToolbarActivity {

    public static final String ARG_PHOTO_URI = "photo_uri";

    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    public static int REQUEST_PAINT_ROUTE = 3;


    private Uri outputFileUri;
    private Uri croppedOutputFileUri;

    private ImageView croppedImage;
    private Button paintRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        enableToolbar();
        paintRoute = (Button) findViewById(R.id.button_topo);
        paintRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClimbingPhotoActivity.this, DrawOnBitmapActivity.class);
                intent.putExtra(ARG_PHOTO_URI, croppedOutputFileUri);
                startActivityForResult(intent, REQUEST_PAINT_ROUTE);
            }
        });
        croppedImage = (ImageView) findViewById(R.id.imageView_cropped_photo);
        croppedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageIntent();
            }
        });
        openImageIntent();
    }

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        outputFileUri =  getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Bitte Quelle auswählen");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, REQUEST_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
                    croppedOutputFileUri =  getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }

                // When the user is done picking a picture, let's start the CropImage Activity,
                // setting the output image file and size to 200x200 pixels square.
                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(0,0, 1920, 1080, croppedOutputFileUri);
                cropImage.setOutlineColor(getResources().getColor(R.color.primary_500));
                cropImage.setSourceImage(selectedImageUri);
                startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);

            } else if ((requestCode == REQUEST_CROP_PICTURE) ) {

                croppedImage.setImageURI(croppedOutputFileUri);
            } else if (requestCode == REQUEST_PAINT_ROUTE) {
                croppedImage.setImageDrawable(getResources().getDrawable(R.drawable.routeicon));
                croppedImage.setImageURI(croppedOutputFileUri);
            }
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
                //TODO Upload Image + Information to Server
                //TODO show Progressbar
                Snackbar.make(croppedImage, "Vielen Dank für dein Foto! Es wird schon bald online für andere zur Verfügung stehen.", Snackbar.LENGTH_LONG).show();
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
