package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.rycusboss.ptp.Camera.RetrieveImageInfoListener;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.ObjectFormat;
import cc.yelinvan.photographhome.rycusboss.ptp.model.ObjectInfo;
import android.graphics.Bitmap;

public class RetrieveImageInfoAction implements PtpAction {
    private final PtpCamera camera;
    private final boolean isObjectAdd;
    private final RetrieveImageInfoListener listener;
    private final int objectHandle;

    public RetrieveImageInfoAction(PtpCamera camera, RetrieveImageInfoListener listener, int objectHandle, boolean isObjectAdd) {
        this.camera = camera;
        this.listener = listener;
        this.objectHandle = objectHandle;
        this.isObjectAdd = isObjectAdd;
    }

    public void exec(IO io) {
        GetObjectInfoCommand getInfo = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getInfo);
        if (getInfo.getResponseCode() != 8193) {
            this.listener.onImageInfoRetrievedFailed(this.isObjectAdd);
            return;
        }
        ObjectInfo objectInfo = getInfo.getObjectInfo();
        if (objectInfo == null) {
            this.listener.onImageInfoRetrievedFailed(this.isObjectAdd);
            return;
        }
        Bitmap thumbnail = null;
        if (objectInfo.thumbFormat == ObjectFormat.JFIF || objectInfo.thumbFormat == ObjectFormat.EXIF_JPEG) {
            GetThumb getThumb = new GetThumb(this.camera, this.objectHandle);
            io.handleCommand(getThumb);
            if (getThumb.getResponseCode() == 8193) {
                thumbnail = getThumb.getBitmap();
            }
        }
        GetObjectExifCommand getObjectExif = new GetObjectExifCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectExif);
        if (getObjectExif.getResponseCode() != 8193) {
            getObjectExif.objectOrientation = -1;
        }
        objectInfo.orientation = getObjectExif.objectOrientation;
        this.listener.onImageInfoRetrieved(this.objectHandle, objectInfo.captureDate, objectInfo.filename, objectInfo.storageId, thumbnail, this.isObjectAdd);
    }

    public void reset() {
    }
}
