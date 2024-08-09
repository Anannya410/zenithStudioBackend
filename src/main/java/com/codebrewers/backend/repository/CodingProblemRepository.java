package com.codebrewers.backend.repository;

import com.codebrewers.backend.dao.CodingProblem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingProblemRepository extends MongoRepository<CodingProblem, String> {

    List<CodingProblem> findByTitle(String title);

    @Query("{'$text': {'$search': ?0}}")
    List<CodingProblem> searchByText(String text);
}
