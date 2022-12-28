package ling.android.操作;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {
    /*
     * 加密用的Key 可以用26个字母和数字组成 使用AES-128-CBC加密模式，key需要为16位。
     */
    private final byte[] key;
    private final byte[] iv;
    private final byte[] aad;

    /*public static void main(String[] argc) {
        AES aes = new AES();
        String a = "你好，世界";
        System.out.println(aes.加密(a));
        System.out.println(aes.解密(aes.加密(a)));
    }*/

    /*public static void main(String[] args) throws KeyFormatException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        String data = "Hello World"; // 待加密的原文
        String key = "12345678abcdefgh"; // key 长度只能是 16、24 或 32 字节
        System.out.println(key.getBytes().length);
        String iv = "iviviviviviviviv";
        String aad = "aad"; // AAD 长度无限制，可为空
        AES aes = new AES(new KeyObject(key, iv));
        byte[] ciphertext = aes.加密(data.getBytes());
        System.out.println("GCM 模式加密结果（Base64）：" + Base64.getEncoder().encodeToString(ciphertext));

        byte[] plaintext = aes.解密(ciphertext);
        System.out.println("解密结果：" + new String(plaintext));
    }*/

    public AES(KeyObject keyObject) {
        this.key = keyObject.getKey();
        this.iv = keyObject.getIv();
        this.aad = keyObject.getAad();
    }

    /**
     * @deprecated 此类构造函数在当前的代码实现中已经不便于使用了。建议使用KeyObject类来完成此类需求
     */
    @Deprecated(since = "2022-12-28")
    public AES() {
        String temp = 加解密操作.MD5加密(String.valueOf(时间操作.取时间戳()));
        this.key = temp.substring(0, 16).getBytes();
        this.iv = temp.substring(16, 32).getBytes();
        this.aad = "".getBytes();
    }

    /**
     * @deprecated 此类构造函数在当前的代码实现中已经不便于使用了。建议使用KeyObject类来完成此类需求
     */
    @Deprecated(since = "2022-12-28")
    public AES(String key, String iv) {
        this.key = key.getBytes();
        this.iv = iv.getBytes();
        this.aad = "".getBytes();
    }

    /**
     * @deprecated 此类构造函数在当前的代码实现中已经不便于使用了。建议使用KeyObject类来完成此类需求
     */
    @Deprecated(since = "2022-12-28")
    public AES(boolean 向量) {
        this();
    }

    /**
     * @deprecated 此类构造函数在当前的代码实现中已经不便于使用了。建议使用KeyObject类来完成此类需求
     */
    @Deprecated(since = "2022-12-28")
    public AES(String key) {
        this.key = key.getBytes();
        this.iv = key.getBytes();
        this.aad = "".getBytes();
    }

    public byte[] 加密(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        cipher.updateAAD(aad);
        return cipher.doFinal(data);
    }

    public byte[] 解密(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        cipher.updateAAD(aad);
        return cipher.doFinal(data);
    }


    public byte[] getKey() {
        return key;
    }

    public byte[] getIv() {
        return iv;
    }

    public static class KeyObject {
        protected byte[] key;
        protected byte[] iv;
        protected byte[] aad;

        public KeyObject(@NotNull byte[] key, @NotNull byte[] iv, @NotNull byte[] aad) throws KeyFormatException {
            if (key.length != 16 && key.length != 24 && key.length != 32)
                throw new KeyFormatException("密钥长度错误！应为16、24或32位，实为" + key.length + "位");
            if (iv.length != 16)
                throw new KeyFormatException("向量长度错误！应为16位，实为" + iv.length + "位");
            this.key = key;
            this.iv = iv;
            this.aad = aad;
        }

        public KeyObject(@NotNull String key, @NotNull String iv, @NotNull String aad) throws KeyFormatException {
            this(key.getBytes(), iv.getBytes(), aad.getBytes());
        }

        public KeyObject(@NotNull String key, @NotNull String iv) throws KeyFormatException {
            this(key.getBytes(), iv.getBytes(), "".getBytes());
        }

        public KeyObject(@NotNull byte[] key, @NotNull byte[] iv) throws KeyFormatException {
            this(key, iv, "".getBytes());
        }

        public static KeyObject init(boolean aad) throws KeyFormatException {
            String temp = 加解密操作.MD5加密(String.valueOf(时间操作.取时间戳()));
            byte[] key = temp.substring(0, 16).getBytes();
            byte[] iv = temp.substring(16, 32).getBytes();
            if (aad) {
                return new KeyObject(key, iv, 加解密操作.MD5加密(key).getBytes());
            } else {
                return new KeyObject(key, iv, "".getBytes());
            }
        }

        public byte[] getKey() {
            return key;
        }

        public void setKey(byte[] key) {
            this.key = key;
        }

        public byte[] getIv() {
            return iv;
        }

        public void setIv(byte[] iv) {
            this.iv = iv;
        }

        public byte[] getAad() {
            return aad;
        }

        public void setAad(byte[] aad) {
            this.aad = aad;
        }
    }

    /**
     * Key格式错误
     */
    public static class KeyFormatException extends Exception {
        protected String 异常;

        public KeyFormatException(String 异常) {
            this.异常 = 异常;
        }

        public String get异常() {
            return 异常;
        }
    }


}
