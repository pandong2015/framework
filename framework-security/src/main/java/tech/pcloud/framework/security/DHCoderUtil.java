package tech.pcloud.framework.security;


import tech.pcloud.framework.security.model.DHKeyPair;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DHCoderUtil {
    public static final String ALGORITHM = "DH";
    public static final String SECRET_ALGORITHM = "DES";

    public static DHKeyPair initKey(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(ALGORITHM);
        keyPairGenerator.initialize(keySize);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();
        return DHKeyPair.builder().privateKey(privateKey).publicKey(publicKey).build();
    }

    public static DHKeyPair initKey(String key) throws Exception {
        byte[] keyBytes = BASE64Util.decode(key);
        return initKey(keyBytes);
    }

    public static DHKeyPair initKey(byte[] key) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 由甲方公钥构建乙方密钥
        DHParameterSpec dhParamSpec = ((DHPublicKey) pubKey).getParams();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(keyFactory.getAlgorithm());
        keyPairGenerator.initialize(dhParamSpec);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        return DHKeyPair.builder().privateKey(privateKey).publicKey(publicKey).build();
    }

    public static byte[] encrypt(byte[] data, String publicKey,
                                 String privateKey) throws Exception {
        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);
        return encrypt(data, secretKey);
    }

    public static byte[] encrypt(byte[] data, byte[] publicKey,
                                 byte[] privateKey) throws Exception {
        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);
        return encrypt(data, secretKey);
    }

    public static byte[] encrypt(byte[] data, SecretKey secretKey) throws Exception {
        // 数据加密
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, String publicKey,
                                 String privateKey) throws Exception {
        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);
        return decrypt(data, secretKey);
    }

    public static byte[] decrypt(byte[] data, byte[] publicKey,
                                 byte[] privateKey) throws Exception {
        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);
        return decrypt(data, secretKey);
    }

    public static byte[] decrypt(byte[] data, SecretKey secretKey) throws Exception {
        // 数据解密
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }

    private static SecretKey getSecretKey(String publicKey, String privateKey)
            throws Exception {
        byte[] pubKeyBytes = BASE64Util.decode(publicKey);
        byte[] priKeyBytes = BASE64Util.decode(privateKey);
        return getSecretKey(pubKeyBytes, priKeyBytes);
    }

    private static SecretKey getSecretKey(byte[] publicKey, byte[] privateKey)
            throws Exception {
        // 初始化公钥
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 初始化私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory
                .getAlgorithm());
        keyAgree.init(priKey);
        keyAgree.doPhase(pubKey, true);

        // 生成本地密钥
        byte secret[] = keyAgree.generateSecret();
        SecretKeyFactory skf = SecretKeyFactory.getInstance(SECRET_ALGORITHM);
        DESKeySpec desSpec = new DESKeySpec(secret);
        SecretKey secretKey = skf.generateSecret(desSpec);
//        SecretKey secretKey = keyAgree.generateSecret();////DES、3DES、AES
        return secretKey;
    }
}
