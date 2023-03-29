package io.extremum.functions.doc.service;

import io.extremum.functions.doc.config.FunctionsConfiguration;
import io.extremum.functions.doc.model.Doc;
import io.extremum.functions.doc.model.FunctionDoc;
import io.extremum.functions.doc.FunctionsPackage;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FunctionsPackage.class)
@Import(FunctionsConfiguration.class)
class FileSystemDocSourceTest {

    @Test
    void should_get_doc_properly() {
        DocSource docSource = new FileSystemDocSource("src/test/resources/doc.yml");
        Doc doc = docSource.getDoc();
        assertEquals("my package", doc.get_package());
        assertEquals("Мой пакет функций", doc.getDescription().get("ru"));
        assertEquals("This package is a super functions package created by cool extremum developers", doc.getDescription().get("en"));
        assertEquals(4, doc.getFunctions().size());
        FunctionDoc hashfunc = doc.getFunctions().stream().filter(functionDoc -> functionDoc.getName().equals("hashfunc")).findFirst().get();
        assertEquals("Creates hash of passed object", hashfunc.getDescription().get("en"));
        assertEquals("Хэширует переданный объект", hashfunc.getDescription().get("ru"));
        assertEquals("Context-hashfunc", hashfunc.getContext());
        assertEquals("string", hashfunc.getReturnType());
        assertEquals(new HashMap<String, Object>() {{
            put("p1", "string");
        }}, hashfunc.getParameters());
    }
}