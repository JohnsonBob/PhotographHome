package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.nio.ByteBuffer;

public class GetThumb extends Command {
    private static final String TAG = GetThumb.class.getSimpleName();
    private Bitmap inBitmap;
    private final int objectHandle;

    public GetThumb(PtpCamera camera, int objectHandle) {
        super(camera);
        this.objectHandle = objectHandle;
    }

    public Bitmap getBitmap() {
        return this.inBitmap;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        super.reset();
        this.inBitmap = null;
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, 4106, this.objectHandle);
    }

    protected void decodeData(ByteBuffer b, int length) {
        try {
            this.inBitmap = BitmapFactory.decodeByteArray(b.array(), 12, length - 12);
        } catch (RuntimeException e) {
            Log.i(TAG, "exception on decoding picture : " + e.toString());
        } catch (OutOfMemoryError e2) {
            System.gc();
        }
    }
}
