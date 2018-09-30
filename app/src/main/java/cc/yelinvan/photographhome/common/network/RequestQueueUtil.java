package cc.yelinvan.photographhome.common.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Volley;

public class RequestQueueUtil {
    private static RequestQueue requestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            synchronized (RequestQueue.class) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return requestQueue;
    }
}
