package com.codebrewers.backend.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "coding_problem")
public class CodingProblem {

    @Id
    private String id;
    private String title;
    private String description;
    private String constraints;
    private List<TestCase> testCases;
    private List<Example> examples;
    private List<String> tags;
    private List<String> hints;
    private String difficulty;

    @Data
    public static class TestCase{
        private String input;
        private String expectedOutput;
    }

    @Data
    public static class Example{
        private String input;
        private String output;
        private String explanation;
    }
}
