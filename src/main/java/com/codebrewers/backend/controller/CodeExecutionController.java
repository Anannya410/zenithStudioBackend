package com.codebrewers.backend.controller;


import com.codebrewers.backend.dao.CodeExecutionRequest;
import com.codebrewers.backend.dao.CodeExecutionResponse;
import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("/api")
public class CodeExecutionController {

    @PostMapping("/execute")
    public CodeExecutionResponse executeCode(@RequestBody CodeExecutionRequest request) {
        CodeExecutionResponse response = new CodeExecutionResponse();
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        long startTime = System.currentTimeMillis();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        try {
            // Save code to a temporary file
            File codeFile = new File("Test.java");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(codeFile))) {
                writer.write(request.getCode());
            }

            // Compile the code
            Process compileProcess = Runtime.getRuntime().exec(new String[]{"javac", codeFile.getPath()});
            compileProcess.waitFor();

            // Check for compilation errors
            if (compileProcess.exitValue() != 0) {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                }
                response.setError("Compilation failed:\n" + error.toString());
                return response;
            }

            // Execute the compiled code
            String className = "Test";  // Assuming the class name is the same as the file name
            Process execProcess = Runtime.getRuntime().exec(new String[]{"java", className}, null, new File("."));
            try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(execProcess.getOutputStream()));
                 BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(execProcess.getInputStream()));
                 BufferedReader processErrorReader = new BufferedReader(new InputStreamReader(execProcess.getErrorStream()))) {

                // Send input to the process
                processInputWriter.write(request.getInput());
                processInputWriter.newLine();
                processInputWriter.flush();
                processInputWriter.close();

                // Collect output
                String line;
                while ((line = processOutputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // Collect errors
                while ((line = processErrorReader.readLine()) != null) {
                    error.append("ERROR: ").append(line).append("\n");
                }

                execProcess.waitFor();
            }

            // Delete temporary files
            new File("Test.class").delete();
            codeFile.delete();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            response.setError("Error: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Calculate execution time and memory usage
        long executionTime = endTime - startTime;
        long memoryUsage = endMemory - startMemory;

        response.setOutput(output.toString());
        response.setError(error.toString());
        response.setExecutionTime(executionTime);
        response.setMemoryUsage(memoryUsage);

        return response;
    }
}