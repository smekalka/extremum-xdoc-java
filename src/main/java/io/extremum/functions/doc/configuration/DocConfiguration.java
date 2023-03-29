package io.extremum.functions.doc.configuration;

import io.extremum.functions.doc.service.AnnotationsDocSource;
import io.extremum.functions.doc.service.DocService;
import io.extremum.functions.doc.service.DocSource;
import io.extremum.functions.doc.service.FileSystemDocSource;
import io.extremum.functions.doc.service.translator.Translator;
import io.extremum.functions.doc.service.translator.YandexTranslator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "extremum.doc", name = "enabled", matchIfMissing = true, havingValue = "true")
@ComponentScan("io.extremum.functions.doc")
public class DocConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "extremum.doc.translator.yandex", name = "folderId")
    public Translator translator(
            @Value("${extremum.doc.translator.yandex.folderId}") String folderId,
            @Value("${extremum.doc.translator.yandex.apiKey}") String api_key
    ) {
        return new YandexTranslator(folderId, api_key);
    }

    @Bean
    @ConditionalOnMissingBean
    public Translator dummyTranslator() {
        return (string, language) -> "not translated :(";
    }

    @Bean
    public DocService docService(Translator translator, @Value("${extremum.doc.translator.preferred-lang-to-translate-from:en}") String preferredLangToTranslateFrom, DocSource annotationDocSource, DocSource durableSource) {
        return new DocService(translator, preferredLangToTranslateFrom, annotationDocSource, durableSource);
    }

    @Bean
    public DocSource annotationDocSource(ApplicationContext applicationContext) {
        String name = applicationContext.getBeansWithAnnotation(SpringBootApplication.class)
                .values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Bean annotated with SpringBootApplication not present in context"))
                .getClass()
                .getPackage()
                .getName();

        return new AnnotationsDocSource(applicationContext, name);
    }

    @Bean
    public DocSource durableSource(@Value("${extremum.doc.file}") String path) {
        return new FileSystemDocSource(path);
    }

    @Bean
    public YamlMessageConverter yamlMessageConverter() {
        return new YamlMessageConverter();
    }
}
