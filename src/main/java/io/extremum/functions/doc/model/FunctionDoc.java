package io.extremum.functions.doc.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FunctionDoc {
    private String name;
    private Object context;
    private Map<String, String> description = new HashMap<>();
    private String returnType;
    private Map<String, Object> parameters = new HashMap<>();

    public void setDescription(String lang, String description) {
        this.description.put(lang, description);
    }
}
