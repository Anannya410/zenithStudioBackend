package com.codebrewers.backend.dao;

import lombok.Data;

@Data
public class JDoodleResponse {
    private String output;
    private String error;
    private int statusCode;
    private String memory;
    private String cpuTime;
    private String compilationStatus;
    private String projectKey;
    private boolean isExecutionSuccess;
    private boolean isCompiled;
}
