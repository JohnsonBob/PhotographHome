package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PacketUtil;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;

import java.nio.ByteBuffer;

public class NikonGetVendorPropCodesCommand extends NikonCommand {
    private int[] propertyCodes = new int[0];

    public NikonGetVendorPropCodesCommand(NikonCamera camera) {
        super(camera);
    }

    public int[] getPropertyCodes() {
        return this.propertyCodes;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonGetVendorPropCodes);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.propertyCodes = PacketUtil.readU16Array(b);
    }
}
