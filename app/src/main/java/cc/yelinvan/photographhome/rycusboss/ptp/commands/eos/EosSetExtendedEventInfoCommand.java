package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;

import java.nio.ByteBuffer;

public class EosSetExtendedEventInfoCommand extends EosCommand {
    public EosSetExtendedEventInfoCommand(EosCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != 8193) {
            this.camera.onPtpError(String.format("Couldn't initialize session! Setting extended event info failed, error code %s", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosSetEventMode, 1);
    }
}
