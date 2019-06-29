package tech.framework.spring.boot.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.swagger")
public class SwaggerProperties {
    private String title;
    private String description;
    private String userName;
    private String url;
    private String email;
    private String license;
    private String licenseUrl;
    private String version;
    private String controllerPackage;
    private boolean hasAuth;
    private boolean close;
}
