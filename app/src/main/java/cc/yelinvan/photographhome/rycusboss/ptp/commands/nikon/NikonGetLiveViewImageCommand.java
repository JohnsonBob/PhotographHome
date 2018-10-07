package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Product;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.model.LiveViewData;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.facebook.imagepipeline.memory.BitmapCounterProvider;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NikonGetLiveViewImageCommand extends NikonCommand {
    private static final String TAG = NikonGetLiveViewImageCommand.class.getSimpleName();
    private static boolean haveAddedDumpToAcra = false;
    private static byte[] tmpStorage = new byte[16384];
    private LiveViewData data;
    private final Options options;

    public NikonGetLiveViewImageCommand(NikonCamera camera, LiveViewData data) {
        super(camera);
        this.data = data;
        if (data == null) {
            this.data = new LiveViewData();
        } else {
            this.data = data;
        }
        this.options = new Options();
        this.options.inBitmap = this.data.bitmap;
        this.options.inSampleSize = 1;
        this.options.inTempStorage = tmpStorage;
        this.data.bitmap = null;
    }

    public void exec(IO io) {
        if (this.camera.isLiveViewOpen()) {
            io.handleCommand(this);
            if (this.responseCode == Response.DeviceBusy) {
                this.camera.onDeviceBusy(this, true);
                return;
            }
            this.data.hasHistogram = false;
            if (this.data.bitmap == null || this.responseCode != 8193) {
                this.camera.onLiveViewReceived(null);
            } else {
                this.camera.onLiveViewReceived(this.data);
            }
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, 37379);
    }

    protected void decodeData(ByteBuffer b, int length) {
        if (length > 128) {
            int pictureOffset;
            Log.d(TAG, "--------decodeData: " + b);
            this.data.hasAfFrame = false;
            int productId = this.camera.getProductId();
            int start = b.position();
            switch (productId) {
                case 1050:
                case Product.NikonD3 /*1052*/:
                case Product.NikonD3X /*1056*/:
                case Product.NikonD700 /*1058*/:
                case 1061:
                    pictureOffset = 64;
                    break;
                case 1057:
                case Product.NikonD5000 /*1059*/:
                case 1062:
                    pictureOffset = 128;
                    break;
                case 1064:
                case Product.NikonD5100 /*1065*/:
                    pictureOffset = BitmapCounterProvider.MAX_BITMAP_COUNT;
                    break;
                default:
                    return;
            }
            b.order(ByteOrder.BIG_ENDIAN);
            this.data.hasAfFrame = true;
            int wholeWidth = b.getShort() & 65535;
            int wholeHeight = b.getShort() & 65535;
            float multX = ((float) (b.getShort() & 65535)) / ((float) wholeWidth);
            float multY = ((float) (b.getShort() & 65535)) / ((float) wholeHeight);
            b.position(start + 16);
            this.data.nikonWholeWidth = wholeWidth;
            this.data.nikonWholeHeight = wholeHeight;
            this.data.nikonAfFrameWidth = (int) (((float) (b.getShort() & 65535)) * multX);
            this.data.nikonAfFrameHeight = (int) (((float) (b.getShort() & 65535)) * multY);
            this.data.nikonAfFrameCenterX = (int) (((float) (b.getShort() & 65535)) * multX);
            this.data.nikonAfFrameCenterY = (int) (((float) (b.getShort() & 65535)) * multY);
            b.order(ByteOrder.LITTLE_ENDIAN);
            b.position(start + pictureOffset);
            if (b.remaining() <= 128) {
                this.data.bitmap = null;
                return;
            }
            try {
                this.data.bitmap = BitmapFactory.decodeByteArray(b.array(), b.position(), length - b.position(), this.options);
            } catch (RuntimeException e) {
                Log.e(TAG, "decoding failed " + e.toString());
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
    }
}
