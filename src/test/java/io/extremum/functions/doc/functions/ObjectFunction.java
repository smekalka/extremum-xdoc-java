package io.extremum.functions.doc.functions;


import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;

@Function(name = "objectfunc", description = {
        @Localized(lang = "ru", value = "Складывает два числа"),
        @Localized(lang = "en", value = "Adds two numbers"),
})
public class ObjectFunction {

    @FunctionContext
    public String context = "Context-objectfunc";

    @FunctionMethod
    public SomeValues func(int param1, int param2) {
        return new SomeValues("1", "2");
    }


    public static class SomeValues {
        String val1;
        String val2;

        public SomeValues(String val1, String val2) {
            this.val1 = val1;
            this.val2 = val2;
        }
    }
}