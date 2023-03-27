package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.handler.exception.TaskNotFoundException;
import com.pvp.codingtournament.mapper.TaskMapStruct;
import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapStruct taskMapper;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        TaskEntity taskEntity = taskMapper.dtoToEntity(taskDto);
        UserEntity entity = userRepository.findByUsername(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).get();
        taskEntity.setUser(entity);
        taskRepository.save(taskEntity);
        return taskMapper.entityToDto(taskEntity);
    }

    @Override
    public AnalysisResults analyzeCode(Long taskId, String code) throws IOException, InterruptedException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);

        if (!optionalTaskEntity.isPresent()){
            throw new TaskNotFoundException();
        }

        TaskEntity taskEntity = optionalTaskEntity.get();
        File codeFile = createSubmittedCodeJavaFile(code);
        String filePath = codeFile.getAbsolutePath();

        compileSubmittedCodeJavaFile(filePath);
        codeFile.delete();

        filePath = filePath.replace("\\" + codeFile.getName(), "");

        int testCasesPassed = runSubmittedCodeTestCases(taskEntity, filePath);

        AnalysisResults analysisResults = new AnalysisResults();
        analysisResults.setPassed(testCasesPassed == taskEntity.getInputOutput().size());
        analysisResults.setPassedTestCases(testCasesPassed);
        analysisResults.setTotalTestCases(taskEntity.getInputOutput().size());

        return analysisResults;
    }

    private File createSubmittedCodeJavaFile(String code) throws IOException {
        File codeFile = File.createTempFile("Solution", ".java");
        FileWriter fileWriter = new FileWriter(codeFile);
        fileWriter.write(code);
        fileWriter.close();
        return codeFile;
    }

    private void compileSubmittedCodeJavaFile(String filePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("javac", filePath);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int exitCode = process.waitFor();
        System.out.println("Code compilation process exited with code " + exitCode);
    }

    private int runSubmittedCodeTestCases(TaskEntity taskEntity, String filePath) throws IOException, InterruptedException {
        ArrayList<String[]> inputOutputList = taskEntity.getInputOutput();
        int testCasesPassed = 0;

        for (int i = 0; i < inputOutputList.size(); i++) {
            String input = inputOutputList.get(i)[0];
            String output = inputOutputList.get(i)[1];
            ProcessBuilder codeRunnerBuilder = new ProcessBuilder("java", "-cp", filePath, "Solution", input);
            Process codeRunner = codeRunnerBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(codeRunner.getInputStream()));
            String resultLine;

            while ((resultLine = reader.readLine()) != null) {
                if(resultLine.equals(output)){
                    testCasesPassed++;
                }
            }
            codeRunner.waitFor();
        }
        return testCasesPassed;
    }
}
