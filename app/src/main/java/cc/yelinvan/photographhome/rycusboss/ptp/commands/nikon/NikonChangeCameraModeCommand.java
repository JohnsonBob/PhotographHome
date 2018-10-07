package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;

import java.nio.ByteBuffer;

public class NikonChangeCameraModeCommand extends NikonCommand {
    public NikonChangeCameraModeCommand(NikonCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == 8193) {
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonChangeCameraMode, 0);
    }
}
