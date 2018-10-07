package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.Camera.StorageInfoListener;
import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;

import java.nio.ByteBuffer;

public class GetObjectHandlesCommand extends Command {
    private final int associationHandle;
    private final StorageInfoListener listener;
    private final int objectFormat;
    private int[] objectHandles;
    private final int storageId;

    public int[] getObjectHandles() {
        if (this.objectHandles == null) {
            return new int[0];
        }
        return this.objectHandles;
    }

    public GetObjectHandlesCommand(PtpCamera camera, StorageInfoListener listener, int storageId) {
        this(camera, listener, storageId, 0, 0);
    }

    public GetObjectHandlesCommand(PtpCamera camera, StorageInfoListener listener, int storageId, int objectFormat) {
        this(camera, listener, storageId, objectFormat, 0);
    }

    public GetObjectHandlesCommand(PtpCamera camera, StorageInfoListener listener, int storageId, int objectFormat, int associationHandle) {
        super(camera);
        this.listener = listener;
        this.storageId = storageId;
        this.objectFormat = objectFormat;
        this.associationHandle = associationHandle;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == 8193) {
            this.listener.onImageHandlesRetrieved(this.objectHandles);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, Operation.GetObjectHandles, this.storageId, this.objectFormat);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.objectHandles = PacketUtil.readU32Array(b);
    }
}
