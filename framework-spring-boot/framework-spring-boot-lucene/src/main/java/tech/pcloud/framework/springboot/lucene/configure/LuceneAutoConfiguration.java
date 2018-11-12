package tech.pcloud.framework.springboot.lucene.configure;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConditionalOnClass({Analyzer.class, IndexWriter.class, IndexReader.class, Query.class, IndexSearcher.class})
@EnableConfigurationProperties(LuceneProperties.class)
@ConditionalOnProperty(prefix = "tech.pcloud.lucene",
        value = "enabled",
        matchIfMissing = true)
public class LuceneAutoConfiguration {
    @Autowired
    private LuceneProperties luceneProperties;

    @Bean(name = "directory")
    @ConditionalOnMissingBean(name = "directory")
    public Directory getDirectory() throws IOException {
        Directory directory = null;
        if (StringUtils.isEmpty(luceneProperties.getIndex())) {
            directory = new RAMDirectory();
        } else {
            Path path = Paths.get(luceneProperties.getIndex());
            directory = FSDirectory.open(path);
        }
        return directory;
    }

    @Bean
    @ConditionalOnMissingBean(Analyzer.class)
    public Analyzer getAnalyzer() throws IllegalAccessException, InstantiationException {
        Analyzer analyzer = null;
        if (luceneProperties.getAnalyzerClass() == null) {
            analyzer = new StandardAnalyzer();
        } else {
            analyzer = luceneProperties.getAnalyzerClass().newInstance();
        }
        return analyzer;
    }

    @Bean
    @ConditionalOnMissingBean(IndexWriter.class)
    public IndexWriter getIndexWriter(@Autowired Directory directory,
                                      @Autowired Analyzer analyzer) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(directory, config);
    }

    @Bean
    @ConditionalOnMissingBean(SearcherManager.class)
    public SearcherManager getSearcherManager(@Autowired IndexWriter indexWriter) throws IOException {
        return new SearcherManager(indexWriter, new SearcherFactory());
    }
}
