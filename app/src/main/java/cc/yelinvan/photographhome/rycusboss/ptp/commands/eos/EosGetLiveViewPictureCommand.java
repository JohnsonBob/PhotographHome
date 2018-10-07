package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Response;
import cc.yelinvan.photographhome.rycusboss.ptp.model.LiveViewData;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EosGetLiveViewPictureCommand extends EosCommand {
    private static final String TAG = EosGetLiveViewPictureCommand.class.getSimpleName();
    private static byte[] tmpStorage = new byte[16384];
    private LiveViewData data;
    private final Options options;

    public EosGetLiveViewPictureCommand(EosCamera camera, LiveViewData data) {
        super(camera);
        if (data == null) {
            this.data = new LiveViewData();
            this.data.histogram = ByteBuffer.allocate(4096);
            this.data.histogram.order(ByteOrder.LITTLE_ENDIAN);
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
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (this.data.bitmap == null || this.responseCode != 8193) {
            this.camera.onLiveViewReceived(null);
        } else {
            this.camera.onLiveViewReceived(this.data);
        }
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosGetLiveViewPicture, 1048576);
    }

    protected void decodeData(ByteBuffer b, int length) {
        this.data.hasHistogram = false;
        this.data.hasAfFrame = false;
        if (length >= 1000) {
            do {
                try {
                    if (b.hasRemaining()) {
                        int subLength = b.getInt();
                        int type = b.getInt();
                        if (subLength >= 8) {
                            switch (type) {
                                case 1:
                                    this.data.bitmap = BitmapFactory.decodeByteArray(b.array(), b.position(), subLength - 8, this.options);
                                    b.position((b.position() + subLength) - 8);
                                    break;
                                case 3:
                                    this.data.hasHistogram = true;
                                    b.get(this.data.histogram.array(), 0, 4096);
                                    break;
                                case 4:
                                    this.data.zoomFactor = b.getInt();
                                    break;
                                case 5:
                                    this.data.zoomRectRight = b.getInt();
                                    this.data.zoomRectBottom = b.getInt();
                                    break;
                                case 6:
                                    this.data.zoomRectLeft = b.getInt();
                                    this.data.zoomRectTop = b.getInt();
                                    break;
                                case 7:
                                    int unknownInt = b.getInt();
                                    break;
                                default:
                                    b.position((b.position() + subLength) - 8);
                                    break;
                            }
                        }
                        throw new RuntimeException("Invalid sub size " + subLength);
                    }
                    return;
                } catch (RuntimeException e) {
                    Log.e(TAG, "" + e.toString());
                    Log.e(TAG, "" + e.getLocalizedMessage());
                    return;
                }
            } while (length - b.position() >= 8);
        }
    }
}
