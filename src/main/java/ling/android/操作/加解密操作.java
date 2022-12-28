package ling.android.操作;

import android.annotation.TargetApi;
import android.os.Build;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class 加解密操作 {

    public static String MD5加密(String 值) {
        return MD5加密(值.getBytes());
    }

    public static String MD5加密(byte[] bytes) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] md5Bytes = md5.digest(bytes);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static String Base64编码(String 值) {
        byte[] str = Base64.getEncoder().encode(值.getBytes());
        return new String(str);
    }

    //base64解码
    @TargetApi(Build.VERSION_CODES.O)
    public static String Base64解码(String 值) {
        byte[] str = Base64.getDecoder().decode(值);
        return new String(str);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String RC4加密(String 值, String 密码) {
        if (值 == null || 密码 == null)
            return null;
        try {
            byte[] a = RC4Base(值.getBytes(StandardCharsets.UTF_8), 密码);
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            int j = a.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : a) {
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xf)];
                str[(k++)] = hexDigits[(byte0 & 0xf)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String RC4解密(String 值, String 密码) {
        if ((值 == null) || (密码 == null))
            return null;
        try {
            return new String(RC4Base(HexString2Bytes(值), 密码), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(mKkey);

        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = x + 1 & 0xFF;
            y = (key[x] & 0xFF) + y & 0xFF;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            int xorIndex = (key[x] & 0xFF) + (key[y] & 0xFF) & 0xFF;
            result[i] = ((byte) (input[i] ^ key[xorIndex]));
        }
        return result;
    }

    private static byte[] initKey(String aKey) {
        try {
            byte[] b_key = aKey.getBytes("GBK");
            byte[] state = new byte[256];

            for (int i = 0; i < 256; i++) {
                state[i] = ((byte) i);
            }
            int index1 = 0;
            int index2 = 0;
            if ((b_key == null) || (b_key.length == 0)) {
                return null;
            }
            for (int i = 0; i < 256; i++) {
                index2 = (b_key[index1] & 0xFF) + (state[i] & 0xFF) + index2 & 0xFF;
                byte tmp = state[i];
                state[i] = state[index2];
                state[index2] = tmp;
                index1 = (index1 + 1) % b_key.length;
            }
            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] HexString2Bytes(String src) {
        try {
            int size = src.length();
            byte[] ret = new byte[size / 2];
            byte[] tmp = src.getBytes("GBK");
            for (int i = 0; i < size / 2; i++) {
                ret[i] = uniteBytes(tmp[(i * 2)], tmp[(i * 2 + 1)]);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[]{src0})).byteValue();

        _b0 = (char) (_b0 << '\004');
        char _b1 = (char) Byte.decode("0x" + new String(new byte[]{src1})).byteValue();

        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }


}
