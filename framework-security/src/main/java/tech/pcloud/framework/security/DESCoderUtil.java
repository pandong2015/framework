package tech.pcloud.framework.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by zhouxiang on 2016/8/24.
 */
public class DESCoderUtil {
    public static final String ALGORITHM = "DES";
    public static final byte[] desKey = new byte[]{21, 1, -110, 82, -32, -85, -128, -65};

    /**
     * 数据加密，算法（DES）
     *
     * @param data   要进行加密的数据
     * @param desKey DES密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, byte[] desKey) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(desKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符串
            encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
//            log.error("加密错误，错误信息：", e);
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 向前兼容，key固定
     *
     * @param data 要加密的数据
     * @return 加密后的数据
     * @deprecated
     */
    public static String encrypt(String data) {
        return encrypt(data, desKey);
    }

    /**
     * 数据解密，算法（DES）
     *
     * @param cryptData 加密数据
     * @param desKey    DES密钥
     * @return 解密后的数据
     */
    public static String decrypt(String cryptData, byte[] desKey) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(desKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(cryptData)));
        } catch (Exception e) {
//            log.error("解密错误，错误信息：", e);
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

    /**
     * 向前兼容，key固定
     *
     * @param cryptData 加密数据
     * @return 解密后的数据
     * @deprecated
     */
    public static String decrypt(String cryptData) {
        return decrypt(cryptData, desKey);
    }


    public static void main(String[] args) {
        String encryptString = encrypt("user_social_mustang", desKey);
        System.out.println("user_social_mustang   " + encryptString);

        encryptString = encrypt("wwqwVh94WD3SFq/26tHNYUJt+HtbRhTU", desKey);
        System.out.println("wwqwVh94WD3SFq/26tHNYUJt+HtbRhTU   " + encryptString);

        //user_ocs_v5
        //9$r05YT*6G

        //user_IGS
        //KM#d025*t0$rf
        String d = decrypt("wwqwVh94WD3SFq/26tHNYVPYPjWiOdmi", desKey);
        System.out.println(d);
        d = decrypt("2mnXIfw3KD0nRjuN5dg5WwxtrQrzzUma", desKey);
        System.out.println(d);

        //hn*uW#05$GTj

        //String desencryptString = encrypt("multimer_2017", desKey);
        //System.out.println(desencryptString);
        System.out.println(encrypt("root"));
        System.out.println(encrypt("Zaq1-2wsx"));
    }
}
