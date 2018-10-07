package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.model.DeviceInfo;

import java.nio.ByteBuffer;

public class GetDeviceInfoCommand extends Command {
    private DeviceInfo info;

    public GetDeviceInfoCommand(PtpCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != 8193) {
            this.camera.onPtpError(String.format("Couldn't read device information, error code \"%s\"", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        } else if (this.info == null) {
            this.camera.onPtpError("Couldn't retrieve device information");
        }
    }

    public void reset() {
        super.reset();
        this.info = null;
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, 4097);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.info = new DeviceInfo(b, length);
        this.camera.setDeviceInfo(this.info);
    }
}
