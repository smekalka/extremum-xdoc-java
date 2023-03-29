package io.extremum.functions.doc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonPropertyOrder({"package", "description", "functions"})
public class LocalizedDoc {
    @JsonProperty("package")
    private String _package;
    private String description;
    private List<LocalizedFunctionDoc> functions;
}
