package com.codebrewers.backend.controller;

import com.codebrewers.backend.dao.CodingProblem;
import com.codebrewers.backend.service.CodingProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins="*")
public class CodingProblemController {

    @Autowired
    private CodingProblemService codingProblemService;

    @PostMapping("/upload")
    public CodingProblem uploadProblem(@RequestBody CodingProblem codingProblem){
        return codingProblemService.saveProblem(codingProblem);
    }

    @GetMapping
    public List<CodingProblem> getAllProblems(){
        return codingProblemService.getAllProblems();
    }

    @GetMapping("/{id}")
    public CodingProblem getProblemById(@PathVariable String id){
        return codingProblemService.getProblemById(id);
    }

    @GetMapping("/search/text")
    public List<CodingProblem> searchProblemsByText(@RequestParam String text) {
        return codingProblemService.searchProblemsByText(text);
    }

}
