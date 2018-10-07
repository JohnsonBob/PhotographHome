package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.SimpleCommand;

public class NikonStopLiveViewAction implements PtpAction {
    private final NikonCamera camera;
    private final boolean notifyUser;

    public NikonStopLiveViewAction(NikonCamera camera, boolean notifyUser) {
        this.camera = camera;
        this.notifyUser = notifyUser;
    }

    public void exec(IO io) {
        SimpleCommand simpleCmd = new SimpleCommand(this.camera, 37378);
        io.handleCommand(simpleCmd);
        if (simpleCmd.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (this.notifyUser) {
            this.camera.onLiveViewStopped();
        } else {
            this.camera.onLiveViewStoppedInternal();
        }
    }

    public void reset() {
    }
}
