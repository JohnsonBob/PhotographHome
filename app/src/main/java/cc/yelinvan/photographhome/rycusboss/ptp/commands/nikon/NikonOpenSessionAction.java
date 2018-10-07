package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.OpenSessionCommand;

public class NikonOpenSessionAction implements PtpAction {
    private final NikonCamera camera;

    public NikonOpenSessionAction(NikonCamera camera) {
        this.camera = camera;
    }

    public void exec(IO io) {
        OpenSessionCommand openSession = new OpenSessionCommand(this.camera);
        io.handleCommand(openSession);
        if (openSession.getResponseCode() == 8193) {
            if (this.camera.hasSupportForOperation(Operation.NikonGetVendorPropCodes)) {
                NikonGetVendorPropCodesCommand getPropCodes = new NikonGetVendorPropCodesCommand(this.camera);
                io.handleCommand(getPropCodes);
                if (getPropCodes.getResponseCode() == 8193) {
                    this.camera.setVendorPropCodes(getPropCodes.getPropertyCodes());
                    this.camera.onSessionOpened();
                    return;
                }
                this.camera.onPtpError(String.format("Couldn't read device property codes! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(getPropCodes.getResponseCode())}));
                return;
            }
            this.camera.onSessionOpened();
        } else if (openSession.getResponseCode() == 8222) {
            this.camera.onSessionOpened();
        } else {
            this.camera.onPtpError(String.format("Couldn't open session! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSession.getResponseCode())}));
        }
    }

    public void reset() {
    }
}
