package com.pvp.codingtournament.business.utils.impl;

import com.pvp.codingtournament.business.connection.JDoodleConnection;
import com.pvp.codingtournament.business.utils.CodeRunner;
import com.pvp.codingtournament.handler.exception.CodeCompilationException;
import com.pvp.codingtournament.model.AnalysisResults;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CodeRunnerImpl implements CodeRunner {
    private String code;
    private ArrayList<String[]> inputsAndOutputs;
    private final JDoodleConnection jDoodleConnection;

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setInputsAndOutputs(ArrayList<String[]> inputsAndOutputs) {
        this.inputsAndOutputs = inputsAndOutputs;
    }

    @Override
    public AnalysisResults runCode(String language) throws IOException, InterruptedException {
        return runSubmittedCodeTestCases(language);
    }

    private AnalysisResults runSubmittedCodeTestCases(String language) throws IOException, InterruptedException {
        int testCasesPassed = 0;
        double averageCpuTime = 0;
        int memoryInKilobytes = 0;

        for (int i = 0; i < inputsAndOutputs.size(); i++) {
            String testInput = inputsAndOutputs.get(i)[0];
            String testOutput = inputsAndOutputs.get(i)[1];
            String results = jDoodleConnection.executeCode(code, language, testInput);
            JSONObject resultsJson = new JSONObject(results);
            validateOutput(resultsJson, language);
            String codeOutput = resultsJson.getString("output");
            double cpuTime = resultsJson.getDouble("cpuTime");
            averageCpuTime += cpuTime;
            memoryInKilobytes = resultsJson.getInt("memory");
            codeOutput = codeOutput.replace("\n", "");

            if (codeOutput.equals(testOutput)){
                testCasesPassed++;
            }
        }
        averageCpuTime /= inputsAndOutputs.size();
        return new AnalysisResults(inputsAndOutputs.size() == testCasesPassed,
                inputsAndOutputs.size(),
                testCasesPassed,
                memoryInKilobytes,
                averageCpuTime);
    }

    private void validateOutput(JSONObject resultsJson, String language) {
        if (resultsJson.getString("output").contains("error")){
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String fileExtension = "";
            switch (language){
                case "java":
                    fileExtension = ".java";
                    break;
                case "python":
                    fileExtension = ".py";
                    break;
                case "javascript":
                    fileExtension = ".js";
                    break;
            }
            String fileName = "/" + "Solution" + username + fileExtension;
            String output = resultsJson.getString("output").replaceAll(fileName, "Line");
            throw new CodeCompilationException(output.toString());
        }
    }
}
