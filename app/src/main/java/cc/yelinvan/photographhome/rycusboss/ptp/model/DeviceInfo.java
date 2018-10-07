package cc.yelinvan.photographhome.rycusboss.ptp.model;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Event;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.ObjectFormat;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Property;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DeviceInfo {
    public int[] captureFormats;
    public int[] devicePropertiesSupported;
    public String deviceVersion;
    public int[] eventsSupported;
    public short functionalMode;
    public int[] imageFormats;
    public String manufacture;
    public String model;
    public int[] operationsSupported;
    public String serialNumber;
    public short standardVersion;
    public String vendorExtensionDesc;
    public int vendorExtensionId;
    public short vendorExtensionVersion;

    public DeviceInfo(ByteBuffer b, int length) {
        decode(b, length);
    }

    public void decode(ByteBuffer b, int length) {
        this.standardVersion = b.getShort();
        this.vendorExtensionId = b.getInt();
        this.vendorExtensionVersion = b.getShort();
        this.vendorExtensionDesc = PacketUtil.readString(b);
        this.functionalMode = b.getShort();
        this.operationsSupported = PacketUtil.readU16Array(b);
        this.eventsSupported = PacketUtil.readU16Array(b);
        this.devicePropertiesSupported = PacketUtil.readU16Array(b);
        this.captureFormats = PacketUtil.readU16Array(b);
        this.imageFormats = PacketUtil.readU16Array(b);
        this.manufacture = PacketUtil.readString(b);
        this.model = PacketUtil.readString(b);
        this.deviceVersion = PacketUtil.readString(b);
        this.serialNumber = PacketUtil.readString(b);
    }

    public void encode(ByteBuffer b) {
        b.putShort(this.standardVersion);
        b.putInt(this.vendorExtensionId);
        b.putInt(this.vendorExtensionVersion);
        PacketUtil.writeString(b, "");
        b.putShort(this.functionalMode);
        PacketUtil.writeU16Array(b, new int[0]);
        PacketUtil.writeU16Array(b, new int[0]);
        PacketUtil.writeU16Array(b, new int[0]);
        PacketUtil.writeU16Array(b, new int[0]);
        PacketUtil.writeU16Array(b, new int[0]);
        PacketUtil.writeString(b, "");
        PacketUtil.writeString(b, "");
        PacketUtil.writeString(b, "");
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("DeviceInfo\n");
        b.append("StandardVersion: ").append(this.standardVersion).append(10);
        b.append("VendorExtensionId: ").append(this.vendorExtensionId).append(10);
        b.append("VendorExtensionVersion: ").append(this.vendorExtensionVersion).append(10);
        b.append("VendorExtensionDesc: ").append(this.vendorExtensionDesc).append(10);
        b.append("FunctionalMode: ").append(this.functionalMode).append(10);
        appendU16Array(b, "OperationsSupported", Operation.class, this.operationsSupported);
        appendU16Array(b, "EventsSupported", Event.class, this.eventsSupported);
        appendU16Array(b, "DevicePropertiesSupported", Property.class, this.devicePropertiesSupported);
        appendU16Array(b, "CaptureFormats", ObjectFormat.class, this.captureFormats);
        appendU16Array(b, "ImageFormats", ObjectFormat.class, this.imageFormats);
        b.append("Manufacture: ").append(this.manufacture).append(10);
        b.append("Model: ").append(this.model).append(10);
        b.append("DeviceVersion: ").append(this.deviceVersion).append(10);
        b.append("SerialNumber: ").append(this.serialNumber).append(10);
        return b.toString();
    }

    private static void appendU16Array(StringBuilder b, String name, Class<?> cl, int[] a) {
        Arrays.sort(a);
        b.append(name).append(":\n");
        for (int constantToString : a) {
            b.append("    ").append(PtpConstants.constantToString(cl, constantToString)).append(10);
        }
    }
}
