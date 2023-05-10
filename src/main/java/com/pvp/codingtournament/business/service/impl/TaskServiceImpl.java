package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.TournamentParticipationRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TournamentParticipationRepository tournamentParticipationRepository;
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
    public String buildTaskCode(Long taskId, Long tournamentId) {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (optionalTaskEntity.isEmpty()) {
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }
        TaskEntity taskEntity = optionalTaskEntity.get();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TournamentParticipationEntity tournamentParticipationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);
        tournamentParticipationEntity.setTask(taskEntity);
        tournamentParticipationEntity.setFinishedCurrentTask(false);
        tournamentParticipationRepository.save(tournamentParticipationEntity);

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
    public TaskDto getNextTournamentTask(Long tournamentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TournamentParticipationEntity participationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);
        List<Long> unfinishedTaskIds = participationEntity.getUnfinishedTaskIds();
        if (unfinishedTaskIds.isEmpty()){
            return null;
        }
        Random random = new Random();
        long nextTaskId = random.nextLong(0, unfinishedTaskIds.size());
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(unfinishedTaskIds.get((int) nextTaskId));
        if (optionalTaskEntity.isEmpty()){
            throw new NoSuchElementException("Task with id: " + nextTaskId + " does not exist");
        }
        TaskEntity taskEntity = optionalTaskEntity.get();
        return taskMapper.entityToDto(taskEntity);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isEmpty()){
            throw new NoSuchElementException("User with username: " + username + " does not exist");
        }
        UserEntity userEntity = optionalUserEntity.get();
        List<TaskDto> tasks = new ArrayList<>();
        switch (userEntity.getRole()){
            case ROLE_SPONSOR -> tasks = userEntity.getCreatedTasks().stream().map(taskMapper::entityToDto).collect(Collectors.toList());
            case ROLE_ADMIN -> tasks = taskRepository.findAll().stream().map(taskMapper::entityToDto).collect(Collectors.toList());
        }
        return tasks;
    }

    @Override
    public AnalysisResults analyzeCode(Long taskId, Long tournamentId, String code) throws IOException, InterruptedException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (optionalTaskEntity.isEmpty()) {
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }
        TaskEntity taskEntity = optionalTaskEntity.get();
        codeRunner.setCode(code);
        codeRunner.setInputsAndOutputs(taskEntity.getInputOutput());
        AnalysisResults analysisResults = codeRunner.runCode(taskEntity.getLanguage());

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TournamentParticipationEntity tournamentParticipationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);

        if (analysisResults.getPassed() && !tournamentParticipationEntity.isFinishedCurrentTask() && !tournamentParticipationEntity.isFinishedParticipating()){
            tournamentParticipationEntity.setFinishedCurrentTask(true);
            tournamentParticipationEntity.incrementCompletedTaskCount();
            tournamentParticipationEntity.addPoints(taskEntity.getPoints());
            tournamentParticipationEntity.addMemoryInKilobytes(analysisResults.getMemoryInKilobytes());
            tournamentParticipationEntity.removeTaskIdFromUnfinishedTasks(taskId);
            tournamentParticipationRepository.save(tournamentParticipationEntity);
        }
        return analysisResults;
    }
}
