package cc.yelinvan.photographhome.rycusboss.ptp.model;

import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;

import java.nio.ByteBuffer;

public class DevicePropDesc {
    public int code;
    public int currentValue;
    public int datatype;
    public int[] description;
    public int factoryDefault;
    public boolean readOnly;

    public DevicePropDesc(ByteBuffer b, int length) {
        decode(b, length);
    }

    public void decode(ByteBuffer b, int length) {
        this.code = b.getShort() & 65535;
        this.datatype = b.getShort() & 65535;
        this.readOnly = b.get() == (byte) 0;
        int form;
        int mini;
        int maxi;
        int step;
        int i;
        if (this.datatype == 1 || this.datatype == 2) {
            this.factoryDefault = b.get() & 255;
            this.currentValue = b.get() & 255;
            form = b.get();
            if (form == 2) {
                this.description = PacketUtil.readU8Enumeration(b);
            } else if (form == 1) {
                mini = b.get();
                maxi = b.get();
                b.get();
            }
        } else if (this.datatype == 4) {
            this.factoryDefault = b.getShort() & 65535;
            this.currentValue = b.getShort() & 65535;
            form = b.get();
            if (form == 2) {
                this.description = PacketUtil.readU16Enumeration(b);
            } else if (form == 1) {
                mini = b.getShort() & 65535;
                step = b.getShort() & 65535;
                this.description = new int[((((b.getShort() & 65535) - mini) / step) + 1)];
                for (i = 0; i < this.description.length; i++) {
                    this.description[i] = (step * i) + mini;
                }
            }
        } else if (this.datatype == 3) {
            this.factoryDefault = b.getShort();
            this.currentValue = b.getShort();
            form = b.get();
            if (form == 2) {
                this.description = PacketUtil.readS16Enumeration(b);
            } else if (form == 1) {
                mini = b.getShort();
                maxi = b.getShort();
                step = b.getShort();
                this.description = new int[(((maxi - mini) / step) + 1)];
                for (i = 0; i < this.description.length; i++) {
                    this.description[i] = (step * i) + mini;
                }
            }
        } else if (this.datatype == 5 || this.datatype == 6) {
            this.factoryDefault = b.getInt();
            this.currentValue = b.getInt();
            if (b.get() == 2) {
                this.description = PacketUtil.readU32Enumeration(b);
            }
        }
        if (this.description == null) {
            this.description = new int[0];
        }
    }
}
