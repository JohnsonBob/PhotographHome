package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Operation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GetObjectCommand extends Command {
    private static final String TAG = GetObjectCommand.class.getSimpleName();
    String FLASH_DOWNLAOD_PAHT = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuu/");
    private String fileName;
    private String filePath;
    private Bitmap inBitmap;
    private final int objectHandle;
    private final Options options;
    private boolean outOfMemoryError;

    public GetObjectCommand(PtpCamera camera, int objectHandle, int sampleSize, String fileName) {
        super(camera);
        this.objectHandle = objectHandle;
        this.options = new Options();
        this.options.inSampleSize = sampleSize;
        this.fileName = fileName;
    }

    public Bitmap getBitmap() {
        return this.inBitmap;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public boolean isOutOfMemoryError() {
        return this.outOfMemoryError;
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        super.reset();
        this.inBitmap = null;
        this.outOfMemoryError = false;
    }

    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.GetObject, this.objectHandle);
    }

    protected void decodeData(ByteBuffer b, int length) {
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        try {
            this.filePath = null;
            File file = new File(this.FLASH_DOWNLAOD_PAHT + this.fileName);
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                BufferedOutputStream bos2 = null;
                File dir = new File(this.FLASH_DOWNLAOD_PAHT);
                if (!(dir != null && dir.exists() && dir.isDirectory())) {
                    dir.mkdir();
                }
                FileOutputStream fos2 = new FileOutputStream(file);
                try {
                    bos2 = new BufferedOutputStream(fos2);
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    fos.close();
                    bos.close();
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                try {
                    bos2.write(b.array(), 12, length - 12);
                    bos2.flush();
                    this.filePath = file.getAbsolutePath();
                    try {
                        fos2.close();
                        bos2.close();
                        bos = bos2;
                        fos = fos2;
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                        bos = bos2;
                        fos = fos2;
                    }
                } catch (FileNotFoundException e5) {
                    e = e5;
                    bos = bos2;
                    fos = fos2;
                    e.printStackTrace();
                    fos.close();
                    bos.close();
                } catch (IOException e6) {
                    bos = bos2;
                    fos = fos2;
                    e6.printStackTrace();
                    fos.close();
                    bos.close();
                } catch (Throwable th4) {
                    th = th4;
                    bos = bos2;
                    fos = fos2;
                    fos.close();
                    bos.close();
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e7) {
                e = e7;
                e.printStackTrace();
                try {
                    fos.close();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e8) {
                e8.printStackTrace();
                try {
                    fos.close();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (RuntimeException e9) {
            Log.i(TAG, "exception on decoding picture : " + e9.toString());
        } catch (OutOfMemoryError e10) {
            System.gc();
            this.outOfMemoryError = true;
        }
    }
}
