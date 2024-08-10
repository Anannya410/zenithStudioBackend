package com.codebrewers.backend.dao;

import lombok.Data;

@Data
public class CodeExecutionRequest {
    private String code;
    private String input;
}
