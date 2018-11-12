package tech.pcloud.framework.utility.common;

public class StringUtils {
  public static boolean isNull(String str) {
    if (str == null || str.isEmpty()) {
      return true;
    }
    return false;
  }
}
