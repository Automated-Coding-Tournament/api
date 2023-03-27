package com.pvp.codingtournament.handler;

import com.pvp.codingtournament.handler.exception.CustomException;
import com.pvp.codingtournament.handler.exception.TaskNotFoundException;
import com.pvp.codingtournament.handler.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorModel> handleCustomException(CustomException ex, HttpServletRequest request){
        return new ResponseEntity<>(ex.getErrorModel(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorModel> handleUserNotFoundException(Exception ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.NOT_FOUND, "User is not found");
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    protected ResponseEntity<ErrorModel> handleTaskNotFoundException(Exception ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.NOT_FOUND, "Task is not found");
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }
}
