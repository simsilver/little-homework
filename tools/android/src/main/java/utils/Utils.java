package utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class Utils {
    public static Intent getImageFileIntent(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static boolean saveJPG(Bitmap bmp, String path) {
        boolean success = false;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
