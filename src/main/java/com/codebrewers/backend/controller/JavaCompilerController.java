package com.codebrewers.backend.controller;

import com.codebrewers.backend.dao.CodeExecutionRequest;
import com.codebrewers.backend.dao.CodeExecutionResponse;
import com.codebrewers.backend.dao.JavaExecutionResponse;
import com.codebrewers.backend.service.JavaCompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/java")
@CrossOrigin(origins = "*")
public class JavaCompilerController {

    @Autowired
    private JavaCompilerService javaCompilerService;

    @PostMapping("/execute")
    public JavaExecutionResponse executeJavaCode(@RequestBody CodeExecutionRequest request) {
        // Compile and execute the Java code
        return javaCompilerService.compileJavaCode(request.getCode());
    }
}