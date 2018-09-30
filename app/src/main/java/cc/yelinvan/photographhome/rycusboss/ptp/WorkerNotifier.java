package cc.yelinvan.photographhome.rycusboss.ptp;

import cc.yelinvan.photographhome.rycusboss.util.NotificationIds;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat.Builder;


public class WorkerNotifier implements Camera.WorkerListener {
    private final Notification notification;
    private final NotificationManager notificationManager;
    private final int uniqueId = NotificationIds.getInstance().getUniqueIdentifier(WorkerNotifier.class.getName() + ":running");

    public WorkerNotifier(Context context) {
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
        this.notification = new Builder(context).setContentText("").setWhen(System.currentTimeMillis()).setContentTitle("").setSmallIcon(R.mipmap.icon).build();
    }

    public void onWorkerStarted() {
        Notification notification = this.notification;
        notification.flags |= 32;
        this.notificationManager.notify(this.uniqueId, this.notification);
    }

    public void onWorkerEnded() {
        this.notificationManager.cancel(this.uniqueId);
    }
}
