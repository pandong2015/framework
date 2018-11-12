package tech.pcloud.framework.exception;

public class SecurityException extends PcloudException {
    public SecurityException(int code) {
        super(code);
    }

    public SecurityException(String message, int code) {
        super(message, code);
    }

    public SecurityException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public SecurityException(Throwable cause, int code) {
        super(cause, code);
    }

    public SecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
