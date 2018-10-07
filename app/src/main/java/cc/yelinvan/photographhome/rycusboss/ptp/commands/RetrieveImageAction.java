package cc.yelinvan.photographhome.rycusboss.ptp.commands;

import cc.yelinvan.photographhome.flash.been.UploadPhotoInfoBeen;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera.RetrieveImageListener;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpAction;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpCamera.IO;
import cc.yelinvan.photographhome.support.media.ExifInterface;
import android.util.Log;

public class RetrieveImageAction implements PtpAction {
    String actId;
    private final PtpCamera camera;
    private final RetrieveImageListener listener;
    private final int objectHandle;
    private final int qualty;
    private final int sampleSize;
    String sepId;
    UploadPhotoInfoBeen uploadPhotoInfoBeen;

    public RetrieveImageAction(PtpCamera camera, RetrieveImageListener listener, int objectHandle, int sampleSize, int qualty, String actId, String sepId, UploadPhotoInfoBeen uploadPhotoInfoBeen) {
        this.camera = camera;
        this.listener = listener;
        this.objectHandle = objectHandle;
        this.sampleSize = sampleSize;
        this.qualty = qualty;
        this.actId = actId;
        this.sepId = sepId;
        this.uploadPhotoInfoBeen = uploadPhotoInfoBeen;
    }

    public void exec(IO io) {
        GetObjectCommand getObject = new GetObjectCommand(this.camera, this.objectHandle, this.sampleSize, "Alltuu_" + this.objectHandle);
        io.handleCommand(getObject);
        GetObjectExifCommand getObjectExif = new GetObjectExifCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectExif);
        if (getObject.getResponseCode() != 8193 || getObject.getFilePath() == null || getObjectExif.getResponseCode() != 8193) {
            Log.d(ExifInterface.TAG_FLASH, "exec: RetrieveImageAction:读取照片失败了");
            if (this.sampleSize == 6) {
                this.listener.onImageRetrievedForLookBigPhoto(this.objectHandle, null, 0, 0);
                return;
            }
            this.listener.onImageRetrieved(this.objectHandle, null, 0, 0, this.actId, this.sepId, this.uploadPhotoInfoBeen);
        } else if (this.sampleSize == 6) {
            this.listener.onImageRetrievedForLookBigPhoto(this.objectHandle, getObject.getFilePath(), getObjectExif.objectOrientation, this.qualty);
        } else {
            this.listener.onImageRetrieved(this.objectHandle, getObject.getFilePath(), getObjectExif.objectOrientation, this.qualty, this.actId, this.sepId, this.uploadPhotoInfoBeen);
        }
    }

    public void reset() {
    }
}
