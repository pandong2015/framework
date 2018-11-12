package tech.pcloud.framework.exception;



public class StoreNotFoundException extends SecurityException {
  public static final int ERROR_CODE_SECURITY_STORE_NOT_FOUNT = 2001;

  public StoreNotFoundException(String s) {
    super(s, ERROR_CODE_SECURITY_STORE_NOT_FOUNT);
  }

  public StoreNotFoundException(String s, Throwable throwable) {
    super(s, throwable, ERROR_CODE_SECURITY_STORE_NOT_FOUNT);
  }
}
