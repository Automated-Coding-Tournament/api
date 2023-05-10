package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.enums.RoleEnum;
import com.pvp.codingtournament.business.enums.TournamentStatus;
import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.TournamentParticipationRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.business.utils.BaseTaskCodeBuilder;
import com.pvp.codingtournament.business.utils.CodeRunner;
import com.pvp.codingtournament.business.utils.impl.BaseTaskCodeBuilderImpl;
import com.pvp.codingtournament.business.mapper.TaskMapStruct;
import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.task.TaskDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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
        for (TournamentEntity tournament : taskEntity.getTournaments()) {
            if (tournament.getStatus().equals(TournamentStatus.Started)){
                throw new ValidationException("Task cannot be edited as it is in an active tournament");
            }
        }

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

        if (participationEntity.getTask() != null && !participationEntity.isFinishedCurrentTask()){
            return taskMapper.entityToDto(participationEntity.getTask());
        }

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
        List<TaskEntity> tasks = new ArrayList<>();
        switch (userEntity.getRole()){
            case ROLE_SPONSOR -> tasks = userEntity.getCreatedTasks();
            case ROLE_ADMIN -> tasks = taskRepository.findAll();
        }

        List<TaskDto> taskDtos = new ArrayList<>();

        for (TaskEntity task : tasks) {
            TaskDto taskDto = taskMapper.entityToDto(task);
            taskDto.setMutable(task.getTournaments().stream().anyMatch(x -> x.getStatus().equals(TournamentStatus.Started)));
            taskDtos.add(taskDto);
        }
        return taskDtos;
    }

    @Override
    public void deleteTaskById(Long taskId) {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
        if (optionalTaskEntity.isEmpty()){
            throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
        }

        TaskEntity taskEntity = optionalTaskEntity.get();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        if (!taskEntity.getUser().getUsername().equals(username) && !roles.contains("ROLE_ADMIN")){
            throw new SecurityException("Only the creator of the task can delete it");
        }

        for (TournamentEntity tournament : taskEntity.getTournaments()) {
            if (tournament.getStatus().equals(TournamentStatus.Started)){
                throw new ValidationException("Task cannot be deleted as it is in an active tournament");
            }
        }

        taskRepository.deleteById(taskId);
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
