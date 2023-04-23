package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.task.TaskDto;

import java.io.IOException;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);
    TaskDto editTask(Long taskId, TaskDto taskDto);
    AnalysisResults analyzeJavaCode(Long taskId, String code) throws IOException, InterruptedException;
    String buildTaskCode(Long taskId);
}
