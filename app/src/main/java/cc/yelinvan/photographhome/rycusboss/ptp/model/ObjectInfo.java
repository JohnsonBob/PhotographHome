package cc.yelinvan.photographhome.rycusboss.ptp.model;

import java.nio.ByteBuffer;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;

public class ObjectInfo {
    public int associationDesc;
    public int associationType;
    public String captureDate;
    public String filename;
    public int imageBitDepth;
    public int imagePixHeight;
    public int imagePixWidth;
    public int keywords;
    public String modificationDate;
    public int objectCompressedSize;
    public int objectFormat;
    public int orientation;
    public int parentObject;
    public int protectionStatus;
    public int sequenceNumber;
    public int storageId;
    public int thumbCompressedSize;
    public int thumbFormat;
    public int thumbPixHeight;
    public int thumbPixWidth;

    public ObjectInfo(ByteBuffer b, int length) {
        decode(b, length);
    }

    public void decode(ByteBuffer b, int length) {
        this.storageId = b.getInt();
        this.objectFormat = b.getShort();
        this.protectionStatus = b.getShort();
        this.objectCompressedSize = b.getInt();
        this.thumbFormat = b.getShort();
        this.thumbCompressedSize = b.getInt();
        this.thumbPixWidth = b.getInt();
        this.thumbPixHeight = b.getInt();
        this.imagePixWidth = b.getInt();
        this.imagePixHeight = b.getInt();
        this.imageBitDepth = b.getInt();
        this.parentObject = b.getInt();
        this.associationType = b.getShort();
        this.associationDesc = b.getInt();
        this.sequenceNumber = b.getInt();
        this.filename = PacketUtil.readString(b);
        this.captureDate = PacketUtil.readString(b);
        this.modificationDate = PacketUtil.readString(b);
        this.keywords = b.get();
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("ObjectInfo\n");
        b.append("StorageId: ").append(String.format("0x%08x\n", new Object[]{Integer.valueOf(this.storageId)}));
        b.append("ObjectFormat: ").append(PtpConstants.objectFormatToString(this.objectFormat)).append(10);
        b.append("ProtectionStatus: ").append(this.protectionStatus).append(10);
        b.append("ObjectCompressedSize: ").append(this.objectCompressedSize).append(10);
        b.append("ThumbFormat: ").append(PtpConstants.objectFormatToString(this.thumbFormat)).append(10);
        b.append("ThumbCompressedSize: ").append(this.thumbCompressedSize).append(10);
        b.append("ThumbPixWdith: ").append(this.thumbPixWidth).append(10);
        b.append("ThumbPixHeight: ").append(this.thumbPixHeight).append(10);
        b.append("ImagePixWidth: ").append(this.imagePixWidth).append(10);
        b.append("ImagePixHeight: ").append(this.imagePixHeight).append(10);
        b.append("ImageBitDepth: ").append(this.imageBitDepth).append(10);
        b.append("ParentObject: ").append(String.format("0x%08x", new Object[]{Integer.valueOf(this.parentObject)})).append(10);
        b.append("AssociationType: ").append(this.associationType).append(10);
        b.append("AssociatonDesc: ").append(this.associationDesc).append(10);
        b.append("Filename: ").append(this.filename).append(10);
        b.append("CaptureDate: ").append(this.captureDate).append(10);
        b.append("ModificationDate: ").append(this.modificationDate).append(10);
        b.append("Keywords: ").append(this.keywords).append(10);
        return b.toString();
    }
}
