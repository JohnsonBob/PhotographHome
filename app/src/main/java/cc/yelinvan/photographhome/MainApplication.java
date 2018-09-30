package cc.yelinvan.photographhome;

import android.alltuu.com.newalltuuapp.common.network.RequestQueueUtil;
import android.alltuu.com.newalltuuapp.reactnative.AlltuuReactPackage;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.airbnb.android.react.lottie.LottiePackage;
import com.android.volley.RequestQueue;
import com.blankj.utilcode.util.Utils;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.liulishuo.filedownloader.FileDownloader;
import com.microsoft.codepush.react.CodePush;
import com.mob.MobSDK;
import com.reactnative.ivpusic.imagepicker.PickerPackage;

import org.litepal.LitePal;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class MainApplication extends Application implements ReactApplication {
    public static RequestQueue requestQueue;
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        public boolean getUseDeveloperSupport() {
            return false;
        }

        protected List<ReactPackage> getPackages() {
            return Arrays.asList(new ReactPackage[]{new MainReactPackage(), new LottiePackage(), new PickerPackage(), new AlltuuReactPackage(), new CodePush("iMrq12jRLTOBd09k3U5VFSF5z68hbfdc8f24-a4ac-4ff2-8b2e-89139855777a", MainApplication.this, false), new LottiePackage()});
        }

        @Nullable
        protected String getJSBundleFile() {
            return CodePush.getJSBundleFile();
        }
    };

    public void onCreate() {
        super.onCreate();
        Utils.init((Application) this);
        MobSDK.init(this);
        LitePal.initialize(this);
        FileDownloader.setup(this);
        requestQueue = RequestQueueUtil.getRequestQueue(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public ReactNativeHost getReactNativeHost() {
        return this.mReactNativeHost;
    }
}
