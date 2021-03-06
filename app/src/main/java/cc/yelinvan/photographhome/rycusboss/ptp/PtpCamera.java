package cc.yelinvan.photographhome.rycusboss.ptp;

import android.graphics.Bitmap;
import android.hardware.usb.UsbRequest;
import android.mtp.MtpDevice;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.yelinvan.photographhome.flash.been.UploadPhotoInfoBeen;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.ObjectFormat;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Product;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.CloseSessionCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.Command;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.GetDeviceInfoCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.GetDevicePropValueCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.GetObjectHandlesCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.GetStorageInfosAction;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.InitiateCaptureCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.OpenSessionCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.RetrieveImageAction;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.RetrieveImageInfoAction;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.RetrievePictureAction;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.SetDevicePropValueCommand;
import cc.yelinvan.photographhome.rycusboss.ptp.model.DeviceInfo;
import cc.yelinvan.photographhome.rycusboss.ptp.model.DevicePropDesc;
import cc.yelinvan.photographhome.rycusboss.ptp.model.LiveViewData;
import cc.yelinvan.photographhome.rycusboss.ptp.model.ObjectInfo;
import cc.yelinvan.photographhome.utils.AppConfig;

import static cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Property.NikonApplicationMode;

public abstract class PtpCamera implements Camera {
    private static final String TAG = PtpCamera.class.getSimpleName();
    private static volatile CancellationSignal cancellationSignal;
    protected boolean autoFocusSupported;
    protected boolean bulbSupported;
    protected boolean cameraIsCapturing;
    private final PtpUsbConnection connection;
    protected DeviceInfo deviceInfo;
    protected boolean driveLensSupported;
    protected final Handler handler = new Handler();
    protected boolean histogramSupported;
    protected CameraListener listener;
    protected boolean liveViewAfAreaSupported;
    protected boolean liveViewOpen;
    protected boolean liveViewSupported;
    private MtpDevice mtpDevice;
    private int pictureSampleSize;
    protected final int productId;
    protected final Map<Integer, Integer> properties = new HashMap();
    private final Map<Integer, int[]> propertyDescriptions = new HashMap();
    protected final Set<Integer> ptpInternalProperties = new HashSet();
    protected final Map<Integer, Integer> ptpProperties = new HashMap();
    protected final Map<Integer, DevicePropDesc> ptpPropertyDesc = new HashMap();
    protected final Map<Integer, Integer> ptpToVirtualProperty = new HashMap();
    protected final LinkedBlockingQueue<PtpAction> queue = new LinkedBlockingQueue();
    protected State state;
    private int transactionId;
    private final int vendorId;
    protected final Map<Integer, Integer> virtualToPtpProperty = new HashMap();
    private WorkerListener workerListener;
    private final WorkerThread workerThread = new WorkerThread(this, null);

    public interface IO {
        void handleCommand(Command command);
    }

    enum State {
        Starting,
        Active,
        Stoping,
        Stopped,
        Error
    }

    private class WorkerThread extends Thread implements IO {
        private ByteBuffer bigIn1;
        private ByteBuffer bigIn2;
        private ByteBuffer bigIn3;
        private final int bigInSize;
        private ByteBuffer fullIn;
        private int fullInSize;
        private long lastEventCheck;
        private int maxPacketInSize;
        private int maxPacketOutSize;
        private UsbRequest r1;
        private UsbRequest r3;
        private UsbRequest r2;
        private ByteBuffer smallIn;
        public boolean stop;

        WorkerThread(PtpCamera x0, Object x1) {
            this();
        }

        private WorkerThread() {
            this.bigInSize = 16384;
            this.fullInSize = 16384;
        }

        public void run() {
            WorkerThread workerThread = this;
            int r4 = 65535;
            int r6 = 16384;
            workerThread.notifyWorkStarted();
            PtpCamera ptpCamera = PtpCamera.this;
            PtpUsbConnection ptpUsbConnection = ptpCamera.connection;
            workerThread.maxPacketOutSize = ptpUsbConnection.getMaxPacketOutSize();
            workerThread.maxPacketInSize = ptpUsbConnection.getMaxPacketInSize();
            synchronized(workerThread){
                if (workerThread.maxPacketOutSize <= 0){
                    String r44 = "Usb initialization error: out size invalid %d";
                    Object[] r55 = new Object[1];
                    r6 = java.lang.Integer.valueOf(workerThread.maxPacketOutSize);
                    r55[0] = r6;
                    String r444 = java.lang.String.format(r44, r55);
                    ptpCamera.onUsbError(r444);
                }
                if (workerThread.maxPacketOutSize <= r4) {
                    if (workerThread.maxPacketInSize <= 0) {
                        String r44 = "usb initialization error: in size invalid %d";
                        Object[] r55 = new java.lang.Object[1];
                        r6 = java.lang.Integer.valueOf(workerThread.maxPacketInSize);
                        r55[0] = r6;
                        String r444 = java.lang.String.format(r44, r55);
                        ptpCamera.onUsbError(r444);
                        return;
                    }
                }
                if (workerThread.maxPacketInSize <= r4){
                    int r333 = java.lang.Math.max(workerThread.maxPacketInSize, workerThread.maxPacketOutSize);
                    ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(r333);
                    workerThread.smallIn = byteBuffer;
                    ByteOrder byteOrder = java.nio.ByteOrder.LITTLE_ENDIAN;
                    workerThread.smallIn.order(byteOrder);
                    workerThread.bigIn1 = java.nio.ByteBuffer.allocate(r6);
                    workerThread.bigIn1.order(byteOrder);
                    workerThread.bigIn2 = java.nio.ByteBuffer.allocate(r6);
                    workerThread.bigIn2.order(byteOrder);
                    workerThread.bigIn3 = java.nio.ByteBuffer.allocate(r6);
                    workerThread.bigIn3.order(byteOrder);
                    workerThread.fullIn = java.nio.ByteBuffer.allocate(workerThread.fullInSize);
                    workerThread.fullIn.order(byteOrder);
                    workerThread.r1 = ptpUsbConnection.createInRequest();
                    workerThread.r2 = ptpUsbConnection.createInRequest();
                    workerThread.r3 = ptpUsbConnection.createInRequest();
                    if (workerThread.r1 == null){
                        workerThread.stop = true;
                    }
                }
                if (workerThread.r2 == null){
                    workerThread.stop = true;
                }
                PtpAction ptpAction = null;
                if (workerThread.r3 != null){
                    if (workerThread.stop == false){
                        long rr4 = workerThread.lastEventCheck + 700;
                        long r66 = java.lang.System.currentTimeMillis();
                        int tempr3 = (rr4 > r66 ? 1 : (rr4 == r66 ? 0 : -1));
                        if (tempr3 >= 0){
                            TimeUnit r666 = TimeUnit.MILLISECONDS;
                            try {
                                ptpAction = (PtpAction) ptpCamera.queue.poll(1000, r666);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

                if (workerThread.r3 == null){
                    if (workerThread.r2 == null){
                        if (workerThread.r1 == null){
                            workerThread.notifyWorkEnded();
                            return;
                        }
                    }
                }
                /*workerThread.r3.close();
                workerThread.r2.close();
                workerThread.r1.close();
*/
                long rr44 = java.lang.System.currentTimeMillis();
                workerThread.lastEventCheck = rr44;
                ptpCamera.queueEventCheck();


                if(ptpAction != null)ptpAction.exec(workerThread);
                PtpAction ptpAction2 = null;
                if (workerThread.stop == false){
                    long r44 = workerThread.lastEventCheck + 700;
                    long r66 = System.currentTimeMillis();
                    int tempr3 = (r44 > r66 ? 1 : (r44 == r66 ? 0 : -1));
                    if (tempr3 >= 0){

                        TimeUnit timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS;
                        try {
                            ptpAction2 = (PtpAction) ptpCamera.queue.poll(1000, timeUnit);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                PtpAction ptpAction3 = null;
                if (ptpAction2 == null){
                    if (workerThread.stop == false){
                        long r44 = workerThread.lastEventCheck+700;
                        long r66 = java.lang.System.currentTimeMillis();
                        int tempr3 = (r44 > r66 ? 1 : (r44 == r66 ? 0 : -1));
                        if (tempr3 >= 0){
                            TimeUnit timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS;
                            try {
                                ptpAction3 = (PtpAction) ptpCamera.queue.poll(1000, timeUnit);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        }

        public void handleCommand(Command command) {
            ByteBuffer b = this.smallIn;
            b.position(0);
            command.encodeCommand(b);
            int outLen = b.position();
            int res;
            final int bulkTransferOut = PtpCamera.this.connection.bulkTransferOut(smallIn.array(), outLen, 3000000);
            if (PtpCamera.this.connection.bulkTransferOut(b.array(), outLen, AppConfig.USB_TRANSFER_TIMEOUT) < outLen) {
                PtpCamera.this.onUsbError(String.format("Code CP %d %d", new Object[]{Integer.valueOf(bulkTransferOut), Integer.valueOf(outLen)}));
                return;
            }
            if (command.hasDataToSend()) {
                b = ByteBuffer.allocate(PtpCamera.this.connection.getMaxPacketOutSize());
                b.order(ByteOrder.LITTLE_ENDIAN);
                command.encodeData(b);
                outLen = b.position();
                final int bulkTransferOut2 = PtpCamera.this.connection.bulkTransferOut(b.array(), outLen, 3000000);
                if (PtpCamera.this.connection.bulkTransferOut(b.array(), outLen, AppConfig.USB_TRANSFER_TIMEOUT) < outLen) {
                    PtpCamera.this.onUsbError(String.format("Code DP %d %d", new Object[]{Integer.valueOf(bulkTransferOut2), Integer.valueOf(outLen)}));
                    return;
                }
            }
            while (!command.hasResponseReceived()) {
                int maxPacketSize = this.maxPacketInSize;
                ByteBuffer in = this.smallIn;
                in.position(0);
                res = 0;
                while (res == 0) {
                    res = PtpCamera.this.connection.bulkTransferIn(in.array(), maxPacketSize, AppConfig.USB_TRANSFER_TIMEOUT);
                }
                if (res < 12) {
                    PtpCamera.this.onUsbError(String.format("Couldn't read header, only %d bytes available!", new Object[]{Integer.valueOf(res)}));
                    return;
                }
                ByteBuffer infull;
                int read = res;
                int length = in.getInt();
                if (read < length) {
                    if (length > this.fullInSize) {
                        this.fullInSize = (int) (((double) length) * 1.1d);
                        this.fullIn = ByteBuffer.allocate(this.fullInSize);
                        this.fullIn.order(ByteOrder.LITTLE_ENDIAN);
                    }
                    infull = this.fullIn;
                    infull.position(0);
                    infull.put(in.array(), 0, read);
                    int nextSize = Math.min(16384, length - read);
                    int nextSize2 = Math.max(0, Math.min(16384, (length - read) - nextSize));
                    this.r1.queue(this.bigIn1, nextSize);
                    if (nextSize2 > 0) {
                        this.r2.queue(this.bigIn2, nextSize2);
                    }
                    while (read < length) {
                        int nextSize3 = Math.max(0, Math.min(16384, ((length - read) - nextSize) - nextSize2));
                        if (nextSize3 > 0) {
                            this.bigIn3.position(0);
                            this.r3.queue(this.bigIn3, nextSize3);
                        }
                        if (nextSize > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn1.array(), 0, infull.array(), read, nextSize);
                            read += nextSize;
                        }
                        nextSize = Math.max(0, Math.min(16384, ((length - read) - nextSize2) - nextSize3));
                        if (nextSize > 0) {
                            this.bigIn1.position(0);
                            this.r1.queue(this.bigIn1, nextSize);
                        }
                        if (nextSize2 > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn2.array(), 0, infull.array(), read, nextSize2);
                            read += nextSize2;
                        }
                        nextSize2 = Math.max(0, Math.min(16384, ((length - read) - nextSize) - nextSize3));
                        if (nextSize2 > 0) {
                            this.bigIn2.position(0);
                            this.r2.queue(this.bigIn2, nextSize2);
                        }
                        if (nextSize3 > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn3.array(), 0, infull.array(), read, nextSize3);
                            read += nextSize3;
                        }
                    }
                } else {
                    infull = in;
                }
                infull.position(0);
                try {
                    command.receivedRead(infull);
                } catch (RuntimeException e) {
                    PtpCamera.this.onPtpError(String.format("Error parsing %s with length %d", new Object[]{command.getClass().getSimpleName(), Integer.valueOf(length)}));
                }
            }
        }

        private void notifyWorkStarted() {
            WorkerListener l = PtpCamera.this.workerListener;
            if (l != null) {
                l.onWorkerStarted();
            }
        }

        private void notifyWorkEnded() {
            WorkerListener l = PtpCamera.this.workerListener;
            if (l != null) {
                l.onWorkerEnded();
            }
        }
    }

    protected abstract boolean isBulbCurrentShutterSpeed();

    protected abstract void onOperationCodesReceived(Set<Integer> set);

    protected abstract void queueEventCheck();

    public PtpCamera(PtpUsbConnection connection, CameraListener listener, WorkerListener workerListener) {
        this.connection = connection;
        this.listener = listener;
        this.workerListener = workerListener;
        this.pictureSampleSize = 2;
        this.state = State.Starting;
        this.vendorId = connection.getVendorId();
        this.productId = connection.getProductId();
        this.queue.add(new GetDeviceInfoCommand(this));
        openSession();
        this.workerThread.start();
    }

    protected void addPropertyMapping(int virtual, int ptp) {
        this.ptpToVirtualProperty.put(Integer.valueOf(ptp), Integer.valueOf(virtual));
        this.virtualToPtpProperty.put(Integer.valueOf(virtual), Integer.valueOf(ptp));
    }

    protected void addInternalProperty(int ptp) {
        this.ptpInternalProperties.add(Integer.valueOf(ptp));
    }

    public void setListener(CameraListener listener) {
        this.listener = listener;
    }

    public void shutdown() {
        this.state = State.Stoping;
        this.workerThread.lastEventCheck = System.currentTimeMillis() + 1000000;
        this.queue.clear();
        closeSession();
    }

    public void shutdownHard() {
        this.state = State.Stopped;
        synchronized (this.workerThread) {
            this.workerThread.stop = true;
        }
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public State getState() {
        return this.state;
    }

    public int nextTransactionId() {
        transactionId = (transactionId + 1);
        return transactionId;
    }

    public int currentTransactionId() {
        return this.transactionId;
    }

    public void resetTransactionId() {
        this.transactionId = 0;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        Set<Integer> operations = new HashSet();
        for (int valueOf : deviceInfo.operationsSupported) {
            operations.add(Integer.valueOf(valueOf));
        }
        onOperationCodesReceived(operations);
    }

    public void enqueue(final Command cmd, int delay) {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                if (PtpCamera.this.state == State.Active) {
                    PtpCamera.this.queue.add(cmd);
                }
            }
        }, (long) delay);
    }

    public int getPtpProperty(int property) {
        Integer value = (Integer) this.ptpProperties.get(Integer.valueOf(property));
        return value != null ? value.intValue() : 0;
    }

    public void onSessionOpened() {
        Log.d("CameraEvent", "PtpCamera onSessionOpened");
        if (!(getVendorId().intValue() == PtpConstants.CanonVendorId || ((getVendorId().intValue() == PtpConstants.NikonVendorId && getProductId() == Product.NikonD700) || this.connection == null))) {
            this.mtpDevice = new MtpDevice(this.connection.getUsbDevice());
            this.mtpDevice.open(this.connection.getConnection());
            Log.d("CameraEvent", "MtpDevice已开启");
            setApplicationModeForD850(1);
        }
        this.state = State.Active;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStarted(PtpCamera.this);
                }
            }
        });
    }

    public void onRatingChange(final int handle) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onRatingChange(handle);
                }
            }
        });
    }

    public void onSessionClosed() {
        shutdownHard();
        if (!(getVendorId().intValue() == PtpConstants.CanonVendorId || ((getVendorId().intValue() == PtpConstants.NikonVendorId && getProductId() == Product.NikonD700) || this.mtpDevice == null))) {
            this.mtpDevice.close();
            Log.d("CameraEvent", "关闭MtpDevice");
        }
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStopped(PtpCamera.this);
                }
            }
        });
    }

    public void onPropertyChanged(int property, final int value, int type) {
        Log.i("CameraEvent", "p " + property + " " + value + "   " + type);
        if (type == 1) {
            if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
                return;
            }
            if (!(getVendorId().intValue() == PtpConstants.NikonVendorId && getProductId() == Product.NikonD700) && VERSION.SDK_INT >= 24) {
                if (!(cancellationSignal == null || cancellationSignal.isCanceled())) {
                    cancellationSignal.cancel();
                    Log.d("CameraEvent", "监听事件被取消");
                }
                cancellationSignal = new CancellationSignal();
                new Thread() {
                    public void run() {
                        if (VERSION.SDK_INT >= 24) {
                            while (true) {
                                try {
                                    Log.d("CameraEvent", "开始监听事件");
                                    Log.d("CameraEvent", "监听到事件:" + PtpCamera.this.mtpDevice.readEvent(PtpCamera.cancellationSignal).getEventCode());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("CameraEvent", "监听事件意外终止");
                                    return;
                                }
                            }
                        }
                    }
                }.start();
            }
        } else if (type != 2) {
            this.ptpProperties.put(Integer.valueOf(property), Integer.valueOf(value));
            final Integer virtual = (Integer) this.ptpToVirtualProperty.get(Integer.valueOf(property));
            if (virtual != null) {
                this.handler.post(new Runnable() {
                    public void run() {
                        PtpCamera.this.properties.put(virtual, Integer.valueOf(value));
                        if (PtpCamera.this.listener != null) {
                            PtpCamera.this.listener.onPropertyChanged(virtual.intValue(), value);
                        }
                    }
                });
            }
        } else if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
        } else {
            if ((getVendorId().intValue() != PtpConstants.NikonVendorId || getProductId() != Product.NikonD700) && VERSION.SDK_INT >= 24 && cancellationSignal != null && !cancellationSignal.isCanceled()) {
                cancellationSignal.cancel();
            }
        }
    }

    public void onPropertyDescChanged(int property, final int[] values) {
        Log.d(TAG, String.format("onPropertyDescChanged %s:\n%s", new Object[]{PtpConstants.propertyToString(property), Arrays.toString(values)}));
        final Integer virtual = (Integer) this.ptpToVirtualProperty.get(Integer.valueOf(property));
        if (virtual != null) {
            this.handler.post(new Runnable() {
                public void run() {
                    PtpCamera.this.propertyDescriptions.put(virtual, values);
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onPropertyDescChanged(virtual.intValue(), values);
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int property, DevicePropDesc desc) {
        this.ptpPropertyDesc.put(Integer.valueOf(property), desc);
        onPropertyDescChanged(property, desc.description);
    }

    public void onLiveViewStarted() {
        this.liveViewOpen = true;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStarted();
                }
            }
        });
    }

    public void onLiveViewRestarted() {
        this.liveViewOpen = true;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStarted();
                }
            }
        });
    }

    public void onLiveViewStopped() {
        this.liveViewOpen = false;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStopped();
                }
            }
        });
    }

    public void onLiveViewReceived(final LiveViewData data) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewData(data);
                }
            }
        });
    }

    public void onPictureReceived(int objectHandle, ObjectInfo info, Bitmap thumbnail, Bitmap bitmap) {
        final int i = objectHandle;
        final ObjectInfo objectInfo = info;
        final Bitmap bitmap2 = thumbnail;
        final Bitmap bitmap3 = bitmap;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCapturedPictureReceived(i, objectInfo, bitmap2, bitmap3);
                }
            }
        });
    }

    public void onEventCameraCapture(boolean started) {
        this.cameraIsCapturing = started;
        if (isBulbCurrentShutterSpeed()) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener == null) {
                        return;
                    }
                    if (PtpCamera.this.cameraIsCapturing) {
                        PtpCamera.this.listener.onBulbStarted();
                    } else {
                        PtpCamera.this.listener.onBulbStopped();
                    }
                }
            });
        }
    }

    public void onEventDevicePropChanged(int property) {
        if ((this.ptpToVirtualProperty.containsKey(Integer.valueOf(property)) || this.ptpInternalProperties.contains(Integer.valueOf(property))) && this.ptpPropertyDesc.containsKey(Integer.valueOf(property))) {
            this.queue.add(new GetDevicePropValueCommand(this, property, ((DevicePropDesc) this.ptpPropertyDesc.get(Integer.valueOf(property))).datatype));
        }
    }

    public void onEventObjectAdded(final int handle, final int format) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onObjectAdded(handle, format);
                }
            }
        });
    }

    public void onBulbExposureTime(final int seconds) {
        if (seconds >= 0 && seconds <= 360000) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onBulbExposureTime(seconds);
                    }
                }
            });
        }
    }

    public void onFocusStarted() {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusStarted();
                }
            }
        });
    }

    public void onFocusEnded(final boolean hasFocused) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusEnded(hasFocused);
                }
            }
        });
    }

    public void onDeviceBusy(PtpAction action, boolean requeue) {
        if (requeue) {
            action.reset();
            this.queue.add(action);
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }

    public void onPtpWarning(String message) {
    }

    public void onPtpError(final String message) {
        if (this.state == State.Active) {
            shutdown();
        } else {
            shutdownHard();
        }
        this.state = State.Error;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(message);
                }
            }
        });
    }

    private void onUsbError(final String message) {
        this.queue.clear();
        if (this.state == State.Active) {
            shutdown();
        } else {
            shutdownHard();
        }
        this.state = State.Error;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(String.format("Error in USB communication: %s", new Object[]{message}));
                }
            }
        });
    }

    protected void openSession() {
        this.queue.add(new OpenSessionCommand(this));
    }

    protected void closeSession() {
        this.queue.add(new CloseSessionCommand(this));
    }

    public void setWorkerListener(WorkerListener listener) {
        this.workerListener = listener;
    }

    public String getDeviceName() {
        return this.deviceInfo != null ? this.deviceInfo.model : "";
    }

    public boolean isSessionOpen() {
        return this.state == State.Active;
    }

    public int getProperty(int property) {
        if (this.properties.containsKey(Integer.valueOf(property))) {
            return ((Integer) this.properties.get(Integer.valueOf(property))).intValue();
        }
        return Integer.MAX_VALUE;
    }

    public boolean getPropertyEnabledState(int property) {
        return false;
    }

    public int[] getPropertyDesc(int property) {
        if (this.propertyDescriptions.containsKey(Integer.valueOf(property))) {
            return (int[]) this.propertyDescriptions.get(Integer.valueOf(property));
        }
        return new int[0];
    }

    public void setProperty(int property, int value) {
        Integer ptpProperty = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(property));
        if (ptpProperty != null && this.ptpPropertyDesc.containsKey(ptpProperty)) {
            this.queue.add(new SetDevicePropValueCommand(this, ptpProperty.intValue(), value, ((DevicePropDesc) this.ptpPropertyDesc.get(ptpProperty)).datatype));
        }
    }

    public String propertyToString(int property, int value) {
        Integer ptpProperty = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(property));
        if (ptpProperty == null) {
            return "";
        }
        String text = PtpPropertyHelper.mapToString(this.productId, ptpProperty.intValue(), value);
        if (text != null) {
            return text;
        }
        return "?";
    }

    public Integer propertyToIcon(int property, int value) {
        if (((Integer) this.virtualToPtpProperty.get(Integer.valueOf(property))) == null || null != null) {
        }
        return null;
    }

    public String getBiggestPropertyValue(int property) {
        Integer ptpProperty = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(property));
        if (ptpProperty != null) {
            return PtpPropertyHelper.getBiggestValue(ptpProperty.intValue());
        }
        return "";
    }

    public void capture() {
        this.queue.add(new InitiateCaptureCommand(this));
    }

    public boolean isAutoFocusSupported() {
        return this.autoFocusSupported;
    }

    public boolean isLiveViewSupported() {
        return this.liveViewSupported;
    }

    public boolean isLiveViewAfAreaSupported() {
        return this.liveViewAfAreaSupported;
    }

    public boolean isHistogramSupported() {
        return this.histogramSupported;
    }

    public boolean isLiveViewOpen() {
        return this.liveViewOpen;
    }

    public boolean isDriveLensSupported() {
        return this.driveLensSupported;
    }

    public String getDeviceInfo() {
        return this.deviceInfo != null ? this.deviceInfo.toString() : EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public void writeDebugInfo(File out) {
        try {
            FileWriter writer = new FileWriter(out);
            writer.append(this.deviceInfo.toString());
            writer.close();
        } catch (IOException e) {
        }
    }

    public void retrievePicture(int objectHandle) {
        this.queue.add(new RetrievePictureAction(this, objectHandle, this.pictureSampleSize));
    }

    public void retrieveStorages(StorageInfoListener listener) {
        Log.d("CameraEvent", "retrieveStorages");
        setApplicationModeForD850(0);
        this.queue.add(new GetStorageInfosAction(this, listener));
        setApplicationModeForD850(1);
    }

    public void retrieveImageHandles(StorageInfoListener listener, int storageId, int objectFormat, int isAllowRaw) {
        Log.d("CameraEvent", "retrieveImageHandles: ");
        if (getVendorId().intValue() == PtpConstants.CanonVendorId || (getVendorId().intValue() == PtpConstants.NikonVendorId && getProductId() == Product.NikonD700)) {
            if (isAllowRaw == 1) {
                if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, ObjectFormat.EosCR2));
                    return;
                } else if (getVendorId().intValue() == PtpConstants.NikonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, 12288));
                    return;
                }
            } else if (isAllowRaw == 2) {
                if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, ObjectFormat.EosMOV));
                    return;
                } else if (getVendorId().intValue() == PtpConstants.NikonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, ObjectFormat.NiKonMOV));
                    return;
                }
            }
            this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, objectFormat));
            return;
        }
        setApplicationModeForD850(0);
        if (isAllowRaw == 1) {
            this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, 12288));
        } else if (isAllowRaw == 2) {
            this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, ObjectFormat.NiKonMOV));
        } else {
            this.queue.add(new GetObjectHandlesCommand(this, listener, storageId, objectFormat));
        }
        setApplicationModeForD850(1);
    }

    private void setApplicationModeForD850(int value) {
        Log.d("CameraEvent", "setApplicationModeForD850+++" + value);
        if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
            return;
        }
        if (getVendorId().intValue() != PtpConstants.NikonVendorId || getProductId() != Product.NikonD700) {
            this.queue.add(new SetDevicePropValueCommand(this, NikonApplicationMode, value, 2));
        }
    }

    public void retrieveImageInfo(RetrieveImageInfoListener listener, int objectHandle, boolean isObjectAdd) {
        this.queue.add(new RetrieveImageInfoAction(this, listener, objectHandle, isObjectAdd));
    }

    public synchronized void retrieveImage(RetrieveImageListener listener, int objectHandle, int sampleSize, int qualty, String actId, String sepId, UploadPhotoInfoBeen uploadPhotoInfoBeen) {
        this.queue.add(new RetrieveImageAction(this, listener, objectHandle, sampleSize, qualty, actId, sepId, uploadPhotoInfoBeen));
    }

    public synchronized void retrieveImageForLookBigPhoto(RetrieveImageListener listener, int objectHandle, int sampleSize, int qualty) {
        this.queue.add(new RetrieveImageAction(this, listener, objectHandle, sampleSize, qualty, null, null, null));
    }

    public void setCapturedPictureSampleSize(int sampleSize) {
        this.pictureSampleSize = sampleSize;
    }
}
