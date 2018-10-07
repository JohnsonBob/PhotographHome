package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.Camera.StorageInfoListener;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;

public class GetStorageInfosAction implements PtpAction {
    private final PtpCamera camera;
    private final StorageInfoListener listener;

    public GetStorageInfosAction(PtpCamera camera, StorageInfoListener listener) {
        this.camera = camera;
        this.listener = listener;
    }

    public void exec(IO io) {
        GetStorageIdsCommand getStorageIds = new GetStorageIdsCommand(this.camera);
        io.handleCommand(getStorageIds);
        if (getStorageIds.getResponseCode() != 8193) {
            this.listener.onAllStoragesFound();
            return;
        }
        int[] ids = getStorageIds.getStorageIds();
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] >= 0) {
                this.listener.onStorageFound(ids[i], "卡 " + (i + 1));
            }
        }
        this.listener.onAllStoragesFound();
    }

    public void reset() {
    }
}
