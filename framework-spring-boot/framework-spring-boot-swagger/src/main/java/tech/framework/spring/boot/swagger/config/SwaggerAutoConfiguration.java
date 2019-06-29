package tech.framework.spring.boot.swagger.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @ClassName SwaggerConfig
 * @Author pandong
 * @Date 2018/12/4 17:51
 **/
@Configuration
@EnableSwagger2
@EnableWebMvc
@ConditionalOnClass({Docket.class})
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "spring.swagger",
        value = "enabled",
        matchIfMissing = true)
public class SwaggerAutoConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnClass(Docket.class)
    public Docket api() {
        if(swaggerProperties.isClose()){
            return  new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.none())
                    .paths(PathSelectors.none())
                    .build().apiInfo(apiEndPointsInfo());
        }
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getControllerPackage()))
                .paths(PathSelectors.any())
                .build().apiInfo(apiEndPointsInfo());
        if (swaggerProperties.isHasAuth()) {
            List<ApiKey> securityContexts = Lists.newArrayList(new ApiKey("X-User-Sign", "X-User-Sign", "header"),
                    new ApiKey("X-Tenant-Id", "X-Tenant-Id", "header"));
            docket.securitySchemes(securityContexts);
        }
        return docket;
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getUserName(), swaggerProperties.getUrl(), swaggerProperties.getEmail()))
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .version(swaggerProperties.getVersion())
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
