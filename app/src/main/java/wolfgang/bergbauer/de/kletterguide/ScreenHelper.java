package wolfgang.bergbauer.de.kletterguide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by berg21 on 05.08.2015.
 */
public class ScreenHelper {

    public static Bitmap createScaledBitmap(Resources resources, int id, int resizeTo) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        BitmapFactory.decodeResource(resources, id, o);
        o.inJustDecodeBounds = true;

        // The new size we want to scale to
        final int REQUIRED_SIZE=resizeTo;

        // Find the correct scale value. It should be the power of 2.
        int scale = 1;
        while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeResource(resources, id, o2);
    }

}
