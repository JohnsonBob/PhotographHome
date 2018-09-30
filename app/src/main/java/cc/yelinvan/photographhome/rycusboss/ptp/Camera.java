package cc.yelinvan.photographhome.rycusboss.ptp;

import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import java.io.File;
import java.util.List;

import cc.yelinvan.photographhome.flash.been.UploadPhotoInfoBeen;
import cc.yelinvan.photographhome.rycusboss.ptp.model.LiveViewData;
import cc.yelinvan.photographhome.rycusboss.ptp.model.ObjectInfo;

public interface Camera {

    public interface CameraListener {
        public static final int NO = 1;
        public static final int START = 0;

        void onBulbExposureTime(int i);

        void onBulbStarted();

        void onBulbStopped();

        void onCameraStarted(Camera camera);

        void onCameraStopped(Camera camera);

        void onCapturedPictureReceived(int i, ObjectInfo objectInfo, Bitmap bitmap, Bitmap bitmap2);

        void onError(String str);

        void onFocusEnded(boolean z);

        void onFocusPointsChanged();

        void onFocusStarted();

        void onLiveViewData(LiveViewData liveViewData);

        void onLiveViewStarted();

        void onLiveViewStopped();

        void onNoCameraFound();

        void onObjectAdded(int i, int i2);

        void onPermissonAuth(int i);

        void onPropertyChanged(int i, int i2);

        void onPropertyDescChanged(int i, int[] iArr);

        void onPropertyStateChanged(int i, boolean z);

        void onRatingChange(int i);

        void onSony(UsbDevice usbDevice, UsbDeviceConnection usbDeviceConnection);
    }

    public interface RetrieveImageInfoListener {
        void onImageInfoRetrieved(int i, String str, String str2, int i2, Bitmap bitmap, boolean z);

        void onImageInfoRetrievedFailed(boolean z);
    }

    public interface RetrieveImageListener {
        void onImageRetrieved(int i, String str, int i2, int i3, String str2, String str3, UploadPhotoInfoBeen uploadPhotoInfoBeen);

        void onImageRetrievedForLookBigPhoto(int i, String str, int i2, int i3);
    }

    public interface StorageInfoListener {
        void onAllStoragesFound();

        void onImageHandlesRetrieved(int[] iArr);

        void onStorageFound(int i, String str);
    }

    public static class DriveLens {
        public static final int Far = 2;
        public static final int Hard = 3;
        public static final int Medium = 2;
        public static final int Near = 1;
        public static final int Soft = 1;
    }

    public static class Property {
        public static final int ApertureValue = 2;
        public static final int AvailableShots = 7;
        public static final int BatteryLevel = 6;
        public static final int ColorTemperature = 8;
        public static final int CurrentExposureIndicator = 14;
        public static final int CurrentFocusPoint = 13;
        public static final int ExposureCompensation = 16;
        public static final int ExposureMeteringMode = 11;
        public static final int FocusMeteringMode = 12;
        public static final int FocusMode = 9;
        public static final int FocusPoints = 15;
        public static final int IsoSpeed = 3;
        public static final int PictureStyle = 10;
        public static final int ShootingMode = 5;
        public static final int ShutterSpeed = 1;
        public static final int Whitebalance = 4;
    }

    public interface WorkerListener {
        void onWorkerEnded();

        void onWorkerStarted();
    }

    void capture();

    void driveLens(int i, int i2);

    void focus();

    String getBiggestPropertyValue(int i);

    String getDeviceInfo();

    String getDeviceName();

    List<FocusPoint> getFocusPoints();

    void getLiveViewPicture(LiveViewData liveViewData);

    int getProperty(int i);

    int[] getPropertyDesc(int i);

    boolean getPropertyEnabledState(int i);

    Integer getVendorId();

    boolean isAutoFocusSupported();

    boolean isDriveLensSupported();

    boolean isHistogramSupported();

    boolean isLiveViewAfAreaSupported();

    boolean isLiveViewOpen();

    boolean isLiveViewSupported();

    boolean isSessionOpen();

    boolean isSettingPropertyPossible(int i);

    Integer propertyToIcon(int i, int i2);

    String propertyToString(int i, int i2);

    void retrieveImage(RetrieveImageListener retrieveImageListener, int i, int i2, int i3, String str, String str2, UploadPhotoInfoBeen uploadPhotoInfoBeen);

    void retrieveImageForLookBigPhoto(RetrieveImageListener retrieveImageListener, int i, int i2, int i3);

    void retrieveImageHandles(StorageInfoListener storageInfoListener, int i, int i2, int i3);

    void retrieveImageInfo(RetrieveImageInfoListener retrieveImageInfoListener, int i, boolean z);

    void retrievePicture(int i);

    void retrieveStorages(StorageInfoListener storageInfoListener);

    void setCapturedPictureSampleSize(int i);

    void setLiveView(boolean z);

    void setLiveViewAfArea(float f, float f2);

    void setProperty(int i, int i2);

    void setWorkerListener(WorkerListener workerListener);

    void writeDebugInfo(File file);
}
