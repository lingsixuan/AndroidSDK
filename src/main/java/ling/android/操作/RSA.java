package ling.android.操作;

import android.annotation.TargetApi;
import android.os.Build;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA算法实现类
 */
public class RSA {

    /**
     * 密钥长度，DSA算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static int KEY_SIZE = 1024;
    public static String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static String UTF8 = "UTF-8";

    static {
        try {
            KeyFactory.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            RSA_ALGORITHM = "RSA";
        }
    }

    private final RSAKey key;

    public RSA(RSAKey key) {
        this.key = key;
    }

    public RSA() throws NoSuchAlgorithmException {
        this.key = new RSAKey();
    }

    /**
     * 私钥加密的数据只能通过公钥解密，通过此特性可以验证信息来源真实性。
     *
     * @param data 待加密数据
     * @return 加密结果
     */
    public byte[] 私钥加密(byte[] data) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key.getPrivateKey());
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥可以解开私钥加密的数据，通过此特性可以验证信息来源真实性
     *
     * @param data 密文
     * @return 明文
     * @throws Exception 异常
     */
    public byte[] 公钥解密(byte[] data) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key.getPublicKey());
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密的数据只能通过私钥解密
     *
     * @param data 明文
     * @return 密文
     * @throws Exception 异常
     */
    public byte[] 公钥加密(byte[] data) throws Exception {
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //初始化公钥,根据给定的编码密钥创建一个新的 X509EncodedKeySpec。
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getPublicKey());
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密的数据只能通过私钥解密
     *
     * @param data 密文
     * @return 明文
     * @throws Exception 异常
     */
    public byte[] 私钥解密(byte[] data) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key.getPrivateKey());
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * RSA密钥类
     */
    public static class RSAKey {
        private final byte[] publicKey;
        private final byte[] privateKey;

        public RSAKey() throws NoSuchAlgorithmException {
            //KeyPairGenerator用于生成公钥和私钥对。密钥对生成器是使用 getInstance 工厂方法
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            this.publicKey = publicKey.getEncoded();
            this.privateKey = privateKey.getEncoded();
        }

        public RSAKey(byte[] publicKey, byte[] privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        @TargetApi(Build.VERSION_CODES.O)
        public RSAKey(String publicKey, String privateKey) {
            this.publicKey = Base64.getDecoder().decode(publicKey.getBytes());
            this.privateKey = Base64.getDecoder().decode(privateKey.getBytes());
        }

        public byte[] getPublicKey() {
            return publicKey;
        }

        public byte[] getPrivateKey() {
            return privateKey;
        }
    }
}
