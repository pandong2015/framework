package tech.pcloud.framework.utility.http;

import okhttp3.Response;

import java.lang.reflect.Type;

public abstract class ResponseCallback<T> {
    private T response;

    void response(Response response) {
        try {
            this.response = doResponse(response);
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), e);
        } finally {
            response.close();
        }
    }

    public T get() {
        return response;
    }

    public abstract T doResponse(Response response) throws HttpException;

}