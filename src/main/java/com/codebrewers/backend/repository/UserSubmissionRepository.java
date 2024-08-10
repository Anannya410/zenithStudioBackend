package com.codebrewers.backend.repository;

import com.codebrewers.backend.dao.user_submission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface UserSubmissionRepository extends MongoRepository<user_submission, String> {
    List<user_submission> findByContestIdAndUsername(String contestId, String username);

    List<user_submission> findByContestIdAndSubmissionTimeBetween(String contestId, Date startTime, Date endTime);

}
