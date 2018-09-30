package cc.yelinvan.photographhome.rycusboss.ptp;

import android.util.Log;
import java.nio.ByteBuffer;

public class PacketUtil {
    public static int[] readU32Array(ByteBuffer b) {
        int len = b.getInt();
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.getInt();
        }
        return a;
    }

    public static int[] readU16Array(ByteBuffer b) {
        int len = b.getInt();
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.getShort() & 65535;
        }
        return a;
    }

    public static void writeU16Array(ByteBuffer b, int[] a) {
        b.putInt(a.length);
        for (int v : a) {
            b.putShort((short) v);
        }
    }

    public static int[] readU8Array(ByteBuffer b) {
        int len = b.getInt();
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.get() & 255;
        }
        return a;
    }

    public static int[] readU32Enumeration(ByteBuffer b) {
        int len = b.getShort() & 65535;
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.getInt();
        }
        return a;
    }

    public static int[] readS16Enumeration(ByteBuffer b) {
        int len = b.getShort() & 65535;
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.getShort();
        }
        return a;
    }

    public static int[] readU16Enumeration(ByteBuffer b) {
        int len = b.getShort() & 65535;
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.getShort() & 65535;
        }
        return a;
    }

    public static int[] readU8Enumeration(ByteBuffer b) {
        int len = b.getShort() & 65535;
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = b.get() & 255;
        }
        return a;
    }

    public static String readString(ByteBuffer b) {
        int len = b.get() & 255;
        if (len <= 0) {
            return "";
        }
        char[] ch = new char[(len - 1)];
        for (int i = 0; i < len - 1; i++) {
            ch[i] = b.getChar();
        }
        b.getChar();
        return String.copyValueOf(ch);
    }

    public static void writeString(ByteBuffer b, String s) {
        b.put((byte) s.length());
        if (s.length() > 0) {
            for (int i = 0; i < s.length(); i++) {
                b.putShort((short) s.charAt(i));
            }
            b.putShort((short) 0);
        }
    }

    public static String hexDumpToString(byte[] a, int offset, int len) {
        int k;
        char ch;
        int lines = len / 16;
        int rest = len % 16;
        StringBuilder b = new StringBuilder((lines + 1) * 97);
        for (int i = 0; i < lines; i++) {
            b.append(String.format("%04x ", new Object[]{Integer.valueOf(i * 16)}));
            for (k = 0; k < 16; k++) {
                b.append(String.format("%02x ", new Object[]{Byte.valueOf(a[((i * 16) + offset) + k])}));
            }
            for (k = 0; k < 16; k++) {
                ch = (char) a[((i * 16) + offset) + k];
                if (ch < ' ' || ch > '~') {
                    ch = '.';
                }
                b.append(ch);
            }
            b.append(10);
        }
        if (rest != 0) {
            b.append(String.format("%04x ", new Object[]{Integer.valueOf(lines * 16)}));
            for (k = 0; k < rest; k++) {
                b.append(String.format("%02x ", new Object[]{Byte.valueOf(a[((lines * 16) + offset) + k])}));
            }
            for (k = 0; k < (16 - rest) * 3; k++) {
                b.append(' ');
            }
            for (k = 0; k < rest; k++) {
                ch = (char) a[((lines * 16) + offset) + k];
                if (ch < ' ' || ch > '~') {
                    ch = '.';
                }
                b.append(ch);
            }
            b.append(10);
        }
        return b.toString();
    }

    public static void logHexdump(String tag, byte[] a, int offset, int len) {
        Log.i(tag, hexDumpToString(a, offset, len));
    }

    public static void logHexdump(String tag, byte[] a, int len) {
        logHexdump(tag, a, 0, len);
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }
}
