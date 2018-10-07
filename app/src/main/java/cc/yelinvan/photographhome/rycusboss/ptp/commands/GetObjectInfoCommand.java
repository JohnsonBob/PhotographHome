package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.model.ObjectInfo;

import java.nio.ByteBuffer;

public class GetObjectInfoCommand extends Command {
    private final String TAG = GetObjectInfoCommand.class.getSimpleName();
    private ObjectInfo inObjectInfo;
    private final int outObjectHandle;

    public GetObjectInfoCommand(PtpCamera camera, int objectHandle) {
        super(camera);
        this.outObjectHandle = objectHandle;
    }

    public ObjectInfo getObjectInfo() {
        return this.inObjectInfo;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.inObjectInfo != null) {
        }
    }

    public void reset() {
        super.reset();
        this.inObjectInfo = null;
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.GetObjectInfo, this.outObjectHandle);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.inObjectInfo = new ObjectInfo(b, length);
    }
}
