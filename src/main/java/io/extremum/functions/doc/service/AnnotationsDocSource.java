package io.extremum.functions.doc.service;

import io.extremum.common.annotation.Localized;
import io.extremum.common.annotation.function.Function;
import io.extremum.common.annotation.function.FunctionContext;
import io.extremum.common.annotation.function.FunctionMethod;
import io.extremum.common.annotation.function.FunctionPackage;
import io.extremum.functions.doc.model.Doc;
import io.extremum.functions.doc.model.FunctionDoc;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;

@Slf4j
public class AnnotationsDocSource implements DocSource {

    public AnnotationsDocSource(ApplicationContext applicationContext, String basePackage) {
        this.applicationContext = applicationContext;
        this.basePackage = basePackage;
        this.doc = scan();
    }

    @Getter
    @Setter
    private Doc doc;
    private final String basePackage;

    private final ApplicationContext applicationContext;

    public Doc scan() {
        Doc doc = new Doc();
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWithFunctionPackage = reflections.getTypesAnnotatedWith(FunctionPackage.class);
        if (typesAnnotatedWithFunctionPackage.size() == 0) {
            return doc;
        }
        if (typesAnnotatedWithFunctionPackage.size() > 1) {
            log.warn("Found more than 1 class annotated with {}. Use the first found to compose docs", FunctionPackage.class);
        }
        Class<?> annotatedWithFunctionPackage = typesAnnotatedWithFunctionPackage.iterator().next();
        FunctionPackage annotation = annotatedWithFunctionPackage.getAnnotation(FunctionPackage.class);
        Map<String, String> collect = Arrays.stream(annotation.description()).collect(Collectors.toMap(Localized::lang, Localized::value));

        doc.set_package(annotation.name());
        doc.setDescription(collect);

        List<FunctionDoc> functionDocs = applicationContext.getBeansWithAnnotation(Function.class)
                .entrySet()
                .stream()
                .map(entry -> {
                    Function functionAnnotation = applicationContext.findAnnotationOnBean(entry.getKey(), Function.class);
                    if (functionAnnotation == null) {
                        throw new IllegalStateException("No Function annotation present");
                    }

                    FunctionDoc functionDoc = new FunctionDoc();
                    functionDoc.setName(functionAnnotation.name());

                    functionDoc.setDescription(
                            Arrays.stream(functionAnnotation.description())
                                    .collect(Collectors.toMap(Localized::lang, Localized::value))
                    );

                    Arrays.stream(entry.getValue().getClass().getDeclaredFields())
                            .filter(field -> field.getAnnotation(FunctionContext.class) != null)
                            .forEach(
                                    field -> {
                                        try {
                                            boolean inaccessible = false;
                                            if (!field.isAccessible()) {
                                                field.setAccessible(true);
                                                inaccessible = true;
                                            }
                                            functionDoc.setContext(field.get(entry.getValue()));
                                            if (inaccessible) {
                                                field.setAccessible(false);
                                            }
                                        } catch (IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            );
                    Arrays.stream(entry.getValue().getClass().getDeclaredMethods())
                            .filter(method -> method.getAnnotation(FunctionMethod.class) != null)
                            .forEach(
                                    method -> {
                                        String returnType = getTypeString(method.getReturnType());
                                        functionDoc.setReturnType(returnType);
                                        functionDoc.setParameters(
                                                Arrays.stream(method.getParameters()).collect(Collectors.toMap(
                                                        Parameter::getName, o -> getTypeString(o.getType())
                                                ))
                                        );
                                    }
                            );

                    return functionDoc;
                })
                .collect(Collectors.toList());
        doc.setFunctions(functionDocs);

        return doc;
    }

    private String getTypeString(Class<?> clazz) {
        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            return "array";
        }
        if (clazz.getName().equals("int") ||
                clazz.getName().equals("long") ||
                clazz.getName().equals("double") ||
                clazz.getName().equals("float") ||
                clazz.getName().equals("short") ||
                clazz.getName().equals("byte") ||
                Number.class.isAssignableFrom(clazz)
        ) {
            return "number";
        }
        if (clazz.equals(String.class)) {
            return "string";
        }
        if (clazz.equals(Boolean.class) || clazz.getName().equals("boolean")) {
            return "boolean";
        }

        return "object";
    }
}

