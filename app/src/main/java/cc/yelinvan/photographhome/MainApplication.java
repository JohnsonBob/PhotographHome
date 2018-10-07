package cc.yelinvan.photographhome;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.blankj.utilcode.util.Utils;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import cc.yelinvan.photographhome.common.network.RequestQueueUtil;

public class MainApplication extends Application  {
    public static RequestQueue requestQueue;


    public void onCreate() {
        super.onCreate();
        Utils.init((Application) this);
        requestQueue = RequestQueueUtil.getRequestQueue(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
