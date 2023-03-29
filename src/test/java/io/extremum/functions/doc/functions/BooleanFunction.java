package io.extremum.functions.doc.functions;


import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;

@Function(name = "booleanf", description = {
        @Localized(lang = "ru", value = "Складывает два числа"),
        @Localized(lang = "en", value = "Adds two numbers"),
})
public class BooleanFunction {

    @FunctionContext
    public String context = "Context-booleanf";

    @FunctionMethod
    public boolean func(int param1, int param2) {
        return true;
    }
}