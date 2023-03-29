package io.extremum.functions.doc.functions;

import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;

@Function(name = "hashfunc", description = {
        @Localized(lang = "en", value = "Creates hash of passed object"),
        @Localized(lang = "ru", value = "Хэширует переданный объект")
})
public class StringFunction {

    @FunctionContext
    public String context = "Context-hashfunc";

    @FunctionMethod
    public String hash(String p1) {
        return String.valueOf(p1.hashCode());
    }
}
