package tech.pcloud.framework.utility.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class ByteUtil {

  public static byte[] random(int length) {
    Random random = new Random();
    byte[] bytes = new byte[length];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) random.nextInt(256);
    }
    return bytes;
  }

  public static byte[] string2Byte(String str) {
    return string2Byte(str, Charset.forName("UTF-8"));
  }

  public static byte[] string2Byte(String str, Charset charset) {
    return str.getBytes(charset);
  }

  public static String byteArray2String(byte[] data) {
    return byteArray2String(data, Charset.forName("UTF-8"));
  }

  public static String byteArray2String(byte[] data, Charset charset) {
    return new String(data, charset);
  }

  public static int byteArray2Short(byte[] b) {
    return (b[1] & 0xFF |
            (b[0] & 0xFF) << 8) & 0x0FFFF;
  }

  public static byte[] short2ByteArray(int a) {
    return new byte[]{
            (byte) ((a >> 8) & 0xFF),
            (byte) (a & 0xFF)};
  }

  public static int byteArray2Int(byte[] b) {
    return b[3] & 0xFF |
            (b[2] & 0xFF) << 8 |
            (b[1] & 0xFF) << 16 |
            (b[0] & 0xFF) << 24;
  }

  public static byte[] int2ByteArray(int a) {
    return new byte[]{
            (byte) ((a >> 24) & 0xFF),
            (byte) ((a >> 16) & 0xFF),
            (byte) ((a >> 8) & 0xFF),
            (byte) (a & 0xFF)
    };
  }

  public static byte[] longToBytes(long x) {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putLong(0, x);
    return buffer.array();
  }

  public static long bytesToLong(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.put(bytes, 0, bytes.length);
    buffer.flip();//need flip
    return buffer.getLong();
  }

  public static byte[] hexStringToByteArray(String s) {
    byte[] b = new byte[s.length() / 2];
    for (int i = 0; i < b.length; i++) {
      int index = i * 2;
      int v = Integer.parseInt(s.substring(index, index + 2), 16);
      b[i] = (byte) v;
    }
    return b;
  }

  public static String bytesToHexString(byte[] src) {
    StringBuilder stringBuilder = new StringBuilder("");
    if (src == null || src.length <= 0) {
      return null;
    }
    for (int i = 0; i < src.length; i++) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString();
  }
}
