package cc.yelinvan.photographhome.rycusboss.ptp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat.Builder;

import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.rycusboss.util.NotificationIds;

/**
 * 通知管理 2018年10月6日21:58:38
 */
public class WorkerNotifier implements Camera.WorkerListener {
    private final Notification notification;
    private final NotificationManager notificationManager;
    private final int uniqueId = NotificationIds.getInstance().getUniqueIdentifier(WorkerNotifier.class.getName() + ":running");

    public WorkerNotifier(Context context) {
        this.notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        this.notification = new Builder(context).setContentText("").setWhen(System.currentTimeMillis()).setContentTitle("").setSmallIcon(R.mipmap.push_alltuu).build();
    }

    public void onWorkerStarted() {
        Notification notification = this.notification;
        notification.flags |= 32;
        this.notificationManager.notify(this.uniqueId, this.notification);
    }

    public void onWorkerEnded() {
        this.notificationManager.cancel(this.uniqueId);
        String CHANNEL_ONE_ID = "cc.yelinvan.photographhome";
        String CHANNEL_ONE_NAME = "cc.yelinvan.photographhome";

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
