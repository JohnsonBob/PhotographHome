package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Property;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;

import java.nio.ByteBuffer;

public class SetDevicePropValueCommand extends Command {
    private static final String TAG = "SetDevicePropValueCommand";
    private final int datatype;
    private final int property;
    private final int value;

    public SetDevicePropValueCommand(PtpCamera camera, int property, int value, int datatype) {
        super(camera);
        this.property = property;
        this.value = value;
        this.datatype = datatype;
        this.hasDataToSend = true;
    }

    public void exec(IO io) {
        if (this.property == Property.NikonApplicationMode) {
            this.camera.onPropertyChanged(this.property, this.value, 1);
        }
        io.handleCommand(this);
        if (this.property == Property.NikonApplicationMode) {
            this.camera.onPropertyChanged(this.property, this.value, 2);
        }
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (this.responseCode == 8193) {
            this.camera.onPropertyChanged(this.property, this.value, 0);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.SetDevicePropValue, this.property);
    }

    public void encodeData(ByteBuffer b) {
        b.putInt(PtpConstants.getDatatypeSize(this.datatype) + 12);
        b.putShort((short) 2);
        b.putShort((short) 4118);
        b.putInt(this.camera.currentTransactionId());
        if (this.datatype == 1 || this.datatype == 2) {
            b.put((byte) this.value);
        } else if (this.datatype == 3 || this.datatype == 4) {
            b.putShort((short) this.value);
        } else if (this.datatype == 5 || this.datatype == 6) {
            b.putInt(this.value);
        } else {
            throw new UnsupportedOperationException();
        }
        PacketUtil.logHexdump(TAG, b.array(), 13);
    }
}
