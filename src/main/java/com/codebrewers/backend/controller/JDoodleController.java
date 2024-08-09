package com.codebrewers.backend.controller;

import com.codebrewers.backend.dao.JDoodleRequest;
import com.codebrewers.backend.dao.JDoodleResponse;
import com.codebrewers.backend.service.JDoodleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class JDoodleController {

    @Autowired
    private JDoodleService jdoodleService;

    @PostMapping("/compile")
    public JDoodleResponse executeScript(@RequestBody JDoodleRequest jdoodleRequest) {
        return jdoodleService.executeScript(jdoodleRequest);
    }
}