package tech.pcloud.framework.exception;

import lombok.Data;

@Data
public class PcloudException extends RuntimeException {
    private int code;

    public PcloudException(int code) {
        this.code = code;
    }

    public PcloudException(String message, int code) {
        super(message);
        this.code = code;
    }

    public PcloudException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public PcloudException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public PcloudException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
