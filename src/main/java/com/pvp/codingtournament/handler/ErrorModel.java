package com.pvp.codingtournament.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel {
    private HttpStatus status;
    private ArrayList<Error> errors = new ArrayList<>();
    private String message;

    public ErrorModel(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
