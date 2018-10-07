package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import java.nio.ByteBuffer;

public class NikonAfDriveDeviceReadyCommand extends NikonCommand {
    public NikonAfDriveDeviceReadyCommand(NikonCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.DeviceBusy) {
            reset();
            this.camera.enqueue(this, Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            return;
        }
        this.camera.onFocusEnded(getResponseCode() == 8193);
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonDeviceReady);
    }
}
