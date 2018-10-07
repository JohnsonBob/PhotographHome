package cc.yelinvan.photographhome.rycusboss.ptp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;

import com.blankj.utilcode.util.ToastUtils;

import java.util.Map.Entry;

import cc.yelinvan.photographhome.rycusboss.ptp.Camera.CameraListener;

    public class PtpUsbService implements PtpService {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final String TAG = PtpUsbService.class.getSimpleName();
    private PtpCamera camera;
    private final Handler handler = new Handler();
    private CameraListener listener;
    private final BroadcastReceiver permissonReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (PtpUsbService.ACTION_USB_PERMISSION.equals(intent.getAction())) {
                PtpUsbService.this.unregisterPermissionReceiver(context);
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    //UsbDevice device =null;
                    if (intent.getBooleanExtra("permission", false)) {
                        PtpUsbService.this.connect(context, device);
                    } else {
                        PtpUsbService.this.listener.onPermissonAuth(1);
                    }
                }
            }
        }
    };
    Runnable shutdownRunnable = new Runnable() {
        public void run() {
            PtpUsbService.this.shutdown();
        }
    };
    private final UsbManager usbManager;

    public PtpUsbService(Context context) {
        this.usbManager = (UsbManager) context.getSystemService("usb");
    }

    public void setCameraListener(CameraListener listener) {
        this.listener = listener;
        if (this.camera != null) {
            this.camera.setListener(listener);
        }
    }

    public void initialize(Context context, Intent intent) {
        this.handler.removeCallbacks(this.shutdownRunnable);
        if (this.camera != null) {
            if (this.camera.getState() != PtpCamera.State.Active) {
                this.camera.shutdownHard();
            } else if (this.listener != null) {
                this.listener.onCameraStarted(this.camera);
                return;
            } else {
                return;
            }
        }
        UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
        //UsbDevice device = null;
        if (device != null) {
            connect(context, device);
            return;
        }
        device = lookupCompatibleDevice(this.usbManager);
        if (device != null) {
            this.listener.onPermissonAuth(0);
            registerPermissionReceiver(context);
            connect(context, device);
            this.usbManager.requestPermission(device, PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0));
            return;
        }
        this.listener.onNoCameraFound();
    }

    public void shutdown() {
        if (this.camera != null) {
            this.camera.shutdown();
            this.camera = null;
        }
    }

    public void lazyShutdown() {
        this.handler.postDelayed(this.shutdownRunnable, 4000);
    }

    private void registerPermissionReceiver(Context context) {
        context.registerReceiver(this.permissonReceiver, new IntentFilter(ACTION_USB_PERMISSION));
    }

    private void unregisterPermissionReceiver(Context context) {
        context.unregisterReceiver(this.permissonReceiver);
    }

    private UsbDevice lookupCompatibleDevice(UsbManager manager) {
        for (Entry<String, UsbDevice> e : manager.getDeviceList().entrySet()) {
            UsbDevice d = (UsbDevice) e.getValue();
            if (d.getVendorId() == PtpConstants.CanonVendorId || d.getVendorId() == PtpConstants.NikonVendorId) {
                return d;
            }
            if (d.getVendorId() == PtpConstants.SONYVendorId) {
                return d;
            }
        }
        return null;
    }

    private boolean connect(Context context, UsbDevice device) {
        if (this.camera != null) {
            this.camera.shutdown();
            this.camera = null;
        }
        int n = device.getInterfaceCount();
        for (int i = 0; i < n; i++) {
            UsbInterface intf = device.getInterface(i);
            if (intf.getEndpointCount() == 3) {
                UsbEndpoint in = null;
                UsbEndpoint out = null;
                int en = intf.getEndpointCount();
                for (int e = 0; e < en; e++) {
                    UsbEndpoint endpoint = intf.getEndpoint(e);
                    if (endpoint.getType() == 2) {
                        if (endpoint.getDirection() == 128) {
                            in = endpoint;
                        } else if (endpoint.getDirection() == 0) {
                            out = endpoint;
                        }
                    }
                }
                if (!(in == null || out == null)) {
                    if (device.getVendorId() == PtpConstants.CanonVendorId) {
                        this.camera = new EosCamera(new PtpUsbConnection(
                                this.usbManager.openDevice(device), in, out, device.getVendorId(), device.getProductId(), device),
                                this.listener, new WorkerNotifier(context));
                    } else if (device.getVendorId() == PtpConstants.NikonVendorId) {
                        this.camera = new NikonCamera(new PtpUsbConnection(this.usbManager.openDevice(device), in, out, device.getVendorId(), device.getProductId(), device), this.listener, new WorkerNotifier(context));
                    } else if (device.getVendorId() == PtpConstants.SONYVendorId) {
                        if (this.listener != null) {
                            this.listener.onSony(device, this.usbManager.openDevice(device));
                        }
                    } else if (this.listener != null) {
                        ToastUtils.showShort((CharSequence) "相机可能并不适配，正在以兼容模式打开");
                        this.listener.onSony(device, this.usbManager.openDevice(device));
                    }
                    return true;
                }
            }
        }
        if (this.listener != null) {
            this.listener.onError("No compatible camera found");
        }
        return false;
    }
}
