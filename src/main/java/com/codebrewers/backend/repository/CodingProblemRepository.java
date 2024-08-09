package com.codebrewers.backend.repository;

import com.codebrewers.backend.dao.CodingProblem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodingProblemRepository extends MongoRepository<CodingProblem, String> {
}
