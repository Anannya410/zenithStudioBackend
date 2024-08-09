package com.codebrewers.backend.dao;

import lombok.Data;

@Data
public class JDoodleRequest {
    private String clientId;
    private String clientSecret;
    private String script;
    private String language;
    private String versionIndex;
    private String stdin;

}