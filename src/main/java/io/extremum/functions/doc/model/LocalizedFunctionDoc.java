package io.extremum.functions.doc.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalizedFunctionDoc {
    private String name;
    private Object context;
    private String description;
    private String returns;
    private Map<String, Object> parameters;
}
