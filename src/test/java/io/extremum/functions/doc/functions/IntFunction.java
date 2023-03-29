package io.extremum.functions.doc.functions;


import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;

@Function(name = "abfunc", description = {
        @Localized(lang = "ru", value = "Складывает два числа"),
        @Localized(lang = "en", value = "Adds two numbers"),
})
public class IntFunction {

    @FunctionContext
    public String context = "Context-abfunc";

    @FunctionMethod
    public int func(int param1, int param2) {
        return param1 + param2;
    }
}