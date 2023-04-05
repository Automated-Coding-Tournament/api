package com.pvp.codingtournament.business.utils.impl;

import com.pvp.codingtournament.business.utils.CodeRunner;
import com.pvp.codingtournament.handler.exception.CodeCompilationException;
import com.pvp.codingtournament.model.AnalysisResults;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeRunnerImpl implements CodeRunner {
    private String code;
    private ArrayList<String[]> inputsAndOutputs;

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setInputsAndOutputs(ArrayList<String[]> inputsAndOutputs) {
        this.inputsAndOutputs = inputsAndOutputs;
    }

    @Override
    public AnalysisResults runCode() throws IOException, InterruptedException {
        File codeFile = createSubmittedCodeJavaFile(code);
        String filePath = compileAndDeleteSubmittedCodeJavaFile(codeFile);
        int testCasesPassed = runSubmittedCodeTestCases(inputsAndOutputs, filePath);
        return new AnalysisResults(testCasesPassed == inputsAndOutputs.size(),
                inputsAndOutputs.size(),
                testCasesPassed);
    }

    private File createSubmittedCodeJavaFile(String code) throws IOException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        File codeFile = File.createTempFile("Solution" + username, ".java");
        FileWriter fileWriter = new FileWriter(codeFile);
        fileWriter.write(code);
        fileWriter.close();
        return codeFile;
    }

    private String compileAndDeleteSubmittedCodeJavaFile(File codeFile) throws IOException, InterruptedException {
        String filePath = codeFile.getAbsolutePath();
        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", filePath);
        Process compilationProcess = compilationProcessBuilder.start();
        int exitCode = compilationProcess.waitFor();
        codeFile.delete();

        if (exitCode == 1) {
            String error = buildCompilationErrorString(filePath, compilationProcess.errorReader());
            System.out.println(error);
            throw new CodeCompilationException(error);
        }

        return filePath.replace("\\" + codeFile.getName(), "");
    }

    private String buildCompilationErrorString(String filePath, BufferedReader errorOutputReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = errorOutputReader.readLine()) != null) {
            stringBuilder.append(line.replace(filePath + ":", ""));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private int runSubmittedCodeTestCases(ArrayList<String[]> inputsAndOutputs, String filePath) throws IOException, InterruptedException {
        int testCasesPassed = 0;
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String className = "Solution" + username;

        for (int i = 0; i < inputsAndOutputs.size(); i++) {
            String input = inputsAndOutputs.get(i)[0];
            String[] inputArray = input.split(";");
            String output = inputsAndOutputs.get(i)[1];
            List<String> command = buildProcessCommand(filePath, className, inputArray);
            ProcessBuilder codeRunnerBuilder = new ProcessBuilder(command);
            Process codeRunner = codeRunnerBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(codeRunner.getInputStream()));
            String resultLine;
            while ((resultLine = reader.readLine()) != null) {
                if (resultLine.equals(output)) {
                    testCasesPassed++;
                }
            }
            codeRunner.waitFor();
        }

        Files.delete(Path.of(filePath + "\\" + className + ".class"));
        return testCasesPassed;
    }

    private List<String> buildProcessCommand(String filePath, String className, String[] inputArray) {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(filePath);
        command.add(className);
        command.addAll(Arrays.asList(inputArray));
        return command;
    }
}
