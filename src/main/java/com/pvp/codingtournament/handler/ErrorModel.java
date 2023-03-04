package com.pvp.codingtournament.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel {
    private HttpStatus status;
    private String statusName;
    private String message;
    private String path;
}
