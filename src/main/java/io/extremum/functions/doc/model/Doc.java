package io.extremum.functions.doc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Doc {
    private String _package;
    private Map<String, String> description = new HashMap<>();
    private List<FunctionDoc> functions = new ArrayList<>();

    public void setDescription(String lang, String description) {
        this.description.put(lang, description);
    }
}