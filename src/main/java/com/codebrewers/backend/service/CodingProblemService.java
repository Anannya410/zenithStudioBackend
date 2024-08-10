package com.codebrewers.backend.service;

import com.codebrewers.backend.dao.CodingProblem;
import com.codebrewers.backend.repository.CodingProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodingProblemService {

    @Autowired
    private CodingProblemRepository codingProblemRepository;

    public CodingProblem saveProblem(CodingProblem codingProblem) {
        return codingProblemRepository.save(codingProblem);
    }

    public List<CodingProblem> getAllProblems() {
        return codingProblemRepository.findAll();
    }

    public CodingProblem getProblemById(String id) {
        return codingProblemRepository.findById(id).orElse(null);
    }

    public List<CodingProblem> searchProblemsByText(String text) {
        return codingProblemRepository.searchByText(text);
    }
}
