package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;

import java.nio.ByteBuffer;


public class OpenSessionCommand extends Command {
    public OpenSessionCommand(PtpCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == 8193) {
            this.camera.onSessionOpened();
            return;
        }
        this.camera.onPtpError(String.format("Couldn't open session, error code \"%s\"", new Object[]{PtpConstants.responseToString(this.responseCode)}));
    }

    public void encodeCommand(ByteBuffer b) {
        this.camera.resetTransactionId();
        encodeCommand(b, 4098, 1);
    }
}
