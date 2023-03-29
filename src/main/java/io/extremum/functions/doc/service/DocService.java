package io.extremum.functions.doc.service;

import io.extremum.functions.doc.model.Doc;
import io.extremum.functions.doc.model.LocalizedDoc;
import io.extremum.functions.doc.model.LocalizedFunctionDoc;
import io.extremum.functions.doc.service.translator.Translator;
import io.extremum.functions.doc.service.translator.TranslatorException;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DocService {

    private final Doc doc;
    private final Translator translator;
    private final String preferredLanguageToTranslateFrom;
    private final DocSource durableSource;

    public DocService(Translator translator, String preferredLanguageToTranslateFrom, DocSource annotationSource, DocSource durableSource) {
        this.translator = translator;
        Doc durableDoc = durableSource.getDoc();
        if (durableDoc == null) {
            doc = annotationSource.getDoc();
        } else {
            doc = durableDoc;
        }
        this.durableSource = durableSource;

        this.preferredLanguageToTranslateFrom = preferredLanguageToTranslateFrom;
    }

    public LocalizedDoc getDoc(String language) {
        Locale locale = Locale.forLanguageTag(language);
        LocalizedDoc result = new LocalizedDoc();

        String description = toLocalizedString(doc.getDescription(), locale);
        doc.setDescription(locale.getLanguage(), description);
        result.setDescription(description);
        result.set_package(doc.get_package());
        result.setFunctions(
                doc.getFunctions()
                        .stream()
                        .map(functionDoc -> {
                                    String funcDescription = toLocalizedString(functionDoc.getDescription(), locale);
                                    functionDoc.setDescription(locale.getLanguage(), funcDescription);

                                    return new LocalizedFunctionDoc(
                                            functionDoc.getName(),
                                            functionDoc.getContext(),
                                            funcDescription,
                                            functionDoc.getReturnType(), functionDoc.getParameters()
                                    );
                                }
                        )
                        .collect(Collectors.toList()));
        durableSource.setDoc(doc);

        return result;
    }

    private String toLocalizedString(Map<String, String> multiLingual, Locale language) {
        String localizedString = multiLingual.get(language.getLanguage());

        if (localizedString == null) {
            Optional<Map.Entry<String, String>> en = multiLingual.entrySet().stream().filter(
                    multilingualLanguageStringEntry -> multilingualLanguageStringEntry.getKey().startsWith(preferredLanguageToTranslateFrom)
            ).findFirst();
            if (en.isPresent()) {
                try {
                    localizedString = translator.translate(en.get().getValue(), language);
                } catch (TranslatorException exception) {
                    throw new DocumentLanguageException(String.format("Language %s is not supported", language.getLanguage()));
                }
            } else {
                Optional<Map.Entry<String, String>> first = multiLingual.entrySet().stream().findFirst();
                if (!first.isPresent()) {
                    return "";
                }
                try {
                    localizedString = translator.translate(first.get().getValue(), language);
                } catch (TranslatorException exception) {
                    throw new DocumentLanguageException(String.format("Language %s is not supported", language.getLanguage()));
                }
            }
        }

        return localizedString;
    }
}
