package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Property;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;

public class EosSetLiveViewAction implements PtpAction {
    private final EosCamera camera;
    private final boolean enabled;

    public EosSetLiveViewAction(EosCamera camera, boolean enabled) {
        this.camera = camera;
        this.enabled = enabled;
    }

    public void exec(IO io) {
        int evfMode = this.camera.getPtpProperty(Property.EosEvfMode);
        if ((this.enabled && evfMode != 1) || !(this.enabled || evfMode == 0)) {
            EosSetPropertyCommand setEvfMode = new EosSetPropertyCommand(this.camera, Property.EosEvfMode, this.enabled ? 1 : 0);
            io.handleCommand(setEvfMode);
            if (setEvfMode.getResponseCode() == Response.DeviceBusy) {
                this.camera.onDeviceBusy(this, true);
                return;
            } else if (setEvfMode.getResponseCode() != 8193) {
                this.camera.onPtpWarning("Couldn't open live view");
                return;
            }
        }
        int outputDevice = this.camera.getPtpProperty(Property.EosEvfOutputDevice);
        if (this.enabled) {
            outputDevice |= 2;
        } else {
            outputDevice &= -3;
        }
        EosSetPropertyCommand setOutputDevice = new EosSetPropertyCommand(this.camera, Property.EosEvfOutputDevice, outputDevice);
        io.handleCommand(setOutputDevice);
        if (setOutputDevice.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (setOutputDevice.getResponseCode() != 8193) {
            this.camera.onPtpWarning("Couldn't open live view");
        } else if (this.enabled) {
            this.camera.onLiveViewStarted();
        } else {
            this.camera.onLiveViewStopped();
        }
    }

    public void reset() {
    }
}
