package cc.yelinvan.photographhome.uploadutils;


import com.blankj.utilcode.util.ToastUtils;

public class AlbumPhotoUploadState {
    static String TAG = "AlbumPhotoUploadState";
    public static final int UPLOAD_COPY_OSS_FAIL = -6;
    public static final int UPLOAD_DEFAULT = -1017;
    public static final int UPLOAD_FIRST_REQUEST_FAIL = -4;
    public static final int UPLOAD_FIRST_REQUEST_JSON_FAIL = -3;
    public static final int UPLOAD_FIRST_REQUEST_NETWORK_ERROR = -5;
    public static final int UPLOAD_LAST_REQUEST_FAIL = -7;
    public static final int UPLOAD_LAST_REQUEST_JSON_FAIL = -9;
    public static final int UPLOAD_LAST_REQUEST_NETWORK_ERROR = -8;
    public static final int UPLOAD_NOT_LOGIN = 2;
    public static final int UPLOAD_OSS_FAIL = -1;
    public static final int UPLOAD_READ_EXIF_FAIL = -2;
    public static final int UPLOAD_SUCCESS = 0;

    public static void showStateCodeMsg(int stateCode) {
        switch (stateCode) {
            case -9:
                ToastUtils.showShort((CharSequence) "确认上传时解析数据失败，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case -8:
                ToastUtils.showShort((CharSequence) "确认上传时失败，请您检查网络连接，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case -6:
                ToastUtils.showShort((CharSequence) "拷贝失败，请您检查网络连接，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case -5:
                ToastUtils.showShort((CharSequence) "请求服务器失败，请您检查网络连接，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case -3:
                ToastUtils.showShort((CharSequence) "请求时解析数据失败，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case -2:
                ToastUtils.showShort((CharSequence) "解析照片信息失败，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case -1:
                ToastUtils.showShort((CharSequence) "上传失败，请您检查网络连接，我们会在所有照片上传完之后，重新上传失败的照片");
                return;
            case 2:
                ToastUtils.showShort((CharSequence) "您处于未登录状态，请您登录后再进行交片");
                return;
            default:
                return;
        }
    }
}
