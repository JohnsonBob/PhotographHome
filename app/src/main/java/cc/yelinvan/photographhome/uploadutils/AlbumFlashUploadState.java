package cc.yelinvan.photographhome.uploadutils;

public interface AlbumFlashUploadState {
    public static final int ADD_CURRENT_LIST = 105;
    public static final int GIVE_FAIL_COUNT = 106;
    public static final int READ_CAMERA_ERROR = -106;
    public static final int READ_CAMERA_FILE_INFO_FAIL = -104;
    public static final int READ_CAMERA_FILE_STABLE = 103;
    public static final int READ_CAMERA_NOCAMERA = -101;
    public static final int READ_CAMERA_NODATA = -103;
    public static final int READ_CAMERA_NOSTORAGE = -102;
    public static final int READ_CAMERA_THUMBNAIL_OBJECT_ADD = 101;
    public static final int READ_CAMERA_THUMBNAIL_START = 102;
    public static final int READ_CAMERA_THUMBNAIL_SUCCESS = 100;
    public static final int REMOVE_CURRENT_LIST = 104;
    public static final int REUPLOAD = 107;
    public static final int UPLOAD_VIDEO_ABANDON = -105;
}
