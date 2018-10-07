package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.model.DevicePropDesc;

import java.nio.ByteBuffer;

public class GetDevicePropDescCommand extends Command {
    private DevicePropDesc devicePropDesc;
    private final int property;

    public GetDevicePropDescCommand(PtpCamera camera, int property) {
        super(camera);
        this.property = property;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.devicePropDesc != null) {
            this.camera.onPropertyDescChanged(this.property, this.devicePropDesc);
            this.camera.onPropertyChanged(this.property, this.devicePropDesc.currentValue, 0);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.GetDevicePropDesc, this.property);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.devicePropDesc = new DevicePropDesc(b, length);
    }
}
