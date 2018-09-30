package cc.yelinvan.photographhome.rycusboss.ptp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class PtpConstants {
    public static final int CanonVendorId = 1193;
    public static final int NikonVendorId = 1200;
    public static final int SONYVendorId = 1356;

    public static class Datatype {
        public static final int aint128 = 16393;
        public static final int aint16 = 16387;
        public static final int aint32 = 16389;
        public static final int aint64 = 16391;
        public static final int aint8 = 16385;
        public static final int auInt16 = 16388;
        public static final int auint128 = 16394;
        public static final int auint32 = 16390;
        public static final int auint64 = 16392;
        public static final int auint8 = 16386;
        public static final int int128 = 9;
        public static final int int16 = 3;
        public static final int int32 = 5;
        public static final int int64 = 7;
        public static final int int8 = 1;
        public static final int string = 0;
        public static final int uint128 = 10;
        public static final int uint16 = 4;
        public static final int uint32 = 6;
        public static final int uint64 = 8;
        public static final int uint8 = 2;
    }

    public static class Event {
        public static final int CancelTransaction = 16385;
        public static final int CaptureComplete = 16397;
        public static final int DeviceInfoChanged = 16392;
        public static final int DevicePropChanged = 16390;
        public static final int EosBulbExposureTime = 49556;
        public static final int EosCameraStatus = 49547;
        public static final int EosDevicePropChanged = 49545;
        public static final int EosDevicePropDescChanged = 49546;
        public static final int EosObjectAdded = 49537;
        public static final int EosObjectAddedForUsb3 = 49575;
        public static final int EosObjectRatingChanged = 49544;
        public static final int EosWillSoonShutdown = 49549;
        public static final int NikonCaptureCompleteRecInSdram = 49410;
        public static final int NikonObjectAddedInSdram = 49409;
        public static final int NikonPreviewImageAdded = 49412;
        public static final int ObjectAdded = 16386;
        public static final int ObjectInfoChanged = 16391;
        public static final int ObjectRemoved = 16387;
        public static final int RequestObjectTransfer = 16393;
        public static final int StorageInfoChanged = 16396;
        public static final int StoreAdded = 16388;
        public static final int StoreFull = 16394;
        public static final int StoreRemoved = 16389;
    }

    public static class ObjectFormat {
        public static final int AIFF = 12295;
        public static final int ASF = 12300;
        public static final int AVI = 12298;
        public static final int Association = 12289;
        public static final int BMP = 14340;
        public static final int CIFF = 14341;
        public static final int DPOF = 12294;
        public static final int EXIF_JPEG = 14337;
        public static final int EosCR2 = -20221;
        public static final int EosCRW = 45313;
        public static final int EosCRW3 = 45315;
        public static final int EosMOV = -20219;
        public static final int Executable = 12291;
        public static final int FlashPix = 14339;
        public static final int GIF = 14343;
        public static final int HTML = 12293;
        public static final int JFIF = 14344;
        public static final int JP2 = 14351;
        public static final int JPX = 14352;
        public static final int MP3 = 12297;
        public static final int MPEG = 12299;
        public static final int NiKonMOV = 12301;
        public static final int NiKonNEF = 12288;
        public static final int PCD = 14345;
        public static final int PICT = 14346;
        public static final int PNG = 14347;
        public static final int Script = 12290;
        public static final int SonyARW = 45313;
        public static final int SonyMP4 = 47490;
        public static final int TIFF = 14349;
        public static final int TIFF_EP = 14338;
        public static final int TIFF_IT = 14350;
        public static final int Text = 12292;
        public static final int Undefined_Reserved1 = 14342;
        public static final int Undefined_Reserved2 = 14348;
        public static final int UnknownImageObject = 14336;
        public static final int UnknownNonImageObject = 12288;
        public static final int WAV = 12296;
    }

    public static class Operation {
        public static final int CloseSession = 4099;
        public static final int CopyObject = 4122;
        public static final int DeleteObject = 4107;
        public static final int EosBulbEnd = 37158;
        public static final int EosBulbStart = 37157;
        public static final int EosDriveLens = 37205;
        public static final int EosEventCheck = 37142;
        public static final int EosGetDevicePropValue = 37159;
        public static final int EosGetLiveViewPicture = 37203;
        public static final int EosGetObjectExif = 37127;
        public static final int EosRemoteReleaseOff = 37161;
        public static final int EosRemoteReleaseOn = 37160;
        public static final int EosResetTransfer = 37145;
        public static final int EosSetDevicePropValue = 37136;
        public static final int EosSetEventMode = 37141;
        public static final int EosSetPCConnectMode = 37140;
        public static final int EosTakePicture = 37135;
        public static final int EosTransferComplete = 37143;
        public static final int FormatStore = 4111;
        public static final int GetDeviceInfo = 4097;
        public static final int GetDevicePropDesc = 4116;
        public static final int GetDevicePropValue = 4117;
        public static final int GetNumObjects = 4102;
        public static final int GetObject = 4105;
        public static final int GetObjectHandles = 4103;
        public static final int GetObjectInfo = 4104;
        public static final int GetPartialObject = 4123;
        public static final int GetStorageIDs = 4100;
        public static final int GetStorageInfo = 4101;
        public static final int GetThumb = 4106;
        public static final int InitiateCapture = 4110;
        public static final int InitiateOpenCapture = 4124;
        public static final int MoveObject = 4121;
        public static final int NikonAfAndCaptureInSdram = 37067;
        public static final int NikonAfDrive = 37057;
        public static final int NikonAfDriveCancel = 37382;
        public static final int NikonChangeAfArea = 37381;
        public static final int NikonChangeCameraMode = 37058;
        public static final int NikonDeleteCustomPicCtrl = 37070;
        public static final int NikonDeleteImagesInSdram = 37059;
        public static final int NikonDeviceReady = 37064;
        public static final int NikonEndLiveView = 37378;
        public static final int NikonGetEvent = 37063;
        public static final int NikonGetLargeThumb = 37060;
        public static final int NikonGetLiveViewImage = 37379;
        public static final int NikonGetObjectExif = 4123;
        public static final int NikonGetObjectPropDesc = 38914;
        public static final int NikonGetObjectPropList = 38917;
        public static final int NikonGetObjectPropValue = 38915;
        public static final int NikonGetObjectPropsSupported = 38913;
        public static final int NikonGetPicCtrlCapability = 37071;
        public static final int NikonGetPicCtrlData = 37068;
        public static final int NikonGetPreviewImage = 37376;
        public static final int NikonGetVendorPropCodes = 37066;
        public static final int NikonGetVendorStorageIDs = 37385;
        public static final int NikonInitiateCaptureRecInMedia = 37383;
        public static final int NikonInitiateCaptureRecInSdram = 37056;
        public static final int NikonMfDrive = 37380;
        public static final int NikonSetPicCtrlData = 37069;
        public static final int NikonSetPreWbData = 37065;
        public static final int NikonStartLiveView = 37377;
        public static final int OpenSession = 4098;
        public static final int PowerDown = 4115;
        public static final int ResetDevice = 4112;
        public static final int ResetDevicePropValue = 4119;
        public static final int SelfTest = 4113;
        public static final int SendObject = 4109;
        public static final int SendObjectInfo = 4108;
        public static final int SetDevicePropValue = 4118;
        public static final int SetObjectProtection = 4114;
        public static final int TerminateOpenCapture = 4120;
        public static final int UndefinedOperationCode = 4096;
    }

    public static class Product {
        public static final int NikonD200 = 1040;
        public static final int NikonD3 = 1052;
        public static final int NikonD300 = 1050;
        public static final int NikonD300S = 1061;
        public static final int NikonD3S = 1062;
        public static final int NikonD3X = 1056;
        public static final int NikonD40 = 1044;
        public static final int NikonD5 = 1082;
        public static final int NikonD5000 = 1059;
        public static final int NikonD5100 = 1065;
        public static final int NikonD700 = 1058;
        public static final int NikonD7000 = 1064;
        public static final int NikonD80 = 1042;
        public static final int NikonD90 = 1057;
    }

    public static class Property {
        public static final int Artist = 20510;
        public static final int BatteryLevel = 20481;
        public static final int BurstInterval = 20505;
        public static final int BurstNumber = 20504;
        public static final int CaptureDelay = 20498;
        public static final int CompressionSetting = 20484;
        public static final int Contrast = 20500;
        public static final int CopyrightInfo = 20511;
        public static final int DateTime = 20497;
        public static final int DigitalZoom = 20502;
        public static final int EffectMode = 20503;
        public static final int EosAfMode = 53512;
        public static final int EosApertureValue = 53505;
        public static final int EosAvailableShots = 53531;
        public static final int EosColorTemperature = 53514;
        public static final int EosDriveMode = 53510;
        public static final int EosEvfColorTemperature = 53686;
        public static final int EosEvfMode = 53683;
        public static final int EosEvfOutputDevice = 53680;
        public static final int EosEvfWhitebalance = 53684;
        public static final int EosExposureCompensation = 53508;
        public static final int EosIsoSpeed = 53507;
        public static final int EosMeteringMode = 53511;
        public static final int EosPictureStyle = 53520;
        public static final int EosShootingMode = 53509;
        public static final int EosShutterSpeed = 53506;
        public static final int EosWhitebalance = 53513;
        public static final int ExposureBiasCompensation = 20496;
        public static final int ExposureIndex = 20495;
        public static final int ExposureMeteringMode = 20491;
        public static final int ExposureProgramMode = 20494;
        public static final int ExposureTime = 20493;
        public static final int FNumber = 20487;
        public static final int FlashMode = 20492;
        public static final int FocalLength = 20488;
        public static final int FocusDistance = 20489;
        public static final int FocusMeteringMode = 20508;
        public static final int FocusMode = 20490;
        public static final int FunctionalMode = 20482;
        public static final int ImageSize = 20483;
        public static final int MtpDeviceFriendlyName = 54274;
        public static final int MtpPerceivedDeviceType = 54279;
        public static final int MtpSessionInitiatorInfo = 54278;
        public static final int NikonActivePicCtrlItem = 53760;
        public static final int NikonApplicationMode = 53744;
        public static final int NikonEnableAfAreaPoint = 53389;
        public static final int NikonExposureIndicateStatus = 53681;
        public static final int NikonFocusArea = 53512;
        public static final int NikonRecordingMedia = 53515;
        public static final int NikonShutterSpeed = 53504;
        public static final int NikonWbColorTemp = 53278;
        public static final int RGBGain = 20486;
        public static final int Sharpness = 20501;
        public static final int StillCaptureMode = 20499;
        public static final int TimelapseInterval = 20507;
        public static final int TimelapseNumber = 20506;
        public static final int UndefinedProperty = 20480;
        public static final int UploadURL = 20509;
        public static final int WhiteBalance = 20485;
    }

    public static class Response {
        public static final int AccessDenied = 8207;
        public static final int CameraModeNotAdjustFnumber = 40970;
        public static final int ChangeCameraModeFailed = 40963;
        public static final int DeviceBusy = 8217;
        public static final int DevicePropNotSupported = 8202;
        public static final int DustReferenceError = 40967;
        public static final int EosUnknown_MirrorUp = 41218;
        public static final int GeneralError = 8194;
        public static final int HardwareError = 40961;
        public static final int IncompleteTransfer = 8199;
        public static final int InvalidDevicePropFormat = 8219;
        public static final int InvalidDevicePropValue = 8220;
        public static final int InvalidObjectFormatCode = 8203;
        public static final int InvalidObjectHandle = 8201;
        public static final int InvalidObjectPropCode = 43009;
        public static final int InvalidObjectPropFormat = 43010;
        public static final int InvalidParameter = 8221;
        public static final int InvalidParentObject = 8218;
        public static final int InvalidStatus = 40964;
        public static final int InvalidStorageID = 8200;
        public static final int InvalidTransactionID = 8196;
        public static final int MfDriveStepEnd = 40972;
        public static final int MfDriveStepInsufficiency = 40974;
        public static final int MirrorUpSequence = 40969;
        public static final int NoThumbnailPresent = 8208;
        public static final int NoValidObjectInfo = 8213;
        public static final int NotLiveView = 40971;
        public static final int ObjectPropNotSupported = 43018;
        public static final int ObjectWriteProtect = 8205;
        public static final int Ok = 8193;
        public static final int OperationNotSupported = 8197;
        public static final int OutOfFocus = 40962;
        public static final int ParameterNotSupported = 8198;
        public static final int PartialDeletion = 8210;
        public static final int SessionAlreadyOpen = 8222;
        public static final int SessionNotOpen = 8195;
        public static final int SetPropertyNotSupport = 40965;
        public static final int ShutterSpeedBulb = 40968;
        public static final int SpecificationByFormatUnsupported = 8212;
        public static final int SpecificationOfDestinationUnsupported = 8224;
        public static final int StoreIsFull = 8204;
        public static final int StoreNotAvailable = 8211;
        public static final int StoreReadOnly = 8206;
        public static final int TransferCancelled = 8223;
        public static final int WbPresetError = 40966;
    }

    public static class Type {
        public static final int Command = 1;
        public static final int Data = 2;
        public static final int Event = 4;
        public static final int Response = 3;
        public static final int Undefined = 0;
    }

    public static boolean isCompatibleVendor(int vendorId) {
        return vendorId == CanonVendorId || vendorId == NikonVendorId;
    }

    public static String typeToString(int type) {
        return constantToString(Type.class, type);
    }

    public static String operationToString(int operation) {
        return constantToString(Operation.class, operation);
    }

    public static String eventToString(int event) {
        return constantToString(Event.class, event);
    }

    public static String responseToString(int response) {
        return constantToString(Response.class, response);
    }

    public static String objectFormatToString(int objectFormat) {
        return constantToString(ObjectFormat.class, objectFormat);
    }

    public static String propertyToString(int property) {
        return constantToString(Property.class, property);
    }

    public static String datatypetoString(int datatype) {
        return constantToString(Datatype.class, datatype);
    }

    public static int getDatatypeSize(int datatype) {
        switch (datatype) {
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
                return 2;
            case 5:
            case 6:
                return 4;
            case 7:
            case 8:
                return 8;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static String codeToString(int type, int code) {
        switch (type) {
            case 1:
            case 2:
                return operationToString(code);
            case 3:
                return responseToString(code);
            case 4:
                return eventToString(code);
            default:
                return String.format("0x%04x", new Object[]{Integer.valueOf(code)});
        }
    }

    public static String constantToString(Class<?> clazz, int constant) {
        int i = 0;
        String hexString = String.format("0x%04x", new Object[]{Integer.valueOf(constant)});
        Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        while (i < length) {
            Field f = declaredFields[i];
            if (f.getType() == Integer.TYPE && Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {
                try {
                    if (f.getInt(null) == constant) {
                        return f.getName() + "(" + hexString + ")";
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return hexString;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        int state = 0;
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                if ("OperationsSupported:".equals(line)) {
                    state = 1;
                    writer.write(line);
                } else if ("EventsSupported:".equals(line)) {
                    state = 2;
                    writer.write(line);
                } else if ("DevicePropertiesSupported:".equals(line)) {
                    state = 3;
                    writer.write(line);
                } else if ("CaptureFormats:".equals(line)) {
                    state = 4;
                    writer.write(line);
                } else if ("ImageFormats:".equals(line)) {
                    state = 5;
                    writer.write(line);
                } else if (line.startsWith("    0x") || line.matches("    .+\\)$")) {
                    if (line.startsWith("    0x")) {
                        line = line.trim().substring(2);
                    } else {
                        line = line.substring(line.indexOf(40) + 3, line.length() - 1);
                    }
                    int number = Integer.parseInt(line, 16);
                    String value = null;
                    switch (state) {
                        case 1:
                            value = operationToString(number);
                            break;
                        case 2:
                            value = eventToString(number);
                            break;
                        case 3:
                            value = propertyToString(number);
                            break;
                        case 4:
                        case 5:
                            value = objectFormatToString(number);
                            break;
                    }
                    writer.write(String.format("    %s", new Object[]{value}));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            } else {
                writer.flush();
                return;
            }
        }
    }
}
