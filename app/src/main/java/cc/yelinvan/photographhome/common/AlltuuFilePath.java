package cc.yelinvan.photographhome.common;

import android.os.Environment;

public class AlltuuFilePath {
    public static final String FLASH_COMPRESS_PAHT = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuuCompress/");
    public static final String FLASH_DOWNLAOD_PAHT = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuu/");
    public static final String FLASH_GIVEPHOTO_PAHT = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuugivephoto/");
    public static final String TEMP_FLASH_THUMBNAIL_PATH = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuuflashthumbnail/");
    public static final String TEMP_PHOTO_PATH = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuutempphoto/");
}
