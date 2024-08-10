package com.codebrewers.backend.controller;

import com.codebrewers.backend.dao.CodeExecutionRequest;
import com.codebrewers.backend.dao.CodeExecutionResponse;
import com.codebrewers.backend.service.JavaCompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/java")
public class JavaCompilerController {

    @Autowired
    private JavaCompilerService javaCompilerService;

    @PostMapping("/execute")
    public CodeExecutionResponse executeJavaCode(@RequestBody CodeExecutionRequest request) {
        CodeExecutionResponse response = new CodeExecutionResponse();

        // Compile and execute the Java code
        String result = javaCompilerService.compileJavaCode(request.getCode());

        response.setOutput(result);

        return response;
    }
}
