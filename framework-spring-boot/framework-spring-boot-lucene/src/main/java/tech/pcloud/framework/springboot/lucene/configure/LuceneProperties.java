package tech.pcloud.framework.springboot.lucene.configure;

import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tech.pcloud.lucene")
public class LuceneProperties {
    private String index;
    private Class<? extends Analyzer> analyzerClass;
}
