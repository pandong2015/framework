package tech.pcloud.framework.springboot.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.pcloud.framework.utility.http.RestResult;

import java.io.IOException;

public class BasicController {

    public static final Integer RESULT_CODE_SUCCESS = 0;

    @CrossOrigin({"*"})
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult exception(Exception e) throws IOException {
        Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = e.getMessage();
        return this.fail(status, message);
    }

    public RestResult success() {
        return success(null);
    }

    public RestResult success(String message) {
        return success(message, null);
    }

    public <T> RestResult<T> success(T t) {
        return success(null, t);
    }

    public <T> RestResult<T> success(String message, T t) {
        return result(RESULT_CODE_SUCCESS, message, t);
    }

    public RestResult fail(int code, String message) {
        return fail(code, message, null);
    }

    public <T> RestResult<T> fail(int code, String message, T t) {
        return result(code, message, t);
    }

    private <T> RestResult<T> result(int code, String message, T t) {
        return (RestResult<T>) RestResult.builder().code(code).message(message).data(t).build();
    }
}
