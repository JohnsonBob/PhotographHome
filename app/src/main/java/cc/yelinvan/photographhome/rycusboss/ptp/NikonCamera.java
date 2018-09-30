package cc.yelinvan.photographhome.rycusboss.ptp;

import android.alltuu.com.newalltuuapp.rycusboss.ptp.PtpConstants.Operation;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.PtpConstants.Product;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.GetDevicePropDescCommand;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.InitiateCaptureCommand;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.RetrieveAddedObjectInfoAction;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.SimpleCommand;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonAfDriveCommand;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonCloseSessionAction;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonEventCheckCommand;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonGetLiveViewImageAction;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonGetLiveViewImageCommand;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonOpenSessionAction;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonStartLiveViewAction;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.commands.nikon.NikonStopLiveViewAction;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.model.DevicePropDesc;
import android.alltuu.com.newalltuuapp.rycusboss.ptp.model.LiveViewData;

import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class NikonCamera extends PtpCamera {
    private int afAreaHeight;
    private int afAreaWidth;
    private int enableAfAreaPoint;
    private boolean gotNikonShutterSpeed;
    private boolean liveViewStoppedInternal;
    private Set<Integer> supportedOperations;
    private int[] vendorPropCodes;
    private int wholeHeight;
    private int wholeWidth;

    public NikonCamera(PtpUsbConnection connection, CameraListener listener, WorkerListener workerListener) {
        super(connection, listener, workerListener);
        this.vendorPropCodes = new int[0];
        this.histogramSupported = false;
    }

    protected void onOperationCodesReceived(Set<Integer> operations) {
        this.supportedOperations = operations;
        if (operations.contains(Integer.valueOf(37379)) && operations.contains(Integer.valueOf(37377)) && operations.contains(Integer.valueOf(37378))) {
            this.liveViewSupported = true;
        }
        if (operations.contains(Integer.valueOf(37380))) {
            this.driveLensSupported = true;
        }
        if (operations.contains(Integer.valueOf(37381))) {
            this.liveViewAfAreaSupported = true;
        }
        if (operations.contains(Integer.valueOf(Operation.NikonAfDrive))) {
            this.autoFocusSupported = true;
        }
    }

    public void onPropertyChanged(int property, int value, int type) {
        super.onPropertyChanged(property, value, type);
        if (property == Property.NikonEnableAfAreaPoint) {
            this.enableAfAreaPoint = value;
            this.handler.post(new Runnable() {
                public void run() {
                    if (NikonCamera.this.listener != null) {
                        NikonCamera.this.listener.onFocusPointsChanged();
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int property, DevicePropDesc desc) {
        if (!this.gotNikonShutterSpeed) {
            if (property == Property.NikonShutterSpeed) {
                if (desc.description.length > 4) {
                    addPropertyMapping(1, Property.NikonShutterSpeed);
                    this.gotNikonShutterSpeed = true;
                } else {
                    return;
                }
            } else if (property == Property.ExposureTime) {
                addPropertyMapping(1, Property.ExposureTime);
                this.gotNikonShutterSpeed = true;
            }
        }
        super.onPropertyDescChanged(property, desc);
    }

    private void onPropertyCodesReceived(Set<Integer> properties) {
        if (properties.contains(Integer.valueOf(Property.NikonShutterSpeed))) {
            this.queue.add(new GetDevicePropDescCommand(this, Property.NikonShutterSpeed));
        }
        if (properties.contains(Integer.valueOf(Property.ExposureTime))) {
            this.queue.add(new GetDevicePropDescCommand(this, Property.ExposureTime));
        }
        addPropertyMapping(2, Property.FNumber);
        addPropertyMapping(3, Property.ExposureIndex);
        addPropertyMapping(4, Property.WhiteBalance);
        addPropertyMapping(8, Property.NikonWbColorTemp);
        addPropertyMapping(5, Property.ExposureProgramMode);
        addPropertyMapping(6, Property.BatteryLevel);
        addPropertyMapping(9, Property.FocusMode);
        addPropertyMapping(10, 53760);
        addPropertyMapping(11, Property.ExposureMeteringMode);
        addPropertyMapping(12, Property.FocusMeteringMode);
        addPropertyMapping(13, 53512);
        addPropertyMapping(14, Property.NikonExposureIndicateStatus);
        addPropertyMapping(16, Property.ExposureBiasCompensation);
        if (properties.contains(Integer.valueOf(Property.NikonEnableAfAreaPoint))) {
            addInternalProperty(Property.NikonEnableAfAreaPoint);
        }
        for (Integer property : properties) {
            if (this.ptpToVirtualProperty.containsKey(property) || this.ptpInternalProperties.contains(property)) {
                this.queue.add(new GetDevicePropDescCommand(this, property.intValue()));
            }
        }
    }

    protected void openSession() {
        this.queue.add(new NikonOpenSessionAction(this));
    }

    protected void closeSession() {
        this.queue.add(new NikonCloseSessionAction(this));
    }

    protected void queueEventCheck() {
        this.queue.add(new NikonEventCheckCommand(this));
    }

    public void onSessionOpened() {
        super.onSessionOpened();
        Set<Integer> properties = new HashSet();
        for (int valueOf : this.deviceInfo.devicePropertiesSupported) {
            properties.add(Integer.valueOf(valueOf));
        }
        for (int valueOf2 : this.vendorPropCodes) {
            properties.add(Integer.valueOf(valueOf2));
        }
        onPropertyCodesReceived(properties);
    }

    public void setVendorPropCodes(int[] vendorPropCodes) {
        this.vendorPropCodes = vendorPropCodes;
    }

    public void onEventObjectAdded(int objectHandle) {
        this.queue.add(new RetrieveAddedObjectInfoAction(this, objectHandle));
    }

    public void onEventCaptureComplete() {
    }

    public boolean hasSupportForOperation(int operation) {
        return this.supportedOperations.contains(Integer.valueOf(operation));
    }

    public void driveLens(int driveDirection, int pulses) {
        int i = 2;
        LinkedBlockingQueue linkedBlockingQueue = this.queue;
        if (driveDirection != 2) {
            i = 1;
        }
        linkedBlockingQueue.add(new SimpleCommand(this, 37380, i, pulses * 300));
    }

    protected boolean isBulbCurrentShutterSpeed() {
        return false;
    }

    public void onLiveViewStoppedInternal() {
        this.liveViewStoppedInternal = true;
    }

    public void setLiveView(boolean enabled) {
        this.liveViewStoppedInternal = false;
        if (enabled) {
            this.queue.add(new NikonStartLiveViewAction(this));
        } else {
            this.queue.add(new NikonStopLiveViewAction(this, true));
        }
    }

    public void getLiveViewPicture(LiveViewData reuse) {
        if (this.liveViewSupported && this.liveViewStoppedInternal) {
            this.liveViewStoppedInternal = false;
            this.queue.add(new NikonGetLiveViewImageAction(this, reuse));
            return;
        }
        this.queue.add(new NikonGetLiveViewImageCommand(this, reuse));
    }

    public boolean isSettingPropertyPossible(int property) {
        boolean z = false;
        Integer mode = (Integer) this.ptpProperties.get(Integer.valueOf(Property.ExposureProgramMode));
        Integer wb = (Integer) this.ptpProperties.get(Integer.valueOf(Property.WhiteBalance));
        if (mode == null) {
            return false;
        }
        switch (property) {
            case 1:
                if (mode.intValue() == 4 || mode.intValue() == 1) {
                    z = true;
                }
                return z;
            case 2:
                if (mode.intValue() == 3 || mode.intValue() == 1) {
                    z = true;
                }
                return z;
            case 3:
            case 4:
            case 11:
            case 16:
                if (mode.intValue() >= PanasonicMakernoteDirectory.TAG_BABY_AGE_1) {
                    return false;
                }
                return true;
            case 8:
                if (wb == null || wb.intValue() != PanasonicMakernoteDirectory.TAG_TRANSFORM_1) {
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    public Integer getVendorId() {
        return Integer.valueOf(PtpConstants.NikonVendorId);
    }

    public void focus() {
        this.queue.add(new NikonAfDriveCommand(this));
    }

    public void capture() {
        if (this.liveViewOpen) {
            this.queue.add(new NikonStopLiveViewAction(this, false));
        }
        this.queue.add(new InitiateCaptureCommand(this));
    }

    public void onLiveViewReceived(LiveViewData data) {
        super.onLiveViewReceived(data);
        if (data != null) {
            this.wholeWidth = data.nikonWholeWidth;
            this.wholeHeight = data.nikonWholeHeight;
            this.afAreaWidth = data.nikonAfFrameWidth;
            this.afAreaHeight = data.nikonAfFrameHeight;
        }
    }

    public void setLiveViewAfArea(float posx, float posy) {
        if (this.supportedOperations.contains(Integer.valueOf(37381))) {
            this.queue.add(new SimpleCommand(this, 37381, (int) Math.min((float) (this.wholeWidth - (this.afAreaWidth >> 1)), Math.max((float) (this.afAreaWidth >> 1), ((float) this.wholeWidth) * posx)), (int) Math.min((float) (this.wholeHeight - (this.afAreaHeight >> 1)), Math.max((float) (this.afAreaHeight >> 1), ((float) this.wholeHeight) * posy))));
        }
    }

    public List<FocusPoint> getFocusPoints() {
        List<FocusPoint> points = new ArrayList();
        switch (this.productId) {
            case Product.NikonD200 /*1040*/:
            case Product.NikonD80 /*1042*/:
                points.add(new FocusPoint(0, 0.5f, 0.5f, 0.04f));
                points.add(new FocusPoint(1, 0.5f, 0.29f, 0.04f));
                points.add(new FocusPoint(2, 0.5f, 0.71f, 0.04f));
                points.add(new FocusPoint(3, 0.33f, 0.5f, 0.04f));
                points.add(new FocusPoint(4, 0.67f, 0.5f, 0.04f));
                points.add(new FocusPoint(5, 0.22f, 0.5f, 0.04f));
                points.add(new FocusPoint(6, 0.78f, 0.5f, 0.04f));
                points.add(new FocusPoint(7, 0.33f, 0.39f, 0.04f));
                points.add(new FocusPoint(8, 0.67f, 0.39f, 0.04f));
                points.add(new FocusPoint(9, 0.33f, 0.61f, 0.04f));
                points.add(new FocusPoint(10, 0.67f, 0.61f, 0.04f));
                break;
            case 1044:
                points.add(new FocusPoint(0, 0.5f, 0.5f, 0.04f));
                points.add(new FocusPoint(0, 0.3f, 0.5f, 0.04f));
                points.add(new FocusPoint(0, 0.7f, 0.5f, 0.04f));
                break;
            case 1050:
            case Product.NikonD3 /*1052*/:
            case Product.NikonD3X /*1056*/:
            case 1061:
            case 1062:
                points.add(new FocusPoint(1, 0.5f, 0.5f, 0.035f));
                points.add(new FocusPoint(3, 0.5f, 0.36f, 0.035f));
                points.add(new FocusPoint(5, 0.5f, 0.64f, 0.035f));
                points.add(new FocusPoint(21, 0.65f, 0.5f, 0.035f));
                points.add(new FocusPoint(23, 0.65f, 0.4f, 0.035f));
                points.add(new FocusPoint(25, 0.65f, 0.6f, 0.035f));
                points.add(new FocusPoint(31, 0.75f, 0.5f, 0.035f));
                points.add(new FocusPoint(39, 0.35f, 0.5f, 0.035f));
                points.add(new FocusPoint(41, 0.35f, 0.4f, 0.035f));
                points.add(new FocusPoint(43, 0.35f, 0.6f, 0.035f));
                points.add(new FocusPoint(49, 0.25f, 0.5f, 0.035f));
                if (this.enableAfAreaPoint == 0) {
                }
                break;
            case 1057:
            case Product.NikonD5000 /*1059*/:
                points.add(new FocusPoint(1, 0.5f, 0.5f, 0.04f));
                points.add(new FocusPoint(2, 0.5f, 0.3f, 0.04f));
                points.add(new FocusPoint(3, 0.5f, 0.7f, 0.04f));
                points.add(new FocusPoint(4, 0.33f, 0.5f, 0.04f));
                points.add(new FocusPoint(5, 0.33f, 0.35f, 0.04f));
                points.add(new FocusPoint(6, 0.33f, 0.65f, 0.04f));
                points.add(new FocusPoint(7, 0.22f, 0.5f, 0.04f));
                points.add(new FocusPoint(8, 0.67f, 0.5f, 0.04f));
                points.add(new FocusPoint(9, 0.67f, 0.35f, 0.04f));
                points.add(new FocusPoint(10, 0.67f, 0.65f, 0.04f));
                points.add(new FocusPoint(11, 0.78f, 0.5f, 0.04f));
                break;
            case 1064:
                points.add(new FocusPoint(1, 0.5f, 0.5f, 0.035f));
                points.add(new FocusPoint(3, 0.5f, 0.32f, 0.035f));
                points.add(new FocusPoint(5, 0.5f, 0.68f, 0.035f));
                points.add(new FocusPoint(19, 0.68f, 0.5f, 0.035f));
                points.add(new FocusPoint(20, 0.68f, 0.4f, 0.035f));
                points.add(new FocusPoint(21, 0.68f, 0.6f, 0.035f));
                points.add(new FocusPoint(25, 0.8f, 0.5f, 0.035f));
                points.add(new FocusPoint(31, 0.32f, 0.5f, 0.035f));
                points.add(new FocusPoint(32, 0.32f, 0.4f, 0.035f));
                points.add(new FocusPoint(33, 0.32f, 0.6f, 0.035f));
                points.add(new FocusPoint(37, 0.2f, 0.5f, 0.035f));
                if (this.enableAfAreaPoint == 0) {
                }
                break;
        }
        return points;
    }
}
