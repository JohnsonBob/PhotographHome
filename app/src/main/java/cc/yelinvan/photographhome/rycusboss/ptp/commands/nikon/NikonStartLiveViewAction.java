package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.SimpleCommand;


public class NikonStartLiveViewAction implements PtpAction {
    private final NikonCamera camera;

    public NikonStartLiveViewAction(NikonCamera camera) {
        this.camera = camera;
    }

    public void exec(IO io) {
        SimpleCommand simpleCmd = new SimpleCommand(this.camera, 37377);
        io.handleCommand(simpleCmd);
        if (simpleCmd.getResponseCode() == 8193) {
            SimpleCommand deviceReady = new SimpleCommand(this.camera, Operation.NikonDeviceReady);
            int i = 0;
            while (i < 10) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
                deviceReady.reset();
                io.handleCommand(deviceReady);
                if (deviceReady.getResponseCode() == Response.DeviceBusy) {
                    i++;
                } else if (deviceReady.getResponseCode() == 8193) {
                    this.camera.onLiveViewStarted();
                    return;
                } else {
                    return;
                }
            }
        }
    }

    public void reset() {
    }
}
