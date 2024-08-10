package com.codebrewers.backend.controller;


import com.codebrewers.backend.dao.CodeExecutionRequest;
import com.codebrewers.backend.dao.CodeExecutionResponse;
import org.springframework.web.bind.annotation.*;
import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CodeExecutionController {

    @PostMapping("/execute")
    public CodeExecutionResponse executeCode(@RequestBody CodeExecutionRequest request) {
        CodeExecutionResponse response = new CodeExecutionResponse();
        StringWriter output = new StringWriter();
        StringWriter error = new StringWriter();

        long startTime = System.currentTimeMillis();

        try {
            // Create an in-memory Java source file
            String className = "Test";
            String code = request.getCode();
            JavaFileObject javaFile = new InMemoryJavaFileObject(className, code);

            // Set up the compiler and the diagnostic collector
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            // Compile the code
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, Arrays.asList(javaFile));
            boolean success = task.call();

            // Check for compilation errors
            if (!success) {
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    error.append("Error on line ").append(String.valueOf(diagnostic.getLineNumber()))
                            .append(": ").append(diagnostic.getMessage(null)).append("\n");
                }
                response.setError(error.toString());
                return response;
            }

            // Load the compiled class and execute the main method
            InMemoryClassLoader classLoader = new InMemoryClassLoader();
            Class<?> cls = classLoader.loadClass(className);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            PrintStream oldOut = System.out;

            System.setOut(printStream);  // Redirect standard output
            cls.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
            System.setOut(oldOut);  // Restore standard output

            output.write(byteArrayOutputStream.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setError("Execution error: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        response.setOutput(output.toString());
        response.setExecutionTime(executionTime);
        response.setMemoryUsage(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        return response;
    }

    // In-memory JavaFileObject
    private static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        protected InMemoryJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name + ".java"), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    // In-memory ClassLoader
    private static class InMemoryClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classBytes = loadClassData(name);
            return defineClass(name, classBytes, 0, classBytes.length);
        }

        private byte[] loadClassData(String className) throws ClassNotFoundException {
            // Implement loading the class bytes from the compiled in-memory class.
            // This would involve using the file manager or handling the class data in memory.
            throw new ClassNotFoundException("Class not found: " + className);
        }
    }
}


//@RestController
//@RequestMapping("/api")
//public class CodeExecutionController {
//
//    @PostMapping("/execute")
//    public CodeExecutionResponse executeCode(@RequestBody CodeExecutionRequest request) {
//        CodeExecutionResponse response = new CodeExecutionResponse();
//        StringBuilder output = new StringBuilder();
//        StringBuilder error = new StringBuilder();
//
//        long startTime = System.currentTimeMillis();
//        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//
//        try {
//            // Save code to a temporary file
//            File codeFile = new File("Test.java");
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(codeFile))) {
//                writer.write(request.getCode());
//            }
//
//            // Compile the code
//            Process compileProcess = Runtime.getRuntime().exec(new String[]{"javac", codeFile.getPath()});
//            compileProcess.waitFor();
//
//            // Check for compilation errors
//            if (compileProcess.exitValue() != 0) {
//                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))) {
//                    String line;
//                    while ((line = errorReader.readLine()) != null) {
//                        error.append(line).append("\n");
//                    }
//                }
//                response.setError("Compilation failed:\n" + error.toString());
//                return response;
//            }
//
//            // Execute the compiled code
//            String className = "Test";  // Assuming the class name is the same as the file name
//            Process execProcess = Runtime.getRuntime().exec(new String[]{"java", className}, null, new File("."));
//            try (BufferedWriter processInputWriter = new BufferedWriter(new OutputStreamWriter(execProcess.getOutputStream()));
//                 BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(execProcess.getInputStream()));
//                 BufferedReader processErrorReader = new BufferedReader(new InputStreamReader(execProcess.getErrorStream()))) {
//
//                // Send input to the process
//                processInputWriter.write(request.getInput());
//                processInputWriter.newLine();
//                processInputWriter.flush();
//                processInputWriter.close();
//
//                // Collect output
//                String line;
//                while ((line = processOutputReader.readLine()) != null) {
//                    output.append(line).append("\n");
//                }
//
//                // Collect errors
//                while ((line = processErrorReader.readLine()) != null) {
//                    error.append("ERROR: ").append(line).append("\n");
//                }
//
//                execProcess.waitFor();
//            }
//
//            // Delete temporary files
//            new File("Test.class").delete();
//            codeFile.delete();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            response.setError("Error: " + e.getMessage());
//        }
//
//        long endTime = System.currentTimeMillis();
//        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//
//        // Calculate execution time and memory usage
//        long executionTime = endTime - startTime;
//        long memoryUsage = endMemory - startMemory;
//
//        response.setOutput(output.toString());
//        response.setError(error.toString());
//        response.setExecutionTime(executionTime);
//        response.setMemoryUsage(memoryUsage);
//
//        return response;
//    }
//}