package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import java.nio.ByteBuffer;

public class NikonAfDriveCommand extends NikonCommand {
    public NikonAfDriveCommand(NikonCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == 8193) {
            this.camera.onFocusStarted();
            this.camera.enqueue(new NikonAfDriveDeviceReadyCommand(this.camera), Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonAfDrive);
    }
}
