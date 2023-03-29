package io.extremum.functions.doc.functions;

import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;

@Function(name = "arrayfunc", description = {
        @Localized(lang = "ru", value = "Создаёт массив данных"),
        @Localized(lang = "en", value = "Create array of data")
})
public class ArrayFunction {

    @FunctionContext
    public String context = "Context-arrayfunc";

    @FunctionMethod
    public String[] hash(String string) {
        return new String[]{};
    }
}
