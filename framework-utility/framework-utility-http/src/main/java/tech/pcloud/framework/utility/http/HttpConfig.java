package tech.pcloud.framework.utility.http;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpConfig {
    private boolean isTry = true;
    private int tryTimes = 3;
    private long connectTimeOut = 20;
    private long readTimeOut = 20;
    private long writeTimeOut = 20;
}
