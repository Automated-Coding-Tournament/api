package com.pvp.codingtournament.handler;

import com.pvp.codingtournament.handler.exception.CodeCompilationException;
import com.pvp.codingtournament.handler.exception.CustomException;
import com.pvp.codingtournament.handler.exception.TaskNotFoundException;
import com.pvp.codingtournament.handler.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorModel> handleCustomException(CustomException ex, HttpServletRequest request){
        return new ResponseEntity<>(ex.getErrorModel(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorModel> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<ErrorModel> handleSecurityException(SecurityException ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CodeCompilationException.class)
    protected ResponseEntity<ErrorModel> handleCodeCompilationException(CodeCompilationException ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_ACCEPTABLE);
    }
}
