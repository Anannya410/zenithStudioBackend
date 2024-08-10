package com.codebrewers.backend.service;

import com.codebrewers.backend.dao.JavaExecutionResponse;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.util.*;

@Service
public class JavaCompilerService {

    public JavaExecutionResponse compileJavaCode(String script) {
        JavaExecutionResponse response = new JavaExecutionResponse();
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        long compileStartTime = System.currentTimeMillis();
        long compileStartMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            response.setError("Java compiler not available.");
            return response;
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        SimpleJavaFileObject fileObject = new SimpleJavaSourceFromString("Main", script);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);

        StringWriter compileOutput = new StringWriter();
        boolean success = compiler.getTask(compileOutput, fileManager, diagnostics, null, null, compilationUnits).call();

        long compileEndTime = System.currentTimeMillis();
        long compileEndMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long compileTime = compileEndTime - compileStartTime;
        long compileMemoryUsage = compileEndMemory - compileStartMemory;

        if (success) {
            // Compilation successful, now execute the code
            String executionOutput = executeJavaCode();
            response.setOutput(executionOutput);
        } else {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                error.append(diagnostic.getMessage(null)).append("\n");
            }
            response.setError("Compilation failed:\n" + error.toString());
        }

        long endTime = System.currentTimeMillis();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long executionTime = endTime - compileEndTime;
        long memoryUsage = endMemory - compileEndMemory;

        response.setCompilationTime(compileTime);
        response.setCompilationMemoryUsage(compileMemoryUsage);
        response.setExecutionTime(executionTime);
        response.setMemoryUsage(memoryUsage);

        return response;
    }

    private String executeJavaCode() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "Main");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            return output.toString();
        } catch (IOException | InterruptedException e) {
            return "Error executing Java code: " + e.getMessage();
        }
    }
}