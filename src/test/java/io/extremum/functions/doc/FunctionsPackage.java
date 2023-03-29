package io.extremum.functions.doc;

import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.FunctionPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@FunctionPackage(description = {
        @Localized(lang = "en", value = "This package is a super functions package created by cool extremum developers"),
        @Localized(lang = "ru", value = "Мой пакет функций"),
}, name = "my package")
@SpringBootApplication
public class FunctionsPackage {
}
