package tech.pcloud.framework.springboot.http.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.pcloud.framework.utility.http.HttpConfig;
import tech.pcloud.framework.utility.http.HttpHelper;

@Configuration
@ConditionalOnClass({HttpConfig.class, HttpHelper.class})
@EnableConfigurationProperties(HttpClientProperties.class)
@ConditionalOnProperty(prefix = "tech.pcloud.http",
        value = "enabled",
        matchIfMissing = true)
public class HttpClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(HttpHelper.class)
    public HttpHelper getHttpHelper(@Autowired HttpClientProperties properties) {
        return new HttpHelper(properties.getConfig());
    }
}
