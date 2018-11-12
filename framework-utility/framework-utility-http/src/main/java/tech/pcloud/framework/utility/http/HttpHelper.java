package tech.pcloud.framework.utility.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpHelper {
    private OkHttpClient okHttpClient;

    public HttpHelper(HttpConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(config.getConnectTimeOut(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeOut(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeOut(), TimeUnit.SECONDS);
        if (config.isTry()) {
            builder.addInterceptor(new TryInterceptor(config.getTryTimes()));
        }
        okHttpClient = builder.build();
    }

    public <T> void get(HttpRequest<T> httpRequest) {
        httpRequest.setMethod(HttpRequest.Method.GET);
        request(httpRequest);
    }

    public <T> void post(HttpRequest<T> httpRequest) {
        httpRequest.setMethod(HttpRequest.Method.POST);
        request(httpRequest);
    }

    public <T> void put(HttpRequest<T> httpRequest) {
        httpRequest.setMethod(HttpRequest.Method.PUT);
        request(httpRequest);
    }

    public <T> void delete(HttpRequest<T> httpRequest) {
        httpRequest.setMethod(HttpRequest.Method.DELETE);
        request(httpRequest);
    }

    public <T> void head(HttpRequest<T> httpRequest) {
        httpRequest.setMethod(HttpRequest.Method.HEAD);
        request(httpRequest);
    }

    public <T> void request(HttpRequest<T> httpRequest) {
        Request.Builder builder = new Request.Builder();
        builder.url(httpRequest.getUrl());
        switch (httpRequest.getMethod()) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(httpRequest.getBody());
                break;
            case PUT:
                builder.put(httpRequest.getBody());
                break;
            case DELETE:
                builder.delete();
                break;
            case OPTION:
                throw new HttpException("Dot support OPTION method.");
            case HEAD:
                builder.head();
                break;
            default:
                builder.get();
        }
        httpRequest.getHeaders()
                .forEach(keyValuePair -> builder.addHeader(keyValuePair.getName(), keyValuePair.getValue()));

        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        if (httpRequest.isAsync()) {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.warn("request " + httpRequest.getUrl() + " fail, " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    log.info("request " + httpRequest.getUrl() + " success");
                    httpRequest.getCallback().response(response);
                }
            });
        } else {
            try {
                Response response = call.execute();
                httpRequest.getCallback().response(response);
            } catch (IOException e) {
                log.warn("request " + httpRequest.getUrl() + " fail, " + e.getMessage());
                throw new HttpException(e.getMessage(), e);
            }
        }
    }

}