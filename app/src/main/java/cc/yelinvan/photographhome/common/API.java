package cc.yelinvan.photographhome.common;

import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.blankj.utilcode.util.AppUtils;

public class API {
    public static String CHECK_VIDEO_MONEY = "/rest/v3/check/video/upload/change/";
    public static int CLIENT_FROM_ANDROID = 3;
    public static String FLASH_CHECK_STATE = "/rest/v3/app/flashpass/device/detect/";
    public static String FLASH_UPLOAD = "/rest/v3/act/upload/";
    public static String GET_ADV = "/rest/v3/activity/update/";
    public static String GET_ALL_GIVEPHOTO_HANDLE = "/rest/v3/give/handle/";
    public static String GET_NEW_OSS_AUTH = "/rest/v3/auth/change/get/";
    public static String HIDEIMAGE = "/rest/v3/album/photo/display/";
    public static String LOGIN = "/rest/v3/login/";
    public static String PHOTO_SURE_UPLOAD = "/rest/v3/act/update/confirm/";
    public static String POST_APP_UPDATE = "/rest/v3/app/update/";
    public static String SP_FOREVER = "alltuu_forever";
    public static String SP_NORMAL = "alltuu_normal";
    public static int baseUrlFlag;

    static {
        int i;
        if (AppUtils.isAppDebug()) {
            i = 2;
        } else {
            i = 0;
        }
        baseUrlFlag = i;
    }

    public static String getBaseUrl() {
        if (baseUrlFlag == 0) {
            return "https://m.alltuu.com";
        }
        if (baseUrlFlag == 1) {
            return "https://m.showmephoto.com";
        }
        if (baseUrlFlag == 2) {
            return "http://m.guituu.com";
        }
        return "https://m.alltuu.com";
    }

    public static String getBaseUrlV() {
        if (baseUrlFlag == 0) {
            return "https://v.alltuu.com";
        }
        if (baseUrlFlag == 1) {
            return "https://m.showmephoto.com";
        }
        if (baseUrlFlag == 2) {
            return "http://v.guituu.com";
        }
        return "https://v.alltuu.com";
    }

    public static String getEndpoint() {
        if (baseUrlFlag == 1) {
            return "http://oss-ap-southeast-3.aliyuncs.com";
        }
        return OSSConstants.DEFAULT_OSS_ENDPOINT;
    }

    public static String getStorageBucket() {
        if (baseUrlFlag == 1) {
            return "alltuu-storage-malaysia";
        }
        if (baseUrlFlag == 2) {
            return "alltuu-storage-guituu";
        }
        return "alltuu-storage";
    }

    public static String getBucket() {
        if (baseUrlFlag == 1) {
            return "alltuu-malaysia";
        }
        if (baseUrlFlag == 2) {
            return "alltuu-guituu";
        }
        return "alltuu";
    }

    public static String getNewBucket() {
        if (baseUrlFlag == 2) {
            return "alltuu-photo-guituu";
        }
        return "alltuu-photo";
    }
}
