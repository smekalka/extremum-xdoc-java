package io.extremum.functions.doc.service;

import io.extremum.functions.doc.model.Doc;

public interface DocSource {

    Doc getDoc();

    void setDoc(Doc doc);
}
