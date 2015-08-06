package wolfgang.bergbauer.de.kletterguide;

/**
 * Created by Wolfgang on 06.08.2015.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class GraphicHelper
{
    private static final String TAG = GraphicHelper.class.getSimpleName();

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 101;

    public static Drawable scaleDrawable(Resources res, Drawable dr, int targetHeight)
    {
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

        int targetWidth = bitmap.getWidth() * targetHeight / bitmap.getHeight();

        // Scale it
        Drawable d = new BitmapDrawable(res, Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true));
        // Set your new, scaled drawable "d"
        return d;
    }

    public static void scaleImage(Resources res, String imagePath, int targetHeight)
    {
        File imageFile = new File(imagePath);
        if(!imageFile.exists())
        {
            return;
        }

        Drawable drawable = Drawable.createFromPath(imagePath);

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        int targetWidth = bitmap.getWidth() * targetHeight / bitmap.getHeight();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(imagePath);

            try
            {
                scaledBitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
            }
            finally
            {
                fileOutputStream.close();
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    public static Intent getPickImageIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        return Intent.createChooser(intent, "Select picture");
    }

}