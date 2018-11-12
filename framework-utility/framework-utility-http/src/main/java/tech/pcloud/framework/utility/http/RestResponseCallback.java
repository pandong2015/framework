package tech.pcloud.framework.utility.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import okhttp3.Response;

import java.io.IOException;

public class RestResponseCallback<T> extends ResponseCallback<T> {
    @Override
    public T doResponse(Response response) throws HttpException {
        try {
            return JSON.parseObject(response.body().string(), new TypeReference<T>(){});
        } catch (IOException e) {
            throw new HttpException(e.getMessage(), e);
        }
    }
}
