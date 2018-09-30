package cc.yelinvan.photographhome.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotographHomeUtils {

    static final int MAX_ALLOW_RANGE = 5120;
    static final int MAX_PICTURE_SIZE = 30720;
    static final int MAX_WIDTH_ALLOW_RANGE = 5;
    static final int MIN_OPITONS_VALUE = 30;
    static final int MIN_PICTURE_WIDTH = 100;
    public static String TAG = "PhotographHomeUtils";

    public static boolean writeToFile(byte[] data, File file) {
        if (data == null || data.length <= 0) {
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean compressBmpToFile(Bitmap bmp, File file) {
        return writeToFile(compressBmpBelow(bmp, Bitmap.CompressFormat.JPEG, MAX_PICTURE_SIZE), file);
    }

    public static byte[] compressBmpBelow(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int maxSize) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int ol = 30;
        int or = 100;
        int len = 0;
        while (or - ol > 1) {
            int options = (ol + or) / 2;
            baos.reset();
            bitmap.compress(compressFormat, options, baos);
            len = baos.toByteArray().length;
            if (len <= maxSize + 5120) {
                break;
            } else if (len > maxSize) {
                or = options;
            } else {
                ol = options;
            }
        }
        byte[] data;
        if (len <= maxSize + 5120) {
            data = baos.toByteArray();
            safeClose(baos);
            return data;
        }
        int left = 100;
        int right = (bitmap.getWidth() * 2) - 100;
        int originWidth = bitmap.getWidth();
        while (right - left > 1) {
            int width = (left + right) / 2;
            int height = (bitmap.getHeight() * width) / bitmap.getWidth();
            if (width != bitmap.getWidth()) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            baos.reset();
            bitmap.compress(compressFormat, 30, baos);
            len = baos.toByteArray().length;
            if ((len > maxSize && len - maxSize < 5120) || Math.abs(width - 100) < 5) {
                break;
            }
            if (len > maxSize) {
                right = width;
            } else {
                left = width;
            }
            if (right > originWidth) {
                right = originWidth;
            }
        }
        data = baos.toByteArray();
        safeClose(baos);
        return data;
    }

    public static boolean safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
