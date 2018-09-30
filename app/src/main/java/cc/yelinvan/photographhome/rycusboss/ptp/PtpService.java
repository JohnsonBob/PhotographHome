package cc.yelinvan.photographhome.rycusboss.ptp;

import android.content.Context;
import android.content.Intent;

public interface PtpService {

    public static class Singleton {
        private static PtpService singleton;

        public static PtpService getInstance(Context context) {
            if (singleton == null) {
                singleton = new PtpUsbService(context);
            }
            return singleton;
        }

        public static void setInstance(PtpService service) {
            singleton = service;
        }
    }

    void initialize(Context context, Intent intent);

    void lazyShutdown();

    void setCameraListener(Camera.CameraListener cameraListener);

    void shutdown();
}
