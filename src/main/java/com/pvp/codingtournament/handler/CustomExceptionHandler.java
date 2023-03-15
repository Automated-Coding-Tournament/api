package com.pvp.codingtournament.handler;

import com.pvp.codingtournament.handler.exception.DuplicateEmailException;
import com.pvp.codingtournament.handler.exception.DuplicateUsernameAndEmailException;
import com.pvp.codingtournament.handler.exception.DuplicateUsernameException;
import com.pvp.codingtournament.handler.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    protected ResponseEntity<ErrorModel> handleDuplicateUsernameException(Exception ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.CONFLICT, "Conflict", "Username is taken", request.getServletPath());
        return new ResponseEntity<>(errorModel, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    protected ResponseEntity<ErrorModel> handleDuplicateEmailException(Exception ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.CONFLICT, "Conflict", "Email is taken", request.getServletPath());
        return new ResponseEntity<>(errorModel, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateUsernameAndEmailException.class)
    protected ResponseEntity<ErrorModel> handleDuplicateUsernameAndEmailException(Exception ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.CONFLICT, "Conflict", "Username and Email is taken", request.getServletPath());
        return new ResponseEntity<>(errorModel, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorModel> handleUserNotFoundException(Exception ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.NOT_FOUND, "Not Found", "User is not found", request.getServletPath());
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }
}
