package cc.yelinvan.photographhome.rycusboss.ptp;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cc.yelinvan.photographhome.flash.been.UploadPhotoInfoBeen;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.eos.EosEventCheckCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.eos.EosGetLiveViewPictureCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.eos.EosOpenSessionAction;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.eos.EosSetLiveViewAction;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.eos.EosSetPropertyCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.eos.EosTakePictureCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.model.LiveViewData;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.SimpleCommand;

public class EosCamera extends PtpCamera {
    public EosCamera(PtpUsbConnection connection, CameraListener listener, WorkerListener workerListener) {
        super(connection, listener, workerListener);
        addPropertyMapping(1, Property.EosShutterSpeed);
        addPropertyMapping(2, Property.EosApertureValue);
        addPropertyMapping(3, Property.EosIsoSpeed);
        addPropertyMapping(4, Property.EosWhitebalance);
        addPropertyMapping(5, Property.EosShootingMode);
        addPropertyMapping(7, Property.EosAvailableShots);
        addPropertyMapping(8, Property.EosColorTemperature);
        addPropertyMapping(9, 53512);
        addPropertyMapping(10, Property.EosPictureStyle);
        addPropertyMapping(11, Property.EosMeteringMode);
        addPropertyMapping(16, Property.EosExposureCompensation);
        this.histogramSupported = true;
    }

    protected void onOperationCodesReceived(Set<Integer> operations) {
        if (operations.contains(Integer.valueOf(PtpConstants.Operation.EosGetLiveViewPicture))) {
            this.liveViewSupported = true;
        }
        if (operations.contains(Integer.valueOf(PtpConstants.Operation.EosBulbStart)) && operations.contains(Integer.valueOf(PtpConstants.Operation.EosBulbEnd))) {
            this.bulbSupported = true;
        }
        if (operations.contains(Integer.valueOf(PtpConstants.Operation.EosDriveLens))) {
            this.driveLensSupported = true;
        }
        if (!operations.contains(Integer.valueOf(PtpConstants.Operation.EosRemoteReleaseOn)) || operations.contains(Integer.valueOf(PtpConstants.Operation.EosRemoteReleaseOff))) {
        }
    }

    public void onEventDirItemCreated(int objectHandle, int storageId, int objectFormat, String filename) {
        onEventObjectAdded(objectHandle, objectFormat);
    }

    protected void openSession() {
        this.queue.add(new EosOpenSessionAction(this));
    }

    protected void queueEventCheck() {
        this.queue.add(new EosEventCheckCommand(this));
    }

    public void focus() {
    }

    public void capture() {
        if (isBulbCurrentShutterSpeed()) {
            this.queue.add(new SimpleCommand(this, this.cameraIsCapturing ? PtpConstants.Operation.EosBulbEnd : PtpConstants.Operation.EosBulbStart));
        } else {
            this.queue.add(new EosTakePictureCommand(this));
        }
    }

    public void setProperty(int property, int value) {
        if (this.properties.containsKey(Integer.valueOf(property))) {
            this.queue.add(new EosSetPropertyCommand(this, ((Integer) this.virtualToPtpProperty.get(Integer.valueOf(property))).intValue(), value));
        }
    }

    public void setLiveView(boolean enabled) {
        if (this.liveViewSupported) {
            this.queue.add(new EosSetLiveViewAction(this, enabled));
        }
    }

    public void getLiveViewPicture(LiveViewData data) {
        if (this.liveViewOpen) {
            this.queue.add(new EosGetLiveViewPictureCommand(this, data));
        }
    }

    protected boolean isBulbCurrentShutterSpeed() {
        Integer value = (Integer) this.ptpProperties.get(Integer.valueOf(Property.EosShutterSpeed));
        return this.bulbSupported && value != null && value.intValue() == 12;
    }

    public void driveLens(int driveDirection, int pulses) {
        if (this.driveLensSupported && this.liveViewOpen) {
            int value = driveDirection == 1 ? 0 : 32768;
            switch (pulses) {
                case 2:
                    value |= 2;
                    break;
                case 3:
                    value |= 3;
                    break;
                default:
                    value |= 1;
                    break;
            }
            this.queue.add(new SimpleCommand(this, PtpConstants.Operation.EosDriveLens, value));
        }
    }

    public boolean isSettingPropertyPossible(int property) {
        boolean z = true;
        Integer mode = (Integer) this.ptpProperties.get(Integer.valueOf(Property.EosShootingMode));
        Integer wb = (Integer) this.ptpProperties.get(Integer.valueOf(Property.WhiteBalance));
        if (mode == null) {
            return false;
        }
        switch (property) {
            case 1:
                if (mode.intValue() == 3 || mode.intValue() == 1) {
                    return true;
                }
                return false;
            case 2:
                if (mode.intValue() == 3 || mode.intValue() == 2) {
                    return true;
                }
                return false;
            case 3:
            case 4:
            case 11:
                if (mode.intValue() < 0 || mode.intValue() > 6) {
                    z = false;
                }
                return z;
            case 8:
                if (wb == null || wb.intValue() != 9) {
                    z = false;
                }
                return z;
            case 15:
                return false;
            case 16:
                if (mode.intValue() == 0 || mode.intValue() == 1 || mode.intValue() == 2 || mode.intValue() == 5 || mode.intValue() == 6) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    @Override
    public void retrieveImage(RetrieveImageListener retrieveImageListener, int i, int i2, int i3, String str, String str2, UploadPhotoInfoBeen uploadPhotoInfoBeen) {

    }

    public Integer getVendorId() {
        return Integer.valueOf(PtpConstants.CanonVendorId);
    }

    public void setLiveViewAfArea(float posx, float posy) {
    }

    public List<FocusPoint> getFocusPoints() {
        return new ArrayList();
    }
}
