package tech.pcloud.framework.exception;

public class InvalidDataException extends PcloudException {
  public static final int ERROR_CODE_INVALID_DATA = 400000;

  public InvalidDataException(String s) {
    super(s, ERROR_CODE_INVALID_DATA);
  }

  public InvalidDataException(String s, Throwable throwable) {
    super(s, throwable, ERROR_CODE_INVALID_DATA);
  }
}
