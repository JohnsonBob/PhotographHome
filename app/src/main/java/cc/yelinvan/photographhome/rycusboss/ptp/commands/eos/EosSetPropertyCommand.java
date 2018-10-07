package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;

import java.nio.ByteBuffer;

public class EosSetPropertyCommand extends EosCommand {
    private final int property;
    private final int value;

    public EosSetPropertyCommand(EosCamera camera, int property, int value) {
        super(camera);
        this.hasDataToSend = true;
        this.property = property;
        this.value = value;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosSetDevicePropValue);
    }

    public void encodeData(ByteBuffer b) {
        b.putInt(24);
        b.putShort((short) 2);
        b.putShort((short) -28400);
        b.putInt(this.camera.currentTransactionId());
        b.putInt(12);
        b.putInt(this.property);
        b.putInt(this.value);
    }
}
