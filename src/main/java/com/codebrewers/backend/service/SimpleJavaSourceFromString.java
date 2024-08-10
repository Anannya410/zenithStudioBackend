package com.codebrewers.backend.service;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class SimpleJavaSourceFromString extends SimpleJavaFileObject {
    private final String code;

    public SimpleJavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
