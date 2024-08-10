package com.codebrewers.backend.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "userSubmissions")
@Data
public class user_submission {

    @Id
    private String id;

    private String contestId;
    private String username;
    private String solution;
    private Date submissionTime;
    private boolean isCorrect;

}
