package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;

import java.nio.ByteBuffer;

public class GetDevicePropValueCommand extends Command {
    private final int datatype;
    private final int property;
    private int value;

    public GetDevicePropValueCommand(PtpCamera camera, int property, int datatype) {
        super(camera);
        this.property = property;
        this.datatype = datatype;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.responseCode == 8193) {
            this.camera.onPropertyChanged(this.property, this.value, 0);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, 4117, this.property);
    }

    protected void decodeData(ByteBuffer b, int length) {
        if (this.datatype == 1) {
            this.value = b.get();
        } else if (this.datatype == 2) {
            this.value = b.get() & 255;
        } else if (this.datatype == 4) {
            this.value = b.getShort() & 65535;
        } else if (this.datatype == 3) {
            this.value = b.getShort();
        } else if (this.datatype == 5 || this.datatype == 6) {
            this.value = b.getInt();
        }
    }
}
