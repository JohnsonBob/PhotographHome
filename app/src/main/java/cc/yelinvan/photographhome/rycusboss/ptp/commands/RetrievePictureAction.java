package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.ObjectFormat;
import cc.yelinvan.photographhome.rycusboss.ptp.model.ObjectInfo;
import android.graphics.Bitmap;

public class RetrievePictureAction implements PtpAction {
    private final PtpCamera camera;
    private final int objectHandle;
    private final int sampleSize;

    public RetrievePictureAction(PtpCamera camera, int objectHandle, int sampleSize) {
        this.camera = camera;
        this.objectHandle = objectHandle;
        this.sampleSize = sampleSize;
    }

    public void exec(IO io) {
        GetObjectInfoCommand getInfo = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getInfo);
        if (getInfo.getResponseCode() == 8193) {
            ObjectInfo objectInfo = getInfo.getObjectInfo();
            if (objectInfo != null) {
                Bitmap thumbnail = null;
                if (objectInfo.thumbFormat == ObjectFormat.JFIF || objectInfo.thumbFormat == ObjectFormat.EXIF_JPEG) {
                    GetThumb getThumb = new GetThumb(this.camera, this.objectHandle);
                    io.handleCommand(getThumb);
                    if (getThumb.getResponseCode() == 8193) {
                        thumbnail = getThumb.getBitmap();
                    }
                }
                if (thumbnail == null) {
                }
                GetObjectExifCommand getObjectExif = new GetObjectExifCommand(this.camera, this.objectHandle);
                io.handleCommand(getObjectExif);
                if (getObjectExif.getResponseCode() != 8193) {
                    getObjectExif.objectOrientation = -1;
                }
                objectInfo.orientation = getObjectExif.objectOrientation;
                this.camera.onPictureReceived(this.objectHandle, objectInfo, thumbnail, null);
            }
        }
    }

    public void reset() {
    }
}
