package com.codebrewers.backend.dao;

import lombok.Data;

@Data
public class CodeExecutionResponse {
    private String output;
    private String error;
}
