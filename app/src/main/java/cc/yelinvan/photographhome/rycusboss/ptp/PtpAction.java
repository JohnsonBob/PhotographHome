package cc.yelinvan.photographhome.rycusboss.ptp;


public interface PtpAction {
    void exec(PtpCamera.IO io);

    void reset();
}
