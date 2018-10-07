package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;

import java.nio.ByteBuffer;

public class GetStorageIdsCommand extends Command {
    private int[] storageIds;

    public int[] getStorageIds() {
        if (this.storageIds == null) {
            return new int[0];
        }
        return this.storageIds;
    }

    public GetStorageIdsCommand(PtpCamera camera) {
        super(camera);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, 4100);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.storageIds = PacketUtil.readU32Array(b);
        if (this.storageIds.length == 0) {
            this.storageIds = new int[0];
            return;
        }
        for (int i = 0; i < this.storageIds.length; i++) {
            byte[] bytes = PacketUtil.intToByteArray(this.storageIds[i]);
            PacketUtil.logHexdump(TAG, bytes, 4);
            if (bytes[bytes.length - 1] == (byte) 0) {
                this.storageIds[i] = -1;
            }
        }
    }
}
