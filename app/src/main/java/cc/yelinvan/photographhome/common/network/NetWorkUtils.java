package cc.yelinvan.photographhome.common.network;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;
import com.drew.metadata.photoshop.PhotoshopDirectory;

import java.util.Map;

public class NetWorkUtils {
    private static NetWorkUtils instance;
    String TAG = "NetWorkUtils";

    public static NetWorkUtils getInstance() {
        if (instance == null) {
            synchronized (NetWorkUtils.class) {
                if (instance == null) {
                    instance = new NetWorkUtils();
                }
            }
        }
        return instance;
    }

    public void post(String url, Map<String, String> map, Listener listener, ErrorListener errorListener) {
        Log.d(this.TAG, "请求的url为---->" + url);
        final Map<String, String> map2 = map;
        StringRequest request = new StringRequest(1, url, listener, errorListener) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return map2;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(PhotoshopDirectory.TAG_PRINT_FLAGS_INFO, 3, 1.0f));
    }

    public StringRequest get(String url, Listener listener, ErrorListener errorListener) {
        Log.d(this.TAG, "请求的url为---->" + url);
        StringRequest request = new StringRequest(0, url, listener, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 1.0f));
        return request;
    }
}
