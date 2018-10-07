package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;

import java.nio.ByteBuffer;

public class NikonEventCheckCommand extends NikonCommand {
    private static final String TAG = NikonEventCheckCommand.class.getSimpleName();

    public NikonEventCheckCommand(NikonCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonGetEvent);
    }

    protected void decodeData(ByteBuffer b, int length) {
        int count = b.getShort();
        while (count > 0) {
            count--;
            int eventCode = b.getShort();
            int eventParam = b.getInt();
            switch (eventCode) {
                case 16386:
                    this.camera.onEventObjectAdded(eventParam);
                    break;
                default:
                    break;
            }
        }
    }
}
