package tech.pcloud.framework.exception;

public class DigestException extends PcloudException {
    public DigestException() {
        this(ExceptionCode.DIGEST_FAIL.getCode());
    }

    public DigestException(int code) {
        super(code);
    }

    public DigestException(String message) {
        this(message, ExceptionCode.DIGEST_FAIL.getCode());
    }

    public DigestException(String message, int code) {
        super(message, code);
    }

    public DigestException(String message, Throwable cause) {
        this(message, cause, ExceptionCode.DIGEST_FAIL.getCode());
    }

    public DigestException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public DigestException(Throwable cause) {
        this(cause, ExceptionCode.DIGEST_FAIL.getCode());
    }

    public DigestException(Throwable cause, int code) {
        super(cause, code);
    }

}
