package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class SimpleCommand extends Command {
    private int numParams;
    private final int operation;
    private int p0;
    private int p1;

    public SimpleCommand(PtpCamera camera, int operation) {
        super(camera);
        this.operation = operation;
    }

    public SimpleCommand(PtpCamera camera, int operation, int p0) {
        super(camera);
        this.operation = operation;
        this.p0 = p0;
        this.numParams = 1;
    }

    public SimpleCommand(PtpCamera camera, int operation, int p0, int p1) {
        super(camera);
        this.operation = operation;
        this.p0 = p0;
        this.p1 = p1;
        this.numParams = 2;
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        if (this.numParams == 2) {
            encodeCommand(b, this.operation, this.p0, this.p1);
        } else if (this.numParams == 1) {
            encodeCommand(b, this.operation, this.p0);
        } else {
            encodeCommand(b, this.operation);
        }
    }
}
