package tech.pcloud.framework.utility.http;

import lombok.Data;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class HttpRequest<T> {
    public enum Method {
        GET, POST, PUT, DELETE, OPTION, HEAD
    }

    private Method method;
    private String url;
    private List<KeyValuePair> headers = new ArrayList<>();
    private RequestBody body;
    private MediaType mediaType;
    private boolean async;
    private ResponseCallback<T> callback;

    public T getResponse() {
        return callback.get();
    }

    public void addParameter(KeyValuePair keyValuePair) {
        addParameter(keyValuePair.getName(), keyValuePair.getValue());
    }

    public void addParameter(String name, String value) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url is null");
        }
        if ("?".indexOf(url) != -1) {
            url = url + "&" + name + "=" + value;
        } else {
            url = url + "?" + name + "=" + value;
        }
    }

    public void addHeader(String name, String value) {
        headers.add(KeyValuePair.builder().name(name).value(value).build());
    }

    public void addHeader(KeyValuePair keyValuePair) {
        headers.add(keyValuePair);
    }

    public void setBody(MediaType mediaType, String body) {
        this.mediaType = mediaType;
        this.body = RequestBody.create(mediaType, body);
    }

    public void setBody(MediaType mediaType, File body) {
        this.mediaType = mediaType;
        this.body = RequestBody.create(mediaType, body);
    }

    public void setBody(MediaType mediaType, byte[] body) {
        this.mediaType = mediaType;
        this.body = RequestBody.create(mediaType, body);
    }

}
