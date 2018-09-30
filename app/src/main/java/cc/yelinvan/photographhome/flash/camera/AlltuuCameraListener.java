package cc.yelinvan.photographhome.flash.camera;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cc.yelinvan.photographhome.flash.been.UploadPhotoInfoBeen;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera.CameraListener;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera.RetrieveImageInfoListener;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera.RetrieveImageListener;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera.StorageInfoListener;
import cc.yelinvan.photographhome.rycusboss.ptp.model.LiveViewData;
import cc.yelinvan.photographhome.rycusboss.ptp.model.ObjectInfo;

public class AlltuuCameraListener extends Service implements CameraListener, RetrieveImageInfoListener, RetrieveImageListener, StorageInfoListener {
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onPermissonAuth(int state) {
    }

    public void onCameraStarted(Camera camera) {
    }

    public void onCameraStopped(Camera camera) {
    }

    public void onRatingChange(int handle) {
    }

    public void onNoCameraFound() {
    }

    public void onError(String message) {
    }

    public void onSony(UsbDevice device, UsbDeviceConnection connection) {
    }

    public void onPropertyChanged(int property, int value) {
    }

    public void onPropertyStateChanged(int property, boolean enabled) {
    }

    public void onPropertyDescChanged(int property, int[] values) {
    }

    public void onLiveViewStarted() {
    }

    public void onLiveViewData(LiveViewData data) {
    }

    public void onLiveViewStopped() {
    }

    public void onCapturedPictureReceived(int objectHandle, ObjectInfo info, Bitmap thumbnail, Bitmap bitmap) {
    }

    public void onBulbStarted() {
    }

    public void onBulbExposureTime(int seconds) {
    }

    public void onBulbStopped() {
    }

    public void onFocusStarted() {
    }

    public void onFocusEnded(boolean hasFocused) {
    }

    public void onFocusPointsChanged() {
    }

    public void onObjectAdded(int handle, int format) {
    }

    public void onStorageFound(int handle, String label) {
    }

    public void onAllStoragesFound() {
    }

    public void onImageHandlesRetrieved(int[] handles) {
    }

    public void onImageInfoRetrieved(int objectHandle, String captureDate, String fileName, int storageId, Bitmap thumbnail, boolean isObjectAdd) {
    }

    public void onImageInfoRetrievedFailed(boolean isObjectAdd) {
    }

    public void onImageRetrieved(int objectHandle, String filePath, int orientation, int qualty, String actId, String sepId, UploadPhotoInfoBeen uploadPhotoInfoBeen) {
    }

    public void onImageRetrievedForLookBigPhoto(int objectHandle, String filePath, int orientation, int qualty) {
    }
}
