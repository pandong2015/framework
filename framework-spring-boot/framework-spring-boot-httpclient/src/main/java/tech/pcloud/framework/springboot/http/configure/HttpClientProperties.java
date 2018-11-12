package tech.pcloud.framework.springboot.http.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tech.pcloud.framework.utility.http.HttpConfig;

@Data
@ConfigurationProperties(prefix = "tech.pcloud.http")
public class HttpClientProperties {
    private HttpConfig config;
}
