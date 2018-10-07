package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.model.StorageInfo;

import java.nio.ByteBuffer;

public class GetStorageInfoCommand extends Command {
    private final int storageId;
    private StorageInfo storageInfo;

    public StorageInfo getStorageInfo() {
        return this.storageInfo;
    }

    public GetStorageInfoCommand(PtpCamera camera, int storageId) {
        super(camera);
        this.storageId = storageId;
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, 4101, this.storageId);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.storageInfo = new StorageInfo(b, length);
    }
}
