package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import java.nio.ByteBuffer;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;

public abstract class Command implements PtpAction {
    protected static final String TAG = Command.class.getSimpleName();
    protected final PtpCamera camera;
    protected boolean hasDataToSend;
    private boolean hasResponseReceived;
    protected int responseCode;

    public abstract void encodeCommand(ByteBuffer byteBuffer);

    public abstract void exec(PtpCamera.IO io);

    public Command(PtpCamera camera) {
        this.camera = camera;
    }

    public void encodeData(ByteBuffer b) {
    }

    protected void decodeData(ByteBuffer b, int length) {
    }

    protected void decodeResponse(ByteBuffer b, int length) {
    }

    public boolean hasDataToSend() {
        return this.hasDataToSend;
    }

    public boolean hasResponseReceived() {
        return this.hasResponseReceived;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void receivedRead(ByteBuffer b) {
        int length = b.getInt();
        int type = b.getShort() & 65535;
        int code = b.getShort() & 65535;
        int tx = b.getInt();
        if (type == 2) {
            decodeData(b, length);
        } else if (type == 3) {
            this.hasResponseReceived = true;
            this.responseCode = code;
            decodeResponse(b, length);
        } else {
            this.hasResponseReceived = true;
        }
    }

    public void reset() {
        this.responseCode = 0;
        this.hasResponseReceived = false;
    }

    protected void encodeCommand(ByteBuffer b, int code) {
        b.putInt(12);
        b.putShort((short) 1);
        b.putShort((short) code);
        b.putInt(this.camera.nextTransactionId());
    }

    protected void encodeCommand(ByteBuffer b, int code, int p0) {
        b.putInt(16);
        b.putShort((short) 1);
        b.putShort((short) code);
        b.putInt(this.camera.nextTransactionId());
        b.putInt(p0);
    }

    protected void encodeCommand(ByteBuffer b, int code, int p0, int p1) {
        b.putInt(20);
        b.putShort((short) 1);
        b.putShort((short) code);
        b.putInt(this.camera.nextTransactionId());
        b.putInt(p0);
        b.putInt(p1);
    }

    protected void encodeCommand(ByteBuffer b, int code, int p0, int p1, int p2) {
        b.putInt(24);
        b.putShort((short) 1);
        b.putShort((short) code);
        b.putInt(this.camera.nextTransactionId());
        b.putInt(p0);
        b.putInt(p1);
        b.putInt(p2);
    }
}
