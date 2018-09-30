package cc.yelinvan.photographhome.rycusboss.ptp;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbRequest;
import android.util.Log;

public class PtpUsbConnection {
    private final UsbEndpoint bulkIn;
    private final UsbEndpoint bulkOut;
    private final UsbDeviceConnection connection;
    private final int productId;
    private final UsbDevice usbDevice;
    private final int vendorId;

    public PtpUsbConnection(UsbDeviceConnection connection, UsbEndpoint bulkIn, UsbEndpoint bulkOut, int vendorId, int productId, UsbDevice usbDevice) {
        this.connection = connection;
        this.bulkIn = bulkIn;
        this.bulkOut = bulkOut;
        this.vendorId = vendorId;
        this.productId = productId;
        this.usbDevice = usbDevice;
    }

    public int getVendorId() {
        return this.vendorId;
    }

    public int getProductId() {
        return this.productId;
    }

    public void close() {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public UsbDeviceConnection getConnection() {
        return this.connection;
    }

    public UsbDevice getUsbDevice() {
        return this.usbDevice;
    }

    public int getMaxPacketInSize() {
        return this.bulkIn.getMaxPacketSize();
    }

    public int getMaxPacketOutSize() {
        return this.bulkOut.getMaxPacketSize();
    }

    public UsbRequest createInRequest() {
        UsbRequest r = new UsbRequest();
        if (this.connection == null) {
            return null;
        }
        r.initialize(this.connection, this.bulkIn);
        return r;
    }

    public int bulkTransferOut(byte[] buffer, int length, int timeout) {
        return this.connection.bulkTransfer(this.bulkOut, buffer, length, timeout);
    }

    public int bulkTransferIn(byte[] buffer, int maxLength, int timeout) {
        int returnCode = this.connection.bulkTransfer(this.bulkIn, buffer, maxLength, timeout);
        if (returnCode == -1) {
            Log.i("&&&&&&&&", "" + returnCode);
        }
        return returnCode;
    }

    public void requestWait() {
        this.connection.requestWait();
    }
}
