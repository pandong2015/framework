package tech.pcloud.framework.utility.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
public class TryInterceptor implements Interceptor {
    public static final int DEFAULT_TRY_TIMES = 3;
    public static final int DEFAULT_SLEEP_TIME = 20 * 1000;
    private int tryTimes;
    private int sleepTime;

    public TryInterceptor() {
        this(DEFAULT_TRY_TIMES);
    }

    public TryInterceptor(int tryTimes) {
        this(tryTimes, DEFAULT_SLEEP_TIME);
    }

    public TryInterceptor(int tryTimes, int sleepTime) {
        this.tryTimes = tryTimes;
        this.sleepTime = sleepTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        int requestTimes = 0;
        IOException exception = null;
        while (requestTimes < tryTimes) {
            try {
                requestTimes++;
                return chain.proceed(request);
            } catch (IOException e) {
                log.warn("request times: " + requestTimes + " fail, " + e.getMessage());
                exception = e;
                try {
                    Thread.sleep(requestTimes * sleepTime);
                } catch (InterruptedException e1) {
                }
                continue;
            }
        }
        throw exception;
    }
}
