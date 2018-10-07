package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.OpenSessionCommand;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;

public class EosOpenSessionAction implements PtpAction {
    private final EosCamera camera;

    public EosOpenSessionAction(EosCamera camera) {
        this.camera = camera;
    }

    public void exec(IO io) {
        OpenSessionCommand openSession = new OpenSessionCommand(this.camera);
        io.handleCommand(openSession);
        if (openSession.getResponseCode() == 8193) {
            EosSetPcModeCommand setPcMode = new EosSetPcModeCommand(this.camera);
            io.handleCommand(setPcMode);
            if (setPcMode.getResponseCode() == 8193) {
                EosSetExtendedEventInfoCommand c = new EosSetExtendedEventInfoCommand(this.camera);
                io.handleCommand(c);
                if (c.getResponseCode() == 8193) {
                    this.camera.onSessionOpened();
                    return;
                }
                this.camera.onPtpError(String.format("Couldn't open session! Setting extended event info failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(c.getResponseCode())}));
                return;
            }
            this.camera.onPtpError(String.format("Couldn't open session! Setting PcMode property failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(setPcMode.getResponseCode())}));
            return;
        }
        this.camera.onPtpError(String.format("Couldn't open session! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSession.getResponseCode())}));
    }

    public void reset() {
    }
}
