package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Event;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;

import java.nio.ByteBuffer;


public class EosEventCheckCommand extends EosCommand {
    private static final String TAG = EosEventCheckCommand.class.getSimpleName();

    public EosEventCheckCommand(EosCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, false);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosEventCheck);
    }

    protected void decodeData(ByteBuffer b, int length) {
        while (b.position() < length) {
            int eventLength = b.getInt();
            switch (b.getInt()) {
                case Event.EosObjectAdded /*49537*/:
                case Event.EosObjectAddedForUsb3 /*49575*/:
                    int objectHandle = b.getInt();
                    int storageId = b.getInt();
                    int objectFormat = b.getShort();
                    skip(b, eventLength - 18);
                    this.camera.onEventDirItemCreated(objectHandle, storageId, objectFormat, "TODO");
                    break;
                case Event.EosObjectRatingChanged /*49544*/:
                    this.camera.onRatingChange(b.getInt());
                    break;
                default:
                    skip(b, eventLength - 8);
                    break;
            }
        }
    }

    private void skip(ByteBuffer b, int length) {
        for (int i = 0; i < length; i++) {
            b.get();
        }
    }
}
