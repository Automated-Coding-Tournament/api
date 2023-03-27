package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.TaskDto;

import java.io.IOException;

public interface TaskService {

    public TaskDto createTask(TaskDto taskDto);
    public AnalysisResults analyzeCode(Long taskId, String code) throws IOException, InterruptedException;
}
