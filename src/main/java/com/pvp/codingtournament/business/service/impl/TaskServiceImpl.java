package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.business.utils.BaseTaskCodeBuilder;
import com.pvp.codingtournament.business.utils.CodeRunner;
import com.pvp.codingtournament.business.utils.impl.BaseTaskCodeBuilderImpl;
import com.pvp.codingtournament.business.mapper.TaskMapStruct;
import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.task.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapStruct taskMapper;
    private final CodeRunner codeRunner;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        TaskEntity taskEntity = taskMapper.dtoToEntity(taskDto);
        UserEntity entity = userRepository.findByUsername(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).get();
        taskEntity.setUser(entity);
        taskRepository.save(taskEntity);
        return taskMapper.entityToDto(taskEntity);
    }

    @Override
    public TaskDto editTask(Long taskId, TaskDto taskDto) {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (optionalTaskEntity.isEmpty()){
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }

        TaskEntity taskEntity = optionalTaskEntity.get();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!taskEntity.getUser().getUsername().equals(username)){
            throw new SecurityException("Only the task creator can edit this task");
        }

        TaskEntity editedTaskEntity = taskMapper.dtoToEntity(taskDto);
        editedTaskEntity.setId(taskEntity.getId());
        editedTaskEntity.setTournaments(taskEntity.getTournaments());
        editedTaskEntity.setUser(taskEntity.getUser());

        return taskMapper.entityToDto(taskRepository.save(editedTaskEntity));
    }

    @Override
    public String buildTaskCode(Long taskId) {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (optionalTaskEntity.isEmpty()) {
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }
        TaskEntity taskEntity = optionalTaskEntity.get();
        BaseTaskCodeBuilder baseTaskCodeBuilder = new BaseTaskCodeBuilderImpl();
        baseTaskCodeBuilder.setMethodName(taskEntity.getMethodName());
        baseTaskCodeBuilder.setMethodArgumentTypes(taskEntity.getMethodArgumentTypes());
        baseTaskCodeBuilder.setMethodArguments(taskEntity.getMethodArguments());
        baseTaskCodeBuilder.setReturnType(taskEntity.getReturnType());
        baseTaskCodeBuilder.setLanguage(taskEntity.getLanguage());
        return baseTaskCodeBuilder.build();
    }

    @Override
    public TaskDto getById(Long taskId) {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (optionalTaskEntity.isEmpty()){
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }
        return taskMapper.entityToDto(optionalTaskEntity.get());
    }

    @Override
    public AnalysisResults analyzeCode(Long taskId, String code) throws IOException, InterruptedException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (!optionalTaskEntity.isPresent()) {
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }
        TaskEntity taskEntity = optionalTaskEntity.get();
        codeRunner.setCode(code);
        codeRunner.setInputsAndOutputs(taskEntity.getInputOutput());
        return codeRunner.runCode(taskEntity.getLanguage());
    }
}
