package com.codebrewers.backend.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Data
@Document(collection = "coding_contests")
public class CodingContest {

    @Id
    private String id;

    private String title;
    private String description;
    private String constraints;
    private List<TestCase> testCases;
    private List<Example> examples;
    private List<String> tags;
    private List<String> hints;
    private String contestName;
    private int timeDuration; // In minutes
    private Date startTime;
    private Date endTime;
    private String prize;
    private Date date;
    private String createdBy;

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
