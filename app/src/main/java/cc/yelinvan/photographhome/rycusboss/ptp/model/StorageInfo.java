package cc.yelinvan.photographhome.rycusboss.ptp.model;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;

import java.nio.ByteBuffer;

public class StorageInfo {
    public int accessCapability;
    public int filesystemType;
    public long freeSpaceInBytes;
    public int freeSpaceInImages;
    public long maxCapacity;
    public String storageDescription;
    public int storageType;
    public String volumeLabel;

    public StorageInfo(ByteBuffer b, int length) {
        decode(b, length);
    }

    private void decode(ByteBuffer b, int length) {
        this.storageType = b.getShort() & 65535;
        this.filesystemType = b.getShort() & 65535;
        this.accessCapability = b.getShort() & 255;
        this.maxCapacity = b.getLong();
        this.freeSpaceInBytes = b.getLong();
        this.freeSpaceInImages = b.getInt();
        this.storageDescription = PacketUtil.readString(b);
        this.volumeLabel = PacketUtil.readString(b);
    }
}
