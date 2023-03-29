package io.extremum.functions.doc.service.translator;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class YandexTranslator implements Translator {

    private static final String API_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";

    public YandexTranslator(String folderId, String api_key) {
        this.folderId = folderId;
        API_KEY = api_key;
        okHttpClient = new OkHttpClient();
    }

    private final String folderId;
    private final String API_KEY;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String translate(String string, Locale language) throws TranslatorException {
        if (string.isEmpty()) {
            return Strings.EMPTY;
        }

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader(HttpHeaders.AUTHORIZATION, String.format("Api-Key %s", API_KEY))
                .post(RequestBody.create(com.squareup.okhttp.MediaType.parse(MediaType.APPLICATION_JSON.getType()),
                        objectMapper.writeValueAsString(new TranslateRequest(
                                folderId, Collections.singletonList(string), language.getLanguage()
                        ))))
                .build();

        Call call = okHttpClient.newCall(request);
        Response execute = call.execute();
        try (ResponseBody responseBody = execute.body()) {
            TranslationResponse translationResponse = getTranslationResponse(responseBody);
            return translationResponse.translations.get(0).text;
        }
    }

    private TranslationResponse getTranslationResponse(ResponseBody responseBody) throws IOException, TranslatorException {
        try {
            return objectMapper.readValue(responseBody.string(), TranslationResponse.class);
        } catch (JacksonException exception) {
            throw new TranslatorException("Unable to translate");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class TranslateRequest {
        private String folderId;
        private List<String> texts;
        private String targetLanguageCode;
    }


    @NoArgsConstructor
    @Data
    private static class TranslationResponse {
        private List<Translation> translations;
    }

    @NoArgsConstructor
    @Data
    private static class Translation {
        private String text;
        private String detectedLanguageCode;
    }
}
