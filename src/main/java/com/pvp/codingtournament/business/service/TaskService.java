package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.task.TaskDto;

import java.io.IOException;
import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);
    TaskDto editTask(Long taskId, TaskDto taskDto);
    AnalysisResults analyzeCode(Long taskId, Long tournamentId, String code) throws IOException, InterruptedException;
    String buildTaskCode(Long taskId, Long tournamentId);

    TaskDto getById(Long taskId);

    TaskDto getNextTournamentTask(Long tournamentId);

    List<TaskDto> getAllTasks();

    void deleteTaskById(Long taskId);
}
