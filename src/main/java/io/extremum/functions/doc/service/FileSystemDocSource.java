package io.extremum.functions.doc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.extremum.functions.doc.model.Doc;
import java.io.File;
import lombok.Getter;
import lombok.SneakyThrows;

public class FileSystemDocSource implements DocSource {

    private final ObjectMapper objectMapper;
    @Getter
    private Doc doc;
    private final String path;

    public FileSystemDocSource(String path) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.doc = load(path);
        this.path = path;
    }

    @SneakyThrows
    public void setDoc(Doc doc) {
        this.doc = doc;
        objectMapper.writeValue(new File(path), doc);
    }

    @SneakyThrows
    public Doc load(String path) {
        File file = new File(path);
        if (file.exists()) {
            return objectMapper.readValue(file, Doc.class);
        } else {
            return null;
        }
    }
}
