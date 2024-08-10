package com.codebrewers.backend.controller;

import com.codebrewers.backend.dao.*;
import com.codebrewers.backend.exception.ContestNotFoundException;
import com.codebrewers.backend.repository.CodingContestRepository;
import com.codebrewers.backend.repository.UserSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = "*")
public class CodingContestController {

    @Autowired
    private CodingContestRepository contestRepository;

    @Autowired
    private UserSubmissionRepository submissionRepository;

    @Autowired
    private CodeExecutionController codeExecutionController;

    @PostMapping("/upload")
    public CodingContest uploadContest(@RequestBody CodingContest contest) {
        return contestRepository.save(contest);
    }

    @GetMapping("/all")
    public List<CodingContest> getAllContests() {
        return contestRepository.findAll();
    }

    @GetMapping("/{contestId}")
    public CodingContest getContestDetails(@PathVariable String contestId) {
        return contestRepository.findById(contestId)
                .orElseThrow(() -> new ContestNotFoundException("Contest with ID " + contestId + " not found"));
    }

    @PostMapping("/{contestId}/submit")
    public SubmissionResponse submitSolution(@PathVariable String contestId, @RequestBody SubmissionRequest request) {

        CodingContest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ContestNotFoundException("Contest with ID " + contestId + " not found"));

        boolean isCorrect = true;

        user_submission submission = new user_submission();
        submission.setContestId(contestId);
        submission.setUsername(request.getUsername());
        submission.setSolution(request.getSolution());
        submission.setSubmissionTime(new Date());
        submission.setCorrect(isCorrect);

        submissionRepository.save(submission);


        return new SubmissionResponse("Solution submitted successfully");
    }

    @GetMapping("/{contestId}/leaderboard")
    public List<user_submission> getLeaderboard(@PathVariable String contestId) {

        CodingContest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ContestNotFoundException("Contest with ID " + contestId + " not found"));

        List<user_submission> submissions = submissionRepository.findByContestIdAndSubmissionTimeBetween(
                contestId, contest.getStartTime(), contest.getEndTime());

        List<user_submission> distinctSubmissions = submissions.stream()
                .sorted(Comparator.comparing(user_submission::getSubmissionTime))
                .filter(distinctByKey(user_submission::getUsername))
                .collect(Collectors.toList());


        return distinctSubmissions.stream().limit(3).collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
