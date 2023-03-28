package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.handler.exception.CodeCompilationException;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

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
        String filePath = compileSubmittedCodeJavaFile(codeFile);
        int testCasesPassed = runSubmittedCodeTestCases(taskEntity, filePath);

        return new AnalysisResults(testCasesPassed == taskEntity.getInputOutput().size(),
                                                                taskEntity.getInputOutput().size(),
                                                                testCasesPassed);
    }

    private File createSubmittedCodeJavaFile(String code) throws IOException {
        File codeFile = File.createTempFile("Solution", ".java");
        FileWriter fileWriter = new FileWriter(codeFile);
        fileWriter.write(code);
        fileWriter.close();
        return codeFile;
    }

    private String compileSubmittedCodeJavaFile(File codeFile) throws IOException, InterruptedException {
        String filePath = codeFile.getAbsolutePath();
        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", filePath);
        Process compilationProcess = compilationProcessBuilder.start();
        int exitCode = compilationProcess.waitFor();
        codeFile.delete();

        if (exitCode == 1){
            String error = buildCompilationErrorString(filePath, compilationProcess.errorReader());
            System.out.println(error);
            throw new CodeCompilationException(error);
        }

        return filePath.replace("\\" + codeFile.getName(), "");
    }

    private String buildCompilationErrorString(String filePath, BufferedReader errorOutputReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = errorOutputReader.readLine()) != null){
            stringBuilder.append(line.replace(filePath + ":", ""));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
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
