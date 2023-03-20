package com.pvp.codingtournament.handler.exception;

import com.pvp.codingtournament.handler.ErrorModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomException extends RuntimeException{
    private ErrorModel errorModel;
}
