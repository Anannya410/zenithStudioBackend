package com.codebrewers.backend.service;

import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.util.*;

@Service
public class JavaCompilerService {

    public String compileJavaCode(String script) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            return "Java compiler not available.";
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        SimpleJavaFileObject fileObject = new SimpleJavaSourceFromString("Main", script);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);

        StringWriter output = new StringWriter();
        boolean success = compiler.getTask(output, fileManager, diagnostics, null, null, compilationUnits).call();

        if (success) {
            return executeJavaCode();
        } else {
            StringBuilder errorOutput = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                errorOutput.append(diagnostic.getMessage(null)).append("\n");
            }
            return errorOutput.toString();
        }
    }

    public String executeJavaCode() {
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
