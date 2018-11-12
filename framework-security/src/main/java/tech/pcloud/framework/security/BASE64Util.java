package tech.pcloud.framework.security;

import java.util.Base64;

public class BASE64Util {

    public static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static byte[] encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public static String encodeToString(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
