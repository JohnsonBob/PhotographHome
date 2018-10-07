package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;

import java.nio.ByteBuffer;

public class GetObjectExifCommand extends Command {
    private static final int MAXIMUM = 512000;
    private static final int OFFSET = 0;
    private static final String TAG = GetObjectExifCommand.class.getSimpleName();
    private static final int TAG_ORIENTATION = 274;
    private final int objectHandle;
    public int objectOrientation = 1;
    private int orientationIndex = 0;

    public GetObjectExifCommand(PtpCamera camera, int objectHandle) {
        super(camera);
        this.objectHandle = objectHandle;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        super.reset();
    }

    public void encodeCommand(ByteBuffer b) {
        int getObjectExifCommand = 4123;
        if (this.camera.getVendorId().intValue() == PtpConstants.CanonVendorId) {
            getObjectExifCommand = Operation.EosGetObjectExif;
        }
        encodeCommand(b, getObjectExifCommand, this.objectHandle, 0, MAXIMUM);
    }

    protected void decodeData(ByteBuffer b, int length) {
        String data = PacketUtil.hexDumpToString(b.array(), 56, 12);
        if (data.contains("12 01")) {
            this.orientationIndex = 11;
        }
        if (data.contains("01 12")) {
            this.orientationIndex = 12;
        }
        if (this.orientationIndex > 0) {
            String[] byteStr = data.split(" ");
            if (byteStr != null && byteStr.length > this.orientationIndex) {
                this.objectOrientation = Integer.valueOf(byteStr[this.orientationIndex]).intValue();
                if (this.objectOrientation < 0 || this.objectOrientation > 8) {
                    this.objectOrientation = 1;
                }
            }
        }
    }
}
