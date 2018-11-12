package tech.pcloud.framework.utility.http;

import tech.pcloud.framework.exception.PcloudException;

public class HttpException extends PcloudException {
    public static final int HTTP_ERROR_CODE = 10000;

    public HttpException() {
        super(HTTP_ERROR_CODE);
    }

    public HttpException(String message) {
        super(message, HTTP_ERROR_CODE);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause, HTTP_ERROR_CODE);
    }

    public HttpException(Throwable cause) {
        super(cause, HTTP_ERROR_CODE);
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, HTTP_ERROR_CODE);
    }
}
