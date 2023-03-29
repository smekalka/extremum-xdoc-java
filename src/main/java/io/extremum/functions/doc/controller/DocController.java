package io.extremum.functions.doc.controller;

import io.extremum.functions.doc.model.LocalizedDoc;
import io.extremum.functions.doc.service.DocService;
import io.extremum.sharedmodels.dto.Response;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${extremum.doc.path:xdoc}")
@AllArgsConstructor
@ConditionalOnProperty(prefix = "extremum.doc", name = "enabled", matchIfMissing = true, havingValue = "true")
public class DocController {

    private final DocService docService;

    @GetMapping
    public Object getDoc(@RequestParam(defaultValue = "false") boolean unwrapped,
                         @RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "en") String language) {
        LocalizedDoc docs = docService.getDoc(language);
        if (unwrapped) {
            return docs;
        } else {
            return Response.ok(docs);
        }
    }
}