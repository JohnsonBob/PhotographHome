package cc.yelinvan.photographhome.rycusboss.ptp;

import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Product;
import cc.yelinvan.photographhome.rycusboss.ptp.PtpConstants.Property;
import cc.yelinvan.photographhome.support.media.ExifInterface;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import com.adobe.xmp.XMPError;
import com.android.volley.DefaultRetryPolicy;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.HashMap;
import java.util.Map;

public class PtpPropertyHelper {
    public static final int EOS_SHUTTER_SPEED_BULB = 12;
    private static final Map<Integer, String> eosApertureValueMap = new HashMap();
    private static final Map<Integer, Integer> eosDriveModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosDriveModeMap = new HashMap();
    private static final Map<Integer, String> eosFocusModeMap = new HashMap();
    private static final Map<Integer, String> eosIsoSpeedMap = new HashMap();
    private static final Map<Integer, Integer> eosMeteringModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosPictureStyleMap = new HashMap();
    private static final Map<Integer, Integer> eosShootingModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosShootingModeMap = new HashMap();
    private static final Map<Integer, String> eosShutterSpeedMap = new HashMap();
    private static final Map<Integer, Integer> eosWhitebalanceIconsMap = new HashMap();
    private static final Map<Integer, String> eosWhitebalanceMap = new HashMap();
    private static final Map<Integer, String> nikonActivePicCtrlItemMap = new HashMap();
    private static final Map<Integer, String> nikonExposureIndexMap = new HashMap();
    private static final Map<Integer, Integer> nikonExposureProgramMap = new HashMap();
    private static final Map<Integer, Integer> nikonFocusMeteringModeIconsMap = new HashMap();
    private static final Map<Integer, String> nikonFocusMeteringModeMap = new HashMap();
    private static final Map<Integer, String> nikonFocusModeMap = new HashMap();
    private static final Map<Integer, Integer> nikonMeteringModeMap = new HashMap();
    private static final Map<Integer, String> nikonWbColorTempD200Map = new HashMap();
    private static final Map<Integer, String> nikonWbColorTempD300SMap = new HashMap();
    private static final Map<Integer, Integer> nikonWhitebalanceIconsMap = new HashMap();
    private static final Map<Integer, String> nikonWhitebalanceMap = new HashMap();

    static {
        eosShutterSpeedMap.put(Integer.valueOf(12), "Bulb");
        eosShutterSpeedMap.put(Integer.valueOf(16), "30\"");
        eosShutterSpeedMap.put(Integer.valueOf(19), "25\"");
        eosShutterSpeedMap.put(Integer.valueOf(20), "20\"");
        eosShutterSpeedMap.put(Integer.valueOf(21), "20\"");
        eosShutterSpeedMap.put(Integer.valueOf(24), "15\"");
        eosShutterSpeedMap.put(Integer.valueOf(27), "13\"");
        eosShutterSpeedMap.put(Integer.valueOf(28), "10\"");
        eosShutterSpeedMap.put(Integer.valueOf(29), "10\"");
        eosShutterSpeedMap.put(Integer.valueOf(32), "8\"");
        eosShutterSpeedMap.put(Integer.valueOf(35), "6\"");
        eosShutterSpeedMap.put(Integer.valueOf(36), "6\"");
        eosShutterSpeedMap.put(Integer.valueOf(37), "5\"");
        eosShutterSpeedMap.put(Integer.valueOf(40), "4\"");
        eosShutterSpeedMap.put(Integer.valueOf(43), "3\"2");
        eosShutterSpeedMap.put(Integer.valueOf(44), "3\"");
        eosShutterSpeedMap.put(Integer.valueOf(45), "2\"5");
        eosShutterSpeedMap.put(Integer.valueOf(37), "5\"");
        eosShutterSpeedMap.put(Integer.valueOf(40), "4\"");
        eosShutterSpeedMap.put(Integer.valueOf(43), "3\"2");
        eosShutterSpeedMap.put(Integer.valueOf(44), "3\"");
        eosShutterSpeedMap.put(Integer.valueOf(45), "2\"5");
        eosShutterSpeedMap.put(Integer.valueOf(48), "2\"");
        eosShutterSpeedMap.put(Integer.valueOf(51), "1\"6");
        eosShutterSpeedMap.put(Integer.valueOf(52), "1\"5");
        eosShutterSpeedMap.put(Integer.valueOf(53), "1\"3");
        eosShutterSpeedMap.put(Integer.valueOf(59), "0\"8");
        eosShutterSpeedMap.put(Integer.valueOf(60), "0\"7");
        eosShutterSpeedMap.put(Integer.valueOf(61), "0\"6");
        eosShutterSpeedMap.put(Integer.valueOf(64), "0\"5");
        eosShutterSpeedMap.put(Integer.valueOf(67), "0\"4");
        eosShutterSpeedMap.put(Integer.valueOf(68), "0\"3");
        eosShutterSpeedMap.put(Integer.valueOf(69), "0\"3");
        eosShutterSpeedMap.put(Integer.valueOf(72), "1/4");
        eosShutterSpeedMap.put(Integer.valueOf(75), "1/5");
        eosShutterSpeedMap.put(Integer.valueOf(76), "1/6");
        eosShutterSpeedMap.put(Integer.valueOf(77), "1/6");
        eosShutterSpeedMap.put(Integer.valueOf(80), "1/8");
        eosShutterSpeedMap.put(Integer.valueOf(83), "1/10");
        eosShutterSpeedMap.put(Integer.valueOf(84), "1/10");
        eosShutterSpeedMap.put(Integer.valueOf(85), "1/13");
        eosShutterSpeedMap.put(Integer.valueOf(88), "1/15");
        eosShutterSpeedMap.put(Integer.valueOf(91), "1/20");
        eosShutterSpeedMap.put(Integer.valueOf(92), "1/20");
        eosShutterSpeedMap.put(Integer.valueOf(93), "1/25");
        eosShutterSpeedMap.put(Integer.valueOf(96), "1/30");
        eosShutterSpeedMap.put(Integer.valueOf(99), "1/40");
        eosShutterSpeedMap.put(Integer.valueOf(100), "1/45");
        eosShutterSpeedMap.put(Integer.valueOf(101), "1/50");
        eosShutterSpeedMap.put(Integer.valueOf(104), "1/60");
        eosShutterSpeedMap.put(Integer.valueOf(107), "1/80");
        eosShutterSpeedMap.put(Integer.valueOf(108), "1/90");
        eosShutterSpeedMap.put(Integer.valueOf(109), "1/100");
        eosShutterSpeedMap.put(Integer.valueOf(112), "1/125");
        eosShutterSpeedMap.put(Integer.valueOf(115), "1/160");
        eosShutterSpeedMap.put(Integer.valueOf(116), "1/180");
        eosShutterSpeedMap.put(Integer.valueOf(117), "1/200");
        eosShutterSpeedMap.put(Integer.valueOf(120), "1/250");
        eosShutterSpeedMap.put(Integer.valueOf(Opcodes.LSHR), "1/320");
        eosShutterSpeedMap.put(Integer.valueOf(Opcodes.IUSHR), "1/350");
        eosShutterSpeedMap.put(Integer.valueOf(Opcodes.LUSHR), "1/400");
        eosShutterSpeedMap.put(Integer.valueOf(128), "1/500");
        eosShutterSpeedMap.put(Integer.valueOf(131), "1/640");
        eosShutterSpeedMap.put(Integer.valueOf(132), "1/750");
        eosShutterSpeedMap.put(Integer.valueOf(133), "1/800");
        eosShutterSpeedMap.put(Integer.valueOf(136), "1/1000");
        eosShutterSpeedMap.put(Integer.valueOf(139), "1/1250");
        eosShutterSpeedMap.put(Integer.valueOf(140), "1/1500");
        eosShutterSpeedMap.put(Integer.valueOf(141), "1/1600");
        eosShutterSpeedMap.put(Integer.valueOf(144), "1/2000");
        eosShutterSpeedMap.put(Integer.valueOf(147), "1/2500");
        eosShutterSpeedMap.put(Integer.valueOf(148), "1/3000");
        eosShutterSpeedMap.put(Integer.valueOf(149), "1/3200");
        eosShutterSpeedMap.put(Integer.valueOf(152), "1/4000");
        eosShutterSpeedMap.put(Integer.valueOf(155), "1/5000");
        eosShutterSpeedMap.put(Integer.valueOf(156), "1/6000");
        eosShutterSpeedMap.put(Integer.valueOf(157), "1/6400");
        eosShutterSpeedMap.put(Integer.valueOf(160), "1/8000");
        eosApertureValueMap.put(Integer.valueOf(11), "1.1");
        eosApertureValueMap.put(Integer.valueOf(12), "1.2");
        eosApertureValueMap.put(Integer.valueOf(13), "1.2");
        eosApertureValueMap.put(Integer.valueOf(16), "1.4");
        eosApertureValueMap.put(Integer.valueOf(19), "1.6");
        eosApertureValueMap.put(Integer.valueOf(20), "1.8");
        eosApertureValueMap.put(Integer.valueOf(21), "1.8");
        eosApertureValueMap.put(Integer.valueOf(24), ExifInterface.GPS_MEASUREMENT_2D);
        eosApertureValueMap.put(Integer.valueOf(27), "2.2");
        eosApertureValueMap.put(Integer.valueOf(28), "2.5");
        eosApertureValueMap.put(Integer.valueOf(29), "2.5");
        eosApertureValueMap.put(Integer.valueOf(32), "2.8");
        eosApertureValueMap.put(Integer.valueOf(35), "3.2");
        eosApertureValueMap.put(Integer.valueOf(36), "3.5");
        eosApertureValueMap.put(Integer.valueOf(37), "3.5");
        eosApertureValueMap.put(Integer.valueOf(40), "4");
        eosApertureValueMap.put(Integer.valueOf(43), "4.5");
        eosApertureValueMap.put(Integer.valueOf(44), "4.5");
        eosApertureValueMap.put(Integer.valueOf(45), "5.0");
        eosApertureValueMap.put(Integer.valueOf(48), "5.6");
        eosApertureValueMap.put(Integer.valueOf(51), "6.3");
        eosApertureValueMap.put(Integer.valueOf(52), "6.7");
        eosApertureValueMap.put(Integer.valueOf(53), "7.1");
        eosApertureValueMap.put(Integer.valueOf(56), "8");
        eosApertureValueMap.put(Integer.valueOf(59), "9");
        eosApertureValueMap.put(Integer.valueOf(60), "9.5");
        eosApertureValueMap.put(Integer.valueOf(61), "10");
        eosApertureValueMap.put(Integer.valueOf(64), "11");
        eosApertureValueMap.put(Integer.valueOf(67), "13");
        eosApertureValueMap.put(Integer.valueOf(68), "13");
        eosApertureValueMap.put(Integer.valueOf(69), "14");
        eosApertureValueMap.put(Integer.valueOf(72), "16");
        eosApertureValueMap.put(Integer.valueOf(75), "18");
        eosApertureValueMap.put(Integer.valueOf(76), "19");
        eosApertureValueMap.put(Integer.valueOf(77), "20");
        eosApertureValueMap.put(Integer.valueOf(80), "22");
        eosApertureValueMap.put(Integer.valueOf(83), "25");
        eosApertureValueMap.put(Integer.valueOf(84), "27");
        eosApertureValueMap.put(Integer.valueOf(85), "29");
        eosApertureValueMap.put(Integer.valueOf(88), "32");
        eosApertureValueMap.put(Integer.valueOf(91), "36");
        eosApertureValueMap.put(Integer.valueOf(92), "38");
        eosApertureValueMap.put(Integer.valueOf(93), "40");
        eosApertureValueMap.put(Integer.valueOf(96), "45");
        eosApertureValueMap.put(Integer.valueOf(99), "51");
        eosApertureValueMap.put(Integer.valueOf(100), "54");
        eosApertureValueMap.put(Integer.valueOf(101), "57");
        eosApertureValueMap.put(Integer.valueOf(104), "64");
        eosApertureValueMap.put(Integer.valueOf(107), "72");
        eosApertureValueMap.put(Integer.valueOf(108), "76");
        eosApertureValueMap.put(Integer.valueOf(109), "80");
        eosApertureValueMap.put(Integer.valueOf(112), "91");
        eosIsoSpeedMap.put(Integer.valueOf(0), "Auto");
        eosIsoSpeedMap.put(Integer.valueOf(40), "6");
        eosIsoSpeedMap.put(Integer.valueOf(48), "12");
        eosIsoSpeedMap.put(Integer.valueOf(56), "25");
        eosIsoSpeedMap.put(Integer.valueOf(64), "50");
        eosIsoSpeedMap.put(Integer.valueOf(72), "100");
        eosIsoSpeedMap.put(Integer.valueOf(75), "125");
        eosIsoSpeedMap.put(Integer.valueOf(77), "160");
        eosIsoSpeedMap.put(Integer.valueOf(80), "200");
        eosIsoSpeedMap.put(Integer.valueOf(83), "250");
        eosIsoSpeedMap.put(Integer.valueOf(85), "320");
        eosIsoSpeedMap.put(Integer.valueOf(88), "400");
        eosIsoSpeedMap.put(Integer.valueOf(91), "500");
        eosIsoSpeedMap.put(Integer.valueOf(93), "640");
        eosIsoSpeedMap.put(Integer.valueOf(96), "800");
        eosIsoSpeedMap.put(Integer.valueOf(99), "1000");
        eosIsoSpeedMap.put(Integer.valueOf(101), "1250");
        eosIsoSpeedMap.put(Integer.valueOf(104), "1600");
        eosIsoSpeedMap.put(Integer.valueOf(107), "2000");
        eosIsoSpeedMap.put(Integer.valueOf(109), "2500");
        eosIsoSpeedMap.put(Integer.valueOf(112), "3200");
        eosIsoSpeedMap.put(Integer.valueOf(115), "4000");
        eosIsoSpeedMap.put(Integer.valueOf(117), "5000");
        eosIsoSpeedMap.put(Integer.valueOf(120), "6400");
        eosIsoSpeedMap.put(Integer.valueOf(128), "12800");
        eosIsoSpeedMap.put(Integer.valueOf(136), "25600");
        eosIsoSpeedMap.put(Integer.valueOf(144), "51200");
        eosIsoSpeedMap.put(Integer.valueOf(152), "102400");
        eosWhitebalanceMap.put(Integer.valueOf(0), "Auto");
        eosWhitebalanceMap.put(Integer.valueOf(1), "Daylight");
        eosWhitebalanceMap.put(Integer.valueOf(2), "Cloudy");
        eosWhitebalanceMap.put(Integer.valueOf(3), "Tungsten");
        eosWhitebalanceMap.put(Integer.valueOf(4), "Fluorescent");
        eosWhitebalanceMap.put(Integer.valueOf(5), ExifInterface.TAG_FLASH);
        eosWhitebalanceMap.put(Integer.valueOf(6), "Manual 1");
        eosWhitebalanceMap.put(Integer.valueOf(8), "Shade");
        eosWhitebalanceMap.put(Integer.valueOf(9), "Color temperature");
        eosWhitebalanceMap.put(Integer.valueOf(10), "PC-1");
        eosWhitebalanceMap.put(Integer.valueOf(11), "PC-2");
        eosWhitebalanceMap.put(Integer.valueOf(12), "PC-3");
        eosWhitebalanceMap.put(Integer.valueOf(15), "Manual 2");
        eosWhitebalanceMap.put(Integer.valueOf(16), "Manual 3");
        eosWhitebalanceMap.put(Integer.valueOf(18), "Manual 4");
        eosWhitebalanceMap.put(Integer.valueOf(19), "Manual");
        eosWhitebalanceMap.put(Integer.valueOf(20), "PC-4");
        eosWhitebalanceMap.put(Integer.valueOf(21), "PC-5");
        eosShootingModeMap.put(Integer.valueOf(0), "Program AE");
        eosShootingModeMap.put(Integer.valueOf(1), "Shutter-Speed Priority AE");
        eosShootingModeMap.put(Integer.valueOf(2), "Aperture Priority AE");
        eosShootingModeMap.put(Integer.valueOf(3), "Manual Exposure");
        eosShootingModeMap.put(Integer.valueOf(4), "Bulb");
        eosShootingModeMap.put(Integer.valueOf(5), "Auto Depth-of-Field AE");
        eosShootingModeMap.put(Integer.valueOf(6), "Depth-of-Field AE");
        eosShootingModeMap.put(Integer.valueOf(8), "Lock");
        eosShootingModeMap.put(Integer.valueOf(9), "Auto");
        eosShootingModeMap.put(Integer.valueOf(10), "Night Scene Portrait");
        eosShootingModeMap.put(Integer.valueOf(11), "Sports");
        eosShootingModeMap.put(Integer.valueOf(12), "Portrait");
        eosShootingModeMap.put(Integer.valueOf(13), "Landscape");
        eosShootingModeMap.put(Integer.valueOf(14), "Close-Up");
        eosShootingModeMap.put(Integer.valueOf(15), "Flash Off");
        eosShootingModeMap.put(Integer.valueOf(19), "Creative Auto");
        eosDriveModeMap.put(Integer.valueOf(0), "Single Shooting");
        eosDriveModeMap.put(Integer.valueOf(1), "Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(2), "Video");
        eosDriveModeMap.put(Integer.valueOf(3), "?");
        eosDriveModeMap.put(Integer.valueOf(4), "High-Speed Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(5), "Low-Speed Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(6), "Silent Single Shooting");
        eosDriveModeMap.put(Integer.valueOf(7), "10-Sec Self-Timer plus Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(16), "10-Sec Self-Timer");
        eosDriveModeMap.put(Integer.valueOf(17), "2-Sec Self-Timer");
        eosFocusModeMap.put(Integer.valueOf(0), "One-Shot AF");
        eosFocusModeMap.put(Integer.valueOf(1), "AI Servo AF");
        eosFocusModeMap.put(Integer.valueOf(2), "AI Focus AF");
        eosFocusModeMap.put(Integer.valueOf(3), "Manual Focus");
        nikonWhitebalanceMap.put(Integer.valueOf(2), "Auto");
        nikonWhitebalanceMap.put(Integer.valueOf(4), "Sunny");
        nikonWhitebalanceMap.put(Integer.valueOf(5), "Fluorescent");
        nikonWhitebalanceMap.put(Integer.valueOf(6), "Incandescent");
        nikonWhitebalanceMap.put(Integer.valueOf(7), ExifInterface.TAG_FLASH);
        nikonWhitebalanceMap.put(Integer.valueOf(PanasonicMakernoteDirectory.TAG_BABY_AGE_1), "Cloudy");
        nikonWhitebalanceMap.put(Integer.valueOf(32785), "Sunny shade");
        nikonWhitebalanceMap.put(Integer.valueOf(PanasonicMakernoteDirectory.TAG_TRANSFORM_1), "Color temperature");
        nikonWhitebalanceMap.put(Integer.valueOf(32787), "Preset");
        nikonExposureIndexMap.put(Integer.valueOf(100), "100");
        nikonExposureIndexMap.put(Integer.valueOf(Opcodes.LUSHR), "125");
        nikonExposureIndexMap.put(Integer.valueOf(160), "160");
        nikonExposureIndexMap.put(Integer.valueOf(Callback.DEFAULT_DRAG_ANIMATION_DURATION), "200");
        nikonExposureIndexMap.put(Integer.valueOf(250), "250");
        nikonExposureIndexMap.put(Integer.valueOf(ExifSubIFDDirectory.TAG_MIN_SAMPLE_VALUE), "280");
        nikonExposureIndexMap.put(Integer.valueOf(400), "400");
        nikonExposureIndexMap.put(Integer.valueOf(500), "500");
        nikonExposureIndexMap.put(Integer.valueOf(560), "560");
        nikonExposureIndexMap.put(Integer.valueOf(800), "800");
        nikonExposureIndexMap.put(Integer.valueOf(1000), "1000");
        nikonExposureIndexMap.put(Integer.valueOf(1100), "1100");
        nikonExposureIndexMap.put(Integer.valueOf(1250), "1250");
        nikonExposureIndexMap.put(Integer.valueOf(1600), "1600");
        nikonExposureIndexMap.put(Integer.valueOf(2000), "2000");
        nikonExposureIndexMap.put(Integer.valueOf(2200), "2200");
        nikonExposureIndexMap.put(Integer.valueOf(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS), "2500");
        nikonExposureIndexMap.put(Integer.valueOf(3200), "3200");
        nikonExposureIndexMap.put(Integer.valueOf(4000), "4000");
        nikonExposureIndexMap.put(Integer.valueOf(4500), "4500");
        nikonExposureIndexMap.put(Integer.valueOf(5000), "5000");
        nikonExposureIndexMap.put(Integer.valueOf(6400), "6400");
        nikonExposureIndexMap.put(Integer.valueOf(8000), "8000");
        nikonExposureIndexMap.put(Integer.valueOf(9000), "9000");
        nikonExposureIndexMap.put(Integer.valueOf(PhotoshopDirectory.TAG_PRINT_FLAGS_INFO), "10000");
        nikonExposureIndexMap.put(Integer.valueOf(12800), "12800");
        nikonWbColorTempD300SMap.put(Integer.valueOf(0), "2500K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(1), "2560K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(2), "2630K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(3), "2700K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(4), "2780K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(5), "2860K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(6), "2940K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(7), "3030K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(8), "3130K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(9), "3230K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(10), "3330K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(11), "3450K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(12), "3570K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(13), "3700K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(14), "3850K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(15), "4000K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(16), "4170K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(17), "4350K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(18), "4550K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(19), "4760K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(20), "5000K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(21), "5260K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(22), "5560K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(23), "5880K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(24), "6250K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(25), "6670K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(26), "7140K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(27), "7690K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(28), "8330K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(29), "9090K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(30), "10000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(0), "2500K");
        nikonWbColorTempD200Map.put(Integer.valueOf(1), "2550K");
        nikonWbColorTempD200Map.put(Integer.valueOf(2), "2650K");
        nikonWbColorTempD200Map.put(Integer.valueOf(3), "2700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(4), "2800K");
        nikonWbColorTempD200Map.put(Integer.valueOf(5), "2850K");
        nikonWbColorTempD200Map.put(Integer.valueOf(6), "2950K");
        nikonWbColorTempD200Map.put(Integer.valueOf(7), "3000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(8), "3100K");
        nikonWbColorTempD200Map.put(Integer.valueOf(9), "3200K");
        nikonWbColorTempD200Map.put(Integer.valueOf(10), "3300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(11), "3400K");
        nikonWbColorTempD200Map.put(Integer.valueOf(12), "3600K");
        nikonWbColorTempD200Map.put(Integer.valueOf(13), "3700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(14), "3800K");
        nikonWbColorTempD200Map.put(Integer.valueOf(15), "4000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(16), "4200K");
        nikonWbColorTempD200Map.put(Integer.valueOf(17), "4300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(18), "4500K");
        nikonWbColorTempD200Map.put(Integer.valueOf(19), "4800K");
        nikonWbColorTempD200Map.put(Integer.valueOf(20), "5000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(21), "5300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(22), "5600K");
        nikonWbColorTempD200Map.put(Integer.valueOf(23), "5900K");
        nikonWbColorTempD200Map.put(Integer.valueOf(24), "6300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(25), "6700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(26), "7100K");
        nikonWbColorTempD200Map.put(Integer.valueOf(27), "7700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(28), "8300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(29), "9100K");
        nikonWbColorTempD200Map.put(Integer.valueOf(30), "10000K");
        nikonFocusModeMap.put(Integer.valueOf(1), "Manual Focus");
        nikonFocusModeMap.put(Integer.valueOf(PanasonicMakernoteDirectory.TAG_BABY_AGE_1), "Single AF servo");
        nikonFocusModeMap.put(Integer.valueOf(32785), "Continous AF servo");
        nikonFocusModeMap.put(Integer.valueOf(PanasonicMakernoteDirectory.TAG_TRANSFORM_1), "AF servo auto switch");
        nikonFocusModeMap.put(Integer.valueOf(32787), "Constant AF servo");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(1), "SD");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(2), "NL");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(3), "VI");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(4), "MC");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(5), "PT");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(6), "LS");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(101), "O-1");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(102), "O-2");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(103), "O-3");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(104), "O-4");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(XMPError.BADXML), "C-1");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(XMPError.BADRDF), "C-2");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(XMPError.BADXMP), "C-3");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(XMPError.BADSTREAM), "C-4");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(205), "C-5");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(206), "C-6");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(207), "C-7");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(208), "C-8");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(209), "C-9");
        nikonFocusMeteringModeMap.put(Integer.valueOf(2), "Dynamic");
        nikonFocusMeteringModeMap.put(Integer.valueOf(PanasonicMakernoteDirectory.TAG_BABY_AGE_1), "Single point");
        nikonFocusMeteringModeMap.put(Integer.valueOf(32785), "Auto area");
        nikonFocusMeteringModeMap.put(Integer.valueOf(PanasonicMakernoteDirectory.TAG_TRANSFORM_1), "3D");
        eosPictureStyleMap.put(Integer.valueOf(129), "ST");
        eosPictureStyleMap.put(Integer.valueOf(130), "PT");
        eosPictureStyleMap.put(Integer.valueOf(131), "LS");
        eosPictureStyleMap.put(Integer.valueOf(132), "NL");
        eosPictureStyleMap.put(Integer.valueOf(133), "FL");
        eosPictureStyleMap.put(Integer.valueOf(134), "MO");
        eosPictureStyleMap.put(Integer.valueOf(33), "UD1");
        eosPictureStyleMap.put(Integer.valueOf(34), "UD2");
        eosPictureStyleMap.put(Integer.valueOf(35), "UD3");
    }

    public static String mapToString(int productId, int property, int value) {
        switch (property) {
            case Property.WhiteBalance /*20485*/:
                return (String) nikonWhitebalanceMap.get(Integer.valueOf(value));
            case Property.FNumber /*20487*/:
                int major = value / 100;
                int minor = value % 100;
                if (minor == 0) {
                    return "f " + major;
                }
                if (minor % 10 == 0) {
                    return "f " + major + '.' + (minor / 10);
                }
                return "f " + major + '.' + minor;
            case Property.FocusMode /*20490*/:
                return (String) nikonFocusModeMap.get(Integer.valueOf(value));
            case Property.ExposureTime /*20493*/:
                if (value == -1) {
                    return "Bulb";
                }
                int seconds = value / PhotoshopDirectory.TAG_PRINT_FLAGS_INFO;
                int rest = value % PhotoshopDirectory.TAG_PRINT_FLAGS_INFO;
                StringBuilder b = new StringBuilder();
                if (seconds > 0) {
                    b.append(seconds).append("\"");
                }
                if (rest > 0) {
                    b.append("1/").append(Math.round(1.0d / (((double) rest) * 1.0E-4d)));
                }
                return b.toString();
            case Property.ExposureIndex /*20495*/:
                return getNikonExposureIndex(productId, value);
            case Property.ExposureBiasCompensation /*20496*/:
                int dec = Math.round(((float) Math.abs(value)) / 100.0f);
                int upper = dec / 10;
                int lower = dec % 10;
                char sign = value >= 0 ? SignatureVisitor.EXTENDS : SignatureVisitor.SUPER;
                return String.format("%c%d.%d", new Object[]{Character.valueOf(sign), Integer.valueOf(upper), Integer.valueOf(lower)});
            case Property.FocusMeteringMode /*20508*/:
                return (String) nikonFocusMeteringModeMap.get(Integer.valueOf(value));
            case Property.NikonWbColorTemp /*53278*/:
                return getNikonWbColorTemp(productId, value);
            case Property.NikonShutterSpeed /*53504*/:
                int numerator = (value >> 16) & 65535;
                int denominator = value & 65535;
                if (denominator == 1) {
                    return "" + numerator + "\"";
                }
                if (numerator == 1) {
                    return "1/" + denominator;
                }
                if (value == -1) {
                    return "Bulb";
                }
                if (value == -2) {
                    return ExifInterface.TAG_FLASH;
                }
                if (numerator <= denominator) {
                    return "" + numerator + "/" + denominator;
                }
                return String.format("%.1f\"", new Object[]{Double.valueOf(((double) numerator) / ((double) denominator))});
            case Property.EosApertureValue /*53505*/:
                Object s = (String) eosApertureValueMap.get(Integer.valueOf(value));
                StringBuilder append = new StringBuilder().append("f ");
                if (s == null) {
                    s = Character.valueOf('?');
                }
                return append.append(s).toString();
            case Property.EosShutterSpeed /*53506*/:
                String s2 = (String) eosShutterSpeedMap.get(Integer.valueOf(value));
                if (s2 == null) {
                    s2 = "?";
                }
                return s2;
            case Property.EosIsoSpeed /*53507*/:
                return (String) eosIsoSpeedMap.get(Integer.valueOf(value));
            case Property.EosExposureCompensation /*53508*/:
                int v;
                char ch;
                if (value > 128) {
                    v = 256 - value;
                    ch = SignatureVisitor.SUPER;
                } else {
                    v = value;
                    ch = SignatureVisitor.EXTENDS;
                }
                if (v == 0) {
                    return " 0";
                }
                int first = v / 8;
                int second = v % 8;
                String dec2 = second == 3 ? "1/3" : second == 4 ? "1/2" : second == 5 ? "2/3" : "";
                if (first > 0) {
                    return String.format("%c%d %s", new Object[]{Character.valueOf(ch), Integer.valueOf(first), dec2});
                }
                return String.format("%c%s", new Object[]{Character.valueOf(ch), dec2});
            case Property.EosShootingMode /*53509*/:
                return (String) eosShootingModeMap.get(Integer.valueOf(value));
            case Property.EosDriveMode /*53510*/:
                return (String) eosDriveModeMap.get(Integer.valueOf(value));
            case 53512:
                return (String) eosFocusModeMap.get(Integer.valueOf(value));
            case Property.EosWhitebalance /*53513*/:
                return (String) eosWhitebalanceMap.get(Integer.valueOf(value));
            case Property.EosColorTemperature /*53514*/:
                return Integer.toString(value) + "K";
            case Property.EosPictureStyle /*53520*/:
                return (String) eosPictureStyleMap.get(Integer.valueOf(value));
            case Property.NikonExposureIndicateStatus /*53681*/:
                return "" + (value / 6) + "." + (Math.abs(value) % 6) + " EV";
            case 53760:
                return (String) nikonActivePicCtrlItemMap.get(Integer.valueOf(value));
            default:
                return "?";
        }
    }

    private static String getNikonExposureIndex(int productId, int value) {
        switch (productId) {
            case Product.NikonD200 /*1040*/:
            case Product.NikonD80 /*1042*/:
            case 1044:
                if (value == 2000) {
                    return "Hi-0.3";
                }
                if (value == DefaultRetryPolicy.DEFAULT_TIMEOUT_MS) {
                    return "Hi-0.7";
                }
                if (value == 3200) {
                    return "Hi-1";
                }
                if (value == 2200) {
                    return "Hi-0.5";
                }
                break;
            case 1050:
            case Product.NikonD5000 /*1059*/:
            case 1061:
                if (value == 100) {
                    return "LO-1";
                }
                if (value == Opcodes.LUSHR) {
                    return "LO-0.7";
                }
                if (value == 160) {
                    return "LO-0.3";
                }
                if (value == 4000) {
                    return "Hi-0.3";
                }
                if (value == 4500) {
                    return "Hi-0.5";
                }
                if (value == 5000) {
                    return "Hi-0.7";
                }
                if (value == 6400) {
                    return "Hi-1";
                }
                break;
            case Product.NikonD3 /*1052*/:
                if (value == 100) {
                    return "LO-1";
                }
                if (value == Opcodes.LUSHR) {
                    return "LO-0.7";
                }
                if (value == 140) {
                    return "LO-0.5";
                }
                if (value == 160) {
                    return "LO-0.3";
                }
                if (value == 8320) {
                    return "Hi-0.3";
                }
                if (value == 8960) {
                    return "Hi-0.5";
                }
                if (value == 10240) {
                    return "Hi-0.7";
                }
                if (value == 12800) {
                    return "Hi-1";
                }
                if (value == 25600) {
                    return "Hi-2";
                }
                break;
            case Product.NikonD3X /*1056*/:
                if (value == 50) {
                    return "LO-1";
                }
                if (value == 62) {
                    return "LO-0.7";
                }
                if (value == 70) {
                    return "LO-0.5";
                }
                if (value == 80) {
                    return "LO-0.3";
                }
                if (value == 2000) {
                    return "Hi-0.3";
                }
                if (value == 2240) {
                    return "Hi-0.5";
                }
                if (value == 2560) {
                    return "Hi-0.7";
                }
                if (value == 3200) {
                    return "Hi-1";
                }
                if (value == 6400) {
                    return "Hi-2";
                }
                break;
            case 1062:
                if (value == 100) {
                    return "LO-1";
                }
                if (value == Opcodes.LUSHR) {
                    return "LO-0.7";
                }
                if (value == 140) {
                    return "LO-0.5";
                }
                if (value == 160) {
                    return "LO-0.3";
                }
                if (value == 14400) {
                    return "Hi-0.3";
                }
                if (value == 18000) {
                    return "Hi-0.5";
                }
                if (value == 25600) {
                    return "Hi-1";
                }
                if (value == RemoteJWKSet.DEFAULT_HTTP_SIZE_LIMIT) {
                    return "Hi-2";
                }
                break;
            case 1064:
                if (value == 8000) {
                    return "Hi-0.3";
                }
                if (value == 9000) {
                    return "Hi-0.5";
                }
                if (value == PhotoshopDirectory.TAG_PRINT_FLAGS_INFO) {
                    return "Hi-0.7";
                }
                if (value == 12800) {
                    return "Hi-1";
                }
                if (value == 25600) {
                    return "Hi-2";
                }
                break;
        }
        return (String) nikonExposureIndexMap.get(Integer.valueOf(value));
    }

    private static String getNikonWbColorTemp(int productId, int value) {
        switch (productId) {
            case Product.NikonD200 /*1040*/:
            case Product.NikonD80 /*1042*/:
                return (String) nikonWbColorTempD200Map.get(Integer.valueOf(value));
            case 1050:
            case Product.NikonD3 /*1052*/:
            case Product.NikonD3X /*1056*/:
            case 1057:
            case Product.NikonD700 /*1058*/:
            case 1061:
            case 1062:
            case 1064:
                return (String) nikonWbColorTempD300SMap.get(Integer.valueOf(value));
            default:
                return null;
        }
    }

    public static Integer mapToDrawable(int property, int value) {
        return null;
    }

    public static String getBiggestValue(int property) {
        switch (property) {
            case Property.FNumber /*20487*/:
                return "33.3";
            case Property.ExposureTime /*20493*/:
                return "1/10000";
            case Property.ExposureIndex /*20495*/:
                return "LO-0.3";
            case Property.EosApertureValue /*53505*/:
                return "f 9.5";
            case Property.EosShutterSpeed /*53506*/:
                return "1/8000";
            case Property.EosIsoSpeed /*53507*/:
                return "102400";
            default:
                return "";
        }
    }
}
