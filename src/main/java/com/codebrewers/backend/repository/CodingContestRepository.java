package com.codebrewers.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.codebrewers.backend.dao.CodingContest;

public interface CodingContestRepository extends MongoRepository<CodingContest, String> {
    // Custom query methods if necessary
}
