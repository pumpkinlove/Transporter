package org.ia.transporter.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class Base64Util {

    public static Bitmap stringtoBitmap(String string) {
        byte[] bitmapArray;
        bitmapArray = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    public static String bitmaptoString(Bitmap bitmap) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[]bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
