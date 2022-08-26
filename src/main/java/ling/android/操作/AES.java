package ling.android.操作;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {
    /*
     * 加密用的Key 可以用26个字母和数字组成 使用AES-128-CBC加密模式，key需要为16位。
     */
    private final String key;
    private final String iv;
    private boolean 向量;

    public AES() {
        String temp = 加解密操作.MD5加密(String.valueOf(时间操作.取时间戳()));
        this.key = temp.substring(0, 16);
        this.iv = temp.substring(16, 32);
        this.向量 = true;
    }

    public AES(String key, String iv) {
        this.key = key;
        this.iv = iv;
        向量 = true;
    }

    public AES(boolean 向量) {
        this();
        this.向量 = 向量;
    }

    public AES(String key) {
        this.key = key;
        this.iv = key;
        this.向量 = false;
    }


    public String getKey() {
        return key;
    }

    public String getIv() {
        return iv;
    }

    /**
     * @param data 明文
     * @return 密文
     * @author miracle.qu
     * @Description AES算法加密明文
     */
    public String 加密(String data) {
        try {
            Cipher cipher;
            if (向量)
                cipher = Cipher.getInstance("AES/CBC/NoPadding");
            else
                cipher = Cipher.getInstance("AES");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            if (向量) {
                IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());  // CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            } else
                cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new String(Base64.getEncoder().encode(encrypted)); // BASE64做转码。

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param data 密文
     * @return 明文
     * @author miracle.qu
     * @Description AES算法解密密文
     */
    public String 解密(String data) {
        try {
            byte[] encrypted1 = Base64.getDecoder().decode(data);//先用base64解密

            Cipher cipher;
            if (向量)
                cipher = Cipher.getInstance("AES/CBC/NoPadding");
            else
                cipher = Cipher.getInstance("AES");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            if (向量) {
                IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());  // CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            } else
                cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
