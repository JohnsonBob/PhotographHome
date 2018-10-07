package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;

public class RetrieveAddedObjectInfoAction implements PtpAction {
    private final PtpCamera camera;
    private final int objectHandle;

    public RetrieveAddedObjectInfoAction(PtpCamera camera, int objectHandle) {
        this.camera = camera;
        this.objectHandle = objectHandle;
    }

    public void exec(IO io) {
        GetObjectInfoCommand getInfo = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getInfo);
        if (getInfo.getResponseCode() == 8193 && getInfo.getObjectInfo() != null) {
            this.camera.onEventObjectAdded(this.objectHandle, getInfo.getObjectInfo().objectFormat);
        }
    }

    public void reset() {
    }
}
