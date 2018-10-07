package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Property;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.CloseSessionCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.SetDevicePropValueCommand;

public class NikonCloseSessionAction implements PtpAction {
    private final NikonCamera camera;

    public NikonCloseSessionAction(NikonCamera camera) {
        this.camera = camera;
    }

    public void exec(IO io) {
        SetDevicePropValueCommand setRecordingMedia = new SetDevicePropValueCommand(this.camera, Property.NikonRecordingMedia, 0, 2);
        io.handleCommand(setRecordingMedia);
        if (setRecordingMedia.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        io.handleCommand(new CloseSessionCommand(this.camera));
        this.camera.onSessionClosed();
    }

    public void reset() {
    }
}
