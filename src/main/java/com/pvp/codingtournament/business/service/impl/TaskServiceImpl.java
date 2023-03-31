package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.business.utils.BaseTaskCodeBuilder;
import com.pvp.codingtournament.business.utils.CodeRunner;
import com.pvp.codingtournament.business.utils.impl.BaseTaskCodeBuilderImpl;
import com.pvp.codingtournament.business.utils.impl.CodeRunnerImpl;
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
    public String buildTaskCode(Long taskId) {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (!optionalTaskEntity.isPresent()) {
            throw new TaskNotFoundException();
        }
        TaskEntity taskEntity = optionalTaskEntity.get();
        BaseTaskCodeBuilder baseTaskCodeBuilder = new BaseTaskCodeBuilderImpl();
        baseTaskCodeBuilder.setMethodName(taskEntity.getMethodName());
        baseTaskCodeBuilder.setMethodArgumentTypes(taskEntity.getMethodArgumentTypes());
        baseTaskCodeBuilder.setMethodArguments(taskEntity.getMethodArguments());
        baseTaskCodeBuilder.setReturnType(taskEntity.getReturnType());
        return baseTaskCodeBuilder.build();
    }

    @Override
    public AnalysisResults analyzeCode(Long taskId, String code) throws IOException, InterruptedException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (!optionalTaskEntity.isPresent()) {
            throw new TaskNotFoundException();
        }
        CodeRunner codeRunner = new CodeRunnerImpl();
        codeRunner.setCode(code);
        codeRunner.setInputsAndOutputs(optionalTaskEntity.get().getInputOutput());
       return codeRunner.runCode();
    }
}
