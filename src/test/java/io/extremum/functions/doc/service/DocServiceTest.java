package io.extremum.functions.doc.service;

import io.extremum.functions.doc.config.FunctionsConfiguration;
import io.extremum.functions.doc.model.LocalizedDoc;
import io.extremum.functions.doc.model.LocalizedFunctionDoc;
import io.extremum.functions.doc.FunctionsPackage;
import java.io.File;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FunctionsPackage.class)
@Import(FunctionsConfiguration.class)
class DocServiceTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void should_get_doc_properly() {
        DocSource annotationsDocSource = new AnnotationsDocSource(applicationContext, "io.extremum.functions");
        File file = new File("src/test/resources/newDoc.yml");
        if(file.exists()){
            file.delete();
        }
        DocSource fileSystemDocSource = new FileSystemDocSource("src/test/resources/newDoc.yml");

        DocService docService = new DocService((string, language) -> "Prova!", "en", annotationsDocSource, fileSystemDocSource);

        LocalizedDoc localizedDoc = docService.getDoc("ru");
        assertEquals("my package", localizedDoc.get_package());
        assertEquals("Мой пакет функций", localizedDoc.getDescription());
        assertEquals(6, localizedDoc.getFunctions().size());
        LocalizedFunctionDoc localizedFunctionDoc = localizedDoc.getFunctions().stream().filter(doc -> doc.getName().equals("collectionFunc")).findFirst().get();
        assertEquals("Создаёт коллекцию данных", localizedFunctionDoc.getDescription());
        assertEquals("collectionFunc", localizedFunctionDoc.getName());

        localizedDoc = docService.getDoc("it");
        assertEquals("my package", localizedDoc.get_package());
        assertEquals("Prova!", localizedDoc.getDescription());
        assertEquals(6, localizedDoc.getFunctions().size());
        localizedFunctionDoc = localizedDoc.getFunctions().stream().filter(doc -> doc.getName().equals("collectionFunc")).findFirst().get();
        assertEquals("Prova!", localizedFunctionDoc.getDescription());
        assertEquals("collectionFunc", localizedFunctionDoc.getName());
        assertEquals("array", localizedFunctionDoc.getReturns());
        assertEquals("Context-collectionfunc", localizedFunctionDoc.getContext());
        assertEquals(new HashMap<String, Object>(){{put("input", "string");}}, localizedFunctionDoc.getParameters());
    }
}