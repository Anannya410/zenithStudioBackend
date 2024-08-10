package com.codebrewers.backend.dao;

import lombok.Data;

@Data
public class JavaExecutionResponse {
    private String output;
    private String error;
    private long compilationTime;
    private long compilationMemoryUsage;
    private long executionTime;
    private long memoryUsage;
}
