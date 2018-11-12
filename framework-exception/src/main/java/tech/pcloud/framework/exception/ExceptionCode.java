package tech.pcloud.framework.exception;

public enum ExceptionCode {
    DIGEST_FAIL(1000),
    SECURITY_FAIL(2000)
    ;

    private int code;
    ExceptionCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }


}
