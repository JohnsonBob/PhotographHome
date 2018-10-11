package cc.yelinvan.photographhome.flash.camera;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.mtp.MtpDevice;
import android.mtp.MtpObjectInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.common.API;
import cc.yelinvan.photographhome.common.AlltuuFilePath;
import cc.yelinvan.photographhome.flash.event.BatteryEvent;
import cc.yelinvan.photographhome.flash.event.CameraStateEvent;
import cc.yelinvan.photographhome.flash.event.RatingEvent;
import cc.yelinvan.photographhome.flash.event.ReadBigPhotoEvent;
import cc.yelinvan.photographhome.flash.event.ReadThumbnailEvent;
import cc.yelinvan.photographhome.flash.event.ReadThumnailProgressEvent;
import cc.yelinvan.photographhome.flash.event.UploadFailedEvent;
import cc.yelinvan.photographhome.flash.event.UploadSpeedEvent;
import cc.yelinvan.photographhome.rycusboss.ptp.Camera;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.ObjectFormat;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpService;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpService.Singleton;
import cc.yelinvan.photographhome.uploadutils.AlbumFlashUploadState;
import cc.yelinvan.photographhome.utils.PhotographHomeUtils;

public class CameraService extends AlltuuCameraListener {
    static final int FAILEDSUM_CHANGED = 1;
    static final int READING_DATA = 2;
    static final int SPEED_CHANGEED = 0;
    public static String TAG = "CameraService";
    public static String actId;
    public static Double altitude = Double.valueOf(-1.0d);
    public static boolean cleanning = false;
    public static int currentPhotoChoosed;
    public static int currentStorageId;
    public static String currentStorageLabel;
    public static int flashState = 0;
    public static boolean freeze = false;
    public static Map<Integer, String> handleCaptureDateMap = new HashMap();
    public static Map<Integer, String> handleFilePathMap = new HashMap();
    private static int handleNum = 0;
    public static Map<String, Integer> handleProgressMap = new HashMap();
    private static int handlesSum = 0;
    public static int isAllowRaw = 0;
    public static boolean isGivingPhoto = false;
    public static int isNewOssPath;
    public static boolean isReadingThumbnail = false;
    public static boolean isSony = false;
    public static Double latitude = Double.valueOf(-1.0d);
    public static List<Integer> localHandleList = new CopyOnWriteArrayList();
    public static Double longitude = Double.valueOf(-1.0d);
    public static int photoSum = 0;
    public static int quality = 95;
    public static boolean readCamera = true;
    public static boolean readFailePhoto = false;
    public static String sepId;
    public static List<Integer> storageIdList = new ArrayList();
    public static List<Map<String, String>> storageLabelList = new ArrayList();
    public static List<File> unUploadList = new CopyOnWriteArrayList();
    public static int uploadMode = 1;
    public static int usbState = 0;
    public static int vendorId;

    AlertDialog alertDialog;
    int balance = 0;
    private final BroadcastReceiver batteryBroadReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            EventBus.getDefault().post(new BatteryEvent("电量：" + intent.getExtras().getInt("level") + "%"));
        }
    };
    private Camera camera;
    Context context;
    int deductedMoney = 0;
    AlertDialog errorDialog;
    Map<Integer, String> fileNameMap = new HashMap();
    boolean inPhotoListFlag = false;
    private Intent intent;
    private boolean isConnected;
    private MtpDevice mtpDevice;
    SharedPreferences mySharedPrefences;
    private PtpService ptp;
    long totalUploadData = 0;
    String uploadSpeed;
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                Log.d(CameraService.TAG, "相机插入");
                CameraService.usbState = 1;
                if (!CameraService.isGivingPhoto) {
                    if (CameraService.this.alertDialog != null && CameraService.this.alertDialog.isShowing()) {
                        CameraService.this.alertDialog.cancel();
                        ToastUtils.showShort((CharSequence) "闪传连接已恢复");
                    }
                    CameraService.this.connectCamera();
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                CameraService.this.camera = null;
                if (CameraService.isSony) {
                    CameraService.this.onCameraStopped(null);
                    if (CameraService.this.mtpDevice != null) {
                        CameraService.this.mtpDevice.close();
                    }
                }
                CameraService.isSony = false;
                Log.d(CameraService.TAG, "相机拔出");
                CameraService.usbState = 0;
                CameraService.this.isConnected = false;
                if (!CameraService.isGivingPhoto) {
                    CameraService.this.disconnectCameraDialog();
                }
            }
        }
    };
    int usedCent = 0;





    public void onCreate() {
        super.onCreate();
        this.context = this;
        Log.d(TAG, "onCreate");

        String CHANNEL_ONE_ID = "cc.yelinvan.photographhome";
        String CHANNEL_ONE_NAME = "cc.yelinvan.photographhome";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1017, new Notification.Builder(getApplicationContext())
                    .setChannelId(CHANNEL_ONE_ID)
                    .setContentText("摄影专家正在运行")
                    .setSmallIcon(R.mipmap.push_alltuu)
                    .build());
        }else {
            startForeground(1017, new Notification.Builder(getApplicationContext())
                    .setContentText("摄影专家正在运行")
                    .setSmallIcon(R.mipmap.push_alltuu)
                    .build());
        }

        this.mySharedPrefences = getSharedPreferences(API.SP_NORMAL, 0);


        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        registerReceiver(this.usbReceiver, filter);
        registerReceiver(this.batteryBroadReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.ptp = Singleton.getInstance(this.context);
        this.ptp.setCameraListener(this);
    }








    public static void notifyUploadFailedChanged(int sum) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("failedSum", sum);
        message.setData(bundle);
        message.what = 1;
        EventBus.getDefault().post(new UploadFailedEvent(sum));
    }

    public void notifyUploadSpeedChanged(String speed) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("speed", speed);
        message.setData(bundle);
        message.what = 0;
        EventBus.getDefault().post(new UploadSpeedEvent(speed));
    }



    private void retrieveImageForSony(int handle, int quality, String actId, String sepId) {
        if (this.mtpDevice == null) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        }
        MtpObjectInfo mtpObjectInfo = this.mtpDevice.getObjectInfo(handle);
        byte[] b = this.mtpDevice.getObject(handle, mtpObjectInfo.getCompressedSize());
        String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuu/" + mtpObjectInfo.getName();
        FileUtils.createOrExistsFile(filePath);
        PhotographHomeUtils.writeToFile(b, new File(filePath));
    }

    private void disconnectCameraDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("已与相机断开连接");
        if ("ALLTUU_S1".equals(DeviceUtils.getModel())) {
            builder.setMessage(R.string.flash_upload_list_text3);
        } else {
            builder.setMessage(R.string.flash_upload_list_text2);
        }
        builder.setNegativeButton("我知道了", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.getWindow().setType(2003);
        this.alertDialog.show();
    }


    private void showBaseDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setNegativeButton("我知道了", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.errorDialog = builder.create();
        this.errorDialog.getWindow().setType(2003);
        this.errorDialog.show();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");



        this.intent = intent;
        connectCamera();
        return super.onStartCommand(intent, flags, startId);
    }

    public void setInPhotoListFlag(boolean inPhotoListFlag) {
        if (!this.inPhotoListFlag && inPhotoListFlag) {
            readCameraThumnail();
        }
        this.inPhotoListFlag = inPhotoListFlag;
    }

    private void readOtherStorageThumnail() {
        if (this.camera != null || isSony) {
            EventBus.getDefault().post(new ReadThumbnailEvent(102, null));
            new Thread() {
                public void run() {
                    CameraService.this.readCameraStorageHandles();
                }
            }.start();
            return;
        }
        EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
    }

    public void readCameraThumnail() {
        Log.d(TAG, "readCameraThumnail");
        storageIdList.clear();
        storageLabelList.clear();
        if (this.camera != null) {
            Log.d(TAG, "readCameraThumnail1");
            isReadingThumbnail = true;
            this.camera.retrieveStorages(this);
            EventBus.getDefault().post(new ReadThumbnailEvent(102, null));
        } else if (isSony) {
            Log.d(TAG, "readCameraThumnail2");
            isReadingThumbnail = true;
            EventBus.getDefault().post(new ReadThumbnailEvent(102, null));
            new Thread() {
                public void run() {
                    CameraService.this.retrieveStoragesForSony();
                }
            }.start();
        } else {
            Log.d(TAG, "readCameraThumnail3");
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        }
    }

    private void retrieveStoragesForSony() {
        if (this.mtpDevice == null) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        }
        try {
            int[] ids = this.mtpDevice.getStorageIds();
            for (int i = 0; i < ids.length; i++) {
                onStorageFound(this.mtpDevice.getStorageInfo(ids[i]).getStorageId(), "卡" + (i + 1));
            }
        } catch (NullPointerException e) {
            ToastUtils.showShort((CharSequence) "读取存储卡失败，如是索尼相机，请确认USB设置是否为 MTP ");
        }
        onAllStoragesFound();
    }


    private void connectCamera() {
        Log.d(TAG, "connectCamera");
        if (!this.isConnected && this.intent != null) {
            this.ptp.initialize(this.context, this.intent);
        }
    }

    public void onCameraStarted(Camera camera) {
        Log.d(TAG, "onCameraStarted");
        this.isConnected = true;
        EventBus.getDefault().post(new CameraStateEvent(1));
        if (camera != null) {
            flashState = 1;
            this.camera = camera;
            vendorId = camera.getVendorId().intValue();
            if (this.inPhotoListFlag) {
                readCameraThumnail();
            }
        } else if (isSony) {
            flashState = 1;
            vendorId = PtpConstants.SONYVendorId;
            if (this.inPhotoListFlag) {
                readCameraThumnail();
            }
        } else {
            flashState = 2;
            showBaseDialog("检测到相机与设备连接出现问题，请您重新拔插数据线");
        }
    }

    public void onCameraStopped(Camera camera) {
        Log.d(TAG, "onCameraStopped");
        flashState = 0;
        this.isConnected = false;
        EventBus.getDefault().post(new CameraStateEvent(0));
        this.camera = null;
        EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        readCamera = true;
        readFailePhoto = false;
        Log.d(TAG, "camera已停止，检查百分之1观察队列");

        Log.d(TAG, "百分之1观察队列检测完毕");
    }

    public void onNoCameraFound() {
        Log.d(TAG, "onNoCameraFound");
        flashState = 3;
        this.isConnected = false;
        EventBus.getDefault().post(new CameraStateEvent(0));
        this.camera = null;
    }

    public void onError(String message) {
        Log.d(TAG, "onError->" + message);
        if (isReadingThumbnail) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_ERROR, null));
            ToastUtils.showLong("读取异常，请检查相机第" + handleNum + "张照片是否存在问题(被处理过后又放回了存储卡)");
        }
        flashState = 2;
        this.isConnected = false;
        EventBus.getDefault().post(new CameraStateEvent(0));
        this.camera = null;
    }

    public void onSony(UsbDevice usbDevice, UsbDeviceConnection connection) {
        this.camera = null;
        isSony = true;
        this.mtpDevice = new MtpDevice(usbDevice);
        if (this.mtpDevice.open(connection)) {
            onCameraStarted(null);
        } else {
            ToastUtils.showShort((CharSequence) "索尼相机mtp开启失败");
        }
    }

    public void onPermissonAuth(int state) {
        Log.d(TAG, "onPermissonAuth:" + state);
        if (state == 1) {
            Log.d(TAG, "用户拒绝了USB权限");
        }
    }

    public void onStorageFound(int handle, String label) {
        Log.d(TAG, "onStorageFound:handle->" + handle + "////label->" + label);
        storageIdList.add(Integer.valueOf(handle));
        Map<String, String> nameMap = new HashMap();
        nameMap.put("itemName", label);
        storageLabelList.add(nameMap);
    }

    public void onAllStoragesFound() {
        Log.d(TAG, "onAllStoragesFound");
        if (storageIdList.size() <= 0) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOSTORAGE, null));
            return;
        }
        currentStorageId = ((Integer) storageIdList.get(0)).intValue();
        currentStorageLabel = (String) ((Map) storageLabelList.get(0)).get("itemName");
        readCameraStorageHandles();
    }

    public void readCameraStorageHandles() {
        if (this.camera != null) {
            this.camera.retrieveImageHandles(this, currentStorageId, ObjectFormat.EXIF_JPEG, isAllowRaw);
        } else if (isSony) {
            retrieveImageHandlesForSony(currentStorageId, ObjectFormat.EXIF_JPEG, isAllowRaw);
        } else {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        }
    }

    private void retrieveImageHandlesForSony(int currentStorageId, int format, int isAllowRaw) {
        if (this.mtpDevice == null) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        }
        if (isAllowRaw == 0) {
            onImageHandlesRetrieved(this.mtpDevice.getObjectHandles(currentStorageId, format, 0));
        }
        if (isAllowRaw == 1) {
            onImageHandlesRetrieved(this.mtpDevice.getObjectHandles(currentStorageId, 45313, 0));
        }
        if (isAllowRaw == 2) {
            onImageHandlesRetrieved(this.mtpDevice.getObjectHandles(currentStorageId, ObjectFormat.SonyMP4, 0));
        }
    }

    public void onImageHandlesRetrieved(int[] handles) {
        Log.d(TAG, "onImageHandlesRetrieved" + handles.length);
        if (handles == null || handles.length <= 0) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NODATA, null));
            isReadingThumbnail = false;
            return;
        }
        handlesSum = handles.length;
        handleNum = 0;
        Message message = new Message();
        message.what = 2;
        for (int handle : handles) {
            if (localHandleList.contains(Integer.valueOf(handle))) {
                handleNum++;
                EventBus.getDefault().post(new ReadThumnailProgressEvent(handleNum, handlesSum));
                if (handleNum == handlesSum) {
                    EventBus.getDefault().post(new ReadThumbnailEvent(100, null));
                    isReadingThumbnail = false;
                    Message message2 = new Message();
                    message2.what = 2;
                }
            } else if (this.camera != null) {
                this.camera.retrieveImageInfo(this, handle, false);
            } else if (isSony) {
                retrieveImageInfoForSony(handle, false);
            } else {
                EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
                isReadingThumbnail = false;
                return;
            }
        }
    }

    private void retrieveImageInfoForSony(int handle, boolean isObjectAdd) {
        if (this.mtpDevice == null) {
            EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
        }
        MtpObjectInfo mtpObjectInfo = this.mtpDevice.getObjectInfo(handle);
        onImageInfoRetrieved(handle, TimeUtils.millis2String(mtpObjectInfo.getDateCreated()), mtpObjectInfo.getName(), mtpObjectInfo.getStorageId(), ImageUtils.bytes2Bitmap(this.mtpDevice.getThumbnail(handle)), isObjectAdd);
    }

    public void onImageInfoRetrieved(int objectHandle, String captureDate, String fileName, int storageId, Bitmap thumbnail, boolean isObjectAdd) {
        Log.d(TAG, "onImageInfoRetrieved->" + objectHandle);
        boolean isSuccess = false;
        if (!(thumbnail == null || captureDate == null || captureDate.equals("") || fileName == null || fileName.equals(""))) {
            String path = AlltuuFilePath.TEMP_FLASH_THUMBNAIL_PATH + storageId + "/" + isAllowRaw + "/";
            Boolean createdOne = Boolean.valueOf(new File(AlltuuFilePath.TEMP_FLASH_THUMBNAIL_PATH).mkdir());
            Boolean createdTwo = Boolean.valueOf(new File(AlltuuFilePath.TEMP_FLASH_THUMBNAIL_PATH + storageId + "/").mkdir());
            Boolean createdThree = Boolean.valueOf(new File(path).mkdir());
            File file = new File(path + objectHandle + "~~" + captureDate + "~~" + fileName);
            if (FileUtils.isFileExists(file) || PhotographHomeUtils.compressBmpToFile(thumbnail, file)) {
                isSuccess = true;
                localHandleList.add(Integer.valueOf(objectHandle));
                this.fileNameMap.put(Integer.valueOf(objectHandle), fileName);
                handleFilePathMap.put(Integer.valueOf(objectHandle), file.getAbsolutePath());
                if (isObjectAdd) {
                    EventBus.getDefault().post(new ReadThumbnailEvent(101, file));
                }
            } else {
                Log.d(TAG, "onImageInfoRetrieved:保存失败了");
            }
        }
        if (!isSuccess && isObjectAdd) {
            EventBus.getDefault().post(new ReadThumbnailEvent(101, null));
        }
        if (!isObjectAdd) {
            handleNum++;
        }
        EventBus.getDefault().post(new ReadThumnailProgressEvent(handleNum, handlesSum));
        if (handleNum == handlesSum && !isObjectAdd) {
            EventBus.getDefault().post(new ReadThumbnailEvent(100, null));
            isReadingThumbnail = false;
            Message message = new Message();
            message.what = 2;
        }
        if (thumbnail != null) {
            thumbnail.recycle();
            Log.d(TAG, "回收bitmap");
        }
    }

    public void onImageInfoRetrievedFailed(boolean isObjectAdd) {
        Log.d(TAG, "读取失败了");
        if (!isObjectAdd) {
            handleNum++;
        }
        if (handleNum == handlesSum && !isObjectAdd) {
            EventBus.getDefault().post(new ReadThumbnailEvent(100, null));
            isReadingThumbnail = false;
            Message message = new Message();
            message.what = 2;
        }
    }






    private void showConfirmDialog(long fileSize, String filePath, final int objectHandle, String actId, String sepId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage("此视频大小为" + ConvertUtils.byte2FitMemorySize(fileSize) + "，需要花费" + (((float) this.usedCent) / 100.0f) + "元，您当前余额为" + (((float) this.balance) / 100.0f) + "元，上传成功之后会自动扣费，确认上传吗？");
        builder.setNegativeButton("放弃上传", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(CameraService.TAG, "用户放弃上传");
            }
        });
        final int i = objectHandle;
        final String str = filePath;
        final String str2 = actId;
        final String str3 = sepId;
        builder.setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getRepeatCount() == 0) {
                }
                return false;
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.getWindow().setType(2003);
        this.alertDialog.show();
    }

    private void showRechargeMoneyDialog(long fileSize, String filePath, int objectHandle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage("此视频大小为" + ConvertUtils.byte2FitMemorySize(fileSize) + "，需要花费" + (((float) this.usedCent) / 100.0f) + "元，您当前余额为" + (((float) this.balance) / 100.0f) + "元，请充值后再重新上传视频");
        builder.setNegativeButton("好的", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getRepeatCount() == 0) {
                }
                return false;
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.getWindow().setType(2003);
        this.alertDialog.show();
    }

    public void onImageRetrievedForLookBigPhoto(int objectHandle, String filePath, int orientation, int qualty) {
        if (filePath == null) {
            EventBus.getDefault().post(new ReadBigPhotoEvent(0, null, 0));
            return;
        }
        FileUtils.deleteFile(AlltuuFilePath.FLASH_DOWNLAOD_PAHT + "bigPhoto");
        FileUtils.rename(filePath, "bigPhoto");
        EventBus.getDefault().post(new ReadBigPhotoEvent(objectHandle, filePath, orientation));
    }

    public void onObjectAdded(int handle, int format) {
        Log.d(TAG, "onObjectAdded");
        if (isAllowRaw == 1) {
            if (format != ObjectFormat.EosCR2 && format != 12288) {
                return;
            }
            if (this.camera == null) {
                EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
            } else {
                this.camera.retrieveImageInfo(this, handle, true);
            }
        } else if (isAllowRaw == 2) {
            if (format != ObjectFormat.EosMOV && format != ObjectFormat.NiKonMOV) {
                return;
            }
            if (this.camera == null) {
                EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
            } else {
                this.camera.retrieveImageInfo(this, handle, true);
            }
        } else if (format != ObjectFormat.EXIF_JPEG) {
        } else {
            if (this.camera == null) {
                EventBus.getDefault().post(new ReadThumbnailEvent(AlbumFlashUploadState.READ_CAMERA_NOCAMERA, null));
            } else {
                this.camera.retrieveImageInfo(this, handle, true);
            }
        }
    }

    public void onRatingChange(int handle) {
        Log.d(TAG, "onRatingChange");
        EventBus.getDefault().post(new RatingEvent(handle));
    }



    public void onDestroy() {
        super.onDestroy();
        this.ptp.shutdown();
    }
}
