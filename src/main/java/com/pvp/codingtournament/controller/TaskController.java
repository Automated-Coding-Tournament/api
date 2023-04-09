package com.pvp.codingtournament.controller;

import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.TaskDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @ApiOperation("Creates a new task")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Task created")
    })
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody  TaskDto taskDto){
        return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
    }

    @GetMapping("/getCode/{taskId}")
    public ResponseEntity<String> getTaskCode(@PathVariable("taskId") Long taskId){
        return ResponseEntity.ok(taskService.buildTaskCode(taskId));
    }

    @ApiOperation("Analyzes and runs user submitted java code for task")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Task not found"),
            @ApiResponse(code = 200, message = "Analysis successful")
    })
    @PostMapping("/submitSolution/java/{taskId}")
    public ResponseEntity<AnalysisResults> submitJavaTaskSolution(@PathVariable("taskId") Long taskId, @RequestBody String code) throws IOException, InterruptedException {
        return ResponseEntity.ok(taskService.analyzeJavaCode(taskId, code));
    }
}
