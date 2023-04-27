package com.pvp.codingtournament.controller;

import com.pvp.codingtournament.business.service.TaskService;
import com.pvp.codingtournament.model.AnalysisResults;
import com.pvp.codingtournament.model.task.TaskDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @ApiOperation("Updates task data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 403, message = "Not allowed to edit this task"),
            @ApiResponse(code = 404, message = "Task not found")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<TaskDto> editTask(@PathVariable("id") Long taskId, @RequestBody  TaskDto taskDto){
        return ResponseEntity.ok(taskService.editTask(taskId, taskDto));
    }

    @GetMapping("/getCode/{taskId}")
    public ResponseEntity<String> getTaskCode(@PathVariable("taskId") Long taskId){
        return ResponseEntity.ok(taskService.buildTaskCode(taskId));
    }

    @GetMapping("/getTask/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") Long taskId){
        return ResponseEntity.ok(taskService.getById(taskId));
    }

    @ApiOperation("Analyzes and runs user submitted code for task")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Task not found"),
            @ApiResponse(code = 200, message = "Analysis successful")
    })
    @PostMapping("/submitSolution/{taskId}")
    public ResponseEntity<AnalysisResults> submitTaskSolution(@PathVariable("taskId") Long taskId, @RequestBody String code) throws IOException, InterruptedException {
        return ResponseEntity.ok(taskService.analyzeCode(taskId, code));
    }
}
