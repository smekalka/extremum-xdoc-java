package io.extremum.functions.doc.functions;

import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;
import java.util.Collections;
import java.util.Set;

@Function(name = "collectionFunc", description = {
        @Localized(lang = "ru", value = "Создаёт коллекцию данных"),
        @Localized(lang = "en", value = "Creates collection of data")
})
public class CollectionFunction {

    @FunctionContext
    public String context = "Context-collectionfunc";

    @FunctionMethod
    public Set<String> hash(String input) {
        return Collections.emptySet();
    }
}
